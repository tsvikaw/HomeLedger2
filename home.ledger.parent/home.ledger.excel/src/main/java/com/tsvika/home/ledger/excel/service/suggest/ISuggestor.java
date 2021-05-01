package com.tsvika.home.ledger.excel.service.suggest;

import com.tsvika.home.ledger.excel.service.pojos.ExcelLine;
import com.tsvika.home.ledger.excel.service.pojos.LineWithSuggestedCategories;
import com.tsvika.home.ledger.dto.LineDisplayable;

import java.util.List;

public interface ISuggestor {
    List<LineWithSuggestedCategories> getLineWithSuggestedCategories(List<ExcelLine> list, List<LineDisplayable> existingLines);
}
