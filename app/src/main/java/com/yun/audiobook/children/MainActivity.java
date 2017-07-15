package com.yun.audiobook.children;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yun.android.util.AndroidUtil;
import com.yun.android.util.StringUtil;
import com.yun.audiobook.children.database.Book;

public class MainActivity extends TitleActivity {
	public static final String ADMOB_ID = "ca-app-pub-9382095748158087/8276437449";
	public static final String ADMOB_ID_INTER = "ca-app-pub-9382095748158087/6116324642";

	private AlphaAnimation buttonClickAni = new AlphaAnimation(1F, 0.5F);
	private AutoCompleteTextView autoTitle;
	private ArrayAdapter<Book> titleAdapter;
	private String lastLoginTime;

	// Database Helper
	// DatabaseHelper db;

	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SysApplication.getInstance().addActivity(this);

		// hideBackButton();
		// db = new DatabaseHelper(getApplicationContext());

		ImageButton button = (ImageButton) findViewById(R.id.btn_exit);
		button.setImageResource(R.drawable.logout);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exit();

			}
		});

		Button btnCategory = (Button) findViewById(R.id.buttonCategory);
		btnCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClickAni);
				Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
				startActivity(intent);
			}
		});

		Button btnFav = (Button) findViewById(R.id.buttonFav);
		btnFav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClickAni);
				Intent intent = new Intent(MainActivity.this, BookFavActivity.class);
				startActivity(intent);
			}
		});

		Button btnHistory = (Button) findViewById(R.id.buttonHistory);
		btnHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClickAni);
				Intent intent = new Intent(MainActivity.this, BookHistoryActivity.class);
				startActivity(intent);
			}
		});

		titleAdapter = new ArrayAdapter<Book>(this, R.layout.book_drop_down);
		autoTitle = (AutoCompleteTextView) findViewById(R.id.auto_title);
		autoTitle.setThreshold(1);
		titleAdapter.setNotifyOnChange(true);
		autoTitle.setAdapter(titleAdapter);
		autoTitle.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable editable) {
			}

			@Override
			public void beforeTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
				String title = charSequence.toString();
				try {
					if (title.length() >= 3) {
						String temp = URLEncoder.encode(title, "UTF-8");
						new BookTitleAsyncTask().execute(temp, Locale.getDefault()
								.getDisplayLanguage(Locale.ENGLISH));
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});

		autoTitle.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View view, int index, long id) {
				Book item = (Book) av.getItemAtPosition(index);
				Intent intent = new Intent(MainActivity.this, BookInfoActivity.class);
				intent.putExtra("book_info_string", item.getRssUrl() + "*" + item.getImgUrl());
				startActivity(intent);
			}
		});

		ImageButton resetButton = (ImageButton) findViewById(R.id.btn_reset_title);
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				autoTitle.setText(null);
			}
		});

		// advertisement
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		if (adLayout != null) {
			adView = new AdView(this);
			adView.setAdUnitId(ADMOB_ID);
			adView.setAdSize(AdSize.BANNER);
			adLayout.addView(adView);

			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
		}

		Button btnRate = (Button) findViewById(R.id.btnRate);
		btnRate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("market://details?id=" + getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(goToMarket);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(MainActivity.this, "Couldn't launch the market",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		Button btnShare = (Button) findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = "http://play.google.com/store/apps/details?id="
						+ getPackageName();
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources()
						.getString(R.string.app_name));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Share using"));
			}
		});
	}

	public void prepareData() {
		SharedPreferences sp = getSharedPreferences(getResources().getString(R.string.app_name), 0);
		lastLoginTime = sp.getString("last_login_time", null);
		if (lastLoginTime == null) {
			// db.insertLanguages();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class BookTitleAsyncTask extends AsyncTask<String, Void, ArrayList<Book>> {
		private ProgressDialog progress = null;

		@Override
		protected void onPreExecute() {
			// progress = ProgressDialog.show(MainActivity.this, null,
			// getString(R.string.load));
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Book> doInBackground(String... params) {
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("book_name", params[0]));
				nameValuePairs.add(new BasicNameValuePair("book_lang", params[1]));

				String message = AndroidUtil.submitPost(
						"http://aplikatahr.dyndns-office.com:8888/aplikata_tool/YunAudioBookServlet",
						nameValuePairs);
				if (message.startsWith("system exception:") || StringUtil.isStrEmpty(message))
					return null;

				List<String> list = new Gson().fromJson(message, new TypeToken<List<String>>() {
				}.getType());
				List<Book> bookList = new ArrayList<Book>();
				for (String string : list) {
					String[] objs = string.split("\\|");
					Book book = new Book(objs[0], objs[1], objs[2]);
					bookList.add(book);
				}
				return (ArrayList<Book>) bookList;
			} catch (Exception e) {
				Log.v("MUGBUG4", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... strings) {
		}

		@Override
		protected void onPostExecute(ArrayList<Book> result) {
			// update the adapter
			// progress.dismiss();
			titleAdapter = new ArrayAdapter<Book>(getBaseContext(), R.layout.book_drop_down);
			titleAdapter.setNotifyOnChange(true);
			autoTitle.setAdapter(titleAdapter);
			if (result != null) {
				for (Book book : result) {
					titleAdapter.add(book);
					titleAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	private void exit() {
		String exitString = getResources().getString(R.string.sure_exit);
		Dialog dialog = new AlertDialog.Builder(MainActivity.this)
				.setTitle(getString(R.string.warning)).setMessage(exitString)
				.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
						// finish();
						// System.exit(0);
						stopService(audioService);
						SysApplication.getInstance().exit();
					}
				}).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();

		dialog.show();
	}

	// exit
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		
		super.onPause();
	}

	@Override
	public void onResume() {
		if (adView != null) {
			super.onResume();
		}
		adView.resume();
	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}
}
