package com.yun.android.provider;

public interface OnFileDownloadProgressListener {
	public abstract void onProgressUpdate(long dl_uid, long bytesDownloaded, long total);
}
