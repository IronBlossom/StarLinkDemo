package com.ael_bd.starlinkdemo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.ael_bd.starlinkdemo.utils.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by imamin on 12/17/2014.
 */
public class StartLinkDemo extends Application {
    private static final int FLAG_DB_OVERWRITE = 1;
    SharedPreferences sharedPreferences;
    private DatabaseHelper db = null;

    @Override
    public void onCreate() {
        super.onCreate();

        createSPref();
        shiftDataBase();
        createSingletonDB();

        if (db != null) {
            Cursor mCursor = db.getAccounts();

            if (mCursor == null) {
                for (int i = 0; i < 3; i++) {
                    db.insertAccounts("", "", "", "0");
                }
            }
            /*if (mCursor==null) {
                for (int i = 0; i < 3; i++) {
                    db.insertAccounts("", "", "", "0");
                }

            }else
                mCursor.close();*/

        }
    }

    private void createSPref() {
        sharedPreferences = getSharedPreferences("StarLink", Context.MODE_PRIVATE);
    }

    private void shiftDataBase() {

        if (sharedPreferences.getInt("DBVersionChecked", 0) < FLAG_DB_OVERWRITE) {

            sharedPreferences.edit().putInt("DBVersionChecked", FLAG_DB_OVERWRITE).commit();

            File directory = new File(getDatabasePath(
                    DatabaseHelper.DATABASE_NAME).getParent()
                    + File.separator);
            directory.mkdir();

            try {
                InputStream dbIn = getAssets().open(
                        DatabaseHelper.DATABASE_NAME);
                OutputStream dbOut = new FileOutputStream(
                        directory.getAbsolutePath() + File.separator
                                + DatabaseHelper.DATABASE_NAME);

                byte[] buffer = new byte[1024];
                int remainingData;
                while ((remainingData = dbIn.read(buffer)) > 0) {
                    dbOut.write(buffer, 0, remainingData);
                }
                dbOut.flush();
                dbOut.close();

                Log.v("", "New DB created...");

            } catch (Exception e) {
                Log.v("TAG", "Could not create new DB...");
                e.printStackTrace();
            }
        }
    }

    private void createSingletonDB() {
        if (db == null)
            db = DatabaseHelper.getInstance(getApplicationContext());
    }

    public synchronized DatabaseHelper getDatabase() {
        if (db == null)
            db = DatabaseHelper.getInstance(getApplicationContext());
        db.getWritableDatabase();
        return db;
    }

}
