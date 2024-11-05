package com.example.project.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SizeInfoDAO {

    private final Context context;

    public SizeInfoDAO(Context context) {
        this.context = context;
    }

    public List<SizeInfo> getSizesByProductCode(String productCode) {

        SQLiteDatabase db =  DBHelper.getInstance(context).getReadableDatabase();
        ArrayList<SizeInfo> sizes = new ArrayList<>();
        String query = "SELECT * FROM " + SizeInfo.TABLE_NAME + " WHERE productCode = ?";
        Cursor cursor = db.rawQuery(query, new String[]{productCode});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                SizeInfo size = new SizeInfo();
                size.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SizeInfo.ID_COL)));
                size.setProductCode(cursor.getString(cursor.getColumnIndexOrThrow(SizeInfo.PRODUCT_CODE_COL)));
                size.setSize(cursor.getDouble(cursor.getColumnIndexOrThrow(SizeInfo.SIZE_COL)));
                size.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(SizeInfo.QUANTITY_COL)));

                sizes.add(size);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return sizes;
    }
}
