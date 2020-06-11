package com.ian.submission2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.ian.submission2.db.DatabaseContract.NoteColumns.AVATAR;
import static com.ian.submission2.db.DatabaseContract.NoteColumns.USERNAME;
import static com.ian.submission2.db.DatabaseContract.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbgithubuserapp";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE = String.format("CREATE TABLE %s" +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL)",
            TABLE_NAME,
            _ID,
            USERNAME,
            AVATAR);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
