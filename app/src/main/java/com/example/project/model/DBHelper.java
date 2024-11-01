package com.example.project.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "goshoes.db";
    private static final int DB_VERSION = 11;

    private static DBHelper instance;
    private final WeakReference<Context> context;

    // Table names
    private static final String TABLE_SHOES = "shoes";
    private static final String TABLE_REVIEWS = "reviews";

    // Review columns
    private static final String REVIEW_ID_COL = "review_id";
    private static final String SHOE_ID_COL = "shoe_id";
    private static final String RATING_COL = "rating";
    private static final String REVIEW_TITLE_COL = "review_title";
    private static final String REVIEW_COMMENT_COL = "review_comment";

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = new WeakReference<>(context);
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

        // Create reviews table
        String createReviewsTable = "CREATE TABLE " + TABLE_REVIEWS + " ("
                + REVIEW_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SHOE_ID_COL + " INTEGER, "
                + RATING_COL + " REAL, "
                + REVIEW_TITLE_COL + " TEXT, "
                + REVIEW_COMMENT_COL + " TEXT, "
                + "FOREIGN KEY(" + SHOE_ID_COL + ") REFERENCES " + TABLE_SHOES + "(" + Shoe.ID_COL + "))";

        db.execSQL(createReviewsTable);

        insertIntShoeData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOES);
        onCreate(db);
    }

    private void insertIntShoeData(SQLiteDatabase db) {
        // Existing code for inserting initial shoe data from JSON file
    }

    // Method to add a review for a shoe
    public void addReview(int shoeId, float rating, String title, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SHOE_ID_COL, shoeId);
        values.put(RATING_COL, rating);
        values.put(REVIEW_TITLE_COL, title);
        values.put(REVIEW_COMMENT_COL, comment);

        db.insert(TABLE_REVIEWS, null, values);
        updateShoeRating(shoeId);  // Update the average rating for the shoe
    }

    private void updateShoeRating(int shoeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT AVG(" + RATING_COL + ") as avg_rating, COUNT(" + RATING_COL + ") as review_count " +
                "FROM " + TABLE_REVIEWS + " WHERE " + SHOE_ID_COL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(shoeId)});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") float avgRating = cursor.getFloat(cursor.getColumnIndex("avg_rating"));
            @SuppressLint("Range") int reviewCount = cursor.getInt(cursor.getColumnIndex("review_count"));

            ContentValues values = new ContentValues();
            values.put(Shoe.RATING_COL, avgRating);
            values.put(Shoe.REVIEW_COUNT_COL, reviewCount);

            db.update(TABLE_SHOES, values, Shoe.ID_COL + " = ?", new String[]{String.valueOf(shoeId)});
        }
        cursor.close();
    }

}
