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
		
		//create instance of sensor manager and get system service to interact with Sensor
		isSensorInstalled = true;
		
		sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		Sensor proximitySensor= sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		if (proximitySensor == null){
			isSensorInstalled = false;
			Toast.makeText(MainActivity.this,"No Proximity Sensor Found! ",Toast.LENGTH_LONG).show();
		}
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

}
