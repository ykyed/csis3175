package com.example.project.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ShoeDAO {

    private final Context context;

    public ShoeDAO(Context context) {
        this.context = context;
    }

    public ArrayList<Shoe> getAllShoes() {
        SQLiteDatabase db =  DBHelper.getInstance(context).getReadableDatabase();
        ArrayList<Shoe> shoes = new ArrayList<>();
        Cursor cursor = db.query("shoes", null, null, null, null, null, null);

        while (cursor.moveToNext()) {

            Shoe shoe = new Shoe();
            shoe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Shoe.ID_COL)));
            shoe.setProductCode(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.PRODUCT_CODE_COL)));

            shoe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.TITLE_COL)));
            shoe.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(Shoe.PRICE_COL)));
            shoe.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(Shoe.RATING_COL)));
            shoe.setReviewCount(cursor.getInt(cursor.getColumnIndexOrThrow(Shoe.REVIEW_COUNT_COL)));
            shoe.setColor(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.COLOR_COL)));
            shoe.setStyle(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.STYLE_COL)));
            shoe.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.BRAND_COL)));
            shoe.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.THUMBNAIL_COL)));

            shoes.add(shoe);
        }
        cursor.close();
        db.close();

        return shoes;
    }
}
