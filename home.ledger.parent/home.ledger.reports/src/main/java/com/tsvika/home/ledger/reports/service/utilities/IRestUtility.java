package com.tsvika.home.ledger.reports.service.utilities;

import com.tsvika.home.ledger.dto.*;

import java.util.List;

public interface IRestUtility {
     List<CategoryDisplayable> getCategories(String url);
     List<String> getAccounts(String url);
     List<CategoryDisplayable> getDecendentCategories(String url, String categoryId);
     List<CategoryDisplayable> getChildrenCategories(String url, String categoryId);
     LineDisplayableQueryResult getLinesByQuery(String url, LineQuery lineQuery);
}
