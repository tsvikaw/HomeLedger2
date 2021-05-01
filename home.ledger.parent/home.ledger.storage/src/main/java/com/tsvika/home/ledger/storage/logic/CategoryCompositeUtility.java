package com.tsvika.home.ledger.storage.logic;

import com.tsvika.home.ledger.storage.entities.dao.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CategoryCompositeUtility implements ICategoryCompositeUtility {

    @Override
    public Collection<Category> getChildrenOf(String categoryId, Collection<Category> categories){
        return getChildrenOf(categoryId, getComposites(categories));
    }

    private Collection<Category> getChildrenOf(String categoryId, Map<String, CategoryComposite> categories){
        return categories.getOrDefault(categoryId, new CategoryComposite()).getChildren();
    }

    @Override
    public Collection<Category> getDecendentsOf(String categoryId, Collection<Category> categories){
        Collection<Category> decendents = getDecendentsOf(categoryId, getComposites(categories));
        decendents.removeIf(c -> c.getId() == categoryId);
        return decendents;
    }

    public Collection<Category> getDecendentsOf(String categoryId, Map<String, CategoryComposite>  categories){
        Collection<Category> decendents = getChildrenOf(categoryId, categories).stream().flatMap(c -> getDecendentsOf(c.getId(), categories).stream())
                .collect(Collectors.toList());
        decendents.add(categories.get(categoryId));
        return decendents;
    }

    @Override
    public Category getParentOf(String categoryId, Collection<Category> categories){
        return getParentOf(categoryId, getComposites(categories));
    }

    public Category getParentOf(String categoryId, Map<String, CategoryComposite> categories){
        return categories.getOrDefault(categoryId, new CategoryComposite()).getParent();
    }

    @Override
    public Collection<Category> getAncestorsOf(String categoryId, Collection<Category> categories){
        Collection<Category> ancestors = getAncestorsOf(categoryId, getComposites(categories));
        ancestors.removeIf(c -> c.getId() == categoryId);
        return ancestors;
    }

    public Collection<Category> getAncestorsOf(String categoryId, Map<String, CategoryComposite> categories){
        Category parent = getParentOf(categoryId, categories);
        if(parent == null) {
            return new ArrayList<Category>() {{ add(categories.get(categoryId));}};
        }

        Collection<Category> parentAsCollection = new ArrayList<Category>() {{ add(parent);}};
        Collection<Category> ancestors = parentAsCollection.stream().flatMap(c -> getAncestorsOf(c.getId(), categories).stream())
                .collect(Collectors.toList());
        ancestors.add(categories.get(categoryId));
        return ancestors;
    }

    private Map<String, CategoryComposite> getComposites (Collection<Category> categories){
        Map<String, CategoryComposite> map = categories.stream()
                .map(CategoryComposite::fromCategory)
                .collect(Collectors.toMap(c -> c.getId(), c -> c));

        map.values().forEach(c -> {
            if(c.getParentId() != null) {
                map.get(c.getParentId()).addChild(c);
                c.setParent(map.get(c.getParentId()));
            }
        });

        return map;
    }
}
