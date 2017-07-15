package com.yun.audiobook.children;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.yun.audiobook.children.database.History;
import com.yun.audiobook.children.database.HistoryDataSource;

public class BookFavActivity extends TitleActivity {
	HistoryDataSource dataSource;
	HistoryFavButtonAdapter historyButtonAdapter;
	ListView favListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_fav);

		// open database
		dataSource = new HistoryDataSource(this);
		dataSource.open();

		List<History> favList = dataSource.getAllFavs();
		favListView = (ListView) findViewById(R.id.listFavBook);

		historyButtonAdapter = new HistoryFavButtonAdapter(this, favList, dataSource);
		favListView.setAdapter(historyButtonAdapter);
		Button clearAllButton = (Button) findViewById(R.id.buttonClearAll);
		clearAllButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dataSource.deleteAllFavs();
				historyButtonAdapter = new HistoryFavButtonAdapter(BookFavActivity.this,
						new ArrayList<History>(), dataSource);
				historyButtonAdapter.notifyDataSetChanged();
				favListView.setAdapter(historyButtonAdapter);
			}
		});
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
