package com.example.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project.R;
import com.example.project.util.HashUtils;

import java.util.Map;

public class UserInfoDAO {

    private final Context context;

    public UserInfoDAO(Context context) {
        this.context = context;
    }

    public void addUser(UserInfo userInfo) {
        SQLiteDatabase db =  DBHelper.getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserInfo.EMAIL_COL, userInfo.getEmail());
        values.put(UserInfo.PASSWORD_COL, HashUtils.hashPassword(userInfo.getPassword()));
        values.put(UserInfo.FIRST_NAME_COL, userInfo.getFirstName());
        values.put(UserInfo.LAST_NAME_COL, userInfo.getLastName());

        db.insert(UserInfo.TABLE_NAME, null, values);
    }

    public Map<String, String> signIn(String email, String password) {
        SQLiteDatabase db =  DBHelper.getInstance(context).getReadableDatabase();
        Map<String, String> info = null;
        String query = "SELECT * FROM " + UserInfo.TABLE_NAME + " WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            String originPwd = cursor.getString(cursor.getColumnIndexOrThrow(UserInfo.PASSWORD_COL));
            String hashedPassword = HashUtils.hashPassword(password);
            if (originPwd.equals(hashedPassword)) {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(UserInfo.FIRST_NAME_COL));
                info = Map.of(context.getResources().getString(R.string.key_email), email, context.getResources().getString(R.string.key_first_name), firstName);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return info;
    }
}
