package com.dinkydetails.telly.db;

import java.util.ArrayList;

import com.dinkydetails.telly.AppConstants;
import com.dinkydetails.telly.model.Contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper 
{	
	
	    private SQLiteDatabase db;
	    
	    private Context context;
	
	    public DatabaseHelper(Context context)
	    {
	       super(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
	       
	       this.context = context;
	    }
	 
	    @Override
	    public void onCreate(SQLiteDatabase db) 
	    {
	    	if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"DatabaseHelper onCreate() called");
	    	
	    	createContactsTable(db);      	    		
	       	    
	    }
	  	  
	    public void openDb()
	    {
	    	db = this.getWritableDatabase();
	    }
	    
	    public void closeDb()
	    {
	    	db.close();
	    }
	    
	    private void createContactsTable(SQLiteDatabase db)
	    {
	        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + AppConstants.TABLE_CONTACTS + "("
	        		+ AppConstants.KEY_ID + " INTEGER PRIMARY KEY,"
	                + AppConstants.KEY_FIRST_NAME + " TEXT," 
	        		+ AppConstants.KEY_LAST_NAME + " TEXT,"
	                + AppConstants.KEY_PH_NO + " TEXT," 
	                + AppConstants.KEY_IMAGE_PATH + " TEXT"+")";
	       
	        if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG," CREATE_APP_TABLE: "+CREATE_CONTACTS_TABLE.toString());
	        
	        db.execSQL(CREATE_CONTACTS_TABLE);
	        
	        

	    }
	 		  
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	    {
	        // Drop older table if existed
	        db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_CONTACTS);
	 
	        // Create tables again
	        onCreate(db);
	    }
	    	    
	    public void clearTableData()
	    {
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"clearTableData()");
	    	       
			db.delete(AppConstants.TABLE_CONTACTS, null, null);
			
	    }
	    
	   public String addContact(Contact cObj) 
	    {
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"addContact() called First name:"+ cObj.getFirstName());
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"addContact() called Last name:"+ cObj.getLastName());
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"addContact() called Phone no:"+ cObj.getPhNo());
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"addContact() called Image Path:"+ cObj.getImagePath());
	    	     	
	    	ContentValues values = new ContentValues();
             
		    values.put(AppConstants.KEY_FIRST_NAME, cObj.getFirstName() );
		    values.put(AppConstants.KEY_LAST_NAME, cObj.getLastName() );
		    values.put(AppConstants.KEY_PH_NO, cObj.getPhNo());
		    values.put(AppConstants.KEY_IMAGE_PATH, cObj.getImagePath() );
		    		    		       	        	      
		    long rowid = db.insert(AppConstants.TABLE_CONTACTS , null, values);
		    	    		    
		    if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"Created rowid: "+rowid);
	        	    
		    return rowid+"";

	    }
	   
	   public void removeContact(String firstName)
	   {
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"removeApp() called");
	    	        	 	    	
	    	db.delete(AppConstants.TABLE_CONTACTS, AppConstants.KEY_FIRST_NAME + " = ?" , new String[] { firstName });
	    	    	    	
	   }
	   
	   public void updateContact(Contact cObj,String firstName)
	    {
	    	if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"updateContact() called");
	    	
	    	ContentValues cv = new ContentValues();
			cv.put(AppConstants.KEY_FIRST_NAME, cObj.getFirstName());
			cv.put(AppConstants.KEY_LAST_NAME, cObj.getLastName());
			cv.put(AppConstants.KEY_PH_NO, cObj.getPhNo());
			cv.put(AppConstants.KEY_IMAGE_PATH, cObj.getImagePath());
									
			db.update(AppConstants.TABLE_CONTACTS,  cv , AppConstants.KEY_FIRST_NAME + " = ?" , new String[] { firstName });
			
	    	
	    }
	   
	   public ArrayList<Contact> getAllContactsInfo() 
	   {	    	
	    	if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"getAllContactsInfo()");
	    	
	    	ArrayList<Contact> alContacts = new ArrayList<Contact>();
	    		    	
	    	String selectQuery = "SELECT * FROM " + AppConstants.TABLE_CONTACTS ;
	    		          
	        Cursor cursor = db.rawQuery(selectQuery, null);	       
	    	       
		       if (cursor.moveToFirst()) 
		       {
		           do
		           {
		        	   Contact dcObj = new Contact();
		        	   alContacts.add(dcObj);
		        	 	        	   
		        	   dcObj.setFirstName(cursor.getString(1));
		        	   dcObj.setLastName(cursor.getString(2));
		        	   dcObj.setPhNo(cursor.getString(3));
		        	   dcObj.setImagePath(cursor.getString(4));
		        	   
    	           } while (cursor.moveToNext());
		       }
	    
		   cursor.close();    
		       
	       return alContacts;
	   }
	    
	   /*
	    public void updatePassword(String rowId , String strPass)
	    {
	    	if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"updatePassword() called rowId: "+rowId+" strPass:"+strPass);
	    	
	    	ContentValues cv = new ContentValues();
			cv.put(AppConstants.KEY_PASSWORD, strPass);
									
			db.update(AppConstants.TABLE_PASSWORD,  cv , AppConstants.KEY_PASS_ID + " = ?" , new String[] { rowId });
			
	    	
	    }
	    
	    public boolean isPasswordMatched(String rowId , String strPassword)
	    {
	    	if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"isPassword() called");
	    	
	     	Cursor cursor = db.query(AppConstants.TABLE_PASSWORD, 
	    			new String[]{ AppConstants.KEY_PASS_ID, AppConstants.KEY_PASSWORD }, 
	    			AppConstants.KEY_PASS_ID + " = ? ", new String[] { rowId } , null, null, null, null);
	     	
	     	 if (cursor.moveToFirst()) 
		     {		        
   	        	  String storedPass = cursor.getString(1);
   	        	     
   	        	  if(storedPass.equalsIgnoreCase(strPassword))
   	        	  {
   	        		  cursor.close();
   	        		  return true;
   	        	  }
             }
	     	 
	     	 cursor.close();
	     	 
	     	 return false;
    	
	    }
	    
	    public String getPassword(String rowId )
	    {
	    	if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"getPassword() called rowId: "+rowId);
	    	
	     	Cursor cursor = db.query(AppConstants.TABLE_PASSWORD, 
	    			new String[]{ AppConstants.KEY_PASS_ID, AppConstants.KEY_PASSWORD }, 
	    			AppConstants.KEY_PASS_ID + " = ? ", new String[] { rowId } , null, null, null, null);
	     	
	     	 String storedPass = "";
	     	
	     	 if (cursor.moveToFirst()) 
		     {		        
   	        	  storedPass = cursor.getString(1);
   	        	  cursor.close();   	
             }
	     	 
	     	if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"storedPass: "+storedPass);
	     	 
	     	 return storedPass;
	    }
	    	    
	    public String addApp(AppLock alObj) 
	    {
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"addApp() called");
	    	     	
	        ContentValues values = new ContentValues();
	                 
	        values.put(AppConstants.KEY_PACKAGE_NAME, alObj.getPackageName());
	        values.put(AppConstants.KEY_APP_NAME, alObj.getAppName());
	        values.put(AppConstants.KEY_LOCK, alObj.isLock() +"");
	              	      
	        long rowid = db.insert(AppConstants.TABLE_APP , null, values);
                
	        return rowid+"";
	    }
	    	   	
	    public void removeApp(String packageName)
	    {
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"removeApp() called");
	    	        	 	    	
	    	db.delete(AppConstants.TABLE_APP, AppConstants.KEY_PACKAGE_NAME + " = ?" , new String[] { packageName });
	    	    	    	
	    }
	    
	    public boolean getAppLockStatus(String packageName) 
	    {	    	
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"getAppLockStatus() packageName: "+packageName);
	    	    	
	    	boolean flag = false ;
	    	
	    	Cursor cursor = db.query(AppConstants.TABLE_APP, 
	    			new String[]{ AppConstants.KEY_APP_ID,AppConstants.KEY_PACKAGE_NAME,AppConstants.KEY_APP_NAME,AppConstants.KEY_ALLOW,AppConstants.KEY_LOCK }, 
	    			AppConstants.KEY_PACKAGE_NAME + " = ?", new String[] { packageName } , null, null, null, null);
    	
		       if (cursor.moveToFirst()) 
		       {
		           do
		           {
     	        	 flag = Boolean.parseBoolean(cursor.getString(4));
		        	   
		           } while (cursor.moveToNext());
		       }
	    
		   cursor.close();    
		       
	       return flag;
	   }
	    
	    public boolean getAppAllowStatus(String packageName) 
	    {	    	
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"getAppAllowStatus() packageName: "+packageName);
    	
	    	boolean flag = false ;
	    	
	    	Cursor cursor = db.query(AppConstants.TABLE_APP, 
	    			new String[]{ AppConstants.KEY_APP_ID,AppConstants.KEY_PACKAGE_NAME,AppConstants.KEY_APP_NAME,AppConstants.KEY_ALLOW,AppConstants.KEY_LOCK }, 
	    			AppConstants.KEY_PACKAGE_NAME + " = ?", new String[] { packageName } , null, null, null, null);
	            	       
		       if (cursor.moveToFirst()) 
		       {
		           do
		           {
		        	 flag = Boolean.parseBoolean(cursor.getString(3));
		        	   
		           } while (cursor.moveToNext());
		       }
	    
		   cursor.close();    
		       
	       return flag;
	   }
	    
	    public String getAppName(String packageName) 
	    {	    	
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"getAppName() packageName: "+packageName);
    		    	
	    	Cursor cursor = db.query(AppConstants.TABLE_APP, 
	    			new String[]{ AppConstants.KEY_APP_ID,AppConstants.KEY_PACKAGE_NAME,AppConstants.KEY_APP_NAME,AppConstants.KEY_ALLOW,AppConstants.KEY_LOCK }, 
	    			AppConstants.KEY_PACKAGE_NAME + " = ?", new String[] { packageName } , null, null, null, null);
	            	       
	    	String strAppName = "";
	    	
		       if (cursor.moveToFirst()) 
		       {
		           do
		           {
		        	   strAppName = cursor.getString(2);		        	   
		           } while (cursor.moveToNext());
		       }
	    
		   cursor.close();    
		       
	       return strAppName;
	   }
	    
	    public void updateAppRow(String packageName , String value)
	    {
	    	ContentValues cv = new ContentValues();
			cv.put(AppConstants.KEY_ALLOW, value);
			
			db.update(AppConstants.TABLE_APP,  cv , AppConstants.KEY_PACKAGE_NAME + " = ?" , new String[] { packageName });
			
	    }
	   	    
	    public void resetAllowFlagExcept(String packageName)
	    {
	    	//if(AppConstants.DEBUG)Log.v(AppConstants.DEBUG_TAG,"resetAllowFlagExcept() called");
		
			ContentValues cv = new ContentValues();
			cv.put(AppConstants.KEY_ALLOW, "false");
									
			db.update(AppConstants.TABLE_APP,  cv , AppConstants.KEY_PACKAGE_NAME + " != ?" , new String[] { packageName });
			
		}	    
	   	    
	   
	    */
	 /*   public ArrayList<AppLock> getSearchedAppInfo(String strSearchKeyWord)
	    {
	    	ArrayList<AppLock> alAppLock = new ArrayList<AppLock>();
	    	
	    	String selectQuery = "SELECT * FROM " + AppConstants.TABLE_APP +" where "
	    			+ AppConstants.KEY_APP_NAME + " like '%"+strSearchKeyWord+"%'";
	    		          
	        Cursor cursor = db.rawQuery(selectQuery, null);	       
	    	       
		       if (cursor.moveToFirst()) 
		       {
		           do
		           {
		        	   AppLock dcObj = new AppLock();
		        	   alAppLock.add(dcObj);
		        	 	        	   
		        	   dcObj.setPackageName(cursor.getString(1));
		        	   dcObj.setAppName(cursor.getString(2));
		        	   dcObj.setAllow(Boolean.parseBoolean(cursor.getString(3)));
		        	   dcObj.setLock(Boolean.parseBoolean(cursor.getString(4)));
		        	   
     	           } while (cursor.moveToNext());
		       }
	    
		   cursor.close();    
		       
	       return alAppLock;
	    }*/
	    
	
}
