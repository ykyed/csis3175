package com.example.project.model;

public class CartInfo {

    public static final String TABLE_NAME = "carts";
    public static final String ID_COL = "id";
    public static final String EMAIL_COL = "email";
    public static final String PRODUCT_CODE_COL = "productCode";
    public static final String SIZE_COL = "size";
    public static final String QUANTITY_COL = "quantity";

    private int id;
    private String email;
    private String productCode;
    private double size;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
