package com.tsvika.home.ledger.editing.service.utilities;

import com.tsvika.home.ledger.dto.*;

public interface IRestUtility {
     CategoryDisplayable createCategory(String url, CategoryForCreation categoryForCreation);
     CategoryDisplayable updateCategory(String url, String id, CategoryForUpdate categoryForUpdate);
     boolean deleteCategory(String url, String id);
     LineDisplayable putLine(String url, LineForUpdate item);
     LineDisplayable getLine(String url, String id);
}
