package com.tsvika.home.ledger.editing.service.pojos;

import java.util.List;

public class LinesForCategoryUpdate {
    private String categoryId;
    private List<String> lineIds;

    public LinesForCategoryUpdate() {
    }

    public LinesForCategoryUpdate(String categoryId, List<String> lineIds) {
        this.categoryId = categoryId;
        this.lineIds = lineIds;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getLineIds() {
        return lineIds;
    }

    public void setLineIds(List<String> lineIds) {
        this.lineIds = lineIds;
    }
}
