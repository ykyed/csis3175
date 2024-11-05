package com.example.project.model;

public class SizeInfo {

    public static final String TABLE_NAME = "size";
    public static final String ID_COL = "id";
    public static final String PRODUCT_CODE_COL = "productCode";
    public static final String SIZE_COL = "size";
    public static final String QUANTITY_COL = "quantity";

    private int id;
    private String productCode;
    private double size;
    private int quantity;


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

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
