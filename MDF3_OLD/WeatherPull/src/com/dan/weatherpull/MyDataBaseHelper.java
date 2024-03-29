package com.dan.weatherpull;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is a singleton class
 * 
 * @author admin
 * 
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Weather";
	private static final int DATABASE_VERSION = 1;

	@SuppressWarnings("unused")
	private final Context myContext;
	private static MyDataBaseHelper mInstance;
	private static SQLiteDatabase myWritableDb;

	/**
	 * Constructor takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 *            the application context
	 */
	public MyDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}

	/**
	 * Get default instance of the class to keep it a singleton
	 * 
	 * @param context
	 *            the application context
	 */
	public MyDataBaseHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MyDataBaseHelper(context);
		}
		return mInstance;
	}

	/**
	 * Returns a writable database instance in order not to open and close many
	 * SQLiteDatabase objects simultaneously
	 * 
	 * @return a writable instance to SQLiteDatabase
	 */
	public SQLiteDatabase getMyWritableDatabase() {
		if ((myWritableDb == null) || (!myWritableDb.isOpen())) {
			myWritableDb = this.getWritableDatabase();
		}

		return myWritableDb;
	}

	@Override
	public void close() {
		super.close();
		if (myWritableDb != null) {
			myWritableDb.close();
			myWritableDb = null;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		WeatherDb.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		WeatherDb.onUpgrade(db, oldVersion, newVersion);
	}

}
