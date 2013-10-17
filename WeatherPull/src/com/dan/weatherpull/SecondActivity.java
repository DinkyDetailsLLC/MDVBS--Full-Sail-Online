package com.dan.weatherpull;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity implements OnClickListener {

	private TextView mHeading;
	private TextView mWeekday;
	private TextView mTemp;
	private TextView mSkyIcon;
	private TextView mMaxWind;
	private TextView mAvgWind;
	private TextView mMaxHumidity;

	private Button btnImplicit;
	private Button btnBack;

	String weekday;
	String fahrenheit;
	String skyicon;
	String maxWind;
	String avgWind;
	String maxHumidity;

	Typeface typeFace;

	private SQLiteDatabase db;

	private String url = "http://api.wunderground.com/cgi-bin/findweather/hdfForecast?query=";
	private String zipCode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_second);
		
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, createIntent(this),
				R.drawable.ic_menu_home));
		actionBar.setTitle("Details Activity");
		actionBar.addAction(new Action() {
			@Override
			public void performAction(View view) {
				Intent infoIntent = new Intent(SecondActivity.this,
						InfoActivity.class);
				startActivity(infoIntent);
			}

			@Override
			public int getDrawable() {
				return R.drawable.ic_menu_info_details;
			}
		});

		actionBar.addAction(new Action() {
			@Override
			public void performAction(View view) {
				Toast.makeText(SecondActivity.this,
						"This will refresh the Weather Data",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public int getDrawable() {
				return R.drawable.ic_menu_refresh;
			}
		});



		Bundle params = getIntent().getExtras();
		if (params != null) {
			zipCode = params.getString("zip");
		}

		url = url + zipCode;

		typeFace = Typeface.createFromAsset(getAssets(), "font/Aller.ttf");

		MyDataBaseHelper helper = new MyDataBaseHelper(this);
		db = helper.getInstance(this).getWritableDatabase();

		mHeading = (TextView) findViewById(R.id.textheading);
		mWeekday = (TextView) findViewById(R.id.textweekday);
		mTemp = (TextView) findViewById(R.id.texttemp);
		mSkyIcon = (TextView) findViewById(R.id.textskyicon);
		mMaxWind = (TextView) findViewById(R.id.textmaxwind);
		mAvgWind = (TextView) findViewById(R.id.textavgwind);
		mMaxHumidity = (TextView) findViewById(R.id.textmaxhumid);

		mHeading.setTypeface(typeFace);
		mWeekday.setTypeface(typeFace);
		mTemp.setTypeface(typeFace);
		mSkyIcon.setTypeface(typeFace);
		mMaxWind.setTypeface(typeFace);
		mAvgWind.setTypeface(typeFace);
		mMaxHumidity.setTypeface(typeFace);

		displayData();

		btnImplicit = (Button) findViewById(R.id.btnimplicit);
		btnBack = (Button) findViewById(R.id.btnback);

		btnImplicit.setTypeface(typeFace);
		btnBack.setTypeface(typeFace);

		btnImplicit.setOnClickListener(this);
		btnBack.setOnClickListener(this);

	}

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}
	
	private void displayData() {
		Cursor cursor = db.rawQuery("SELECT * FROM " + WeatherDb.SQLITE_TABLE,
				null);
		if (cursor != null && cursor.moveToFirst()) {
			weekday = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
			fahrenheit = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
			skyicon = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));
			maxWind = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_MAXWIND));
			avgWind = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_AVGWIND));
			maxHumidity = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_MAXHUMIDITY));

			mHeading.setText("Today's Details for " + zipCode);
			mWeekday.setText("Weekday : " + weekday);
			mTemp.setText("Temprature : " + fahrenheit + " F");
			mSkyIcon.setText("Sky : " + skyicon);
			mMaxWind.setText("Max Wind : " + maxWind + " mph");
			mAvgWind.setText("Average Wind : " + avgWind + " mph");
			mMaxHumidity.setText("Max Humidity : " + maxHumidity);

		} else {
			mHeading.setText("No cities match your search query");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnimplicit:
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(i);
			break;

		case R.id.btnback:
			Intent iBackIntent = new Intent(SecondActivity.this,
					MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("maxWind", maxWind);
			bundle.putString("avgWind", avgWind);
			bundle.putString("maxHumidity", maxHumidity);
			bundle.putString("zip", zipCode);
			iBackIntent.putExtras(bundle);
			startActivity(iBackIntent);
			finish();
			break;
		}

	}

	public void finish() {
		super.finish();
		Intent data = new Intent();
		data.putExtra("maxWind", maxWind);
		data.putExtra("avgWind", avgWind);
		data.putExtra("maxHumidity", maxHumidity);
		setResult(RESULT_OK, data);
	}
}
