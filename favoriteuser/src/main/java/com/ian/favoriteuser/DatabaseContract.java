package com.ian.favoriteuser;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.ian.submission2";
    public static final String SCHEME = "content";
    public static String TABLE_NAME = "userfavorite";

    public static final class NoteColumns implements BaseColumns {
        public static String USERNAME = "username";
        public static String AVATAR = "avatar";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
