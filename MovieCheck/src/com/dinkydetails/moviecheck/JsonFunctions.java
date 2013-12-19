package com.dinkydetails.moviecheck;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonFunctions {

	public static JSONObject getJSONObjectfromURL2(String url, JSONArray json) {
		InputStream is = null;
		String result = "";
		JSONObject jObject = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost = new HttpGet(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			// Log.e("result:",""+result);

		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {

			jObject = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jObject;
	}

	public static JSONObject getJSONObjectfromURL(String url) {
		InputStream is = null;
		String result = "";
		JSONObject jObject = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			// Log.e("result:",""+result);

		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {

			jObject = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jObject;
	}

	public static JSONObject getJSONArrayfromURL(String url) {

		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {
			URLEncoder.encode(url, "utf-8");
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				Log.e("===>", "===>" + line + "\n");
			}
			is.close();
			result = sb.toString();
			// Log.e("result:",""+result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {

			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jArray;
	}

}
