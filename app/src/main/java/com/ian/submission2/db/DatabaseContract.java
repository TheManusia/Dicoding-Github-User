package com.ian.submission2.db;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_NAME = "userfavorite";

    public static final class NoteColumns implements BaseColumns {
        public static String USERNAME = "username";
        public static String AVATAR = "avatar";
    }
}
