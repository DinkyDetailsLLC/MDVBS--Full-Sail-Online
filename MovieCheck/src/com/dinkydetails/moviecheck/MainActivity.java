package com.dinkydetails.moviecheck;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	// Setting up known variables
	JavaScriptInterface JSInterface;

	Button refuse;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// changing main layout to show the splash first
		setContentView(R.layout.activity_main);

		// Tie my webviews and set up the webview to handle JS
		WebView webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		// Expecting UI
		webView.setWebChromeClient(new WebChromeClient());
		// allowing to inject Java objects into a page's JavaScript
		webView.addJavascriptInterface(new JavaScriptInterface(this), "webapi");
		// Load the local file
		webView.loadUrl("file:///android_asset/www/index.html");

		refuse = new Button(MainActivity.this);
		refuse.setOnClickListener(this);

		WebSettings ws = webView.getSettings();
		ws.setJavaScriptEnabled(true);
		// Add the interface to record javascript events
		// webView.addJavascriptInterface(valid, "valid");
		webView.addJavascriptInterface(refuse, "refuse");

	}

	public class JavaScriptInterface {
		Context mContext;

		// Instantiate the interface and set the context
		JavaScriptInterface(Context c) {
			mContext = c;
		}

		// Call the function changeActivity defined in my JS of my HTML
		public void changeActivity(String movieName) {
			// Call the Movie App and store the intent to pass it onto the app.
			Log.e("XXXXXXXXXXXXXXX", "X==========>" + movieName);
			Intent i = new Intent(MainActivity.this, second.class);
			i.putExtra("movie_name", movieName);
			startActivity(i);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.equals(refuse)) {
			Log.e("XXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXX");

		}
	}
}
