package com.sqliteHelper;

import android.content.Context;  
import android.database.Cursor;  
import android.database.sqlite.SQLiteDatabase;  
import android.util.Log;  
  

public class DBHelper {  
    private static final String TAG = "DBDemo_DBHelper";
  
    private static final String DATABASE_NAME = "weather.db";
    SQLiteDatabase db;  
    Context context;
  
    public DBHelper(Context _context) {  
        context = _context;  
           
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE,null);  
        CreateTable();  
        Log.v(TAG, "db path=" + db.getPath());  
    }  

    public void CreateTable() {  
        try {  
            db.execSQL("CREATE TABLE weather (" +  
                    "id INTEGER PRIMARY KEY autoincrement,"  
                    + "city TEXT,weathercode TEXT,temperature TEXT"   
                    + ");");  
            Log.v(TAG, "Create Table weather ok");  
        } catch (Exception e) {  
            Log.v(TAG, "Create Table weather err,table exists.");  
        }  
    }  

    public boolean save(String city,String weather,String temperature){  
        String sql="";  
        try{  
            sql="insert into weather values(null,'"+city+"','"+weather+"','"+temperature+"')";  
            db.execSQL(sql);  
            Log.v(TAG,"insert Table weather ok");  
            return true;  
              
        }catch(Exception e){  
            Log.v(TAG,"insert Table weather err ,sql: "+sql);  
            return false;  
        }  
    }  
    public boolean delete(){  
        String sql="";  
        try{  
            sql="delete from weather where 1"; 
            db.execSQL(sql);  
            Log.v(TAG,"delete Table weather ok");  
            return true;  
              
        }catch(Exception e){  
            Log.v(TAG,"delete Table weather err ,sql: "+sql);  
            return false;  
        }  
    }  
    public boolean exec(String inputSql){   
    	String sql="";
        try{
        	sql=inputSql;
            db.execSQL(sql);  
            Log.v(TAG,"sql exec ok");  
            return true;  
              
        }catch(Exception e){  
            Log.v(TAG,"sql exec err");  
            return false;  
        }  
    }
    public String loadDefault(String uname)
    {
    	Cursor cur=db.query("weather", new String[]{"city","weathercode","temperature"}, null,null, null, null, null);
    	if(cur.getCount()==0)
    	{
    		return "wrong";
    	}
    	cur.moveToFirst();
        String result="";
        if(uname.equals("city"))
        {
        	result=cur.getString(0);
        	return result;
        }
        if(uname.equals("weather"))
        {
        	result=cur.getString(1);
        	return result;
        }
        else if(uname.equals("temperature"))
        {
        	result=cur.getString(2);
        	return result;
        }
        return "wrong";
    }
      public void close(){  
        db.close();  
    }  
}  