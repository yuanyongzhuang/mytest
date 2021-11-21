package com.example.testcloudprovider.utils.newexcel;

import com.example.testcloudprovider.utils.DateTimeUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * excel大文件处理工具类
 * @author yyz
 * @date 2021/11/2122:34
 */
public class ExcelBigFileUtil {

    public static SXSSFWorkbook writeBigExcel(List<Map<String, String>> headerMapList, List<List<Map<String, Object>>> dataList, List<String> sheetNameList){
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(true);
        for (int i = 0; i < sheetNameList.size(); i++) {
            Sheet sheet = workbook.createSheet(sheetNameList.get(i));
            Row row;
            Cell cell;
            Map<String, String> headerMap = headerMapList.get(i);
            row = sheet.createRow(0);
            int counter = 0;
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                cell = row.createCell(counter);
                cell.setCellValue(entry.getKey());
                counter++;
            }
            List<Map<String, Object>> list = dataList.get(i);
            for (int j = 0; j < list.size(); j++) {
                row = sheet.createRow(j + 1);

                Map<String, Object> map = list.get(j);
                int counter2 = 0;
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    cell = row.createCell(counter2);
                    Object obj = map.get(entry.getValue());
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
            } else if (obj instanceof LocalDateTime) {
                cell.setCellValue(DateTimeUtil.getDateTimeStr((LocalDateTime) obj, "yyyy-MM-dd HH:mm:ss"));
            } else {//Boolean
                cell.setCellValue((boolean) obj ? "是" : "否");
            }
        } else {
            cell.setCellValue("");
        }
    }
}
