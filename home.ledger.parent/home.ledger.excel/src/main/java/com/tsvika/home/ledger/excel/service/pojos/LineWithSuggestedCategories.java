package com.tsvika.home.ledger.excel.service.pojos;

import java.util.List;

public class LineWithSuggestedCategories {
    private ExcelLine line;
    private List<CategoryHit> categoryHits;
    private boolean isPreviouselyImported;

    public LineWithSuggestedCategories(ExcelLine line, List<CategoryHit> categoryHits, boolean isPreviouselyImported) {
        this.setLine(line);
        this.setCategoryHits(categoryHits);
        this.setPreviouselyImported(isPreviouselyImported);
    }

    public ExcelLine getLine() {
        return line;
    }

    public void setLine(ExcelLine line) {
        this.line = line;
    }

    public List<CategoryHit> getCategoryHits() {
        return categoryHits;
    }

    public void setCategoryHits(List<CategoryHit> categoryHits) {
        this.categoryHits = categoryHits;
    }

    public boolean isPreviouselyImported() {
        return isPreviouselyImported;
    }

    public void setPreviouselyImported(boolean previouselyImported) {
        isPreviouselyImported = previouselyImported;
    }
}
