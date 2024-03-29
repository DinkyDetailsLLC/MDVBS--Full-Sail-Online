package com.dan.weatherpull;

/**
 * This is our Service class that calls the API and fetches the data We still
 * needs to add two more buttons to Main Activity to fetch data for
 * corresponding days
 * 
 * @author admin
 * 
 */

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class WeatherService extends IntentService {

	// Defining Constants
	public static final String MESSENGER_KEY = "messenger";
	public static final String SEARCH_STRING = "user_input";

	Messenger messenger;
	Message message;
	String search_string;

	// Class Constructor
	public WeatherService() {
		super("WeatherService");
	}

	/**
	 * This method is used to make API call and fetch the data
	 * 
	 * @param intent
	 */

	@Override
	protected void onHandleIntent(Intent intent) {
		String baseURL = "http://api.wunderground.com/api/a988d453ebe759ad/forecast10day/q/";
		String endURL = ".json";
		URL finalURL;

		Log.i("onHandleIntent", "onHandleIntent started");

		Bundle extras = intent.getExtras();
		messenger = (Messenger) extras.get(MESSENGER_KEY);
		search_string = (String) extras.getString(SEARCH_STRING);

		try {
			search_string = URLEncoder.encode(search_string, "UTF-8");
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

			if (networkInfo != null && networkInfo.isConnected()) {
				try {
					finalURL = new URL(baseURL + search_string + endURL);
					Log.i("URL", finalURL.toString());
					String response = "";

					URLConnection conn = finalURL.openConnection();
					BufferedInputStream bin = new BufferedInputStream(
							conn.getInputStream());

					byte[] contentBytes = new byte[1024];
					int bytesRead = 0;
					StringBuffer responseBuffer = new StringBuffer();

					while ((bytesRead = bin.read(contentBytes)) != -1) {
						response = new String(contentBytes, 0, bytesRead);
						responseBuffer.append(response);
					}

					response = responseBuffer.toString();
					Log.i("JSON DATA", "JSON Response = " + response);

					URI[] uri = new URI[1];
					try {
						uri[0] = new URI(finalURL.toString());
					} catch (URISyntaxException use) {
						use.printStackTrace();
					}

					JSONObject json = null;
					try {
						json = new JSONObject(response);
						Log.i("JSON RESPONSE", "Valid JSON response" + json);
					} catch (JSONException je) {
						Log.e("ERROR StoreJSON", uri.toString());
						Log.e("ERROR StoreJSON", je.getMessage().toString());
					}

					/**
					 * Checking if JSON Response is null or not and saving all
					 * the required values in the database
					 */
					if (json != null) {
						if (json.toString().contains(
								"No cities match your search query")) {
							// Display Text in TextView
						} else {

							JSONArray weather = json.getJSONObject("forecast")
									.getJSONObject("simpleforecast")
									.getJSONArray("forecastday");

							for (int i = 0; i < weather.length(); i++) {
								JSONObject c = weather.getJSONObject(i);

								JSONObject date = c.getJSONObject("date");
								String weekday = date.getString("weekday");

								JSONObject high = c.getJSONObject("high");
								String fahrenheit = high
										.getString("fahrenheit");

								String skyicon = c.getString("conditions");

								JSONObject wind = c.getJSONObject("maxwind");
								String maxWind = wind.getString("mph");

								JSONObject aWind = c.getJSONObject("avewind");
								String avgWind = aWind.getString("mph");

								String maxHumidity = c.getString("maxhumidity");

								ContentValues values = new ContentValues();
								values.put(WeatherDb.KEY_WEEKDAY, weekday);
								values.put(WeatherDb.KEY_FAHRENHEIT, fahrenheit);
								values.put(WeatherDb.KEY_SKYICON, skyicon);
								values.put(WeatherDb.KEY_MAXWIND, maxWind);
								values.put(WeatherDb.KEY_AVGWIND, avgWind);
								values.put(WeatherDb.KEY_MAXHUMIDITY,
										maxHumidity);

								getContentResolver().insert(
										myContentProvider.CONTENT_URI, values);
								Log.i("Values saved in database are ", weekday
										+ " " + fahrenheit + " " + skyicon
										+ " " + maxWind + " " + avgWind + " "
										+ maxHumidity);

							}

						}
					}

				} catch (MalformedURLException mue) {
					Log.e("BAD URL", "Malformed URL");
					finalURL = null;
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"No Network Detected", Toast.LENGTH_SHORT);
				toast.show();
			}

			try {
				message = Message.obtain();
				message.arg1 = Activity.RESULT_OK;
				message.obj = "Data Service Complete";
				messenger.send(message);
				
				Log.d("Service Class", "onHandleIntent()");

			} catch (RemoteException re) {
				Log.e("Exception On Handle Intent", re.getMessage().toString());
				re.printStackTrace();
			}
		} catch (Exception e) {
			Log.e("Bad URL", "Encoding Problem");
			e.printStackTrace();
		}
	}
}