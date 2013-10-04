package com.dinkydetails.dansimplebrowser;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class WebViewActivity extends Activity {
	
	//Setting up my views/variables
	private WebView webView;
	private EditText urlEditText;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        
        //Connecting the views/components to their IDs
        urlEditText = (EditText) findViewById(R.id.urlField);
		webView = (WebView) findViewById(R.id.webView);
		//Creating a function to handle override of the webview and load the URL 
		webView.setWebViewClient(new MyWebViewClient());
		
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
    
}
