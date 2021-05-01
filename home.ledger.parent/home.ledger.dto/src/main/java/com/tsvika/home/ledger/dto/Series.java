package com.tsvika.home.ledger.dto;

import java.util.Map;

public class Series {
    public Series(String id, String label, Map<String,Double> data) {
        this.setId(id);
        this.label = label;
        this.data = data;
    }

    private String label;
    private String id;
    private Map<String,Double> data;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Map<String,Double> getData() {
        return data;
    }

    public void setData(Map<String,Double> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
