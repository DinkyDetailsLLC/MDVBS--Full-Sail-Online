package com.dan.weatherpull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, createIntent(this),
				R.drawable.ic_menu_home));
		actionBar.setTitle("Home");
		actionBar.addAction(new Action() {
			@Override
			public void performAction(View view) {
				Intent infoIntent = new Intent(MainActivity.this,
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
				Toast.makeText(MainActivity.this,
						"This will refresh the Weather Data",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public int getDrawable() {
				return R.drawable.ic_menu_refresh;
			}
		});
	}

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
