package com.ian.submission2.helper;

import android.database.Cursor;

import com.ian.submission2.model.User;

import java.util.ArrayList;

import static com.ian.submission2.db.DatabaseContract.NoteColumns.AVATAR;
import static com.ian.submission2.db.DatabaseContract.NoteColumns.USERNAME;

public class MappingHelper {
    public static ArrayList<User> mapCursorToArrayList(Cursor userCursor) {
        ArrayList<User> userList = new ArrayList<>();

        while (userCursor.moveToNext()) {
            String username = userCursor.getString(userCursor.getColumnIndexOrThrow(USERNAME));
            String avatar = userCursor.getString(userCursor.getColumnIndexOrThrow(AVATAR));
            userList.add(new User(username, avatar));
        }

        return userList;
    }
}
