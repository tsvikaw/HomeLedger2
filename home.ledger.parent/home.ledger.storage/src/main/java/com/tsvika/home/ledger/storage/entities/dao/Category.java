package com.tsvika.home.ledger.storage.entities.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Category {

    @Id
    private String id;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 50)
    @Column(name = "parentId")
    private String parentId;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "isEnabled", nullable = false)
    private Boolean isEnabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Category clone(Category destination){
        destination.setCreatedDate(this.getCreatedDate());
        destination.setDescription(this.getDescription());
        destination.setEnabled(this.getEnabled());
        destination.setId(this.getId());
        destination.setParentId(this.getParentId());
        destination.setName(this.getName());

        return destination;
    }
}
