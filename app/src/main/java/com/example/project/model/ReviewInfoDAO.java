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

    public void addReview(ReviewInfo reviewInfo) {
        SQLiteDatabase db =  DBHelper.getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReviewInfo.PRODUCT_CODE_COL, reviewInfo.getProductCode());
        values.put(ReviewInfo.TITLE_COL, reviewInfo.getTitle());
        values.put(ReviewInfo.COMMENT_COL, reviewInfo.getComment());
        values.put(ReviewInfo.RATING_COL, reviewInfo.getRating());

        db.insert(ReviewInfo.TABLE_NAME, null, values);

        ShoeDAO shoeDAO = new ShoeDAO(context);
        Shoe shoeInfo = shoeDAO.getShoe(reviewInfo.getProductCode());
        shoeInfo.setReviewCount(shoeInfo.getReviewCount() + 1);
        shoeInfo.setTotalRating(shoeInfo.getTotalRating() + reviewInfo.getRating());
        shoeDAO.updateShoe(shoeInfo);
    }
}
