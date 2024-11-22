package com.example.project.model;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.List;

public class Shoe {

    public static final String TABLE_NAME = "shoes";
    public static final String ID_COL = "id";
    public static final String PRODUCT_CODE_COL = "productCode";
    public static final String TITLE_COL = "title";
    public static final String PRICE_COL = "price";
    public static final String RATING_COL = "rating";
    public static final String REVIEW_COUNT_COL = "reviewCount";
    public static final String COLOR_COL = "color";
    public static final String STYLE_COL = "style";
    public static final String BRAND_COL = "brand";
    public static final String THUMBNAIL_COL = "thumbnail";
    public static final String IMAGES_COL = "images";

    private int id;
    private String productCode;
    private String title;
    private double price;
    private double totalRating;
    private int reviewCount;
    private String color;
    private String style;
    private String brand;
    private String thumbnail;
    private String images;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double rating) {
        this.totalRating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
