package com.tsvika.home.ledger.excel.service.utilities;

import com.tsvika.home.ledger.excel.service.pojos.ExcelFormat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelFormatStore {
    public ExcelFormatStore() {
    }

    public ExcelFormat getExcelLinesFormat(String path) {
        List<String> ignorePatterns = new ArrayList<String>();

        if (path.indexOf("transaction") != -1) {
            return new ExcelFormat(4, 0, 1, 5, -1,
                    -1, -100000, ignorePatterns);
        }

        if (path.indexOf("Deals") != -1) {
            return new ExcelFormat(1, 0, 2, 6, -1,
                    -1, -100000, ignorePatterns);
        }

        ignorePatterns.add("ויזה");
        ignorePatterns.add("מסטרקארד");
        ignorePatterns.add("גביה/החזר מס");
        ignorePatterns.add("נ\"ע בבורסה");
        ignorePatterns.add("קנית נ\"ע");
        ignorePatterns.add("החזר.שיק*");
        ignorePatterns.add("כרטיסי אשראי-י");
        ignorePatterns.add("קניה-אינטרנט");

        return new ExcelFormat(22, 0, 1, 3, -1,
                -1, 5,
                ignorePatterns);
    }

    public List<String> getIgnorePatterns(){
        List<String> ignorePatterns = new ArrayList<String>();
        ignorePatterns.add("ויזה");
        ignorePatterns.add("מסטרקארד");
        ignorePatterns.add("גביה/החזר מס");
        ignorePatterns.add("נ\"ע בבורסה");
        ignorePatterns.add("קנית נ\"ע");
        ignorePatterns.add("החזר.שיק*");
        ignorePatterns.add("כרטיסי אשראי-י");
        ignorePatterns.add("קניה-אינטרנט");

        return ignorePatterns;
    }
}