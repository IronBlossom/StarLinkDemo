package com.ael_bd.starlinkdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by imamin on 12/23/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Properties
    public static final String DATABASE_NAME = "StarLink.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE1 = "Accounts";
    //Accounts Table properties
    public static final String _ID = "_id";
    public static final String _ACCOUNTNO = "_accountNo";
    public static final String _PASSWORD = "_password";
    public static final String _DESCRIPTION = "_description";
    public static final String _STATETYPE = "_stateType";
    private static DatabaseHelper sInstance = null;
    private SQLiteDatabase sqliteDB;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.onCreate(getWritableDatabase());
    }

    public static DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sqliteDB = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE1);
    }

    public boolean isOpen() {
        return sqliteDB.isOpen();
    }

    // Insert||Update||Delete
    public long insertAccounts(String _ACCOUNTNO, String _PASSWORD,
                               String _DESCRIPTION, String _STATETYPE) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper._ACCOUNTNO, _ACCOUNTNO);
        values.put(DatabaseHelper._PASSWORD, _PASSWORD);
        values.put(DatabaseHelper._DESCRIPTION, _DESCRIPTION);
        values.put(DatabaseHelper._STATETYPE, _STATETYPE);


        return sqliteDB.insert(DATABASE_TABLE1, null, values);
    }

    public boolean updateAccounts(String _ID,String _ACCOUNTNO, String _PASSWORD,
                                  String _DESCRIPTION, String _STATETYPE) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper._ACCOUNTNO, _ACCOUNTNO);
        values.put(DatabaseHelper._PASSWORD, _PASSWORD);
        values.put(DatabaseHelper._DESCRIPTION, _DESCRIPTION);
        values.put(DatabaseHelper._STATETYPE, _STATETYPE);

        return sqliteDB.update(DATABASE_TABLE1, values,
                DatabaseHelper._ID + "='" + _ID + "'", null) > 0;
    }

    public Cursor getAccounts() {
        Cursor mCursor = null;
        mCursor = sqliteDB.query(true, DATABASE_TABLE1, new String[]{_ID,
                        _ACCOUNTNO, _PASSWORD, _DESCRIPTION, _STATETYPE}, null, null, null, null, null,
                null);
        return mCursor.getCount() > 0 ? mCursor : null;
    }

}
