package com.mobdeve.cait.mp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class myDbAdapter {
    myDbHelper myhelper;
    public myDbAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public long insertData(int id,String moviename, String overview,String language, String release)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.ID, id);
        contentValues.put(myDbHelper.MOVIENAME, moviename);
        contentValues.put(myDbHelper.OVERVIEW, overview);
        contentValues.put(myDbHelper.LANGUAGE, language);
        contentValues.put(myDbHelper.RELEASE,release);
        long id2 = dbb.insert(myDbHelper.TABLE_NAME, null , contentValues);
        return id2;
    }

    public String getData()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.MOVIENAME,myDbHelper.OVERVIEW};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.ID));
            String name =cursor.getString(cursor.getColumnIndex(myDbHelper.MOVIENAME));
            String  password =cursor.getString(cursor.getColumnIndex(myDbHelper.OVERVIEW));
            buffer.append(cid+ "   " + name + "   " + password +" \n");
        }
        return buffer.toString();
    }

    public  int delete(String uname)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uname};

        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.MOVIENAME+" = ?",whereArgs);
        return  count;
    }

    public int updateName(String oldName , String newName)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.MOVIENAME,newName);
        String[] whereArgs= {oldName};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.MOVIENAME+" = ?",whereArgs );
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myList";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String ID="_id";     // Column I (Primary Key)
        private static final String MOVIENAME = "Name";    //Column II
        private static final String OVERVIEW= "Overview";    // Column III
        private static final String LANGUAGE= "Language"; // Column IV
        private static final String RELEASE= "Release Date"; // Column V
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+MOVIENAME+" VARCHAR(255) ,"+ OVERVIEW+" VARCHAR(225), "+LANGUAGE+" VARCHART(255), "+LANGUAGE+" VARCHART(255));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                MovieClass.message(context,"OnUpgrade");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                MovieClass.message(context,"OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                MovieClass.message(context,"");
            }
        }
    }
}