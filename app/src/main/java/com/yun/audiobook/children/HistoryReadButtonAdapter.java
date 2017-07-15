package com.yun.audiobook.children;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yun.android.provider.librivox.LVoxChapter;
import com.yun.android.provider.librivox.LVoxRSS;
import com.yun.android.provider.librivox.LVoxRestClient;
import com.yun.audiobook.children.database.History;
import com.yun.audiobook.children.database.HistoryDataSource;

public class HistoryReadButtonAdapter extends ArrayAdapter<History> {
	private Context context;
	private final List<History> list;
	private HistoryDataSource dataSource;
	History history;
	LVoxRestClient lvoxClient;
	LVoxRSS bookRss;
	ProgressDialog progress = null;
	
	public HistoryReadButtonAdapter(Activity context, List<History> list, HistoryDataSource dataSource) {
		super(context, R.layout.row_history, list);
		this.context = context;
		this.list = list;
		this.dataSource = dataSource;
	}

	static class ViewHolder {
		protected TextView textBook;
		protected TextView textChapter;
		protected TextView textDate;
		protected Button btnPlay;
		protected Button btnDelete;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_history, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.textChapter = (TextView) view.findViewById(R.id.txtChapter);
			viewHolder.textBook = (TextView) view.findViewById(R.id.txtBook);
			viewHolder.textDate = (TextView) view.findViewById(R.id.txtDate);
			viewHolder.btnPlay = (Button) view.findViewById(R.id.btnPlay);
			viewHolder.btnDelete = (Button) view.findViewById(R.id.btnDelete);
			view.setTag(viewHolder);
			
			viewHolder.btnPlay.setTag(list.get(position));
			viewHolder.btnPlay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					history = (History) viewHolder.btnPlay.getTag();
					new RetrieveData().execute();
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
		holder.textBook.setText(list.get(position).getBook());
		holder.textChapter.setText(list.get(position).getChapter());
		holder.textDate.setText(list.get(position).getTime());
		return view;
	}
	
	private class RetrieveData extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			String bookRssUrl = history.getLink();
			try {
				lvoxClient = new LVoxRestClient();
				bookRss = lvoxClient.getBookDetails(bookRssUrl);
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
			progress = ProgressDialog.show(context, null, context.getString(R.string.load));
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progress != null) {
				progress.dismiss();
			}
			if (bookRss != null) {
				Intent intent = new Intent(context, ChapterActivity.class);
				intent.putExtra("book_title", history.getBook());
				List<LVoxChapter> chapterList = bookRss.getChapters();
				ArrayList<String> titleList = new ArrayList<String>();
				ArrayList<String> urlList = new ArrayList<String>();
				ArrayList<String> durationList = new ArrayList<String>();
				for (LVoxChapter chapter : chapterList) {
					titleList.add(chapter.getTitle());
					urlList.add(chapter.getUrl());
					durationList.add(chapter.getDuration());
				}
				intent.putExtra("book_rss_url", history.getLink());
				intent.putExtra("book_img_url", history.getImgUrl());
				intent.putStringArrayListExtra("title_list", titleList);
				intent.putStringArrayListExtra("url_list", urlList);
				intent.putStringArrayListExtra("duration_list", durationList);
				intent.putExtra("chapter_index", String.valueOf(history.getChapterIndex()));
				intent.putExtra("stop_point", history.getStopPoint());
				context.startActivity(intent);
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
}
