package com.tsvika.home.ledger.editing.service.controllers;

import com.tsvika.home.ledger.common.IEnvironmentVariablesWrapper;
import com.tsvika.home.ledger.dto.*;
import com.tsvika.home.ledger.editing.service.pojos.LinesForCategoryUpdate;
import com.tsvika.home.ledger.editing.service.utilities.IRestUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;


import static com.tsvika.home.ledger.common.Constants.DEFAULT_STORAGE_SERVICE_URL;
import static com.tsvika.home.ledger.common.Constants.ENV_VAR_STORAGE_SERVICE_URL;

@RestController
@RequestMapping("/lines")
public class LineController {

    @Autowired
    private IRestUtility restUtility;

    @Autowired
    private IEnvironmentVariablesWrapper environmentVariablesWrapper;


    private String getLineStorageUrl(){
        return getStorageUrl() + "line";
    }

    @RequestMapping(
            value = "/updatecategory",
            method = {RequestMethod.PUT})
    ResponseEntity<LinesForCategoryUpdate> update(@RequestBody
                                                          LinesForCategoryUpdate linesForUpdate) {

        String categoryId = linesForUpdate.getCategoryId();
        List<String> lineIds = linesForUpdate.getLineIds();

        List<String> done = new ArrayList<>();
        LinesForCategoryUpdate result = new LinesForCategoryUpdate(categoryId, done);

        for(String lineId : lineIds) {
            try {
                LineForUpdate lineForUpdate =
                        generateLineForUpdate(restUtility.getLine(getLineStorageUrl(), lineId));
                lineForUpdate.setCategoryId(categoryId);

                restUtility.putLine(getLineStorageUrl(), lineForUpdate);

                done.add(lineId);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    private LineForUpdate generateLineForUpdate(LineDisplayable line) {
        LineForUpdate lineForUpdate = new LineForUpdate();
        lineForUpdate.setId(line.getId());
        lineForUpdate.setAccount(line.getAccount());
        lineForUpdate.setAmount(line.getAmount());
        lineForUpdate.setCategoryId(line.getCategoryId());
        lineForUpdate.setDate(line.getDate());
        lineForUpdate.setDescription(line.getDescription());
        return lineForUpdate;
    }

    private String getStorageUrl() {
        String storageServiceUrl = environmentVariablesWrapper.getValue(ENV_VAR_STORAGE_SERVICE_URL);
        if (StringUtils.isEmpty(storageServiceUrl)) {
            storageServiceUrl = DEFAULT_STORAGE_SERVICE_URL;
        }
        return storageServiceUrl;
    }
}
