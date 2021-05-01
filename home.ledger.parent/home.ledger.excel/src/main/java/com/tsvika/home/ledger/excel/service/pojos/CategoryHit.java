package com.tsvika.home.ledger.excel.service.pojos;

import com.tsvika.home.ledger.dto.CategoryDisplayable;

public class CategoryHit {
    private CategoryDisplayable category;
    private int hits;

    public CategoryHit(CategoryDisplayable category, int hits) {
        this.category = category;
        this.hits = hits;
    }

    public CategoryDisplayable getCategory() {
        return category;
    }

    public void setCategory(CategoryDisplayable category) {
        this.category = category;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }
}
