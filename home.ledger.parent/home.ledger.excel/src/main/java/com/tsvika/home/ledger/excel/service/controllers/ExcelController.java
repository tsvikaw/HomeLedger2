package com.tsvika.home.ledger.excel.service.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.tsvika.home.ledger.common.IEnvironmentVariablesWrapper;
import com.tsvika.home.ledger.excel.service.suggest.ISuggestor;
import com.tsvika.home.ledger.excel.service.pojos.*;
import com.tsvika.home.ledger.excel.service.utilities.ExcelFormatStore;
import com.tsvika.home.ledger.excel.service.utilities.ExcelUtility;
import com.tsvika.home.ledger.excel.service.utilities.IRestUtility;
import com.tsvika.home.ledger.dto.CategoryDisplayable;
import com.tsvika.home.ledger.dto.LineDisplayable;
import com.tsvika.home.ledger.dto.LineForCreation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.tsvika.home.ledger.common.Constants.DEFAULT_STORAGE_SERVICE_URL;
import static com.tsvika.home.ledger.common.Constants.ENV_VAR_STORAGE_SERVICE_URL;


@RestController
@RequestMapping("/import")
public class ExcelController {

    public static final String ENV_VAR_UPLOADS_FOLDER = "UPLOADS_FOLDER";
    public static final String DEFAULT_UPLOADS_FOLDER = "/uploads/";

    @Autowired
    private IRestUtility restUtility;

    @Autowired
    private ISuggestor suggestor;

    @Autowired
    private ExcelFormatStore excelFormatStore;

    @Autowired
    private IEnvironmentVariablesWrapper environmentVariablesWrapper;


    private String getLineStorageUrl(){
        return getStorageUrl() + "line";
    }

    private String getCategoryStorageUrl(){
        return getStorageUrl() + "category";
    }

//    @RequestMapping(
//            value = {"/parse"},
//            method = {RequestMethod.GET},
//            produces = {"application/json"}
//    )
//    public ResponseEntity<List<LineWithSuggestedCategories>> getExcelLines(@RequestParam("file") String filePath)
//    {
//            ExcelUtility excelUtility = new ExcelUtility();
//
//            ExcelFormat excelLinesFormat = excelFormatStore.getExcelLinesFormat(filePath);
//            List<ExcelLine> list = getExcelLines(filePath, excelUtility, excelLinesFormat);
//            List<LineDisplayable> existingLines = restUtility.getLines(STORAGE_SERVICE_LINE_URL);
//
//            List<LineWithSuggestedCategories> result = suggestor.getLineWithSuggestedCategories(list, existingLines);
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

    @RequestMapping(
            value = {"/parse/{sheetNumber}/{skipLines}/{dateColumn}/{amountColumn}/{descriptionColumn}"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<List<LineWithSuggestedCategories>> getExcelLinesWithParameters(
            @RequestParam("file") String filePath,
            @PathVariable("sheetNumber") int sheetNumber,
            @PathVariable("skipLines") int skipLines,
            @PathVariable("dateColumn") int dateColumn,
            @PathVariable("amountColumn") int amountColumn,
            @PathVariable("descriptionColumn") int descriptionColumn
    )
    {
        ExcelUtility excelUtility = new ExcelUtility();

        List<String> ignorePatterns = excelFormatStore.getIgnorePatterns();
        ExcelFormat excelLinesFormat = new ExcelFormat(skipLines, dateColumn, descriptionColumn, amountColumn, -1, -1, -10000, ignorePatterns);
        List<ExcelLine> list = getExcelLines(filePath, excelUtility, sheetNumber, excelLinesFormat);
        List<LineDisplayable> existingLines = restUtility.getLines(getLineStorageUrl());
        List<LineWithSuggestedCategories> result = suggestor.getLineWithSuggestedCategories(list, existingLines);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/read"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<List<List<List<Object>>>> readExcel(@RequestParam("file") String filePath) throws IOException, InvalidFormatException {
        ExcelUtility excelUtility = new ExcelUtility();

        List<List<List<Object>>> result = new ArrayList<>();
        List<List<Object>> sheet = excelUtility.readExcel(getUploadsFolder() +filePath, 0, 0);
        result.add(sheet);

        boolean crush = false;
        int sheetIndex = 1;
        while(!crush){
            try{
                sheet = excelUtility.readExcel(getUploadsFolder() + filePath, sheetIndex, 0);
                result.add(sheet);
                sheetIndex++;
            } catch (Exception ex){
                crush = true;
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<List<LineDisplayable>> postExcelLines(@RequestBody List<ExcelLine> lines)
    {
            List<LineDisplayable> insertionLogs = new ArrayList<>();
            lines.forEach(l -> {
                LineForCreation line = new LineForCreation();
                line.setAccount(l.getAccount());
                line.setAmount(l.getAmount());
                line.setCategoryId(l.getCategoryId().toString());
                line.setDate(l.getDate());
                line.setDescription(l.getDescription());

                try {
                    LineDisplayable saved = restUtility.postLine(getLineStorageUrl(), line);
                    insertionLogs.add(saved);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            });

            return new ResponseEntity<>(insertionLogs, HttpStatus.OK);
    }

    public List<ExcelLine> getExcelLines(String pathname, ExcelUtility excelUtility, int sheetNumber, ExcelFormat format){
        try {
            List<List<Object>> content =
                    excelUtility.readExcel(getUploadsFolder() + pathname, sheetNumber, format.getHeaderlineIndex());
            content.removeIf(l -> l.get(format.getAmountIndex()) == "");
            if(format.getThreshold() > 0) {
                content.removeIf(l -> (double) l.get(format.getAmountIndex()) < format.getThreshold());
            }
            content.removeIf(l -> format.getIgnorePattern().stream().anyMatch(p ->
                    ((String)l.get(format.getDescriptionIndex())).indexOf(p) != -1));

            List<ExcelLine> excelLines = content.stream().map(l -> new ExcelLine(
                    l.get(format.getDescriptionIndex()).toString(),
                    (double) l.get(format.getAmountIndex()),
                    getParsedDate(l.get(format.getDateIndex())),
                    format.getAccountIndex() >= 0 ? l.get(format.getAccountIndex()).toString() : "",
                    format.getCategoryIdIndex() >= 0 ? l.get(format.getCategoryIdIndex()).toString() : ""
            )).collect(Collectors.toList());

            excelLines.sort(Comparator.comparing(ExcelLine::getDescription));
            return excelLines;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Date getParsedDate(Object cellValue){
        try {
            String value = cellValue.toString().replace("/", "-");
            return new SimpleDateFormat("dd-MM-yyyy").parse(value);
        }catch (Exception e){
             return DateUtil.getJavaDate((double) cellValue);
        }
    }

    private String getStorageUrl() {
        String storageServiceUrl = environmentVariablesWrapper.getValue(ENV_VAR_STORAGE_SERVICE_URL);
        if (StringUtils.isEmpty(storageServiceUrl)) {
            storageServiceUrl = DEFAULT_STORAGE_SERVICE_URL;
        }
        return storageServiceUrl;
    }

    private String getUploadsFolder() {
        String uploadsFolder = environmentVariablesWrapper.getValue(ENV_VAR_UPLOADS_FOLDER);
        if (StringUtils.isEmpty(uploadsFolder)) {
            uploadsFolder = DEFAULT_UPLOADS_FOLDER;
        }
        return uploadsFolder;
    }
}