package com.dinkydetails.handsontunes;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener{
	//SensorManager lets you access the device's sensors
	//Declare Variables
		private SensorManager sensorManager;
		boolean isSensorInstalled;
		
		TextView tVProximity;
		Button btnToggle;
		
		boolean isSensorEnabled;
		
		MediaPlayer player;
		
		Button btnPlayer;
		Button btnPause;
	//Call this activity when the app is created
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tVProximity = (TextView)findViewById(R.id.tVProximity);
		
		//Create instance of sensor manager and get system service to interact with Sensor
		isSensorInstalled = true;
		
		sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		Sensor proximitySensor= sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		// If sensor is not installed then put up a message saying there is no sensor.
		if (proximitySensor == null){
			isSensorInstalled = false;
			Toast.makeText(MainActivity.this,"No Proximity Sensor Found! ",Toast.LENGTH_LONG).show();
		}
		
		//Set the player to play the song stored in the raw file
		player = MediaPlayer.create(this, R.raw.jinglebellsrock);
		//keep it playing!
		player.setLooping(true);
		
		//Tie the button to the button and listen in for what to do with it
		btnToggle = (Button) findViewById(R.id.toggle);
		btnToggle.setOnClickListener(new OnClickListener() {
 
			@Override
			//Turn on and off the sensor to not interfere with the play/stop buttons
			public void onClick(View arg0) {
				if (isSensorInstalled == true)
					isSensorEnabled = ! isSensorEnabled;
				
				layoutViews(isSensorEnabled);
			}
 
		});
		//Setting up the play button.. 
		btnPlayer = (Button) findViewById(R.id.play);
		btnPlayer.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				//If the Sensor is off and the player is off, play the music
				if (isSensorEnabled == false && player.isPlaying() == false)
					player.start();
			}
 
		});
		//Setting up the pause button.. 
		btnPause = (Button) findViewById(R.id.pause);
		btnPause.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				//If the Sensor is off and the player is playing, pause the music
				if (isSensorEnabled == false && player.isPlaying() == true)
					player.pause();
			}
 
		});
		
		isSensorEnabled = (isSensorInstalled == true)? true: false;
		layoutViews(isSensorEnabled);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}
//Set what the button toggle is going to show. 
	public void layoutViews (boolean _isSensorEnabled) {
		if (_isSensorEnabled == true)
		{
			//If it is on... Hide the buttons
			btnToggle.setText("Sensor Enabled");
			btnPlayer.setVisibility(View.GONE);
			btnPause.setVisibility(View.GONE);
		}
		else 
		{
			//If it is off... Show the buttons
			btnToggle.setText("Sensor disabled");
			btnPlayer.setVisibility(View.VISIBLE);
			btnPause.setVisibility(View.VISIBLE);
		}
	}
}
