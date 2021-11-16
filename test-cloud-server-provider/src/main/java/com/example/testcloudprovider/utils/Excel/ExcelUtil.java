package com.example.testcloudprovider.utils.Excel;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.example.testcloudprovider.utils.DateTimeUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Created by czh on 2020/7/23 09:11
 */
public class ExcelUtil {

    public static String FILE_PATH_PREFIX = "/ZhongYeDisk/excel/upload/excelUpload/";

    /**
     * 导出excel
     *
     * @param list           数据
     * @param title          表头
     * @param sheetName      sheetName
     * @param pojoClass      解析的对象类型
     * @param fileName       文件名称
     * @param isCreateHeader 是否创建表头
     * @return 文件路径
     */
    public static String exportExcelToFile(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader) {
        OutputStream out = null;
        Workbook workbook = null;
        //不启用在表格里面设置一个title
        title = null;
        try {
            ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
            exportParams.setCreateHeadRows(isCreateHeader);
            fileName = encodingFilename(fileName);
            exportParams.setStyle(ExcelStyleUtil.class);
            out = new FileOutputStream(getAbsoluteFile(fileName));

            workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
            workbook.write(out);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * excel 导出到文件
     *
     * @param list      数据
     * @param title     表头
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName  文件名
     * @return 文件路径
     */
    public static String exportExcelToFile(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName) {
        return exportExcelToFile(list, title, sheetName, pojoClass, fileName, true);
    }

    /**
     * excel 导出到文件
     *
     * @param list      数据
     * @param title     表头
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @return 文件路径
     */
    public static String exportExcelToFile(List<?> list, String title, String sheetName, Class<?> pojoClass) {
        return exportExcelToFile(list, title, sheetName, pojoClass, title, true);
    }

    /**
     * excel 导出到文件
     *
     * @param list      数据
     * @param fileName  文件名
     * @param pojoClass pojo类型
     * @return 文件路径
     */
    public static String exportExcelToFile(List<?> list, String fileName, Class<?> pojoClass) {
        return exportExcelToFile(list, fileName, fileName, pojoClass, fileName, true);
    }

    /**
     * excel 导出
     *
     * @param list           数据
     * @param title          标题
     * @param sheetName      sheet名称
     * @param pojoClass      pojo类型
     * @param fileName       文件名称
     * @param isCreateHeader 是否创建表头
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletResponse response) throws IOException {

        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setStyle(ExcelStyleUtil.class);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * excel 导出
     *
     * @param list      数据
     * @param title     标题
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName  文件名称
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName, ExcelType.XSSF));
    }

    /**
     * excel 导出
     *
     * @param list         数据
     * @param pojoClass    pojo类型
     * @param fileName     文件名称
     * @param response
     * @param exportParams 导出参数
     */
    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, ExportParams exportParams, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * excel 导出
     *
     * @param list     数据
     * @param fileName 文件名称
     * @param response
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, fileName, response);
    }

    /**
     * 默认的 excel 导出
     *
     * @param list         数据
     * @param pojoClass    pojo类型
     * @param fileName     文件名称
     * @param response
     * @param exportParams 导出参数
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 默认的 excel 导出
     *
     * @param list     数据
     * @param fileName 文件名称
     * @param response
     */
    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 下载
     *
     * @param fileName 文件名称
     * @param response
     * @param workbook excel数据
     */
    public static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + "." + ExcelTypeEnum.XLSX.getValue(), "UTF-8"));
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * excel 导入
     *
     * @param file      excel文件
     * @param pojoClass pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass) throws IOException {
        return importExcel(file, 1, 1, pojoClass);
    }

    /**
     * excel 导入
     *
     * @param file       excel文件
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        return importExcel(file, titleRows, headerRows, false, pojoClass);
    }

    /**
     * excel 导入
     *
     * @param file       上传的文件
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param needVerify 是否检验excel内容
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, boolean needVerify, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            return importExcel(file.getInputStream(), titleRows, headerRows, needVerify, pojoClass);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param inputStream 文件输入流
     * @param titleRows   标题行
     * @param headerRows  表头行
     * @param needVerify  是否检验excel内容
     * @param pojoClass   pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, Integer titleRows, Integer headerRows, boolean needVerify, Class<T> pojoClass) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setSaveUrl(FILE_PATH_PREFIX);
        params.setNeedSave(true);
        params.setNeedVerify(needVerify);
        try {
            return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("excel文件不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }


    /**
     * 获取下载路径
     *
     * @param downloadPath 文件名称
     */
    private static String getAbsoluteFile(String downloadPath) {
        downloadPath = FILE_PATH_PREFIX + downloadPath;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 编码文件名
     */
    private static String encodingFilename(String filename) {
        filename = DateTimeUtil.getTodayChar14() + "_" + filename + "." + ExcelTypeEnum.XLSX.getValue();
        return filename;
    }

    /**
     * Excel 类型枚举
     */
    enum ExcelTypeEnum {

        XLS("xls"),

        XLSX("xlsx");

        private final String value;

        ExcelTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 多sheet导出
     *
     * @param exportParamList 导出sheet数据集合
     * @return org.apache.poi.ss.usermodel.Workbook
     * @author WendyYang
     **/
    public static Workbook getWorkBook(List<ExportParam> exportParamList) {
        List<Map<String, Object>> mapList = exportParamList.stream()
                .map(w -> Map.of(NormalExcelConstants.CLASS, w.getType(),
                        NormalExcelConstants.DATA_LIST, w.getDataList(),
                        "title", w.getExportParam()))
                .collect(Collectors.toList());
        return ExcelExportUtil.exportExcel(mapList, ExcelType.XSSF);
    }
}