package com.dinkydetails.telly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.dinkydetails.telly.Utils.LibFile;
import com.dinkydetails.telly.Utils.Utils;
import com.dinkydetails.telly.db.DatabaseHelper;
import com.dinkydetails.telly.model.Contact;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactListActivity extends Activity
{
	
	private LinearLayout llContacts;
	private LayoutInflater vi;
	private DatabaseHelper dbHelper;
	private ArrayList<Contact> alContacts;
	private ArrayList<Contact> alSearch;
	private Contact currentObj;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_list);
		
		init();
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"ContactListActivity onResume() called");
		
		alContacts = getAllContactsInfo();
		
		updateContactsList();
		
		int incomingCount = LibFile.getInstance(getApplicationContext()).getIncomingCount();
		
		int outcomingCount = LibFile.getInstance(getApplicationContext()).getOutgoingCount();
		
		((TextView)findViewById(R.id.tvIncomingCount)).setText(incomingCount+"");
		((TextView)findViewById(R.id.tvOutgoingCount)).setText(outcomingCount+"");
	}
		
	private void init()
	{
		vi = (LayoutInflater)getApplicationContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		
		dbHelper = new DatabaseHelper(getApplicationContext());
		
		alContacts = new ArrayList<Contact>();
		
		alSearch = new ArrayList<Contact>();
		
		((Button)findViewById(R.id.btnAddNewContact)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
			   Intent iAddContact = new Intent(ContactListActivity.this, AddContactActivity.class);
			   
			   startActivity(iAddContact);
				
			}
		});
		
		((EditText)findViewById(R.id.etSearchContacts)).addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				alSearch.clear();
				
				getSearchArrayList((s.toString()));
				
				updateSearchList();
				
				
				
			}
		});
		
		llContacts = (LinearLayout)findViewById(R.id.llContacts);
		
		alContacts = getAllContactsInfo();
		
		updateContactsList();
	}
	
	public void sortList()
	{
		Collections.sort(alContacts, new Comparator<Contact>() {
	        
			@Override
			public int compare(Contact lhs, Contact rhs) {
				
				String firstName1 = lhs.getFirstName();
				String firstName2 = rhs.getFirstName();
				
				return firstName1.compareToIgnoreCase(firstName2);
			}
	    });
	}
	
	public void sortSearchList()
	{
		Collections.sort(alSearch, new Comparator<Contact>() {
	        
			@Override
			public int compare(Contact lhs, Contact rhs) {
				
				String firstName1 = lhs.getFirstName();
				String firstName2 = rhs.getFirstName();
				
				return firstName1.compareToIgnoreCase(firstName2);
			}
	    });
	}
		
	private void updateContactsList()
	{
		llContacts.removeAllViews();
				
		if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG," alContacts.size():"+alContacts.size());
		
		sortList();
		
		int i = 0;
				
		String strTmp = null;
		
		for(Contact tmpObj:alContacts)
		{
			View vGroup = vi.inflate(R.layout.group_item,null);
			
			String strFirstChar = tmpObj.getFirstName().toLowerCase().substring(0,1);
			
			if(!strFirstChar.equals(strTmp))
			{				
				((TextView)vGroup.findViewById(R.id.tvGroupName)).setText(strFirstChar.toUpperCase());
				llContacts.addView(vGroup);
			}
			
			strTmp = strFirstChar;
			
			View v = vi.inflate(R.layout.list_item,null);
			
			v.setTag(""+i);
						
			v.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					
					int viewPosition = Integer.parseInt( v.getTag().toString() );
					
					currentObj = alContacts.get(viewPosition);
					
				    Toast.makeText(getApplicationContext(),currentObj.getFirstName()+"Long clicked.",Toast.LENGTH_SHORT).show();
				
				    getOptions();
					
					return false;
				}
			});
			
			((ImageView)v.findViewById(R.id.ivCall)).setTag(""+i);
			
			((ImageView)v.findViewById(R.id.ivCall)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					int viewPosition = Integer.parseInt( v.getTag().toString() );
					
					currentObj = alContacts.get(viewPosition);
					
					Intent callIntent = new Intent(Intent.ACTION_DIAL);
					callIntent.setData(Uri.parse("tel:"+currentObj.getPhNo()));
					startActivity(callIntent);
									
				}
			});
			
			((TextView)v.findViewById(R.id.tvContactFirstName)).setText(tmpObj.getFirstName());
			
			((TextView)v.findViewById(R.id.tvContactPhNo)).setText(tmpObj.getPhNo());
			
			Bitmap croppedImage = Utils.getBmpFromPath(ContactListActivity.this, tmpObj.getImagePath());
			
			((ImageView)v.findViewById(R.id.ivContactPic)).setImageBitmap(croppedImage);
					
			llContacts.addView(v);
			
			i++;
		}
 
	    
	}
	
	private void updateSearchList()
	{
		llContacts.removeAllViews();
		
		if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG," alSearch.size():"+alSearch.size());
		
		sortSearchList();
		
		int i = 0;
				
		String strTmp = null;
		
		for(Contact tmpObj:alSearch)
		{
			View vGroup = vi.inflate(R.layout.group_item,null);
			
			String strFirstChar = tmpObj.getFirstName().toLowerCase().substring(0,1);
			
			if(!strFirstChar.equals(strTmp))
			{				
				((TextView)vGroup.findViewById(R.id.tvGroupName)).setText(strFirstChar.toUpperCase());
				llContacts.addView(vGroup);
			}
			
			strTmp = strFirstChar;
			
			View v = vi.inflate(R.layout.list_item,null);
			
			v.setTag(""+i);
			
			v.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					
					int viewPosition = Integer.parseInt( v.getTag().toString() );
					
					currentObj = alSearch.get(viewPosition);
					
				    Toast.makeText(getApplicationContext(),currentObj.getFirstName()+"Long clicked.",Toast.LENGTH_SHORT).show();
				
				    getOptions();
					
					return false;
				}
			});
			
			((ImageView)v.findViewById(R.id.ivCall)).setTag(""+i);
			
			((ImageView)v.findViewById(R.id.ivCall)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					int viewPosition = Integer.parseInt( v.getTag().toString() );
					
					currentObj = alContacts.get(viewPosition);
					
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:"+currentObj.getPhNo()));
					startActivity(callIntent);
					
					
				}
			});
			
			((TextView)v.findViewById(R.id.tvContactFirstName)).setText(tmpObj.getFirstName());
			
			((TextView)v.findViewById(R.id.tvContactPhNo)).setText(tmpObj.getPhNo());
			
			Bitmap croppedImage = Utils.getBmpFromPath(ContactListActivity.this, tmpObj.getImagePath());
			
			((ImageView)v.findViewById(R.id.ivContactPic)).setImageBitmap(croppedImage);
					
			llContacts.addView(v);
			
			i++;
		}
 
	}
	
	private ArrayList<Contact> getAllContactsInfo()
	{
		dbHelper.openDb();
		ArrayList<Contact> alContacts = dbHelper.getAllContactsInfo();
		dbHelper.closeDb();
		
		return alContacts;
	}
	
	private ArrayList<Contact> getSearchArrayList(String strSearchKeyWord)
	{
			for(Contact tmpObj : alContacts)
			{
				String strFirstName = tmpObj.getFirstName();
							
				if(strFirstName.toLowerCase().contains(strSearchKeyWord.toLowerCase()))
				{
					alSearch.add(tmpObj);
				}
			}
			
			return alSearch;
	 }
	 
	private void getOptions() 
	{
			new AlertDialog.Builder(ContactListActivity.this).setItems(R.array.getOptions,
					new DialogInterface.OnClickListener
					() {

						@Override
						public void onClick(DialogInterface arg0, int which) {
							switch (which) {
							case AppConstants.REQUEST_EDIT: 
							{
								Intent iAddContact = new Intent(ContactListActivity.this,AddContactActivity.class);
								
								iAddContact.putExtra(AppConstants.CURRENT_OBJ, currentObj);
								
								startActivity(iAddContact);
							}
							break;
							
							case AppConstants.REQUEST_DELETE:
							{
								dbHelper.openDb();
								dbHelper.removeContact(currentObj.getFirstName());
								alContacts = dbHelper.getAllContactsInfo();
								dbHelper.closeDb();
								
								updateContactsList();
							}
								break;

							}

						}
					}).show();
		}
			
	
	private void hideKeyboard()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.etSearchContacts)).getWindowToken(), 0);
	}

}
