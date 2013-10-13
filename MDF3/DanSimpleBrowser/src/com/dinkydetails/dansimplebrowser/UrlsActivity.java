package com.dinkydetails.dansimplebrowser;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class UrlsActivity extends Activity {
	DanDataBase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitylistview);

		db = new DanDataBase(getApplicationContext());
		db.open();

		populateListViewFromDB();
	}

	@SuppressWarnings("deprecation")
	private void populateListViewFromDB() {
		Cursor cursor = db.getAllRecords();
		startManagingCursor(cursor);
		Log.i("MyApp", "Total users: " + cursor.getCount());
		Toast.makeText(getApplicationContext(),
				"Number of rows: " + cursor.getCount(), Toast.LENGTH_LONG)
				.show();

		String[] databaseColumnNames = new String[] { DanDataBase.KEY_URLNAME };
		int[] toViewIDs = new int[] { R.id.textViewName};

		SimpleCursorAdapter myCursordapter = new SimpleCursorAdapter(this,
				R.layout.urllayout, cursor, databaseColumnNames,
				toViewIDs);

		ListView list = (ListView) findViewById(R.id.lv_name);
		list.setAdapter(myCursordapter);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
	}

}
