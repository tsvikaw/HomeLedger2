package com.tsvika.home.ledger.dto;

public class CategoryPieSlice {
    public CategoryPieSlice(String categoryId, String categoryName, double amount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amount = amount;
    }

    private String categoryId;
    private String categoryName;
    private double amount;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
