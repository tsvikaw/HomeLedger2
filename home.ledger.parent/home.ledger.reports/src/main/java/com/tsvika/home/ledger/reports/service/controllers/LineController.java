package com.tsvika.home.ledger.reports.service.controllers;

import com.tsvika.home.ledger.common.IEnvironmentVariablesWrapper;
import com.tsvika.home.ledger.dto.*;
import com.tsvika.home.ledger.reports.service.utilities.IRestUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.tsvika.home.ledger.common.Constants.DEFAULT_STORAGE_SERVICE_URL;
import static com.tsvika.home.ledger.common.Constants.ENV_VAR_STORAGE_SERVICE_URL;

@RestController
@RequestMapping("/lines")
public class LineController {
    @Autowired
    private IRestUtility restUtility;

    @Autowired
    private IEnvironmentVariablesWrapper environmentVariablesWrapper;

    @Autowired
    private CategoryController categoryController;

    private String getLineStorageUrl(){
        return getStorageUrl() + "line";
    }

    private String getLineQueryStorageUrl(){
        return getLineStorageUrl() + "/query";
    }

    private String getLineAccountStorageUrl(){
        return getLineStorageUrl() + "/accounts";
    }

    @RequestMapping(
            value = "/query",
            method = {RequestMethod.POST})
    ResponseEntity<LineDisplayableQueryResult> postByquery(@Valid @RequestBody LineQuery lineQuery) {
        assignLineQueryWithDecendentCategories(lineQuery,Arrays.asList(lineQuery.getCategories()));
        LineDisplayableQueryResult linesByQuery = restUtility.getLinesByQuery(getLineQueryStorageUrl(),
                lineQuery);
        return new ResponseEntity<>(linesByQuery, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/accounts"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<List<String>> getCategories() {
        List<String> result = restUtility.getAccounts(getLineAccountStorageUrl());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/summaryByPeriod",
            method = {RequestMethod.POST})
    ResponseEntity<String> postSummaryByPeriod(@Valid @RequestBody LineQuery lineQuery,
                                                       @RequestParam("month") int monthsPeriod ) {
        if(lineQuery.getCategories().length == 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        List<Series> seriesList = new ArrayList<>();
        List<CategoryDisplayable> cloneQueryCategories = new ArrayList<>(Arrays.asList(lineQuery.getCategories()));
        for (CategoryDisplayable category: cloneQueryCategories) {
            Series series = calculateCategorySeries(category, lineQuery, monthsPeriod);
            seriesList.add(series);
        }

        return new ResponseEntity<>(createSeriesForGraph(seriesList), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/pie",
            method = {RequestMethod.POST})
    ResponseEntity<List<CategoryPieSlice>> postPie(@Valid @RequestBody LineQuery lineQuery) {

        if(lineQuery.getCategories().length == 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        CategoryDisplayable firstCategory = Arrays.stream(lineQuery.getCategories()).findFirst().get();

        List<CategoryDisplayable> childCategories = new ArrayList<>
                (restUtility.getChildrenCategories(categoryController.getCategoryStorageUrl(), firstCategory.getId()));

        List<CategoryPieSlice> result = new ArrayList<>();
        List<LineDisplayable> directLinesByQuery = restUtility.getLinesByQuery(getLineQueryStorageUrl(), lineQuery).getItems();

        for (CategoryDisplayable category:
             childCategories) {
            assignLineQueryWithDecendentCategories(lineQuery, Arrays.asList(category));

            List<LineDisplayable> linesByQuery = restUtility.getLinesByQuery(getLineQueryStorageUrl(), lineQuery).getItems();

            CategoryPieSlice categoryResult =
                    new CategoryPieSlice(category.getId(), category.getName(), linesByQuery.stream()
                            .collect(Collectors.summingDouble(l -> l.getAmount())));

            if(categoryResult.getAmount() > 0) {
                result.add(categoryResult);
            }
        }

        if(directLinesByQuery.size() > 0) {
            result.add(new CategoryPieSlice(lineQuery.getCategories()[0].getId(), "other", directLinesByQuery.stream()
                    .collect(Collectors.summingDouble(l -> l.getAmount()))));
        }

        return new ResponseEntity<>(result.stream().sorted(new Comparator<CategoryPieSlice>() {
            @Override
            public int compare(CategoryPieSlice o1, CategoryPieSlice o2) {
                return o1.getAmount() - o2.getAmount() < 0 ? 1 : -1;
            }
        }).collect(Collectors.toList()), HttpStatus.OK);
    }

    private String createSeriesForGraph(List<Series> seriesList){

        List<Pair<String,List<Pair<String, Double>>>> result = new ArrayList<>();

        for (Series series: seriesList){
            for(Map.Entry<String, Double> entry : series.getData().entrySet()){
                if(!result.stream().anyMatch(a -> a.getKey().equals(entry.getKey()))){
                    result.add(new Pair<>(entry.getKey(), new ArrayList<>()));
                }
                List<Pair<String, Double>> dateGroup =
                        result.stream().filter(a -> a.getKey().equals(entry.getKey())).findFirst().get().getValue();

                dateGroup.add(new Pair<>(series.getId(), entry.getValue()));
            }
        }

        // temp manual conversion to object to json because of temporary failure of mvn do import json lib
        result = result.stream().sorted(((o1, o2) -> o1.getKey().compareTo(o2.getKey()))).collect(Collectors.toList());
        String json =  result.stream()
                .map(r -> "{ \"name\": \"" +r.getKey() + "\", " +r.getValue().stream()
                        .map(s-> "\""+ s.getKey()+ "\": \"" + s.getValue() + "\"").collect(Collectors.joining(",")) + " }")
                .collect(Collectors.joining(",")) ;
        return "[" + json +"]";
    }

    private Series calculateCategorySeries(CategoryDisplayable category, LineQuery lineQuery, int monthsPeriod) {
        assignLineQueryWithDecendentCategories(lineQuery, Arrays.asList(category));

        List<LineDisplayable> linesByQuery = restUtility.getLinesByQuery(getLineQueryStorageUrl(), lineQuery).getItems();

        linesByQuery.sort(Comparator.comparing(LineDisplayable::getDate));

        Map<String, List<Map.Entry<LineDisplayable, String>>> groupByTimePeriods =
                linesByQuery.stream().collect(Collectors.toMap(l -> l, l -> getPeriodLabel(monthsPeriod, l)))
                        .entrySet().stream().collect(Collectors.groupingBy(e -> e.getValue()));

        Map<String,Double> sums = groupByTimePeriods.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(),
                e -> e.getValue().stream().collect(Collectors.summingDouble(a -> a.getKey().getAmount()))));

        return new Series(category.getId(), category.getName(), sums);
    }

    private void assignLineQueryWithDecendentCategories(@RequestBody @Valid LineQuery lineQuery,
                                                        List<CategoryDisplayable> categories) {
        List<CategoryDisplayable> queryCategories = new ArrayList<>();
        categories.forEach(category -> {
            queryCategories.add(category);
            restUtility.getDecendentCategories(categoryController.getCategoryStorageUrl(), category.getId())
                    .forEach(c -> {
                        if (!queryCategories.stream().anyMatch(qc -> qc.getId().equals(c.getId()))) {
                            queryCategories.add(c);
                        }
                    });
        });
        lineQuery.setCategories(listToArray(queryCategories));
    }

    private CategoryDisplayable[] listToArray(List<CategoryDisplayable> queryCategories) {
        return queryCategories.toArray(new CategoryDisplayable[queryCategories.size()]);
    }

    private String getPeriodLabel(@RequestParam("month") int monthsPeriod, LineDisplayable l) {
        return String.format("%s-%s", new SimpleDateFormat("yyyy").format(l.getDate()),
                Math.floorDiv(Integer.parseInt(new SimpleDateFormat("MM").format(l.getDate())) - 1,  monthsPeriod)
                                + 1);
    }

    private String getStorageUrl() {
        String storageServiceUrl = environmentVariablesWrapper.getValue(ENV_VAR_STORAGE_SERVICE_URL);
        if (StringUtils.isEmpty(storageServiceUrl)) {
            storageServiceUrl = DEFAULT_STORAGE_SERVICE_URL;
        }
        return storageServiceUrl;
    }
}
