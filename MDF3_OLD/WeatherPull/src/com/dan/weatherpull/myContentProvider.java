package com.dan.weatherpull;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class myContentProvider extends ContentProvider {

	private MyDataBaseHelper dbHelper;

	private static final int TEN_DAYS = 1;
	private static final int FIVE_DAYS = 2;
	private static final int SINGLE_DAY = 3;

	private static final String AUTHORITY = "com.dinkydetails.weatherpull.myContentProvider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/Weather");

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "Weather", TEN_DAYS);
		uriMatcher.addURI(AUTHORITY, "Weather/*", FIVE_DAYS);
		uriMatcher.addURI(AUTHORITY, "Weather/#", SINGLE_DAY);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new MyDataBaseHelper(getContext());
		return false;
	}

	@Override
	public String getType(Uri uri) {

		switch (uriMatcher.match(uri)) {
		case TEN_DAYS:
			return "com.dinkydetails.weatherpull.Weather";
		case FIVE_DAYS:
			return "com.dinkydetails.weatherpull.Weather";
		case SINGLE_DAY:
			return "com.dinkydetails.weatherpull.Weather";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch (uriMatcher.match(uri)) {
		case TEN_DAYS:
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		long id = db.insert(WeatherDb.SQLITE_TABLE, null, values);
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(CONTENT_URI + "/" + id);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(WeatherDb.SQLITE_TABLE);

		switch (uriMatcher.match(uri)) {
		case TEN_DAYS:
			queryBuilder.appendWhere(WeatherDb.KEY_ROWID + "=" + 10);
			break;
		case FIVE_DAYS:
			queryBuilder.appendWhere(WeatherDb.KEY_ROWID + "=" + 5);
			break;
		case SINGLE_DAY:
			queryBuilder.appendWhere(WeatherDb.KEY_ROWID + "=" + 1);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		return cursor;

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case TEN_DAYS:
			db.delete(WeatherDb.SQLITE_TABLE, null, null);
			break;
		case FIVE_DAYS:
			db.delete(WeatherDb.SQLITE_TABLE, null, null);
			break;
		case SINGLE_DAY:
			db.delete(WeatherDb.SQLITE_TABLE, null, null);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		int deleteCount = db.delete(WeatherDb.SQLITE_TABLE, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case TEN_DAYS:
			break;
		case SINGLE_DAY:
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		int updateCount = db.update(WeatherDb.SQLITE_TABLE, values, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
	}
}
