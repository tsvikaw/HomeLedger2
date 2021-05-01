package com.tsvika.home.ledger.excel.service.utilities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtility {
    public List<List<Object>> readExcel(String pathname, int sheetNumber, int skipLines) throws InvalidFormatException, IOException {
        List<List<Object>> content = new ArrayList<List<Object>>();

        Sheet sheet;
        FormulaEvaluator formulaEvaluator;

        try {
            OPCPackage pkg = OPCPackage.open(new File(pathname));
            //creating workbook instance that refers to .xls file
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            //creating a Sheet object to retrieve the object
             sheet = wb.getSheetAt(sheetNumber);
            //evaluating cell type
            formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
        } catch (NotOfficeXmlFileException e){
            //Get the workbook instance for XLS file
            FileInputStream file = new FileInputStream(new File(pathname));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            //Get first sheet from the workbook
             sheet = workbook.getSheetAt(sheetNumber);
            //evaluating cell type
            formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        }

        int index = 0;
        for (Row row : sheet)     //iteration over row using for each loop
        {
            if (index <= skipLines) {
                index++;
                continue;
            }

            List<Object> line = new ArrayList<Object>();
            content.add(line);
            for (Cell cell : row)    //iteration over cell using for each loop
            {
                switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:   //field that represents numeric cell type
                        //getting the value of the cell as a number
                        System.out.print(cell.getNumericCellValue() + "\t\t");
                        line.add(cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_STRING:    //field that represents string cell type
                        //getting the value of the cell as a string
                        System.out.print(cell.getStringCellValue() + "\t\t");
                        line.add(cell.getStringCellValue());
                        break;
                    default:
                        line.add("");
                }
            }
            System.out.println();
        }

        return content;
    }
}