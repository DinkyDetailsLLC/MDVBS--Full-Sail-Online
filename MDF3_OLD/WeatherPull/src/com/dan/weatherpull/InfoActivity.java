package com.dan.weatherpull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class InfoActivity extends Activity {

	private TextView mInfoText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.infoactivity);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		Intent homeIntent = new Intent(this, MainActivity.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		actionBar.setTitle("Info Activity");
		actionBar.setHomeAction(new IntentAction(this, homeIntent,
				R.drawable.ic_menu_home));

		final Action infoAction = new infoOperation(
				R.drawable.ic_menu_info_details, this);
		actionBar.addAction(infoAction);

		final Action refreshAction = new RefreshOperation(
				R.drawable.ic_menu_refresh, this);
		actionBar.addAction(refreshAction);

		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"font/Aller.ttf");

		mInfoText = (TextView) findViewById(R.id.textInfo);
		mInfoText
				.setText("This Application fetches the Data from the Wunderground API for 1 day, 5 days and 10 days respectively.\n\nIt takes the zip code entered by the user and invokes the API in order to receive the JSON response.\n\nThat JSON Response is parsed in the Application and displayed in the List view.\n\nMoreover user has option to visit the Wunderground website on just click of a button.\n\nUser will be navigated to the weather details page on website for the zip code he entered.\n\nCreated By : Dan Annis");
		mInfoText.setTypeface(typeFace);
	}

	public static class RefreshOperation extends AbstractAction {
		public RefreshOperation(int drawable, Context context) {
			super(drawable);
		}

		@Override
		public void performAction(View view) {
			// TODO Auto-generated method stub
		}

	}

	public static class infoOperation extends AbstractAction {
		public infoOperation(int drawable, Context context) {
			super(drawable);
		}

		@Override
		public void performAction(View view) {
			// Do Nothing. Be on Same Screen
		}

	}
}
