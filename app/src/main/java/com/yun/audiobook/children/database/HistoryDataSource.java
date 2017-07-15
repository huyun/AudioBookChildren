package com.yun.audiobook.children.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class HistoryDataSource {

	// Database fields
	private SQLiteDatabase database;
	private HistorySQLiteHelper dbHelper;
	private String[] allColumns = { HistorySQLiteHelper.COLUMN_ID, HistorySQLiteHelper.COLUMN_TYPE,
			HistorySQLiteHelper.COLUMN_BOOK, HistorySQLiteHelper.COLUMN_CHAPTER, HistorySQLiteHelper.COLUMN_LINK,
			HistorySQLiteHelper.COLUMN_TIME, HistorySQLiteHelper.COLUMN_STOP_POINT, HistorySQLiteHelper.COLUMN_IMG_URL,
			HistorySQLiteHelper.COLUMN_CHAPTER_INDEX};

	public HistoryDataSource(Context context) {
		dbHelper = new HistorySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public History createHistory(int type, String book, String chapter, String link, String operTime,
			Integer stopPoint, String imgUrl, int chapterIndex) {
		ContentValues values = new ContentValues();
		values.put(HistorySQLiteHelper.COLUMN_TYPE, type);
		values.put(HistorySQLiteHelper.COLUMN_BOOK, book);
		values.put(HistorySQLiteHelper.COLUMN_CHAPTER, chapter);
		values.put(HistorySQLiteHelper.COLUMN_LINK, link);
		values.put(HistorySQLiteHelper.COLUMN_TIME, operTime);
		values.put(HistorySQLiteHelper.COLUMN_STOP_POINT, stopPoint);
		values.put(HistorySQLiteHelper.COLUMN_IMG_URL, imgUrl);
		values.put(HistorySQLiteHelper.COLUMN_CHAPTER_INDEX, chapterIndex);

		long insertId = database.insert(HistorySQLiteHelper.TABLE_HISTORY, null, values);
		Cursor cursor = database.query(HistorySQLiteHelper.TABLE_HISTORY, allColumns, HistorySQLiteHelper.COLUMN_ID
				+ " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		History newHistory = cursorToHistory(cursor);
		cursor.close();
		return newHistory;
	}

	public History createFavBook(String bookTitle, String link, String operTime, String imgUrl) {
		ContentValues values = new ContentValues();
		values.put(HistorySQLiteHelper.COLUMN_TYPE, History.TYPE_FAV);
		values.put(HistorySQLiteHelper.COLUMN_BOOK, bookTitle);
		values.put(HistorySQLiteHelper.COLUMN_LINK, link);
		values.put(HistorySQLiteHelper.COLUMN_TIME, operTime);
		values.put(HistorySQLiteHelper.COLUMN_IMG_URL, imgUrl);

		long insertId = database.insert(HistorySQLiteHelper.TABLE_HISTORY, null, values);
		Cursor cursor = database.query(HistorySQLiteHelper.TABLE_HISTORY, allColumns, HistorySQLiteHelper.COLUMN_ID
				+ " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		History newHistory = cursorToHistory(cursor);
		cursor.close();
		return newHistory;
	}

	public History searchReadHistory(String bookLink) {
		History newHistory = null;
		Cursor cursor = database.rawQuery("select * from " + HistorySQLiteHelper.TABLE_HISTORY + " where type="
				+ History.TYPE_READ + " and " + HistorySQLiteHelper.COLUMN_LINK + "='" + bookLink + "'", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			newHistory = cursorToHistory(cursor);
		}

		cursor.close();
		return newHistory;
	}

	public History createReadHistory(String book, String chapter, String link, String operTime, Integer stopPoint,
			String imgUrl, int chapterIndex) {
		ContentValues values = new ContentValues();
		values.put(HistorySQLiteHelper.COLUMN_TYPE, History.TYPE_READ);
		values.put(HistorySQLiteHelper.COLUMN_BOOK, book);
		values.put(HistorySQLiteHelper.COLUMN_CHAPTER, chapter);
		values.put(HistorySQLiteHelper.COLUMN_LINK, link);
		values.put(HistorySQLiteHelper.COLUMN_TIME, operTime);
		values.put(HistorySQLiteHelper.COLUMN_STOP_POINT, stopPoint);
		values.put(HistorySQLiteHelper.COLUMN_IMG_URL, imgUrl);
		values.put(HistorySQLiteHelper.COLUMN_CHAPTER_INDEX, chapterIndex);
		
		History searchHistory = searchReadHistory(link);
		if (searchHistory != null) {
			database.update(HistorySQLiteHelper.TABLE_HISTORY, values, HistorySQLiteHelper.COLUMN_ID + "="
					+ searchHistory.getId(), null);
			History history = new History();
			history.setId(searchHistory.getId());
			history.setBook(book);
			history.setChapter(chapter);
			history.setType(History.TYPE_READ);
			history.setLink(link);
			history.setTime(operTime);
			history.setStopPoint(stopPoint);
			history.setImgUrl(imgUrl);
			history.setChapterIndex(chapterIndex);
			return history;
		}
		long id = database.insert(HistorySQLiteHelper.TABLE_HISTORY, null, values);

		Cursor cursor = database.query(HistorySQLiteHelper.TABLE_HISTORY, allColumns, HistorySQLiteHelper.COLUMN_ID
				+ " = " + id, null, null, null, null);
		cursor.moveToFirst();
		History newHistory = cursorToHistory(cursor);
		cursor.close();
		return newHistory;
	}

	public void deleteHistory(History history) {
		long id = history.getId();
		database.delete(HistorySQLiteHelper.TABLE_HISTORY, HistorySQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	public List<History> getAllHistory() {
		List<History> histoies = new ArrayList<History>();
		Cursor cursor = database.query(HistorySQLiteHelper.TABLE_HISTORY, allColumns, null, null, null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			History history = cursorToHistory(cursor);
			histoies.add(history);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return histoies;
	}

	public List<History> getAllFavs() {
		List<History> histoies = new ArrayList<History>();
		Cursor cursor = database.query(HistorySQLiteHelper.TABLE_HISTORY, allColumns, "type=" + History.TYPE_FAV, null,
				null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			History history = cursorToHistory(cursor);
			histoies.add(history);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		// Log.d("getAllFavs()", histoies.toString());
		return histoies;
	}

	public List<History> getAllReadHistory() {
		List<History> histoies = new ArrayList<History>();
		Cursor cursor = database.query(HistorySQLiteHelper.TABLE_HISTORY, allColumns, "type=" + History.TYPE_READ,
				null, null, null, "time desc");
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			History history = cursorToHistory(cursor);
			histoies.add(history);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return histoies;
	}

	public void deleteAllFavs() {
		database.delete(HistorySQLiteHelper.TABLE_HISTORY, "type=" + History.TYPE_FAV, null);
	}

	public void deleteAllReadHistories() {
		database.delete(HistorySQLiteHelper.TABLE_HISTORY, "type=" + History.TYPE_READ, null);
	}

	private History cursorToHistory(Cursor cursor) {
		History history = new History();
		history.setId(cursor.getLong(0));
		history.setType(cursor.getInt(1));
		history.setBook(cursor.getString(2));
		history.setChapter(cursor.getString(3));
		history.setLink(cursor.getString(4));
		history.setTime(cursor.getString(5));
		history.setStopPoint(cursor.getInt(6));
		history.setImgUrl(cursor.getString(7));
		history.setChapterIndex(cursor.getInt(8));
		return history;
	}
}
