package com.ian.submission2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static com.ian.submission2.db.DatabaseContract.NoteColumns.USERNAME;
import static com.ian.submission2.db.DatabaseContract.TABLE_NAME;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    private FavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen()) {
            database.close();
        }
    }

    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC"
        );
    }

    public Cursor queryByUsername(String username) {
        return database.query(
                DATABASE_TABLE,
                null,
                USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null,
                null
        );
    }

    public long insert(ContentValues value) {
        return database.insert(DATABASE_TABLE, null, value);
    }

    public int deleteByUsername(String username) {
        Log.d("TAG", "deleteById: " + username);
        return database.delete(DATABASE_TABLE, USERNAME + " = ?", new String[]{username});
    }
}
