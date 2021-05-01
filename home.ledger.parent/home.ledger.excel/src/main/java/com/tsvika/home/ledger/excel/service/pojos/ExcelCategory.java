package com.tsvika.home.ledger.excel.service.pojos;

import java.util.Date;

public class ExcelCategory {
    public ExcelCategory(Double id, Double parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    private Double id;

    private Double parentId;

    private String name;

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public Double getParentId() {
        return parentId;
    }

    public void setParentId(Double parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
