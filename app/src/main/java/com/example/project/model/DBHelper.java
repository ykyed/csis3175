package com.example.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "goshoes.db";
    private static final int DB_VERSION = 1;

    private static DBHelper instance;
    private final WeakReference<Context> context;

    // for shoes table
    private static final String TABLE_SHOES = "shoes";

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = new WeakReference<>(context);;
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createShoesTable = "CREATE TABLE " + TABLE_SHOES + " ("
                + Shoe.ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Shoe.PRODUCT_CODE_COL + " TEXT, "
                + Shoe.TITLE_COL + " TEXT, "
                + Shoe.PRICE_COL + " REAL, "
                + Shoe.RATING_COL + " REAL, "
                + Shoe.REVIEW_COUNT_COL + " INTEGER, "
                + Shoe.COLOR_COL + " TEXT, "
                + Shoe.STYLE_COL + " TEXT, "
                + Shoe.BRAND_COL + " TEXT, "
                + Shoe.THUMBNAIL_COL + " TEXT)";

        db.execSQL(createShoesTable);

        insertIntShoeData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOES);
        onCreate(db);
    }

    private void insertIntShoeData(SQLiteDatabase db) {
        try {
            InputStream is = context.get().getAssets().open("shoes_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            int bytesRead = 0;
            int totalBytesRead = 0;
            while (totalBytesRead < size) {
                bytesRead = is.read(buffer, totalBytesRead, size - totalBytesRead);
                if (bytesRead == -1) {
                    break;
                }
                totalBytesRead += bytesRead;
            }
            is.close();

            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray shoesArray = jsonObject.getJSONArray("shoes");

            for (int i = 0; i < shoesArray.length(); i++) {
                JSONObject shoeObject = shoesArray.getJSONObject(i);
                String productCode = shoeObject.getString(Shoe.PRODUCT_CODE_COL);
                String title = shoeObject.getString(Shoe.TITLE_COL);
                double price = shoeObject.getDouble(Shoe.PRICE_COL);
                double rating = shoeObject.getDouble(Shoe.RATING_COL);
                int reviewCount = shoeObject.getInt(Shoe.REVIEW_COUNT_COL);
                String color = shoeObject.getString(Shoe.COLOR_COL);
                String style = shoeObject.getString(Shoe.STYLE_COL);
                String brand = shoeObject.getString(Shoe.BRAND_COL);
                String thumbnail = shoeObject.getString(Shoe.THUMBNAIL_COL);

                ContentValues values = new ContentValues();
                values.put(Shoe.PRODUCT_CODE_COL, productCode);
                values.put(Shoe.TITLE_COL, title);
                values.put(Shoe.PRICE_COL, price);
                values.put(Shoe.RATING_COL, rating);
                values.put(Shoe.REVIEW_COUNT_COL, reviewCount);
                values.put(Shoe.COLOR_COL, color);
                values.put(Shoe.STYLE_COL, style);
                values.put(Shoe.BRAND_COL, brand);
                values.put(Shoe.THUMBNAIL_COL, thumbnail);

                db.insert("shoes", null, values);
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error reading JSON file", e);
        }
    }
}
