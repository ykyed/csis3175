package com.example.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShoeDAO {

    private final Context context;

    public ShoeDAO(Context context) {
        this.context = context;
    }

    public ArrayList<Shoe> getAllShoes() {
        SQLiteDatabase db =  DBHelper.getInstance(context).getReadableDatabase();
        ArrayList<Shoe> shoes = new ArrayList<>();
        Cursor cursor = db.query(Shoe.TABLE_NAME, null, null, null, null, null, null);

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

    public Shoe getShoe(String productCode) {
        SQLiteDatabase db =  DBHelper.getInstance(context).getReadableDatabase();
        Shoe shoe = new Shoe();
        String query = "SELECT * FROM " + Shoe.TABLE_NAME + " WHERE productCode = ?";
        Cursor cursor = db.rawQuery(query, new String[]{productCode});

        if (cursor != null && cursor.moveToFirst()) {
            shoe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.TITLE_COL)));
            shoe.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(Shoe.PRICE_COL)));
            shoe.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(Shoe.RATING_COL)));
            shoe.setReviewCount(cursor.getInt(cursor.getColumnIndexOrThrow(Shoe.REVIEW_COUNT_COL)));
            shoe.setColor(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.COLOR_COL)));
            shoe.setStyle(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.STYLE_COL)));
            shoe.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.BRAND_COL)));
            shoe.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(Shoe.THUMBNAIL_COL)));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return shoe;
    }

    public void updateShoe(Shoe shoe) {
        SQLiteDatabase db =  DBHelper.getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Shoe.RATING_COL, shoe.getRating());
        values.put(Shoe.REVIEW_COUNT_COL, shoe.getReviewCount());
        String selection = Shoe.PRODUCT_CODE_COL + " = ?";
        String[] selectionArgs = {shoe.getProductCode()};

        db.update(Shoe.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    public List<Shoe> filterShoes(Set<String> brands, Set<String> colors, Set<String> styles) {

        SQLiteDatabase db =  DBHelper.getInstance(context).getReadableDatabase();

        List<Shoe> filteredShoes = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM " + Shoe.TABLE_NAME + " WHERE 1=1");

        if (!brands.isEmpty()) {
            query.append(" AND brand IN (").append(makePlaceholders(brands.size())).append(")");
        }
        if (!colors.isEmpty()) {
            query.append(" AND color IN (").append(makePlaceholders(colors.size())).append(")");
        }
        if (!styles.isEmpty()) {
            query.append(" AND style IN (").append(makePlaceholders(styles.size())).append(")");
        }

        List<String> args = new ArrayList<>();
        args.addAll(brands);
        args.addAll(colors);
        args.addAll(styles);

        Cursor cursor = db.rawQuery(query.toString(), args.toArray(new String[0]));
        if (cursor.moveToFirst()) {
            do {
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

                filteredShoes.add(shoe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return filteredShoes;
    }

    private String makePlaceholders(int count) {
        if (count < 1) return "";
        StringBuilder builder = new StringBuilder(count * 2 - 1);
        builder.append("?");
        for (int i = 1; i < count; i++) {
            builder.append(",?");
        }
        return builder.toString();
    }

    public List<String> getDistinctBrands() {
        return getDistinctValues("brand");
    }

    public List<String> getDistinctColors() {
        return getDistinctValues("color");
    }

    public List<String> getDistinctStyles() {
        return getDistinctValues("style");
    }

    private List<String> getDistinctValues(String column) {

        SQLiteDatabase db =  DBHelper.getInstance(context).getReadableDatabase();

        List<String> values = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + column + " FROM " + Shoe.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                values.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return values;
    }
}
