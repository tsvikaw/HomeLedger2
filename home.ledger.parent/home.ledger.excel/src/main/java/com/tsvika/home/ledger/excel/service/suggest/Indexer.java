package com.tsvika.home.ledger.excel.service.suggest;

import com.tsvika.home.ledger.excel.service.pojos.CategoryHit;
import com.tsvika.home.ledger.dto.CategoryDisplayable;
import com.tsvika.home.ledger.dto.LineDisplayable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class Indexer implements IIndexer {
    private final int TOO_NANY_CATEGORIERS_HIT = 8;
    private Map<String /* word */, List<CategoryDisplayable>> index;

    @Override
    public void index(List<LineDisplayable> lines){
        if(null == index){
            index = new HashMap<>();
            for (LineDisplayable line:
                 lines) {
                for (String word:
                     line.getDescription().split(" ")) {
                    if(word.length() > 0) {
                        index.putIfAbsent(word, new ArrayList<>());
                        index.getOrDefault(word, null).add(new CategoryDisplayable() {{
                            setId(line.getCategoryId());
                            setName(line.getCategoryName());
                        }});
                    }
                }
            }

            index.entrySet().removeIf(keyValue -> keyValue.getValue().stream().filter(distinctByKey(c -> c.getId())).count() > TOO_NANY_CATEGORIERS_HIT);
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    @Override
    public List<CategoryHit> getCategoryHits(String description){
        List<CategoryHit> result = new ArrayList<>();
        for (String word:
                description.split(" ")) {
            List<CategoryDisplayable> categoryHits = index.getOrDefault(word, null);
            if(categoryHits != null){
                for (CategoryDisplayable categoryHit:
                     categoryHits) {
                    Optional<CategoryHit> foundInResult =
                            result.stream().filter(r -> r.getCategory().getId().equals(categoryHit.getId())).findFirst();
                    if(!foundInResult.isPresent()){
                        CategoryHit e = new CategoryHit(categoryHit, 1);
                        result.add(e);
                    } else {
                        foundInResult.get().setHits(foundInResult.get().getHits() + 1);
                    }
                }
            }
        }

        result.sort(Comparator.comparing(CategoryHit::getHits).reversed());
        return result;
    }
}
