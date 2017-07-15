package com.yun.audiobook.children;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.yun.audiobook.children.database.History;
import com.yun.audiobook.children.database.HistoryDataSource;

public class BookHistoryActivity extends TitleActivity {
	HistoryDataSource dataSource;
	HistoryReadButtonAdapter historyButtonAdapter;
	ListView historyListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_history);

		// open database
		dataSource = new HistoryDataSource(this);
		dataSource.open();

		List<History> historyList = dataSource.getAllReadHistory();
		historyListView = (ListView) findViewById(R.id.listHistoryBook);

		historyButtonAdapter = new HistoryReadButtonAdapter(this, historyList, dataSource);
		historyListView.setAdapter(historyButtonAdapter);
		Button clearAllButton = (Button) findViewById(R.id.buttonClearAll);
		clearAllButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dataSource.deleteAllReadHistories();
				historyButtonAdapter = new HistoryReadButtonAdapter(BookHistoryActivity.this,
						new ArrayList<History>(), dataSource);
				historyButtonAdapter.notifyDataSetChanged();
				historyListView.setAdapter(historyButtonAdapter);
			}
		});

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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}
}
