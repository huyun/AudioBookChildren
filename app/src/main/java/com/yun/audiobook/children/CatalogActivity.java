package com.yun.audiobook.children;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.yun.android.util.AndroidUtil;

public class CatalogActivity extends TitleActivity {
	ListView fictionListView;
	ListView nofictionListView;
	ArrayAdapter<String> fictionAdapter;
	ArrayAdapter<String> nofictionAdapter;
	TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		SysApplication.getInstance().addActivity(this);
		// title
		TextView titleTextView = (TextView) findViewById(R.id.txtTitle);
		titleTextView.setText(R.string.category);

		fictionListView = (ListView) findViewById(R.id.listViewFictionCat);
		nofictionListView = (ListView) findViewById(R.id.listViewNoFictionCat);

		fictionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				int flag = position + 1;
				int iden = getResources().getIdentifier(Constants.CAT_FICTION + "_" + flag + "_rss", "raw",
						getPackageName());
				Context.getInstance().setBookFictionList(
						AndroidUtil.readFileToList(CatalogActivity.this, iden, "utf-8"));
				// finish();
				final String item = (String) parent.getItemAtPosition(position);
				Intent intent = new Intent(CatalogActivity.this, BookListActivity.class);
				intent.putExtra("category_name", Constants.CAT_FICTION);
				intent.putExtra("category_name_text", item);
				intent.putExtra("book_index", flag);
				startActivity(intent);
			}
		});

		nofictionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				int flag = position + 1;
				int iden = getResources().getIdentifier(Constants.CAT_NO_FINCTION + "_" + flag + "_rss", "raw",
						getPackageName());
				Context.getInstance().setBookNoFictionList(
						AndroidUtil.readFileToList(CatalogActivity.this, iden, "utf-8"));
				// finish();
				final String item = (String) parent.getItemAtPosition(position);
				Intent intent = new Intent(CatalogActivity.this, BookListActivity.class);
				intent.putExtra("category_name", Constants.CAT_NO_FINCTION);
				intent.putExtra("category_name_text", item);
				intent.putExtra("book_index", flag);
				startActivity(intent);
			}
		});

		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		new RetrieveData().execute();
		
		//advertisement
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		if (adLayout != null) {
			AdView adView = new AdView(this);
			adView.setAdUnitId(MainActivity.ADMOB_ID);
			adView.setAdSize(AdSize.BANNER);
			adLayout.addView(adView);
			adView.loadAd(new AdRequest.Builder().build());
		}
	}

	private class RetrieveData extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress = null;

		@Override
		protected Void doInBackground(Void... params) {

			List<String> list = AndroidUtil.readFileToList(CatalogActivity.this, R.raw.category_fiction, "utf-8");
			fictionAdapter = new ArrayAdapter<String>(CatalogActivity.this, R.layout.row, list);
			List<String> list1 = AndroidUtil.readFileToList(CatalogActivity.this, R.raw.category_no_fiction, "utf-8");
			nofictionAdapter = new ArrayAdapter<String>(CatalogActivity.this, R.layout.row, list1);
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(CatalogActivity.this, null, getString(R.string.load));
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			fictionListView.setAdapter(fictionAdapter);
			nofictionListView.setAdapter(nofictionAdapter);

			String fictionString = getString(R.string.fiction);
			String nofictionString = getResources().getString(R.string.nofiction);
			tabHost.addTab(tabHost.newTabSpec("fiction").setIndicator(fictionString)
					.setContent(R.id.listViewFictionCat));
			tabHost.addTab(tabHost.newTabSpec("nofiction").setIndicator(nofictionString)
					.setContent(R.id.listViewNoFictionCat));

			TabWidget tabWidget = tabHost.getTabWidget();
			for (int i = 0; i < tabWidget.getChildCount(); i++) {
				tabWidget.getChildAt(i).getLayoutParams().height = 60;
			}

			progress.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
