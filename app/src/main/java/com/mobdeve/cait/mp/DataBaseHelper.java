package com.mobdeve.cait.mp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;
import android.util.Log;


public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "List.db";
    public static final String TABLE_NAME = "ListData_Table";
    public static final String MOVIEID = "MOVIEID";
    public static final String STATUS = "STATUS";
    public static final String MEDIATYPE = "MEDIATYPE" ;




    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null,1);

    }

    Cursor getAllData(){
        String retrieve = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(retrieve, null);   // contains all data
        } return cursor;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (MOVIEID VARCHAR(255) PRIMARY KEY , STATUS VARCHAR(255), MEDIATYPE VARCHAR(255))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String MovieId, String Status, String MediaType){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIEID,MovieId);
        contentValues.put(STATUS,Status);
        contentValues.put(MEDIATYPE, MediaType);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }
        else{
            return true;
        }


    }


    public boolean CheckStatus(String TableName,String Status){
        SQLiteDatabase db = this.getReadableDatabase();
        String Query =" Select * from " + TableName +" WHERE " + Status;
        Cursor cursor = db.rawQuery(Query,null);
        if(cursor.getCount()<=0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public Cursor getData(String Media){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + MEDIATYPE + " LIKE '%" + Media + "%'";

        Cursor res = db.rawQuery(Query,null);
        return res;
    }



    public Cursor getToWatchData(String TableName, String Status){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM" +TableName+ "WHERE " +Status+ " LIKE '%" +Status+ "%'";

        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String movieID, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIEID,movieID);
        contentValues.put(STATUS,status);
        db.update(TABLE_NAME, contentValues, " MOVIEID = ? ",new String[] { movieID });
        return true;
    }

    public Integer deleteData(String movieID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"MOVIEID = ?",new String[] {movieID});
    }
}