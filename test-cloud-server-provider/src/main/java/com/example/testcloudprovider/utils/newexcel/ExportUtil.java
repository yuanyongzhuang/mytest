package com.example.testcloudprovider.utils.newexcel;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ExportUtil {

    public static void exportExcel(String statName, String fileType, String fileName,
                                   Map<String, String> headerMap, List<Map<String, Object>> list, HttpServletResponse response) {
        try {
            fileName += "." + fileType;
            List<Map<String, String>> headerMapList = new ArrayList<>();
            headerMapList.add(headerMap);

            List<List<Map<String, Object>>> dataList = new ArrayList<>();
            dataList.add(list);

            List<String> sheetNameList = new ArrayList<>();
            sheetNameList.add(statName);

            Workbook workbook = ExcelUtil.writeExcel(headerMapList, dataList, sheetNameList, fileType);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
