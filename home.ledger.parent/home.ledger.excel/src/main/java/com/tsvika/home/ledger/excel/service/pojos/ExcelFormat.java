package com.tsvika.home.ledger.excel.service.pojos;

import java.util.List;

public class ExcelFormat {
    private int headerlineIndex;
    private int dateIndex;
    private int descriptionIndex;
    private int amountIndex ;
    private int accountIndex ;
    private int categoryIdIndex ;
    private int threshold ;
    private List<String> ignorePattern;

    public ExcelFormat(int headerlineIndex, int dateIndex, int descriptionIndex, int amountIndex, int accountIndex, int categoryIdIndex, int threshold, List<String> ignorePattern) {
        this.headerlineIndex = headerlineIndex;
        this.dateIndex = dateIndex;
        this.descriptionIndex = descriptionIndex;
        this.amountIndex = amountIndex;
        this.accountIndex = accountIndex;
        this.categoryIdIndex = categoryIdIndex;
        this.threshold = threshold;
        this.ignorePattern = ignorePattern;
    }

    public int getHeaderlineIndex() {
        return headerlineIndex;
    }

    public void setHeaderlineIndex(int headerlineIndex) {
        this.headerlineIndex = headerlineIndex;
    }

    public int getDateIndex() {
        return dateIndex;
    }

    public void setDateIndex(int dateIndex) {
        this.dateIndex = dateIndex;
    }

    public int getDescriptionIndex() {
        return descriptionIndex;
    }

    public void setDescriptionIndex(int descriptionIndex) {
        this.descriptionIndex = descriptionIndex;
    }

    public int getAmountIndex() {
        return amountIndex;
    }

    public void setAmountIndex(int amountIndex) {
        this.amountIndex = amountIndex;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public List<String> getIgnorePattern() {
        return ignorePattern;
    }

    public void setIgnorePattern(List<String> ignorePattern) {
        this.ignorePattern = ignorePattern;
    }

    public int getAccountIndex() {
        return accountIndex;
    }

    public void setAccountIndex(int accountIndex) {
        this.accountIndex = accountIndex;
    }

    public int getCategoryIdIndex() {
        return categoryIdIndex;
    }

    public void setCategoryIdIndex(int categoryIdIndex) {
        this.categoryIdIndex = categoryIdIndex;
    }
}
