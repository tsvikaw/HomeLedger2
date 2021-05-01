package com.tsvika.home.ledger.storage.entities.adapters;

import com.tsvika.home.ledger.storage.entities.dao.Category;
import com.tsvika.home.ledger.dto.CategoryDisplayable;
import com.tsvika.home.ledger.dto.CategoryForCreation;
import com.tsvika.home.ledger.dto.CategoryForUpdate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class CategoryAdapter implements ICategoryAdapter {
    public CategoryDisplayable generateCategoryDisplayable(Category category) {
        CategoryDisplayable categoryDisplayable = new CategoryDisplayable();
        categoryDisplayable.setCreatedDate(category.getCreatedDate());
        categoryDisplayable.setDescription(category.getDescription());
        categoryDisplayable.setEnabled(category.getEnabled());
        categoryDisplayable.setId(category.getId());
        categoryDisplayable.setParentId(category.getParentId());
        categoryDisplayable.setName(category.getName());
        return categoryDisplayable;
    }

    public  Category generateCategory(CategoryDisplayable categoryDisplayable) {
        Category category = new Category();
        category.setId(categoryDisplayable.getId());
        category.setCreatedDate(categoryDisplayable.getCreatedDate());
        category.setEnabled(categoryDisplayable.getEnabled());
        category.setName(categoryDisplayable.getName());
        category.setDescription(categoryDisplayable.getDescription());
        category.setParentId(categoryDisplayable.getParentId());
        return category;
    }

    public  Category generateCategory(CategoryForCreation categoryForCreation) {
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setCreatedDate(new Date());
        category.setEnabled(true);
        category.setName(categoryForCreation.getName());
        category.setDescription(categoryForCreation.getDescription());
        category.setParentId(categoryForCreation.getParentId());
        return category;
    }

    public Category updateCategory(CategoryForUpdate categoryForUpdate, Category existing) {
        existing.setCreatedDate(new Date());
        existing.setName(categoryForUpdate.getName());
        existing.setDescription(categoryForUpdate.getDescription());
        existing.setParentId(categoryForUpdate.getParentId());
        existing.setEnabled(categoryForUpdate.getEnabled());
        return existing;
    }
}
