package com.dinkydetails.telly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class AddContactActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_contact);
		
		
		
	}
}