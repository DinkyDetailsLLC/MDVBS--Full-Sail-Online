//Dan Annis
//MDF3--- Week 2 Redo


package com.dinkydetails.ironmanfan;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
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
import android.os.BatteryManager;

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
		protected String doInBackground(String... params) {
			int count;
			try {
				//Open a Connection and a buffer stream to prepare for storage
				//****ERROR Need to get my video part set up to where I am actually parsing a video
				URL url = new URL(videoUrl[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				int lenghtOfFile = conection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);
				//Save the file to external Directory.
				OutputStream output = new FileOutputStream(Environment
						.getExternalStorageDirectory().getPath().toString()
						+ "/takeaway.mp4");
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
		//setup for my spinner if nothing is selected. Prereq*
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
		
		
		
	
	};
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
