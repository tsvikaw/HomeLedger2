package com.tsvika.home.ledger.excel.service.suggest;

import com.tsvika.home.ledger.excel.service.pojos.CategoryHit;
import com.tsvika.home.ledger.excel.service.pojos.ExcelLine;
import com.tsvika.home.ledger.excel.service.pojos.LineWithSuggestedCategories;
import com.tsvika.home.ledger.dto.CategoryDisplayable;
import com.tsvika.home.ledger.dto.LineDisplayable;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Suggestor implements ISuggestor {

    public static final int CAN_ASSUME_CATEGORY_ONLY_HIT = 3;
    @Autowired
    private IIndexer indexer;

    @Override
    public List<LineWithSuggestedCategories> getLineWithSuggestedCategories(List<ExcelLine> list, List<LineDisplayable> existingLines) {
        indexer.index(existingLines);

        return list.stream().map(l -> {
            List<CategoryDisplayable> categories = getExistingMatchingCategoriesByDescription(existingLines, l);
            List<CategoryHit> categoryHits = getCategoryHits(categories);

            boolean hasExistingIdenticalLine = false;

            if(CollectionUtils.isEmpty(categoryHits)){
                categoryHits = indexer.getCategoryHits(l.getDescription());
            } else {
                Optional<LineDisplayable> existingLine = getExistingIdenticalLine(existingLines, l);

                if(existingLine.isPresent()){
                    hasExistingIdenticalLine = true;
                    l.setCategoryId(existingLine.get().getCategoryId());
                }
            }

            if(canAssumeFirstHitCategory(categoryHits)){
                l.setCategoryId(categoryHits.get(0).getCategory().getId());
            }

            return new LineWithSuggestedCategories(l, categoryHits, hasExistingIdenticalLine);
        }).collect(Collectors.toList());
    }

    private Optional<LineDisplayable> getExistingIdenticalLine(List<LineDisplayable> existingLines, ExcelLine l) {
        return existingLines.stream().filter(el ->
                l.getDate().getTime() >= el.getDate().getTime() - 24*3600*1000 &&
                        l.getDate().getTime() <= el.getDate().getTime() + 24*3600*1000 &&
                l.getAmount().equals(el.getAmount()) &&
                l.getDescription().equals(el.getDescription())).findFirst();
    }

    private boolean canAssumeFirstHitCategory(List<CategoryHit> categoryHits) {
        return categoryHits.size() == 1 && categoryHits.get(0).getHits() > CAN_ASSUME_CATEGORY_ONLY_HIT ||
                categoryHits.size() >1 && categoryHits.get(0).getHits() >
                        categoryHits.stream().map(c -> c.getHits()).reduce(0, Integer::sum)
                        - categoryHits.get(0).getHits();
    }

    private List<CategoryHit> getCategoryHits(List<CategoryDisplayable> categories) {
        List<CategoryHit> categoryHits = new ArrayList<CategoryHit>();
        if (!categories.isEmpty()) {
            Map<String, Long> map = categories.stream().collect(Collectors.groupingBy(
                    CategoryDisplayable::getId, Collectors.counting()));
            map.forEach((key, value) -> {
                CategoryDisplayable category = categories.stream().filter(c -> c.getId().equals(key)).findFirst().get();
                categoryHits.add(new CategoryHit(category, value.intValue()));
            });
            categoryHits.sort(Comparator.comparing(CategoryHit::getHits).reversed());
        }
        return categoryHits;
    }

    private List<CategoryDisplayable> getExistingMatchingCategoriesByDescription(List<LineDisplayable> existingLines, ExcelLine l) {
        List<CategoryDisplayable> categories = existingLines.stream().filter(el -> el.getDescription().equals(l.getDescription()))
                .map(el -> new CategoryDisplayable() {{
                    setName(el.getCategoryName());
                    setId(el.getCategoryId());
                }})
                .collect(Collectors.toList());
        return categories;
    }
}