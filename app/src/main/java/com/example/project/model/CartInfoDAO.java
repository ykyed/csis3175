package com.example.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CartInfoDAO {

    private final Context context;

    public CartInfoDAO(Context context) {
        this.context = context;
    }

    public ArrayList<CartInfo> getCartItemsByUser(String userEmail) {
        SQLiteDatabase db =  DBHelper.getInstance(context).getReadableDatabase();
        ArrayList<CartInfo> infos = new ArrayList<>();
        String query = "SELECT * FROM " + CartInfo.TABLE_NAME + " WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                CartInfo info = new CartInfo();
                info.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CartInfo.ID_COL)));
                info.setProductCode(cursor.getString(cursor.getColumnIndexOrThrow(CartInfo.PRODUCT_CODE_COL)));
                info.setSize(cursor.getDouble(cursor.getColumnIndexOrThrow(CartInfo.SIZE_COL)));
                info.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(CartInfo.QUANTITY_COL)));

                infos.add(info);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return infos;
    }

    public void addItem(CartInfo cartInfo) {

        SQLiteDatabase db =  DBHelper.getInstance(context).getWritableDatabase();
        String query = "SELECT * FROM " + CartInfo.TABLE_NAME + " WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cartInfo.getEmail()});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String productCode = cursor.getString(cursor.getColumnIndexOrThrow(CartInfo.PRODUCT_CODE_COL));
                double size = cursor.getDouble(cursor.getColumnIndexOrThrow(CartInfo.SIZE_COL));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(CartInfo.QUANTITY_COL));

                if (productCode.equals(cartInfo.getProductCode()) && size == cartInfo.getSize()) {
                    // item exist
                    ContentValues values = new ContentValues();
                    values.put(CartInfo.QUANTITY_COL, cartInfo.getQuantity() + quantity);
                    String selection = CartInfo.EMAIL_COL + " = ?";
                    String[] selectionArgs = {cartInfo.getEmail()};

                    db.update(CartInfo.TABLE_NAME, values, selection, selectionArgs);

                    cursor.close();
                    db.close();
                    return;
                }
            } while (cursor.moveToNext());
        }
        ContentValues values = new ContentValues();
        values.put(CartInfo.PRODUCT_CODE_COL, cartInfo.getProductCode());
        values.put(CartInfo.EMAIL_COL, cartInfo.getEmail());
        values.put(CartInfo.SIZE_COL, cartInfo.getSize());
        values.put(CartInfo.QUANTITY_COL, cartInfo.getQuantity());

        db.insert(CartInfo.TABLE_NAME, null, values);
    }

    public void updateItem(CartInfo cartInfo) {

        SQLiteDatabase db =  DBHelper.getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CartInfo.QUANTITY_COL, cartInfo.getQuantity());
        String selection = CartInfo.EMAIL_COL + " = ?";
        String[] selectionArgs = {cartInfo.getEmail()};

        db.update(CartInfo.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    public void deleteItem(int id) {
        SQLiteDatabase db =  DBHelper.getInstance(context).getWritableDatabase();

        String selection = CartInfo.ID_COL + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(CartInfo.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    public int deleteItems(String userEmail) {
        SQLiteDatabase db =  DBHelper.getInstance(context).getWritableDatabase();

        String selection = CartInfo.EMAIL_COL + " = ?";
        String[] selectionArgs = {userEmail};

        int deletedRows = db.delete(CartInfo.TABLE_NAME, selection, selectionArgs);

        db.close();

        return deletedRows;
    }
}
