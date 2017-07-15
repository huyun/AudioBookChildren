package com.yun.audiobook.children.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HistorySQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_HISTORY = "history";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_BOOK = "book";
	public static final String COLUMN_CHAPTER = "chapter";
	public static final String COLUMN_LINK = "link";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_STOP_POINT = "stopPoint";
	public static final String COLUMN_IMG_URL = "imgUrl";
	public static final String COLUMN_CHAPTER_INDEX = "chapterIndexl";
	private static final String DATABASE_NAME = "history.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_HISTORY + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_TYPE + " SMALLINT," + COLUMN_BOOK + " VARCHAR,"
			+ COLUMN_CHAPTER + " VARCHAR," + COLUMN_LINK + " VARCHAR," + COLUMN_TIME + " VARCHAR," + COLUMN_STOP_POINT +" integer,"
			+ COLUMN_IMG_URL + " VARCHAR," + COLUMN_CHAPTER_INDEX + " integer);";

	public HistorySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(HistorySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		onCreate(db);
	}
}
