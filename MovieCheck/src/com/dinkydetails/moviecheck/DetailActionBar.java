package com.dinkydetails.moviecheck;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActionBar extends Activity implements
		ActionBar.OnNavigationListener {

	// action bar
	private ActionBar actionBar;
	String url = "";
	// Title navigation Spinner data
	TextView txtmovietitle;
	TextView txtplot;
	TextView txtrattings;
	// Navigation adapter
	ImageView myimageview;
	// Refresh menu itemmyimageview
	private MenuItem refreshMenuItem;
	String value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);

		value = getIntent().getExtras().getString("zip");

		if (value.length() <= 0) {
			Toast.makeText(DetailActionBar.this,
					"There is some problem! Please try again.",
					Toast.LENGTH_LONG).show();
			finish();
		} else {
			actionBar = getActionBar();

			txtmovietitle = (TextView) findViewById(R.id.txtmovietitle);
			txtplot = (TextView) findViewById(R.id.txtplot);
			txtrattings = (TextView) findViewById(R.id.txtrattings);
			myimageview = (ImageView) findViewById(R.id.myimageview);

			try {
				new FetchAsyncTask().execute();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		// finish();
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_search:

			return true;
		case R.id.action_refresh:
			try {
				new FetchAsyncTask().execute();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return true;
		case R.id.action_check_updates:
			LocationFound();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Launching new activity
	 * */
	private void LocationFound() {
		Intent i = new Intent(DetailActionBar.this, More.class);
		startActivity(i);
	}

	/*
	 * Actionbar navigation item select listener
	 */
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// Action to be taken after selecting a spinner item
		return false;
	}

	/**
	 * Async task to load the data from server
	 * **/
	private class SyncData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			// set the progress bar view
			refreshMenuItem.setActionView(R.layout.action_progressbar);
	//		refreshMenuItem.expandActionView();
		}

		@Override
		protected String doInBackground(String... params) {
			// not making real request in this demo
			// for now we use a timer to wait for sometime
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			refreshMenuItem.collapseActionView();
			// remove the progress bar view
			refreshMenuItem.setActionView(null);
		}
	};

	String title, plot, rattings, imageurl;

	ProgressDialog pd;

	public class FetchAsyncTask extends AsyncTask<String, Integer, String> {

		static final String ERROR = "Some Problem .";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(DetailActionBar.this);
			pd.setMessage("Loading...");
			pd.setTitle("Please wait");
			pd.show();
			// Utils.showProgress(ctx, true, progressBar, null);

		}

		@Override
		protected String doInBackground(String... params) {
			try {
				value = value.replaceAll(" ", "%20");
				String _url = "http://www.omdbapi.com/?t=" + value + "&r=JSON";

				JSONObject response = JsonFunctions.getJSONArrayfromURL(_url);

				title = response.getString("Title");

				plot = response.getString("Plot");

				rattings = response.getString("Rated");
				imageurl = response.getString("Poster");
				Log.e("title", "" + title);
				Log.e("titleLang", "" + plot);

				// }
				// }

			} catch (Exception e) {
				e.printStackTrace();
				return null;

			}

			return "success";

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Utils.showProgress(ctx, false, progressBar, null);
			if (pd.isShowing())
				pd.dismiss();
			if (result != null) {
				if (result.equals(ERROR)) {

					Toast.makeText(DetailActionBar.this, ERROR,
							Toast.LENGTH_SHORT).show();
					finish();
				} else {

					// Drawable drw = LoadImageFromWebOperations(imageurl);
					// myimageview.setImageDrawable(drw);
					new DownloadImageTask(
							(ImageView) findViewById(R.id.myimageview))
							.execute(imageurl);
					txtmovietitle.setText(title);
					txtplot.setText(plot);
					txtrattings.setText("Rating: " + rattings);
				}

			} else {

			}
			// Toast.makeText(ctx, ERROR, Toast.LENGTH_SHORT).show();
		}
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

}
