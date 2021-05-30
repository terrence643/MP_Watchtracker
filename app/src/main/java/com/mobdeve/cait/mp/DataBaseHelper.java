package com.mobdeve.cait.mp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;


public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "List.db";
    public static final String TABLE_NAME = "ListData_Table";
    public static final String MOVIEID = "MOVIEID";
    public static final String STATUS = "STATUS";




    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null,1);

    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (MOVIEID VARCHAR(255) PRIMARY KEY , STATUS VARCHAR(255)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String MovieId, String Status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIEID,MovieId);
        contentValues.put(STATUS,Status);

        if(CheckIsDataAlreadyInDBorNot(TABLE_NAME,MovieId)) {
            long result = db.insert(TABLE_NAME, null, contentValues);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        else{
            return false;
        }
    }


    public boolean CheckIsDataAlreadyInDBorNot(String TableName, String MovieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TableName + " IF EXISTS " + MovieId ;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
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