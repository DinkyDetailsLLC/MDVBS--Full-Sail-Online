package com.dan.weatherpull;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.dan.weatherpull.MyDataBaseHelper;
import com.dan.weatherpull.R;
import com.dan.weatherpull.WeatherDb;

public class UpdateWidgetService extends Service {
//Service to be ran to access the database with the stored zip and get the details
	private SQLiteDatabase db;

	@Override
	public void onStart(Intent intent, int startId) {
// This allows us to use a widget
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
				.getApplicationContext());
//Helps gain access to the DB
		MyDataBaseHelper helper = new MyDataBaseHelper(this);
		db = helper.getInstance(this).getWritableDatabase();
//looks for the ids to reference the data	
		int[] allWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		//grab the data in each column row
		//Get one, we should be able to get them all :)
		
		for (int widgetId : allWidgetIds) {
			Cursor cursor = db.rawQuery("SELECT * FROM "
					+ WeatherDb.SQLITE_TABLE, null);
			if (cursor != null && cursor.moveToFirst()) {
				String weekday = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));
				String maxWind = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_MAXWIND));
				String avgWind = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_AVGWIND));
				String maxHumidity = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_MAXHUMIDITY));
//This sets the textview up for the data to be placed in each row
				RemoteViews remoteViews = new RemoteViews(this
						.getApplicationContext().getPackageName(),
						R.layout.widgetlayout);
				remoteViews.setTextViewText(R.id.textweekday, "Day of Week: "
						+ weekday);
				remoteViews.setTextViewText(R.id.texttemp, "Temprature : "
						+ fahrenheit + " F");
				remoteViews.setTextViewText(R.id.textskyicon, "Sky : "
						+ skyicon);
				remoteViews.setTextViewText(R.id.textmaxwind, "Max Wind : "
						+ maxWind);
				remoteViews.setTextViewText(R.id.textavgwind, "Avg Wind : "
						+ avgWind);
				remoteViews.setTextViewText(R.id.textmaxhumid,
						"Max Humidity : " + maxHumidity);
					//Send the data to the provider
				Intent clickIntent = new Intent(this.getApplicationContext(),
						WidgetProvider.class);
				//Keys for the app widget manager itself
				clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
				clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
						allWidgetIds);
				
				//should be able to change the date in the widget.. but it isn't working/updating
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						getApplicationContext(), 0, clickIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.textweekday,
						pendingIntent);
				appWidgetManager.updateAppWidget(widgetId, remoteViews);
				
				// Can't get widget to update on its own once a new zip is used
				
			}
			stopSelf();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
