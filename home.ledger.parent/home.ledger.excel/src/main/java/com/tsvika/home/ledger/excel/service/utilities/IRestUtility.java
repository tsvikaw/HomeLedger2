package com.tsvika.home.ledger.excel.service.utilities;

import com.tsvika.home.ledger.dto.*;

import java.util.List;

public interface IRestUtility {
     LineDisplayable postLine(String url, LineForCreation item);
     LineDisplayable putLine(String url, LineForUpdate item);
     List<CategoryDisplayable> getCategories(String url);
     CategoryDisplayable postCategory(String url, CategoryForCreation item);
     List<LineDisplayable> getLines(String url);
     LineDisplayable getLine(String url, String id);
}
