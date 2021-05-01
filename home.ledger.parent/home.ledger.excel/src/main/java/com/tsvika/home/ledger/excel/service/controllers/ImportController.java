package com.tsvika.home.ledger.excel.service.controllers;
//
//import com.tsvika.home.ledger.excel.service.pojos.ExcelCategory;
//import com.tsvika.home.ledger.excel.service.pojos.ExcelFormat;
//import com.tsvika.home.ledger.excel.service.pojos.ExcelLine;
//import com.tsvika.home.ledger.excel.service.utilities.ExcelUtility;
//import com.tsvika.home.ledger.excel.service.utilities.IRestUtility;
//import com.tsvika.home.ledger.dto.CategoryDisplayable;
//import com.tsvika.home.ledger.dto.CategoryForCreation;
//import com.tsvika.home.ledger.dto.LineDisplayable;
//import com.tsvika.home.ledger.dto.LineForCreation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/import")
public class ImportController {
//    @Autowired
//    private IRestUtility restUtility;
//
    public static final String STORAGE_SERVICE_URL = "http://localhost:9999/";
    public static final String STORAGE_SERVICE_CATEGORY_URL = STORAGE_SERVICE_URL + "category";
    public static final String STORAGE_SERVICE_LINE_URL = STORAGE_SERVICE_URL + "line";
//
//
//    @RequestMapping(
//            value = {"/import/categories"},
//            method = {RequestMethod.GET},
//            produces = {"application/json"}
//    )
//    public ResponseEntity<List<CategoryDisplayable>> importCategories()
//    {
//        String pathname = "/Users/twiegner/Downloads/a2.xlsx";
//        ExcelUtility excelUtility = new ExcelUtility();
//
//        HashMap<Double, CategoryDisplayable> map = extractCategories(pathname, excelUtility);
//
//        return new ResponseEntity<>(new ArrayList<>(map.values()), HttpStatus.OK);
//    }
//
//    @RequestMapping(
//            value = {"/import/lines"},
//            method = {RequestMethod.GET},
//            produces = {"application/json"}
//    )
//    public ResponseEntity<List<LineDisplayable>> importLines(){
//        List<CategoryDisplayable> storedCategories = restUtility.getCategories(STORAGE_SERVICE_CATEGORY_URL);
//
//        String linesPathname = "/Users/twiegner/Downloads/a3.xlsx";
//        String categoriesPathname = "/Users/twiegner/Downloads/a2.xlsx";
//        ExcelUtility excelUtility = new ExcelUtility();
//
//        List<ExcelCategory> excelCategories = getExcelcategories(categoriesPathname, excelUtility);
//        List<ExcelLine> linesList = ExcelController.getExcelLines(linesPathname, excelUtility, getExcelLinesDBFormat());
//
//        List<LineDisplayable> linesPosted = extractLines(storedCategories, excelCategories, linesList);
//
//        return new ResponseEntity<>(linesPosted, HttpStatus.OK);
//    }
//
//    public HashMap<Double, CategoryDisplayable> extractCategories(String pathname, ExcelUtility excelUtility) {
//        List<ExcelCategory> list = getExcelcategories(pathname, excelUtility);
//        HashMap<Double, CategoryDisplayable> map = new HashMap<>();
//
//        list.forEach(c ->
//                {
//                    Double excelCategoryId = c.getId();
//                    Double excelCategoryParentId = c.getParentId();
//                    CategoryForCreation categoryForCreation = new CategoryForCreation() {{
//                        setDescription(c.getName());
//                        setName(c.getName());
//                        if(null != excelCategoryParentId){
//                            setParentId(map.get(excelCategoryParentId).getId());
//                        } else {
//                            setParentId("c0c40c62-1bc9-4060-89f6-3c15d9fb448b"); //temp category
//                        }
//                    }};
//
//                    CategoryDisplayable categoryDisplayable =
//                            restUtility.postCategory(STORAGE_SERVICE_CATEGORY_URL, categoryForCreation);
//
//                    map.putIfAbsent(excelCategoryId, categoryDisplayable);
//                }
//        );
//        return map;
//    }
//
//    public List<LineDisplayable> extractLines(List<CategoryDisplayable> categories, List<ExcelCategory> categoriesList, List<ExcelLine> linesList) {
//        Map<String, CategoryDisplayable> categoryMap = new HashMap<>();
//        List<LineForCreation> lines = linesList.stream().map(l -> {
//
//            CategoryDisplayable categoryDisplayable;
//            if(null != categoryMap.getOrDefault(l.getCategoryId(), null)){
//                categoryDisplayable = categoryMap.get(l.getCategoryId());
//            } else {
//                ExcelCategory excelCategory = categoriesList.stream().filter(c -> c.getId().equals(l.getCategoryId()))
//                        .findFirst().get();
//                categoryDisplayable = categories.stream().filter(c -> c.getName().equals(excelCategory.getName()))
//                        .findFirst().get();
//                categoryMap.put(l.getCategoryId(), categoryDisplayable);
//            }
//
//            LineForCreation line = new LineForCreation();
//            line.setAccount(l.getAccount());
//            line.setAmount(l.getAmount());
//            line.setCategoryId(categoryDisplayable.getId());
//            line.setDate(l.getDate());
//            line.setDescription(l.getDescription());
//            return line;
//
//        }).collect(Collectors.toList());
//
//        List<LineDisplayable> linesPosted = new ArrayList<>();
//
//        lines.forEach(l -> {
//            try {
//                LineDisplayable lineDisplayable = restUtility.postLine(STORAGE_SERVICE_LINE_URL, l);
//                linesPosted.add(lineDisplayable);
//            }
//            catch (Exception e){
//                System.out.println(e.getMessage());
//            }
//        });
//        return linesPosted;
//    }
//
//    private List<ExcelCategory> getExcelcategories(String pathname, ExcelUtility excelUtility){
//        try {
//            List<List<Object>> content = excelUtility.readExcel(pathname, 1);
//
//            List<ExcelCategory> excelCategories = content.stream().map(l -> new ExcelCategory(
//                    (Double) l.get(0),
//                    l.get(1).getClass().equals(Double.class) ? (Double) l.get(1) : null,
//                    (String) l.get(2)
//            )).collect(Collectors.toList());
//
//            return excelCategories;
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            return null;
//        }
//    }
//
//    private ExcelFormat getExcelLinesDBFormat() {
//        List<String> ignorePatterns = new ArrayList<>();
//
//        return new ExcelFormat(1, 1, 2,3, 0,
//                4, -1000000000,
//                ignorePatterns);
//    }
}
