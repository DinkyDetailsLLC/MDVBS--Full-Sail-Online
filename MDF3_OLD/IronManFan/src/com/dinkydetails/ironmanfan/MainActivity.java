//Dan Annis
//MDF3--- Week 2 Redo


package com.dinkydetails.ironmanfan;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

//Implementing the OnClick and the OnItem for Buttons and Spinners
public class MainActivity extends Activity implements OnClickListener,
OnItemSelectedListener {
	
	//Setting up variables
	private Button mPlayVideo;
	private Button mSaveVideo;
	private Button mCheckBattery;
	private ProgressDialog pDialog;
	private ProgressDialog pVideoDialog;

	private TextView mBatteryInfo;
	private Spinner videoSpinner;
	
	// Setting additional variables to names and references
	VideoView videoview;
	String videoUrl;
	int level = 0;
	String position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Tell the system not to have a title and keep it @ Full Screen. 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//Setting the main Activity
		setContentView(R.layout.activity_main);
		
		//Setting up the receiver intent to get the batter information
		this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		
		//Accessing the Progress Dialog items and setting them to variables
		pDialog = new ProgressDialog(MainActivity.this);
		pVideoDialog = new ProgressDialog(MainActivity.this);
		
		//Linking the items in XML to a variable
		videoview = (VideoView) findViewById(R.id.VideoView);
		mPlayVideo = (Button) findViewById(R.id.playvideo);
		mSaveVideo = (Button) findViewById(R.id.savevideo);
		mCheckBattery = (Button) findViewById(R.id.checkbattery);
		mPlayVideo.setOnClickListener(this);
		mSaveVideo.setOnClickListener(this);
		mCheckBattery.setOnClickListener(this);
		videoSpinner = (Spinner) findViewById(R.id.spinner1);
		videoSpinner.setOnItemSelectedListener(this);
		mBatteryInfo = (TextView) findViewById(R.id.batteryinfo);
		
	}
	//Broadcast Receiver to listen and check the current battery level
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		}
	};
	
	//setting up the Cases for when a button is clicked and what it should do... 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.playvideo:
			//Setup my dialog for when the video has started to pull
			pVideoDialog.setTitle("Streaming Video");
			pVideoDialog.setMessage("Buffering...");
			pVideoDialog.setIndeterminate(false);
			pVideoDialog.setCancelable(true);
			pVideoDialog.show();
			
			try {
			//Setting up the MediaController to handle the video
			MediaController mediacontroller = new MediaController(
					MainActivity.this);
			//Put the controller into my view and grab the VideoURL to play
			mediacontroller.setAnchorView(videoview);
			Uri video = Uri.parse(videoUrl);
			videoview.setMediaController(mediacontroller);
			videoview.setVideoURI(video);
			
			//What happens if there is an error
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			//Assuming all goes well.. prepare the videoview with the listener and start to play
			videoview.requestFocus();
			videoview.setOnPreparedListener(new OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) {
					pVideoDialog.dismiss();
					//Automatically strart playing
					videoview.start();
				}
			});
			
			break;
			//If the Save Video is pressed... Run the Download Function
		case R.id.savevideo:
			new DownloadVideo().execute(videoUrl);
			break;
			
		case R.id.checkbattery:
			mBatteryInfo.setText("Current level of battery is : " + level);
			//Checking the Battery level
			if (level < 20) {
				mBatteryInfo.setText("Current level of battery is : " + level
						+ ".\nGPS is turned off.");
				//Turn the GPS off it is running. Function below
				turnGPSOnOff();
			} else {
				mBatteryInfo
						.setText("Current level of battery is : "
								+ level
								+ ".\n\nGPS will be automatically turned off when battery level reaches below 20.");
			}
			break;
		}

	}
	//Need to create an async task to get my item to download from the web
	class DownloadVideo extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Downloading file...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		//Implementation to run and open up a stream to actually download the data
		@Override
		protected String doInBackground(String... videoUrl) {
			int count;
			try {
				//Open a Connection and a buffer stream to prepare for storage
				URL url = new URL(videoUrl[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				int lenghtOfFile = conection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);
				//Save the file to external Directory.
				OutputStream output = new FileOutputStream(Environment
						.getExternalStorageDirectory().getPath().toString()
						+ "/IronManFanVideo.mp4");
				//writing the data to the space
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}
				//Finalizing Streams
				output.flush();
				output.close();
				input.close();
				//Exception if errored
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}
			
			return null;
		}
		//Standard function for the Progress Dialog
		protected void onProgressUpdate(String... progress) {
			pDialog.setProgress(Integer.parseInt(progress[0]));
		}
		
		//Dismiss the dialog once file is posted
		@Override
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
	}
	
		
		//Setting up the options for my Spinner
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			position = parent.getItemAtPosition(pos).toString();
			if (position.equalsIgnoreCase("Iron Man 1")) {
				//URL of hosted Video 1
				videoUrl = "http://pocketparties.mobi/assets/IronMan_1.mp4";
			} else if (position.equalsIgnoreCase("Iron Man 2")){
				//URL of hosted Video 2
				videoUrl = "http://pocketparties.mobi/assets/IronMan_2.mp4";
			} else if (position.equalsIgnoreCase("Iron Man 3")){
				//URL of hosted Video 3
				videoUrl = "http://pocketparties.mobi/assets/IronMan_3.mp4";
			}
		}
		
		//setup for my spinner if nothing is selected. Pre req*
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
		
		private void turnGPSOnOff() {
			String provider = Settings.Secure.getString(getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			if (!provider.contains("gps")) {
				final Intent poke = new Intent();
				poke.setClassName("com.android.settings",
						"com.android.settings.widget.SettingsAppWidgetProvider");
				poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
				poke.setData(Uri.parse("3"));
				sendBroadcast(poke);
			}
		}

		
		/* (non-Javadoc)
		 * @see android.app.Activity#dispatchKeyEvent(android.view.KeyEvent)
		 */
		@Override
		//Records the key events
		public boolean dispatchKeyEvent(KeyEvent event) {
			return super.dispatchKeyEvent(event);
		}		
		
		//Use the "Back button to handle dismissing if a video is too long
		@Override
		public void onBackPressed() {
			super.onBackPressed();
			if (pVideoDialog.isShowing()) {
				pVideoDialog.cancel();
				pVideoDialog.dismiss();
			} else if (pDialog.isShowing()) {
				pDialog.cancel();
				pDialog.dismiss();
			} else {
				MainActivity.this.finish();
			}
		}
}
	
	
