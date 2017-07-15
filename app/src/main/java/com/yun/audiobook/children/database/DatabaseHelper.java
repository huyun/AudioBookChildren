package com.yun.audiobook.children.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String LOG = DatabaseHelper.class.getName();

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "yunAudioBook";

	// Table Names
	private static final String TABLE_BOOK = "book";
	private static final String TABLE_CATEGORY = "category";
	private static final String TABLE_LANG = "language";

	// Common column names
	private static final String KEY_ID = "id";

	// table book columns
	public static final String COL_TITLE = "title";
	public static final String COL_IMG_URL = "imgUrl";
	public static final String COL_RSS_URL = "rssUrl";

	// table category columns
	public static final String COL_CAT_NAME = "name";
	public static final String COL_PARENT = "parentId";

	// table language columns
	public static final String COL_LANG_NAME = "name";

	// table book creation sql statement
	private static final String CREATE_TABLE_BOOK = "create table " + TABLE_BOOK + "(" + KEY_ID
			+ " integer primary key autoincrement, " + COL_TITLE + " VARCHAR," + COL_IMG_URL + " VARCHAR,"
			+ COL_RSS_URL + " VARCHAR);";

	// table book creation sql statement
	private static final String CREATE_TABLE_CATEGORY = "create table " + TABLE_CATEGORY + "(" + KEY_ID
			+ " integer primary key , " + COL_CAT_NAME + " VARCHAR," + COL_PARENT + " integer);";

	// table book creation sql statement
	private static final String CREATE_TABLE_LANGUAGE = "create table " + TABLE_LANG + "(" + KEY_ID
			+ " integer primary key , " + COL_LANG_NAME + " VARCHAR);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_LANGUAGE);
		
		database.execSQL(CREATE_TABLE_CATEGORY);
		database.execSQL(CREATE_TABLE_BOOK);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANG);
		onCreate(db);
	}

	/**
	 * Creating a language
	 */
	public long createLanguage(Language language) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COL_LANG_NAME, language.getName());

		// insert row
		long lang_id = db.insert(TABLE_LANG, null, values);
		return lang_id;
	}

	/**
	 * Creating a category
	 */
	public long createCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, category.getId());
		values.put(COL_CAT_NAME, category.getName());
		values.put(COL_PARENT, category.getParentId());
		// insert row
		long id = db.insert(TABLE_CATEGORY, null, values);
		return id;
	}

	public long createBook(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, category.getId());
		values.put(COL_CAT_NAME, category.getName());
		values.put(COL_PARENT, category.getParentId());
		// insert row
		long id = db.insert(TABLE_CATEGORY, null, values);
		return id;
	}

	public void insertLanguages() {
		// you can use INSERT only
		String sql = "INSERT OR REPLACE INTO " + TABLE_LANG + " (id, name) VALUES (?, ?)";

		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();

		SQLiteStatement stmt = db.compileStatement(sql);

		stmt.bindLong(1, 1);
		stmt.bindString(2, "English");
		stmt.execute();
		stmt.clearBindings();

		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	public void insertCategories() {
		// you can use INSERT only
		String sql = "INSERT OR REPLACE INTO " + TABLE_CATEGORY + " ( id, name, parentId ) VALUES (?, ?, ?)";

		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();

		SQLiteStatement stmt = db.compileStatement(sql);

		stmt.bindLong(1, 1);
		stmt.bindString(2, "Children's Fiction");
		stmt.bindLong(3, (Long) null);
		stmt.execute();
		stmt.clearBindings();

		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
