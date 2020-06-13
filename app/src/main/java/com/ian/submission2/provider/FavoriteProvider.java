package com.ian.submission2.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.ian.submission2.db.FavoriteHelper;

import java.util.Objects;

import static com.ian.submission2.db.DatabaseContract.AUTHORITY;
import static com.ian.submission2.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.ian.submission2.db.DatabaseContract.TABLE_NAME;

public class FavoriteProvider extends ContentProvider {
    private static final int FAVORITE = 1;
    private static final int FAVORITE_ID = 2;
    private FavoriteHelper favoriteHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME+"/*", FAVORITE_ID);
    }

    public FavoriteProvider() {
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        favoriteHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case FAVORITE :
                cursor = favoriteHelper.queryAll();
                break;
            case FAVORITE_ID :
                cursor = favoriteHelper.queryByUsername(uri.getLastPathSegment());
                break;
            default :
                cursor = null;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        if (uriMatcher.match(uri) == FAVORITE) {
            added = favoriteHelper.insert(values);
        } else {
            added = 0;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI+"/"+added);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        if (uriMatcher.match(uri) == FAVORITE_ID) {
            deleted = favoriteHelper.deleteByUsername(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }
        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}
