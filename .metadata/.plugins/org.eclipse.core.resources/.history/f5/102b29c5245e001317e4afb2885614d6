package com.dinkydetails.telly;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getPhoneNumber();
		init();	
	}
	
	/** Create a function to get the Phone Number */
	private void getPhoneNumber()
	{
	}
	
	/** Set up the View */
	private void init()
	{
		//Create a listener for the "call" button
		findViewById(R.id.btnCall).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//grab the text that is displayed in this box and make sure it is valid. 
				String strNumber = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				
				//validation - check to make sure there is data, if not put up a message
				if(strNumber.length() == 0)
				{
					Toast.makeText(getApplicationContext(), "Please enter number...", Toast.LENGTH_SHORT).show();
					return;
				}
				//Start phone permissions and call the number entered
				Intent callIntent = new Intent(Intent.ACTION_CALL);	
				callIntent.setData(Uri.parse("tel:"+strNumber));
				startActivity(callIntent);
				
			}
		});
		//Create a listener for the "clear" button
		findViewById(R.id.btnClear).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//Clear the text in the text view
				((TextView)findViewById(R.id.tvNumber)).setText("");

			}
		});
		//Create a listener for the "delete" button
		findViewById(R.id.btnDelete).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//Get the text in the text view
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				
				//If the string is not zero, when clicked delete each by one
				if( strText.length() != 0 )												
				((TextView)findViewById(R.id.tvNumber)).setText(strText.substring(0, strText.length() - 1));
			}
		});

		//Create a listener for the "1" button
		findViewById(R.id.btnOne).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//find the current string in the textview
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				//add the textvalue to the view
				if(checkNumberLength(strText))
				((TextView)findViewById(R.id.tvNumber)).setText(strText+"1");
				
			}
		});
		//Create a listener for the "2" button
		findViewById(R.id.btnTwo).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//find the current string in the textview
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				//add the textvalue to the view
				if(checkNumberLength(strText))
				((TextView)findViewById(R.id.tvNumber)).setText(strText+"2");
				
			}
		});
		//Create a listener for the "3" button
		findViewById(R.id.btnThree).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//find the current string in the textview
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				//add the textvalue to the view
				if(checkNumberLength(strText))
				((TextView)findViewById(R.id.tvNumber)).setText(strText+"3");
				
			}
		});
		//Create a listener for the "4" button
		findViewById(R.id.btnFour).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//find the current string in the textview
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				//add the textvalue to the view
				if(checkNumberLength(strText))
				((TextView)findViewById(R.id.tvNumber)).setText(strText+"4");
				
			}
		});
		//Create a listener for the "5" button
		findViewById(R.id.btnFive).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//find the current string in the textview
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				//add the textvalue to the view
				if(checkNumberLength(strText))
				((TextView)findViewById(R.id.tvNumber)).setText(strText+"5");
				
			}
		});
		//Create a listener for the "6" button
		findViewById(R.id.btnSix).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//find the current string in the textview
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				//add the textvalue to the view
				if(checkNumberLength(strText))
				((TextView)findViewById(R.id.tvNumber)).setText(strText+"6");
				
			}
		});
		//Create a listener for the "7" button
		findViewById(R.id.btnSeven).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//find the current string in the textview
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				//add the textvalue to the view
				if(checkNumberLength(strText))
				((TextView)findViewById(R.id.tvNumber)).setText(strText+"7");
				
			}
		});
		//Create a listener for the "8" button
		findViewById(R.id.btnEight).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//find the current string in the textview
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				//add the textvalue to the view
				if(checkNumberLength(strText))
				((TextView)findViewById(R.id.tvNumber)).setText(strText+"8");
				
			}
		});
		//Create a listener for the "9" button
		findViewById(R.id.btnNine).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//find the current string in the textview
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				//add the textvalue to the view
				if(checkNumberLength(strText))
				((TextView)findViewById(R.id.tvNumber)).setText(strText+"9");
				
			}
		});
		//Create a listener for the "0" button
		findViewById(R.id.btnZero).setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				//find the current string in the textview
				String strText = ((TextView)findViewById(R.id.tvNumber)).getText().toString();
				//add the textvalue to the view
				if(checkNumberLength(strText))
				((TextView)findViewById(R.id.tvNumber)).setText(strText+"0");
				
			}
		});
		
		
		
	}
	private boolean checkNumberLength(String strText)
	{
		if(strText.length() < 15)
		return true;
		
		return false;
		
	}

}
