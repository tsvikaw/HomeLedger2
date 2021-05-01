package com.tsvika.home.ledger.excel.service.suggest;

import com.tsvika.home.ledger.excel.service.pojos.CategoryHit;
import com.tsvika.home.ledger.dto.LineDisplayable;

import java.util.List;

public interface IIndexer {
    void index(List<LineDisplayable> lines);

    List<CategoryHit> getCategoryHits(String description);
}
