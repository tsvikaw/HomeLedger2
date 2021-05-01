package com.tsvika.home.ledger.dto;

import java.util.Date;

public class LineQuery extends Query {

    private String description;
    private String account;
    private Date startDate;
    private Date endDate;
    private double startAmount;
    private double endAmount;
    private CategoryDisplayable[] categories;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(double startAmount) {
        this.startAmount = startAmount;
    }

    public double getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(double endAmount) {
        this.endAmount = endAmount;
    }

    public CategoryDisplayable[] getCategories() {
        return categories;
    }

    public void setCategories(CategoryDisplayable[] categories) {
        this.categories = categories;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
