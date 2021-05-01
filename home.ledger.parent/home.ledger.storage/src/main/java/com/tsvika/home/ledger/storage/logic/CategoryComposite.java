package com.tsvika.home.ledger.storage.logic;

import com.tsvika.home.ledger.storage.entities.dao.Category;

import java.util.ArrayList;
import java.util.Collection;

class CategoryComposite extends Category {

    private Category parent;
    private Collection<Category> children;

    CategoryComposite() {
        setChildren(new ArrayList<>());
    }

    void addChild(Category child){
        this.getChildren().add(child);
    }

    Category getParent() {
        return parent;
    }

    void setParent(Category parent) {
        this.parent = parent;
    }

    Collection<Category> getChildren() {
        return children;
    }

    void setChildren(Collection<Category> children) {
        this.children = children;
    }

    static CategoryComposite fromCategory(Category category){
        CategoryComposite categoryComposite = new CategoryComposite();
        category.clone(categoryComposite);
        return  categoryComposite;
    }
}
