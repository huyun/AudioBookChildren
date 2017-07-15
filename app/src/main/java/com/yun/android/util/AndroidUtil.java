package com.yun.android.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

public class AndroidUtil {
	public static boolean getSoundPreference(Activity activity) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		return sharedPreferences.getBoolean("SOUND_ON", true);
	}
	
	public static String submitPost(String url, List<NameValuePair> nameValuePairs) throws Exception {

		StringBuffer sb = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response = client.execute(post);

		int stateCode = response.getStatusLine().getStatusCode();
		if (stateCode == HttpStatus.SC_OK) {
			HttpEntity result = response.getEntity();
			if (result != null) {
				InputStream is = result.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String tempLine;
				while ((tempLine = br.readLine()) != null) {
					sb.append(tempLine);
				}
			}
		}
		post.abort();
		return sb.toString();
	}

	public static String getUriPath(Activity activity, Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		activity.startManagingCursor(cursor);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	public static void writeSmallBitmap(String oldPath, String newPath){
		Bitmap bitmap = getSmallBitmap(oldPath);
	    if (bitmap != null) {
	        try {
	            // build directory
	        	File file = new File(newPath);
	            File path = new File(file.getParent());
	            if (file.getParent() != null && !path.isDirectory()) {
	                path.mkdirs();
	            }
	            // output image to file
	            FileOutputStream fos = new FileOutputStream(newPath);
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
	            fos.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static String readFile(Activity activity, int bookId, String encode) {
		InputStream inputStream = activity.getResources().openRawResource(bookId);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream, encode));// 注意编码
		} catch (UnsupportedEncodingException e1) {
			Log.e("debug", e1.toString());
		}
		StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			reader.close();
			inputStream.close();
		} catch (IOException e) {
			Log.e("debug", e.toString());
		}

		return sb.toString();
	}
	
	public static List<String> readFileToList(Activity activity, int bookId, String encode) {
		InputStream inputStream = activity.getResources().openRawResource(bookId);
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream, encode));
		} catch (UnsupportedEncodingException e1) {
			Log.e("debug", e1.toString());
		}
		List<String> list = new ArrayList<String>();
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
			reader.close();
			inputStream.close();
		} catch (IOException e) {
			Log.e("debug", e.toString());
		}

		return list;
	}
	
	public static String[] readFileToArray(Activity activity, int bookId, String encode, Paint paint, int displayWidth) {
		String text = readFile(activity, bookId, encode);
		if(text == null)
			return null;
		
		paint.setTextSize(25);
		int stringsWidth = 0;
		Vector<String> vector = new Vector<String>();
		int beginIndex = 0, endIndex = 0;

		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '\n') {
				// 遇见换行符自动换行
				endIndex = i;
				vector.addElement(text.substring(beginIndex, endIndex));
				beginIndex = i + 1;
				stringsWidth = 0;
			} else if (stringsWidth > displayWidth) {
				endIndex = --i;
				vector.addElement(text.substring(beginIndex, endIndex));
				beginIndex = i;
				stringsWidth = 0;
				i--;
			} else {
				// 累加单个字符宽度与要求的显示宽度比较
				float[] widths = new float[1];
				String srt = String.valueOf(text.charAt(i));
				paint.getTextWidths(srt, widths);
				stringsWidth += widths[0];
			}

			if (i == text.length() - 1) {
				if (stringsWidth > displayWidth) {
					endIndex = i;
					vector.addElement(text.substring(beginIndex, endIndex));
					beginIndex = i;
					stringsWidth = 0;
				}
			}
		}

		// 最后剩余的字体也要放进去
		if (beginIndex != text.length() - 1) {
			vector.addElement(text.substring(beginIndex, text.length()));
		} else if (beginIndex == text.length() - 1) {
			vector.addElement(text.substring(beginIndex));
		}

		String strings[] = new String[vector.size()];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = (String) vector.elementAt(i);
		}

		return strings;
	}
	
	public static byte[] getBytes(InputStream is) throws Exception{  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while((len = is.read(buffer))!=-1){  
            bos.write(buffer, 0, len);  
        }  
        is.close();  
        bos.flush();  
        byte[] result = bos.toByteArray();  
        return  result;  
    }  
	
	public static Bitmap getImage(String address) throws Exception{  
        //通过代码 模拟器浏览器访问图片的流程   
        URL url = new URL(address);  
        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();  
        conn.setRequestMethod("GET");  
        conn.setConnectTimeout(5000);  
        //获取服务器返回回来的流   
        InputStream is = conn.getInputStream();  
        byte[] imagebytes = getBytes(is);  
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);  
        return bitmap;  
    }  
}
