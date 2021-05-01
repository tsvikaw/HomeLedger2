package com.tsvika.home.ledger.excel.service.pojos;

import java.util.Date;

public class ExcelLine {
    public ExcelLine(String description, Double amount, Date date, String account, String categoryId) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.account = account;
        this.categoryId = categoryId;
    }

    private String description;

    private Double amount;

    private Date date;

    private String account;

    private String categoryId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
