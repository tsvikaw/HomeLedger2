package com.tsvika.home.ledger.editing.service.controllers;

import com.tsvika.home.ledger.common.IEnvironmentVariablesWrapper;
import com.tsvika.home.ledger.dto.CategoryDisplayable;
import com.tsvika.home.ledger.dto.CategoryForCreation;
import com.tsvika.home.ledger.dto.CategoryForUpdate;
import com.tsvika.home.ledger.editing.service.utilities.IRestUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
            value = "",
            method = {RequestMethod.POST})
    ResponseEntity<CategoryDisplayable> create(@Valid @RequestBody CategoryForCreation categoryForCreation) {
            CategoryDisplayable result = restUtility.createCategory(getCategoryStorageUrl(), categoryForCreation); // todo: exception handling
            return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{id}",
            method = {RequestMethod.PUT})
    ResponseEntity<CategoryDisplayable> update(@PathVariable("id") String id,
                                               @Valid @RequestBody CategoryForUpdate categoryForUpdate) {
        CategoryDisplayable result = restUtility.updateCategory(getCategoryStorageUrl(), id, categoryForUpdate); // todo: exception handling
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.DELETE},
            produces = {"application/json"}
    )
    public ResponseEntity deleteCategory(@PathVariable("id") String id) {
        restUtility.deleteCategory(getCategoryStorageUrl(), id); // todo: exception handling
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    private String getStorageUrl() {
        String storageServiceUrl = environmentVariablesWrapper.getValue(ENV_VAR_STORAGE_SERVICE_URL);
        if (StringUtils.isEmpty(storageServiceUrl)) {
            storageServiceUrl = DEFAULT_STORAGE_SERVICE_URL;
        }
        return storageServiceUrl;
    }
}