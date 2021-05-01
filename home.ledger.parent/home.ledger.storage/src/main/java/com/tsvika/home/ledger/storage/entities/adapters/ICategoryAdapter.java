package com.tsvika.home.ledger.storage.entities.adapters;

import com.tsvika.home.ledger.storage.entities.dao.Category;
import com.tsvika.home.ledger.dto.CategoryDisplayable;
import com.tsvika.home.ledger.dto.CategoryForCreation;
import com.tsvika.home.ledger.dto.CategoryForUpdate;

public interface ICategoryAdapter {
    CategoryDisplayable generateCategoryDisplayable(Category category);

    Category generateCategory(CategoryForCreation categoryForCreation);

    Category generateCategory(CategoryDisplayable categoryDisplayable);

    Category updateCategory(CategoryForUpdate categoryForUpdate, Category existing);
}
