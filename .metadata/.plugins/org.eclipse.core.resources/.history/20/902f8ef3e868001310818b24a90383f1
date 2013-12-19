//Dan Annis
//MDF3 Week 4
//Hybrid App

package com.dinkydetails.moviecheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

public class SplashActivity extends Activity {
	
	//Setting a time out for the splash activity
	private static int SPLASH_TIME_OUT = 5000;
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //preparing for a webview
	        WebView wb = new WebView(this);
	        //Load the Webview
	        wb.loadUrl("file:///android_asset/splash.html");
	        //Set the webview
	        setContentView(wb);
	        
	        //Delay the load of the Main Activity by the timeout time
	        new Handler().postDelayed(new Runnable() {

	            @Override
	            //Run and load the splash and then the Main activity
	            
	            public void run() {
	                Intent i = new Intent(SplashActivity.this, MainActivity.class);
	                startActivity(i);
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
	        }
	   
	}

