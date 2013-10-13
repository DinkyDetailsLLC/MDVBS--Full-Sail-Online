package com.dinkydetails.dansimplebrowser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DanDataBase {
	public static final String KEY_USERID = "_id";
	public static final String KEY_URLNAME = "url";

	private static final String DATABASE_NAME = "UserDB";
	private static final String DATABASE_TABLE = "Urls";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table if not exists "
			+ DATABASE_TABLE + " (_id integer primary key autoincrement, "
			+ "url VARCHAR not null );";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DanDataBase(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("DanDatabase", "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DanDataBase open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a record into the database---
	public long insertRecord(String name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_URLNAME, name);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a particular record---
	public boolean deleteUser(long userId) {
		return db.delete(DATABASE_TABLE, KEY_USERID + "=" + userId, null) > 0;
	}

	// ---retrieves all the records---
	public Cursor getAllRecords() {
		return db.query(DATABASE_TABLE,
				new String[] { KEY_USERID, KEY_URLNAME }, null, null, null,
				null, null);
	}

	// ---retrieves a particular record---
	public Cursor getRecord(long userId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_USERID, KEY_URLNAME },
				KEY_USERID + "=" + userId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();

		}
		return mCursor;
	}

	// ---updates a record---
	public boolean updateRecord(long userId, String name) {
		ContentValues args = new ContentValues();
		args.put(KEY_URLNAME, name);
		return db.update(DATABASE_TABLE, args, KEY_USERID + "=" + userId, null) > 0;
	}
}
