package com.yun.audiobook.children;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class AudioService extends Service {
	private MediaPlayer player;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onStart(Intent intent, int startId) {

		if (player == null) {
			int random = (int )(Math.random() * 5 + 1);
			int iden = getResources().getIdentifier( "music_"+random , "raw" , this.getPackageName() );
			player = MediaPlayer.create(this, iden);
			player.setLooping(true);
			player.start();
		}
	}

	@Override
	public void onDestroy() {
		player.stop();
		super.onDestroy();
	}

}
