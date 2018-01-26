package com.example.varun.schedulerapp3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by varun on 11/10/2017.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "MyDB.db";
    private static final String KEY_ID = "id";
    private static final String TABLE_NAME = "event";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TIME = "Time";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME +
                        "("+KEY_ID +" integer primary key autoincrement, "+ COLUMN_NAME +" text, "+ COLUMN_TIME +" text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        throw new SQLiteException("Can't downgrade database from version " +
                i + " to " + i1);

    }

    public boolean dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        return true;
    }

    public boolean insertEvent (String name, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_TIME, time);
        db.insert("event", null, contentValues);
        db.close();
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select "+ COLUMN_NAME+ ","+COLUMN_TIME+ " from event where id="+id+"", null );
        return res;
    }



    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateEvent (Integer id, String name, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_TIME, time);
        db.update("event", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        db.close();
        return true;
    }

    public Integer deleteEvent (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("event",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllEvent() {        //Get values for ListView to display
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from event", null );
        res.moveToFirst();
        if (res.moveToFirst()){
            do{
                String s = res.getString(0).concat(".").concat(res.getString(1)).concat(",").concat(res.getString(2));
                array_list.add(s);
            }while (res.moveToNext());
        }
        return array_list;
    }


    public ArrayList<HashMap<String,String>> getAllEvents() {   //Get values for setting alarm
        ArrayList<HashMap<String,String>> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from event", null );
        while (res.moveToNext()){
            HashMap<String,String> event=new HashMap<>();
            event.put("id",res.getString(0));
            event.put("eventName",res.getString(1));
            event.put("met",res.getString(2));
            eventList.add(event);
        }
        return eventList;
    }
}
