package com.dan.weatherpull;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class WidgetProvider extends AppWidgetProvider {

	//Provider that calls the service and creates the widget when the zip is called
	//Had to go this route because I was trying to get my data from the DB and after
	//further research, this was the best way I could figure out/find how to implement
	//the methods and code i needed to get the data and display it. 
	//maybe making it harder than it needs to be, but.. its working. 
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		ComponentName thisWidget = new ComponentName(context,
				WidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		
		Intent intent = new Intent(context.getApplicationContext(),
				UpdateWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		context.startService(intent);
	}
}

//Did not implement the onDelete 