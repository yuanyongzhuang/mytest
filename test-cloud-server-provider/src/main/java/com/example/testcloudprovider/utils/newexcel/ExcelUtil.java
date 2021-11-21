package com.example.testcloudprovider.utils.newexcel;

import com.example.testcloudprovider.utils.DateTimeUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ExcelUtil {

    public static Workbook writeExcel(List<Map<String, String>> headerMapList, List<List<Map<String, Object>>> dataList, List<String> sheetNameList, String fileType) throws IOException {
            Workbook workbook;
        if (fileType.equalsIgnoreCase("xlsx")) {
            workbook = new XSSFWorkbook();
        } else {// xls
            workbook = new HSSFWorkbook();
        }

        for (int i = 0; i < sheetNameList.size(); i++) {
            Sheet sheet = workbook.createSheet(sheetNameList.get(i));
            Row row;
            Cell cell;
            Map<String, String> headerMap = headerMapList.get(i);
//            if (!EmptyUtil.isEmpty(headerMap)){
            row = sheet.createRow(0);
            int counter = 0;
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                cell = row.createCell(counter);
                cell.setCellValue(entry.getKey());
                counter++;
            }
//            }

            List<Map<String, Object>> list = dataList.get(i);
            for (int j = 0; j < list.size(); j++) {
//                if (!EmptyUtil.isEmpty(headerMap)){
                row = sheet.createRow(j + 1);
//                }else {
//                    row = sheet.createRow(j);
//                }
                Map<String, Object> map = list.get(j);
                int counter2 = 0;
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    cell = row.createCell(counter2);
                    Object obj = map.get(entry.getValue());
//                    cell.setCellValue(obj);
                    setCellValue(cell, obj);
                    counter2++;
                }
            }
        }
        return workbook;
    }

    private static void setCellValue(Cell cell, Object obj) {
        if (obj != null) {
            if (obj instanceof String) {
                cell.setCellValue((String) obj);
            } else if (obj instanceof Number) {
                if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                } else if (obj instanceof Long) {
                    cell.setCellValue((Long) obj);
                } else if (obj instanceof Float) {
                    cell.setCellValue((Float) obj);
                } else if (obj instanceof Double) {
                    cell.setCellValue((Double) obj);
                } else if (obj instanceof BigDecimal) {
                    cell.setCellValue(((BigDecimal) obj).doubleValue());
                }
            } else if (obj instanceof Date) {
                cell.setCellValue(DateTimeUtil.fmtDateToStr((Date) obj, "yyyy-MM-dd HH:mm:ss"));
            } else {//Boolean
                cell.setCellValue((boolean) obj ? "是" : "否");
            }
        } else {
            cell.setCellValue("");
        }
    }
}
