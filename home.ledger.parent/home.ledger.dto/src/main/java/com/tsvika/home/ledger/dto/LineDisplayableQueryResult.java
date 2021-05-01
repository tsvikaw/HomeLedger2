package com.tsvika.home.ledger.dto;

import java.util.List;

public class LineDisplayableQueryResult extends QueryResult<LineDisplayable> {
    public LineDisplayableQueryResult() {
        super();
    }

    public LineDisplayableQueryResult(List<LineDisplayable> items, long total) {
        super(items, total);
    }
}
