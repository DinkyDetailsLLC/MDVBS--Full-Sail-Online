package com.dinkydetails.moviecheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class second extends Activity {
	TextView MovieTitle,plot,rating;
	ImageView imageView1;
	String title;
	String url;
	String Title;
	String Plot;
	String str_rating;
	Double Rating;
	String Poster;
	Bitmap m_bitmap = null;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//changing main layout to  show the first screen 
		setContentView(R.layout.second);
		//Setting up of action bar
		ActionBar actionBar = getActionBar();
		actionBar.show();
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		 getActionBar().setHomeButtonEnabled(true);
	        getActionBar().setDisplayHomeAsUpEnabled(false);
	        //Finding the views that are defined in the layout
		MovieTitle=(TextView)findViewById(R.id.textView1);
		rating= (TextView)findViewById(R.id.rating_value);
		plot=(TextView)findViewById(R.id.textView2);
		imageView1=(ImageView)findViewById(R.id.imageView1);
	    //Getting the movie title passed by user in html page through intent
		 title = getIntent().getExtras().getString("movie_name");
		Log.e("intent","value"+title);
		url ="http://omdbapi.com/?t="+title+"&r=JSON" ;
		//Calling the async task to be executed to get the json from URL
		DownloadFilesTask dwd= new DownloadFilesTask();
		dwd.execute();
}
	//Method to add items in the action bar
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	//perform different operations on menu item click event or selection
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.more:
			// More action
			Intent i= new Intent(this,third.class);
			startActivity(i);
			return true;
		case R.id.search:
			// Search found
		Intent in= new Intent(this,MainActivity.class);
		startActivity(in);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	//Async task method to fetch json and set them to Widgets android
	private class DownloadFilesTask extends AsyncTask<Void, Void, JSONObject> {

		private ProgressDialog Dialog;
// Method to be executed at first when this method starts
		protected void onPreExecute() {
			super.onPreExecute();
			//Showing Dialog of loading 
			Dialog = new ProgressDialog(second.this);
			Dialog.setMessage("Loading Dine In...");
			Dialog.show();
		}
//Method called after preexecute() and include code to be executed in threads at the back ground like fetching json data
		@Override
		protected JSONObject doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			//Calling method to get json object from URL
			JSONObject json = getJSONFromUrl(url);
			//parsing json data
			try {
				Log.e("inside","parsejson");
				
				//Getting "Title" of movie from json object
				 Title=json.getString("Title");
					Log.e("inside","title"+Title);
					
					//Getting "Plot" of movie from json object
					Plot=json.getString("Plot");
					
					//Getting "Rating" of movie from json object
					 Rating=json.getDouble("imdbRating");
					 Log.e("rating","value"+Rating);
					 
					//Getting "Poster" of movie from json object
					 Poster=json.getString("Poster");
					Log.e("message","value"+Title+Plot+Rating+Poster);
					
				//Converting image to the type Bitmap
					try {
						m_bitmap = BitmapFactory
								.decodeStream((InputStream) new URL(Poster)
										.getContent());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}	
			
		//	}
			catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			}catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		// Method executed at last of the Async Task
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			Dialog.dismiss();
			
			//Setting "title" in text view
			MovieTitle.setText(Title);
			
			//Setting "Plot" in text view
			plot.setText(Plot);
			
			//Setting "image" in image view
			imageView1.setImageBitmap(m_bitmap);
			
			//converting double to string and Setting "rating" in text view
			str_rating = String.valueOf(Rating);
			rating.setText(str_rating);
		}
	}

	public static JSONObject getJSONFromUrl(String url) {
	    InputStream is = null;
	    JSONObject jObj = null;
	    String json = "";
	   // Log.e("message","Url"+url);
        // Making HTTP request
        try {
            // defaultHttpClient
        	 Log.e("message","Url"+url);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();          
            Log.e("getting","value"+is);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
        	Log.e("exp","exception"+e);
            e.printStackTrace();
        }
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("", "String Form Rseponse is =>>" +json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }
	
}