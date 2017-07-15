package com.yun.android.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BroadcastExitActivity extends Activity {
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			unregisterReceiver(broadcastReceiver);
			finish();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction("ExitApp");
		this.registerReceiver(broadcastReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		try {
			super.onDestroy();
			unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void close() {
		Intent intent = new Intent();
		intent.setAction("ExitApp"); // 说明动作
		sendBroadcast(intent);// 该函数用于发送广播
		finish();
	}

}
