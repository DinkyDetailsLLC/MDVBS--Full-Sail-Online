//Dan Annis
//MDF3--- Week 2 Redo


package com.dinkydetails.ironmanfan;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;
import android.os.BatteryManager;

//Implementing the OnClick and the OnItem for Buttons and Spinners
public class MainActivity extends Activity implements OnClickListener
{

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
		
		//Setting up the reciever intent to get the batter information
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
	//	videoSpinner.setOnItemSelectedListener(this);
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
		
			
		case R.id.savevideo:
			
			
		case R.id.checkbattery:
			
		}

	}
	
	
	
	};
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
