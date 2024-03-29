//Dan Annis
//MDF3 Week 4
//Hybrid App

package com.dinky.weatherpullhd;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class MainActivity extends Activity {
	// Setting up known variables
	JavaScriptInterface JSInterface;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//changing main layout to  show the splash first
		setContentView(R.layout.activity_splash);
		
		//Tie my webviews and set up the webview to handle JS
		WebView webView = (WebView)findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		//Expecting UI 
		webView.setWebChromeClient(new WebChromeClient());
		//allowing to inject Java objects into a page's JavaScript
		webView.addJavascriptInterface(new JavaScriptInterface(this), "webapi");
        //Load the local file
		webView.loadUrl("file:///android_asset/www/index.html");
	}

	public class JavaScriptInterface {
        Context mContext;
        //Instantiate the interface and set the context
        JavaScriptInterface(Context c) {
            mContext = c;
        }
        //Call the function changeActivity defined in my JS of my HTML
        public void changeActivity(String zip)
        {
        	//Call the WeatherApp and store the intent to pass it onto the app. 
        	PackageManager pm = getPackageManager();
             Intent intent = pm.getLaunchIntentForPackage("com.dan.weatherpull");
             intent.putExtra("zip", zip);
             startActivity(intent);
        }
        
	}
}

