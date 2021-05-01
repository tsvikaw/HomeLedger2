package com.tsvika.home.ledger.storage.entities.dao;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Line {

    @Id
    private String id;

    @Size(max = 255)
    @Column(name = "name")
    private String description;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    private Date date;

    @Size(max = 255)
    @Column(name = "account")
    private String account;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
