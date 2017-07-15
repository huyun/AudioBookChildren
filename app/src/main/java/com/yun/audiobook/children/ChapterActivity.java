package com.yun.audiobook.children;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yun.android.util.RssItem;
import com.yun.android.util.StringUtil;
import com.yun.android.util.YunDateUtil;
import com.yun.audiobook.children.database.HistoryDataSource;

public class ChapterActivity extends TitleActivity {
	AlphaAnimation buttonClickAni = new AlphaAnimation(1F, 0.5F);

	TextView titleTextView;
	ListView chapterListView;
	Button playButton;
	Button stopButton;
	Button favButton;

	String bookTitle;
	String bookRssUrl;
	String bookImgUrl;
	ArrayList<String> titleList;
	ArrayList<String> urlList;
	ArrayList<String> durationList;
	ArrayList<RssItem> itemlist = null;

	HistoryDataSource dataSource;
	MediaPlayer mediaPlayer;
	ProgressDialog progress;
	RssItem selectedItem;
	int selectedIndex = 0;
	boolean pause;
	int sureExit = 0;
	private int playPosition;
	String chapterIndex;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chapter);
		hideSoundButton();

		titleTextView = (TextView) findViewById(R.id.txtTitle);
		bookTitle = getIntent().getStringExtra("book_title");
		chapterIndex = getIntent().getStringExtra("chapter_index");
		if (!StringUtil.isStrEmpty(chapterIndex)) {
			selectedIndex = Integer.valueOf(chapterIndex);
		}
		TextView bookNameTextView = (TextView) findViewById(R.id.txtBookName);
		bookNameTextView.setText(bookTitle);

		bookImgUrl = getIntent().getStringExtra("book_img_url");
		// titleTextView.setText(bookTitle);

		titleList = getIntent().getStringArrayListExtra("title_list");
		urlList = getIntent().getStringArrayListExtra("url_list");
		durationList = getIntent().getStringArrayListExtra("duration_list");
		bookRssUrl = getIntent().getStringExtra("book_rss_url");

		configChapter();
				
		dataSource = new HistoryDataSource(this);
		dataSource.open();

		playButton = (Button) findViewById(R.id.button_play);
		stopButton = (Button) findViewById(R.id.button_stop);
		favButton = (Button) findViewById(R.id.button_fav);
		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClickAni);
				play(0);
			}
		});

		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClickAni);
				stop();
			}
		});

		favButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClickAni);
				dataSource.createFavBook(bookTitle, bookRssUrl, YunDateUtil.formatDateTime(),
						bookImgUrl);
				dataSource.getAllFavs();
				Toast.makeText(ChapterActivity.this, bookTitle + getString(R.string.save_fav_ok),
						Toast.LENGTH_SHORT).show();
			}
		});

		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String setString = getResources().getString(R.string.go_main);
				Dialog dialog = new AlertDialog.Builder(ChapterActivity.this)
						.setTitle(getString(R.string.warning))
						.setMessage(setString)
						.setPositiveButton(getString(R.string.yes),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										dialog.cancel();
										if (mediaPlayer != null && mediaPlayer.isPlaying()) {
											mediaPlayer.stop();
											mediaPlayer.release();
											mediaPlayer = null;
										}

										finish();
										Intent intent = new Intent(ChapterActivity.this,
												MainActivity.class);
										startActivity(intent);
										displayInterstitial();
									}
								})
						.setNegativeButton(getString(R.string.no),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
									}
								}).create();
				dialog.show();
			}
		});

		if (!StringUtil.isStrEmpty(chapterIndex)) {
			play(getIntent().getIntExtra("stop_point", 0));
		}

	}

	private void configChapter() {
		chapterListView = (ListView) findViewById(R.id.listChapter);
		itemlist = new ArrayList<RssItem>();
		for (int i = 0; i < titleList.size(); i++) {
			RssItem item = new RssItem();
			item.title = titleList.get(i);
			item.link = urlList.get(i);
			item.duration = durationList.get(i);
			itemlist.add(item);
		}
		selectedItem = itemlist.get(selectedIndex);
		RSSListAdaptor rssadaptor = new RSSListAdaptor(ChapterActivity.this, R.layout.chapter,
				itemlist);
		chapterListView.setAdapter(rssadaptor);
		chapterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				pause = false;
				stop();
				selectedItem = itemlist.get(position);
				titleTextView.setText(selectedItem.title);
				selectedIndex = position;
				play(0);
			}
		});

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
	}

	/**
	 * 只有电话来了之后才暂停音乐的播放
	 */
	private final class MyPhoneListener extends android.telephony.PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 电话来了
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					playPosition = mediaPlayer.getCurrentPosition();// 获得当前播放位置
					mediaPlayer.stop();
				}
				break;
			case TelephonyManager.CALL_STATE_IDLE: // 通话结束
				if (playPosition > 0) {
					play(playPosition);
					playPosition = 0;
				}
				break;
			}
		}
	}

	protected void play(final int playPosition) {
		try {
			if (progress != null) {
				progress.dismiss();
			}
			// if playing pause the play
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				dataSource.createReadHistory(bookTitle, selectedItem.title, bookRssUrl,
						YunDateUtil.formatDateTime(), mediaPlayer.getCurrentPosition(), bookImgUrl,
						selectedIndex);
				playButton.setBackgroundResource(R.drawable.play);
				pause = true;
				return;
			}

			// if pause continue
			if (pause == true && mediaPlayer != null) {
				mediaPlayer.start();
				dataSource.createReadHistory(bookTitle, selectedItem.title, bookRssUrl,
						YunDateUtil.formatDateTime(), mediaPlayer.getCurrentPosition(), bookImgUrl,
						selectedIndex);
				playButton.setBackgroundResource(R.drawable.pause);
				pause = false;
				return;
			}

			if (selectedItem == null) {
				selectedItem = itemlist.get(0);
				selectedIndex = 0;
			}

			progress = ProgressDialog.show(ChapterActivity.this, null, getString(R.string.load));
			mediaPlayer = new MediaPlayer();
			// 设置指定的流媒体地址
			mediaPlayer.setDataSource(selectedItem.link);

			// 设置音频流的类型
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			// 通过异步的方式装载媒体资源
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					// 装载完毕 开始播放流媒体
					mediaPlayer.start();
					mediaPlayer.seekTo(playPosition);
					playButton.setBackgroundResource(R.drawable.pause);
					dataSource.createReadHistory(bookTitle, selectedItem.title, bookRssUrl,
							YunDateUtil.formatDateTime(), mediaPlayer.getCurrentPosition(),
							bookImgUrl, selectedIndex);
					if (progress != null) {
						progress.dismiss();
					}
					displayInterstitial();
				}
			});
			// 设置循环播放
			// mediaPlayer.setLooping(true);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					dataSource.createReadHistory(bookTitle, selectedItem.title, bookRssUrl,
							YunDateUtil.formatDateTime(), 0, bookImgUrl, selectedIndex);
					// 在播放完毕被回调
					if (selectedIndex == itemlist.size() - 1) {
						stop();
						return;
					}
					selectedIndex++;
					selectedItem = itemlist.get(selectedIndex);
					mediaPlayer.release();
					mediaPlayer = null;
					String title = selectedItem.title;
					if (title.indexOf("(") >= 0) {
						title = title.substring(0, title.indexOf("("));
					}
					titleTextView.setText(title);
					play(0);
				}
			});

			mediaPlayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// 如果发生错误，重新播放
					replay();
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void replay() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
			return;
		}
		play(0);
	}

	protected void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			dataSource.createReadHistory(bookTitle, selectedItem.title, bookRssUrl,
					YunDateUtil.formatDateTime(), mediaPlayer.getCurrentPosition(), bookImgUrl,
					selectedIndex);
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			playButton.setBackgroundResource(R.drawable.play);
		}
	}

	@Override
	protected void onDestroy() {
		// 在activity结束的时候回收资源
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		sureExit = 0;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			String setString = getResources().getString(R.string.go_back);
			Dialog dialog = new AlertDialog.Builder(ChapterActivity.this)
					.setTitle(getString(R.string.warning))
					.setMessage(setString)
					.setPositiveButton(getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									dialog.cancel();
									if (mediaPlayer != null && mediaPlayer.isPlaying()) {
										mediaPlayer.stop();
										mediaPlayer.release();
										mediaPlayer = null;
									}
									sureExit = 1;
									finish();
								}
							})
					.setNegativeButton(getString(R.string.no),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									sureExit = 0;
									dialog.cancel();
								}
							}).create();
			dialog.show();
		}
		if (sureExit == 0) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private class RSSListAdaptor extends ArrayAdapter<RssItem> {
		private List<RssItem> myObjects = null;

		public RSSListAdaptor(Context context, int textviewid, List<RssItem> objects) {
			super(context, textviewid, objects);
			myObjects = objects;
		}

		@Override
		public int getCount() {
			return ((null != myObjects) ? myObjects.size() : 0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public RssItem getItem(int position) {
			return ((null != myObjects) ? myObjects.get(position) : null);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			if (null == view) {
				LayoutInflater vi = (LayoutInflater) ChapterActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.chapter, null);
			}

			RssItem data = myObjects.get(position);

			if (null != data) {
				TextView title = (TextView) view.findViewById(R.id.txtTitle);
				TextView date = (TextView) view.findViewById(R.id.txtDate);
				title.setText(data.title);
				if (StringUtil.isStrEmpty(data.duration)) {
					date.setText("on librivox.org ");
				} else {
					date.setText("on librivox.org (" + data.duration + ")");
				}
			}

			return view;
		}
	}
}
