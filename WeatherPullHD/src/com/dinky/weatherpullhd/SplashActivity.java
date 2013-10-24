//Dan Annis
//MDF3 Week 4
//Hybrid App

package com.dinky.weatherpullhd;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;

public class SplashActivity extends Activity {
	
	//Setting a time out for the splash activity
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //preparing for a webview
	        WebView wb = new WebView(this);
	        //Load the Webview
	        wb.loadUrl("file:///android_asset/demo.html");
	        //Set the webview
	        setContentView(wb);

	   }
	   
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	}

