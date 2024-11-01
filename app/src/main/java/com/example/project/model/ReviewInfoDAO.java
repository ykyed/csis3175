package com.example.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ReviewInfoDAO {

    private final Context context;

    public ReviewInfoDAO(Context context) {
        this.context = context;
    }

    public ArrayList<ReviewInfo> getReviewsByPrductCode(String productCode) {
        SQLiteDatabase db =  DBHelper.getInstance(context).getReadableDatabase();
        ArrayList<ReviewInfo> reviews = new ArrayList<>();
        String query = "SELECT * FROM " + ReviewInfo.TABLE_NAME + " WHERE productCode = ?";
        Cursor cursor = db.rawQuery(query, new String[]{productCode});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ReviewInfo review = new ReviewInfo();
                review.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ReviewInfo.ID_COL)));
                review.setProductCode(cursor.getString(cursor.getColumnIndexOrThrow(ReviewInfo.PRODUCT_CODE_COL)));
                review.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(ReviewInfo.TITLE_COL)));
                review.setComment(cursor.getString(cursor.getColumnIndexOrThrow(ReviewInfo.COMMENT_COL)));
                review.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(ReviewInfo.RATING_COL)));

                reviews.add(review);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return reviews;
    }

    // Method to add a review for a shoe
    public void addReview(ReviewInfo reviewInfo) {
        SQLiteDatabase db =  DBHelper.getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReviewInfo.PRODUCT_CODE_COL, reviewInfo.getProductCode());
        values.put(ReviewInfo.TITLE_COL, reviewInfo.getTitle());
        values.put(ReviewInfo.COMMENT_COL, reviewInfo.getComment());
        values.put(ReviewInfo.RATING_COL, reviewInfo.getRating());

        db.insert(ReviewInfo.TABLE_NAME, null, values);
        //updateShoeRating(shoeId);  // Update the average rating for the shoe
    }

    /*
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
    */

}
