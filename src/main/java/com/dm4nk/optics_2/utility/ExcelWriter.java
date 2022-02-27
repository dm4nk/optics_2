package com.dm4nk.optics_2.utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {
    public static final int n = 200;

    public static void write(String filename, List<Double> x, List<Double> y, List<List<Double>> z) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(filename);

        XSSFRow row;
        Cell cell;

        int firstNum = 0;

        for(int i = firstNum; i < z.size(); ++i){
            row = sheet.createRow(i);

            cell = row.createCell(0);
            cell.setCellValue(x.get(i));

            cell = row.createCell(1);
            cell.setCellValue(y.get(i));

            for(int j = 0; j < z.size(); ++j){
                cell = row.createCell(3 + j);
                cell.setCellValue(z.get(i).get(j));
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream("src/main/resources/" + filename +".xlsx")) {
            workbook.write(outputStream);
        }
    }
}
