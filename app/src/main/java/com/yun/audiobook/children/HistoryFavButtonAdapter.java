package com.yun.audiobook.children;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yun.audiobook.children.database.History;
import com.yun.audiobook.children.database.HistoryDataSource;

public class HistoryFavButtonAdapter extends ArrayAdapter<History> {
	private Context context;
	private final List<History> list;
	private HistoryDataSource dataSource;

	public HistoryFavButtonAdapter(Activity context, List<History> list, HistoryDataSource dataSource) {
		super(context, R.layout.row_fav, list);
		this.context = context;
		this.list = list;
		this.dataSource = dataSource;
	}

	static class ViewHolder {
		protected TextView textBookName;
		protected Button btnPlay;
		protected Button btnDelete;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_fav, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.textBookName = (TextView) view.findViewById(R.id.txtBookName);
			viewHolder.btnPlay = (Button) view.findViewById(R.id.btnPlay);
			viewHolder.btnDelete = (Button) view.findViewById(R.id.btnDelete);
			view.setTag(viewHolder);
			
			viewHolder.btnPlay.setTag(list.get(position));
			viewHolder.btnPlay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					History history = (History) viewHolder.btnPlay.getTag();
					Intent intent = new Intent(context, BookInfoActivity.class);
					intent.putExtra("book_info_string", history.getLink()+"*"+history.getImgUrl());
					intent.putExtra("book_name", history.getBook());
					context.startActivity(intent);
				}
			});
			
			viewHolder.btnDelete.setTag(list.get(position));
			viewHolder.btnDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					History history = (History) viewHolder.btnDelete.getTag();
					dataSource.deleteHistory(history);
					list.remove(history);
					notifyDataSetChanged();
				}
			});
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).btnDelete.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.textBookName.setText(list.get(position).getBook());
		return view;
	}
}
