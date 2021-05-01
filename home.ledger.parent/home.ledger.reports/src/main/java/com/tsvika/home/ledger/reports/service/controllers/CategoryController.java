package com.tsvika.home.ledger.reports.service.controllers;

import java.util.*;

import com.tsvika.home.ledger.common.EnvironmentVariablesWrapper;
import com.tsvika.home.ledger.common.IEnvironmentVariablesWrapper;
import com.tsvika.home.ledger.reports.service.utilities.IRestUtility;
import com.tsvika.home.ledger.dto.CategoryDisplayable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.tsvika.home.ledger.common.Constants.DEFAULT_STORAGE_SERVICE_URL;
import static com.tsvika.home.ledger.common.Constants.ENV_VAR_STORAGE_SERVICE_URL;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private IRestUtility restUtility;

    @Autowired
    private IEnvironmentVariablesWrapper environmentVariablesWrapper;

    public String getCategoryStorageUrl(){
        return getStorageUrl() + "category";
    }

    @RequestMapping(
            value = {""},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<List<CategoryDisplayable>> getCategories() {
        List<CategoryDisplayable> result = restUtility.getCategories(getCategoryStorageUrl());
        result.sort(Comparator.comparing(CategoryDisplayable::getName));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/descendants/{id}"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<List<CategoryDisplayable>> getCategoryDescendants(@PathVariable("id") String id) {
        {
            List<CategoryDisplayable> result = restUtility.getDecendentCategories(getCategoryStorageUrl(), id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    private String getStorageUrl() {
        String storageServiceUrl = environmentVariablesWrapper.getValue(ENV_VAR_STORAGE_SERVICE_URL);
        if (StringUtils.isEmpty(storageServiceUrl)) {
            storageServiceUrl = DEFAULT_STORAGE_SERVICE_URL;
        }
        return storageServiceUrl;
    }
}