package com.example.myfinalproject;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BookLibrary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "book_date";
    private static final String COLUMN_TIME = "book_time";
    public static String realDate;
    public static String realTime;


    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query =
                "CREATE TABLE "+TABLE_NAME+
                        " ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COLUMN_DATE + " TEXT, " +
                        COLUMN_TIME + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addBook(String date, String time)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_TIME, time);
        if(HomeFragment.book_id != null)
            cv.put(COLUMN_ID, String.valueOf(HomeFragment.book_id.size() + 1));
        else
            cv.put(COLUMN_ID, String.valueOf(1));
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1)
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(context, "Success- added", Toast.LENGTH_SHORT).show();
            realDate = date;
            realTime = time;
            HomeFragment.a(date, time);

        }
    }


    Cursor readAllData(){
        String query = "SELECT * FROM "+ TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null)
            cursor = db.rawQuery(query, null);
        return cursor;
    }

    void deleteOneRow(String row_date, String time)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "book_date=?", new String[]{row_date});
        if(result == -1)
            Toast.makeText(context, "Error- Can't delete", Toast.LENGTH_SHORT).show();
        else {
            int second = row_date.indexOf('M'), third = row_date.indexOf('Y');
            String date = row_date.substring(1, second) + "-" + row_date.substring(second + 1, third) + "-" + row_date.substring(third + 1);
            CalendarFragment.addOne(date, time);
            Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
