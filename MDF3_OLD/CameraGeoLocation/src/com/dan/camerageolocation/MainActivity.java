package com.dan.camerageolocation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	//Variables setup
	private static final int CAMERA_PIC_REQUEST = 1111;
	private ImageView mImage;
	private TextView mBatteryInfo;
	private EditText mPicName;
	private Button mSaveName;
	private Button mTakePic;
	private Button mSavePic;
	private Button mGetLocation;
	private Button mBatteryStats;

	private LinearLayout mNameLayout;

	//additional misc variables
	int level = 0;
	Context ctx;
	GPSTracker gps;
	ByteArrayOutputStream bytes;
	double latitude;
	double longitude;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Setting app to a full screen layout
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		//Changing the font type
		Typeface fontAller = Typeface.createFromAsset(getAssets(),
				"font/Aller.ttf");
		
		//Adding an intent Action to understand the battery and its information
		this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		
		//identifying the components.
		mNameLayout = (LinearLayout) findViewById(R.id.namelayout);

		mBatteryInfo = (TextView) findViewById(R.id.batteryinfo);
		mBatteryInfo.setTypeface(fontAller);
		mImage = (ImageView) findViewById(R.id.camera_image);

		mPicName = (EditText) findViewById(R.id.picname);
		mPicName.setTypeface(fontAller);

		mSaveName = (Button) findViewById(R.id.savename);
		mTakePic = (Button) findViewById(R.id.takepic);
		mSavePic = (Button) findViewById(R.id.savepic);
		mGetLocation = (Button) findViewById(R.id.getloc);
		mBatteryStats = (Button) findViewById(R.id.checkbattery);
		//setting fonts
		mSaveName.setTypeface(fontAller);
		mTakePic.setTypeface(fontAller);
		mSavePic.setTypeface(fontAller);
		mGetLocation.setTypeface(fontAller);
		mBatteryStats.setTypeface(fontAller);
		//setting the listeners for the buttons
		mSaveName.setOnClickListener(this);
		mTakePic.setOnClickListener(this);
		mSavePic.setOnClickListener(this);
		mGetLocation.setOnClickListener(this);
		mBatteryStats.setOnClickListener(this);

	}
	//Grabbing the battery info %
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		}
	};
	//Picture formatting and handling the cancel button
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST && data != null && resultCode != 0) {
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			mImage.setImageBitmap(thumbnail);
			bytes = new ByteArrayOutputStream();
			thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		} else {
			Toast.makeText(this, "No Picture was taken", Toast.LENGTH_SHORT)
					.show();
		}
	}
	//Setting up the view
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.takepic:
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, CAMERA_PIC_REQUEST);
			break;

		case R.id.savepic:
			mBatteryInfo.setText("Please Enter Picture Name :");
			mNameLayout.setVisibility(View.VISIBLE);
			break;

		case R.id.getloc:
			mNameLayout.setVisibility(View.GONE);
			gps = new GPSTracker(MainActivity.this);
			//get the location and use the GPS Tracker to get Geo Point
			if (gps.canGetLocation()) {
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				String address = "Not Found";
				address = findUsersAddress(latitude, longitude);
				mBatteryInfo.setText("Latitude : "
						+ latitude + "\nLongitude : " + longitude
						+ "\n\nAddress is : " + address);
			} else {
				gps.showSettingsAlert();
			}

			break;
		//Save the filename to the string.. make sure there is something
		case R.id.savename:
			String picName = mPicName.getText().toString().trim();
			if (picName.length() > 0) {
				showAlertforLocation(picName);
			} else {
				Toast.makeText(this,
						"Please enter the picture name to save it to SD Card",
						Toast.LENGTH_SHORT).show();
			}
			break;
		//Setting up a case for if the battery is less than 20, turn off the GPS
		case R.id.checkbattery:
			mNameLayout.setVisibility(View.GONE);
			mBatteryInfo.setText("Current level of battery is : " + level);
			if (level < 20) {
				mBatteryInfo.setText("Current level of battery is : " + level
						+ ".\nGPS is turned off.");
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
	//Wanted to add the location to the name if the user chooses
	private void showAlertforLocation(final String picName) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Location?");
		//setting up the alert and the functions to handle the answer
		alertDialogBuilder
				.setMessage("Would you like to Include Location in Picture name?")
				.setCancelable(false)
				//If yes, save the file
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (bytes != null && latitude != 0.0
										&& longitude != 0.0) {
									String path = Environment
											.getExternalStorageDirectory()
											.toString();
									new File(path + "/DanPics").mkdirs();
									File file = new File(path + "/DanPics/"
											+ picName + "-" + latitude + "-"
											+ longitude + ".jpg");
									try {
										file.createNewFile();
										FileOutputStream fo = new FileOutputStream(
												file);
										fo.write(bytes.toByteArray());
										fo.close();
										Toast.makeText(MainActivity.this,
												"Picture saved successfully",
												Toast.LENGTH_SHORT).show();
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else {
									//check to make sure location was clicked and a pic was taken
									Toast.makeText(
											MainActivity.this,
											"No Picture was found, Did you check your Location first?",
											Toast.LENGTH_SHORT).show();
								}
								//MainActivity.this.finish();
							}
						})
						
				//If user clicks no, let it go and just save the name
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (bytes != null) {
							String path = Environment
									.getExternalStorageDirectory().toString();
							new File(path + "/DanPics").mkdirs();
							File file = new File(path + "/DanPics/" + picName
									+ ".jpg");
							try {
								file.createNewFile();
								FileOutputStream fo = new FileOutputStream(file);
								fo.write(bytes.toByteArray());
								fo.close();
								Toast.makeText(MainActivity.this,
										"Picture saved successfully",
										Toast.LENGTH_SHORT).show();
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							Toast.makeText(MainActivity.this,
									"No Picture was taken", Toast.LENGTH_SHORT)
									.show();
						}
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	};
	//Using the geocoder to take the lat and long and convert it to physical address
	private String findUsersAddress(double latitude, double longitude) {
		String userAddress = null;
		Geocoder gc = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = gc
					.getFromLocation(latitude, longitude, 1);
			StringBuilder sb = new StringBuilder();
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {

					if (address.getAddressLine(i) != null) {
						sb.append(address.getAddressLine(i)).append("\n");
					}
				}

				if (address.getLocality() != null) {
					sb.append(address.getLocality()).append("\n");
				}

				if (address.getPostalCode() != null) {
					sb.append(address.getPostalCode()).append("\n");
				}

				if (address.getCountryName() != null) {
					sb.append(address.getCountryName());
				}
			}

			userAddress = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return userAddress;
	}
  //turn the gps off
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
}
