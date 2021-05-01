package com.tsvika.home.ledger.dto;

import java.util.List;

public class QueryResult<T> extends Query {
    public QueryResult() {
    }

    public QueryResult(List<T> items, long total) {
        this.setItems(items);
        this.setTotal(total);
    }

    private List<T> items;
    private long total;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
