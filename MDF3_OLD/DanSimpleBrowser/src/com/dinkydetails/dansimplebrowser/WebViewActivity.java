//Dan Annis
//October 2013
//MDF 3

package com.dinkydetails.dansimplebrowser;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class WebViewActivity extends Activity implements OnClickListener  {
	
	//Setting up my views/variables
	private WebView webView;
	private EditText urlEditText;
	private Button mViewUrl;
	DanDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        
        //Connecting the views/components to their IDs
        urlEditText = (EditText) findViewById(R.id.urlField);
        //Text helper for pre-formatted URL
        urlEditText.setText("Http://www.");
        urlEditText.setSelection(urlEditText.getText().length());
        
		webView = (WebView) findViewById(R.id.webView);
		//Creating a function to handle override of the webview and load the URL 
		webView.setWebViewClient(new MyWebViewClient());
		//Setting the background of the webView
		webView.setBackgroundColor(Color.parseColor("#77c9ef"));
		//Accessing the database function
		db = new DanDataBase(getApplicationContext());

		mViewUrl = (Button) findViewById(R.id.viewurls);
		mViewUrl.setOnClickListener(this);
		
		//Setting up the onclick listener for the button
		Button openUrl = (Button) findViewById(R.id.goButton);
		openUrl.setOnClickListener(new OnClickListener() {
			@SuppressLint("SetJavaScriptEnabled")
			@Override
			public void onClick(View view) {
				String url = urlEditText.getText().toString();
				if (validateUrl(url)) {
					webView.getSettings().setJavaScriptEnabled(true);
					webView.loadUrl(url);
					if (url != null) {
						db.open();
						db.insertRecord(url);
						db.close();
					}
				} else {
					Toast.makeText(WebViewActivity.this,
							"Please enter a valid URL", Toast.LENGTH_SHORT)
							.show();
				}

			}
			//Validate that the URL is truly a URL
			private boolean validateUrl(String url) {
				boolean returnValue;
				if (Patterns.WEB_URL.matcher(url).matches()) {
					returnValue = true;
				} else {
					returnValue = false;
				}
				return returnValue;
			}
		});
		//Sending the string/URL after validated and sending it to the webview
		String newURL = new String();
		Uri data = this.getIntent().getData();
		if (data != null) {
			newURL = data.toString();
			Log.d("Dan Browser", "text: " + newURL);
		}

		if (newURL.startsWith("http")) {
			webView.loadUrl(newURL);
		}
	}
		

	//extends the WebClient View since we can't show a webpage in an activity
    private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
    
    //passing the URL from edit text/implicit to load. 
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
	}

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_web_view, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.viewurls:
			Intent iViewUrls = new Intent(WebViewActivity.this,
					UrlsActivity.class);
			startActivity(iViewUrls);
			break;

		default:
			break;
		}
	}
    
}