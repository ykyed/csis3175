package com.example.project.model;

public class ReviewInfo {

    public static final String TABLE_NAME = "reviews";
    public static final String ID_COL = "id";
    public static final String PRODUCT_CODE_COL = "productCode";
    public static final String TITLE_COL = "title";
    public static final String COMMENT_COL = "comment";
    public static final String RATING_COL = "rating";

    private int id;
    private String productCode;
    private String title;
    private String comment;
    private double rating;

    public ReviewInfo() {}

    public ReviewInfo(String productCode, String title, String comment, double rating) {
        this.productCode = productCode;
        this.title = title;
        this.comment = comment;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
