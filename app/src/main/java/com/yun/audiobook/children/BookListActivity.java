package com.yun.audiobook.children;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.yun.android.util.AndroidUtil;
import com.yun.android.util.CItem;

public class BookListActivity extends TitleActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_list);
		SysApplication.getInstance().addActivity(this);

		final String categoryName = getIntent().getStringExtra("category_name");
		// title
		TextView titleTextView = (TextView) findViewById(R.id.txtTitle);
		titleTextView.setText(getIntent().getStringExtra("category_name_text"));

		int bookIndex = getIntent().getIntExtra("book_index", 1);

		ListView listView = (ListView) findViewById(R.id.listBook);
		int iden = getResources().getIdentifier(categoryName + "_" + bookIndex + "_name", "raw",
				getPackageName());
		List<String> list = AndroidUtil.readFileToList(BookListActivity.this, iden, "utf-8");
		List<CItem> newList = new ArrayList<CItem>();
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);
			int flag = i + 1;
			CItem item = new CItem(String.valueOf(flag), flag + "." + string);
			newList.add(item);
		}
		listView.setAdapter(new ArrayAdapter<CItem>(BookListActivity.this, R.layout.row, newList));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				String bookInfo = "";
				if (categoryName.equals(Constants.CAT_FICTION)) {
					bookInfo = Context.getInstance().getBookFictionList().get(position);
				} else if (categoryName.equals(Constants.CAT_NO_FINCTION)) {
					bookInfo = Context.getInstance().getBookNoFictionList().get(position);
				}

				// finish();
				final CItem item = (CItem) parent.getItemAtPosition(position);
				Intent intent = new Intent(BookListActivity.this, BookInfoActivity.class);
				intent.putExtra("book_info_string", bookInfo);
				intent.putExtra("book_name", item.getValue());
				startActivity(intent);
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
}
