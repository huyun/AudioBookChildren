package com.yun.audiobook.children;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.yun.android.provider.librivox.LVoxChapter;
import com.yun.android.provider.librivox.LVoxRSS;
import com.yun.android.provider.librivox.LVoxRestClient;
import com.yun.android.util.AndroidUtil;

public class BookInfoActivity extends TitleActivity {
	private AlphaAnimation buttonClickAni = new AlphaAnimation(1F, 0.5F);
	String bookInfoString;

	TextView bookNameTextView;
	TextView bookTextView;
	ImageView imageView;
	Bitmap bitmap;
	ProgressDialog progress = null;
	LVoxRSS bookRss;
	String bookRssUrl;
	String bookImgUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_info);
		bookInfoString = getIntent().getStringExtra("book_info_string");

		// title
		TextView titleTextView = (TextView) findViewById(R.id.txtTitle);
		titleTextView.setText(getIntent().getStringExtra("book_name"));

		bookNameTextView = (TextView) findViewById(R.id.txtBookName);
		bookTextView = (TextView) findViewById(R.id.txtBook);
		bookTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
		imageView = (ImageView) findViewById(R.id.imgBook);

		Button btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				recycleImage();

				Intent audioService = new Intent(BookInfoActivity.this,
						com.yun.audiobook.children.AudioService.class);
				stopService(audioService);

				finish();
				
				Intent intent = new Intent(BookInfoActivity.this, ChapterActivity.class);
				if (bookRss == null) {
					return;
				}
				intent.putExtra("book_title", bookRss.getTitle());
				List<LVoxChapter> chapterList = bookRss.getChapters();
				ArrayList<String> titleList = new ArrayList<String>();
				ArrayList<String> urlList = new ArrayList<String>();
				ArrayList<String> durationList = new ArrayList<String>();
				for (LVoxChapter chapter : chapterList) {
					titleList.add(chapter.getTitle());
					urlList.add(chapter.getUrl());
					durationList.add(chapter.getDuration());
				}
				intent.putExtra("book_rss_url", bookRssUrl);
				intent.putExtra("book_img_url", bookImgUrl);
				intent.putStringArrayListExtra("title_list", titleList);
				intent.putStringArrayListExtra("url_list", urlList);
				intent.putStringArrayListExtra("duration_list", durationList);
				startActivity(intent);
				displayInterstitial();
			}
		});

		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClickAni);
				finish();
				Intent intent = new Intent(BookInfoActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});

		new RetrieveData().execute();
		// advertisement
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		if (adLayout != null) {
			AdView adView = new AdView(this);
			adView.setAdUnitId(MainActivity.ADMOB_ID);
			adView.setAdSize(AdSize.BANNER);
			adLayout.addView(adView);
			adView.loadAd(new AdRequest.Builder().build());
		}
	}

	private void recycleImage() {
		try {
			if (imageView != null) {
				imageView.setImageBitmap(null);
			}

			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
				System.gc();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private class RetrieveData extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			String[] bookInfoStrings = bookInfoString.split("\\*");
			bookRssUrl = bookInfoStrings[0];
			try {
				LVoxRestClient lvoxClient = new LVoxRestClient();
				bookRss = lvoxClient.getBookDetails(bookRssUrl);
				bookImgUrl = bookInfoStrings[1];
				bitmap = AndroidUtil.getImage(bookImgUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(BookInfoActivity.this, null, getString(R.string.load));
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			imageView.setImageBitmap(bitmap);
			if (bookRss != null) {
				bookNameTextView.setText(bookRss.getTitle());
				bookTextView.setText(Html.fromHtml(bookRss.getDescription()));
			}

			if (progress != null) {
				progress.dismiss();
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
}
