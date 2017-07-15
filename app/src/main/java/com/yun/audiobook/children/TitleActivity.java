package com.yun.audiobook.children;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.yun.android.util.AndroidUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TitleActivity extends Activity {
	private AlphaAnimation buttonClickAni = new AlphaAnimation(1F, 0.5F);
	ImageButton btnExit;
	protected Intent audioService = new Intent("com.yun.audiobook.children.AudioService");
	private boolean serviceFlag;
	private boolean soundOn = true;
	private ImageButton soundButton;

	AdView adView;
	private InterstitialAd interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.title_bar);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

		// Create the interstitial
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(MainActivity.ADMOB_ID_INTER);
		//AdRequest adRequest1 = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		//		.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB").build();
		//interstitial.loadAd(adRequest1);
		interstitial.loadAd(new AdRequest.Builder().build());

		btnExit = (ImageButton) findViewById(R.id.btn_exit);
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClickAni);
				exit();
			}
		});

		soundButton = (ImageButton) findViewById(R.id.imgSound);
		loadSavedPreferences();
		soundButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.startAnimation(buttonClickAni);
				soundOn = !soundOn;
				saveSoundPreferences(soundOn);
				loadSavedPreferences();
			}
		});

		// advertisement
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		if (adLayout != null) {
			adView = new AdView(this);
			adView.setAdUnitId(MainActivity.ADMOB_ID);
			adView.setAdSize(AdSize.BANNER);
			adLayout.addView(adView);

			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
		}
	}

	protected void hideBackButton() {
		btnExit.setVisibility(View.INVISIBLE);
	}

	protected void hideSoundButton() {
		soundButton.setVisibility(View.GONE);
	}

	private void exit() {
		Intent intent = new Intent(TitleActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	protected void onDestroy() {// 销毁Activity之前，所做的事
		stopService(audioService);
		serviceFlag = false;
		super.onDestroy();

		if (adView != null) {
			adView.destroy();
		}

	}

	protected void onPause() {
		if (adView != null) {
			adView.pause();
		}

		super.onPause();
	}

	protected void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	private void loadSavedPreferences() {
		soundOn = AndroidUtil.getSoundPreference(this);

		if (soundOn) {
			soundButton.setImageResource(R.drawable.sound_on);
			if (serviceFlag == false) {
				startService(audioService);
				serviceFlag = true;
			}
		} else {
			soundButton.setImageResource(R.drawable.sound_off);
			// audioService = new Intent(TitleActivity.this,
			// com.yun.audiobook.children.AudioService.class);
			stopService(audioService);
			serviceFlag = false;
		}
	}

	private void saveSoundPreferences(boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("SOUND_ON", value);
		editor.commit();
	}

	// Invoke displayInterstitial() when you are ready to display an
	// interstitial.
	public void displayInterstitial() {
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}
}