//Dan Annis
//MDF3 Week 3
// Weather Pull App with Action Bar & Widget
// Action Bar Library help from - https://github.com/johannilsson/android-actionbar



package com.dan.weatherpull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class InfoActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		Intent homeIntent = new Intent(this, InfoActivity.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		actionBar.setTitle("Info");
		actionBar.setHomeAction(new IntentAction(this, homeIntent,
				R.drawable.ic_menu_home));

		final Action infoAction = new infoOperation(
				R.drawable.ic_menu_info_details, this);
		actionBar.addAction(infoAction);

		final Action refreshAction = new RefreshOperation(
				R.drawable.ic_menu_refresh, this);
		actionBar.addAction(refreshAction);
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
			// TODO Auto-generated method stub
		}

	}
}