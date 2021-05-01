package com.tsvika.home.ledger.storage.logic;

import com.tsvika.home.ledger.storage.entities.dao.Category;

import java.util.Collection;

public interface ICategoryCompositeUtility {
    Collection<Category> getChildrenOf(String categoryId, Collection<Category> categories);

    Collection<Category> getDecendentsOf(String categoryId, Collection<Category> categories);

    Category getParentOf(String categoryId, Collection<Category> categories);

    Collection<Category> getAncestorsOf(String categoryId, Collection<Category> categories);
}
