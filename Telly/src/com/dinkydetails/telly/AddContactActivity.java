package com.dinkydetails.telly;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/** New Activity that will run when the app is launched.  */
/** Need to move the Main Activity to a different location... but still have it prompt, maybe...  */
public class AddContactActivity  extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_contact);
		
		
		
	}
	
}
