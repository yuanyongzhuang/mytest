package com.example.testcloudprovider.utils.SXSSFExcelUtils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.testcloudprovider.entity.ExcelHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * hutool实现excel文件导出
 * @author yyz
 * @date 2021/11/2511:03
 */
@Slf4j
public class HuToolExcelUtil {

    /**
     * 非合并单元格
     * @param clazz        实体类型
     * @param dataList     数据集合
     * @param sheetName     sheet名
     * @param destFilePath 文件路径
     */
    public static void exportBigExcel(Class clazz, List<?> dataList, String destFilePath, String sheetName) {
        exportNeedMergeBigExcel(clazz,dataList,null,destFilePath,sheetName);
    }

    /**
     * 合并单元格
     * @param clazz 映射实体
     * @param dataList
     * @param collect
     * @param destFilePath
     * @param sheetName
     */
    public static void exportNeedMergeBigExcel(Class clazz,  List<?> dataList, LinkedHashMap<Integer, Long> collect, String destFilePath, String sheetName) {
        try {
            if(!dataList.isEmpty()){
                if(dataList.get(0).getClass().equals(clazz)) {
                BigExcelWriter bigWriter = ExcelUtil.getBigWriter(destFilePath);
//                ExcelWriter bigWriter = ExcelUtil.getBigWriter(2000);
//                SXSSFSheet sheet = (SXSSFSheet) bigWriter.getSheet();
//                sheet.setRandomAccessWindowSize(-1);
                //甚至sheet的名称
                bigWriter.renameSheet(sheetName);

                //设置Excel表头
                List<ExcelHeader> mergeList = setTheMergeHeader(clazz, bigWriter);
                //设置合并行
                mergeRow(collect, bigWriter, mergeList);

                System.out.println("导出添加数据=="+dataList.size());

                bigWriter.write(dataList, true);
                // 设置所有列为自动宽度，不考虑合并单元格
//                bigWriter.autoSizeColumnAll();
                //输出别名
                bigWriter.setOnlyAlias(true);
//                OutputStream out = new FileOutputStream(destFilePath);
//                bigWriter.flush(out);
                bigWriter.close();
                log.info("导出完成!");
                } else {
                    log.error("数据类型与传入的集合数据类型不一致！数据类型：{}; 集合数据类型：{}", clazz, dataList.get(0).getClass());
                }
            }else{
                log.error("数据集合为空，数据类型：{}", clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置合并行
     * @param collect 合并参数
     * @param bigWriter 工具类
     * @param mergeList 合并表头
     */
    private static void mergeRow(LinkedHashMap<Integer, Long> collect, ExcelWriter bigWriter, List<ExcelHeader> mergeList) {
        System.out.println("设置Excel表头:返回合并的表头==" + mergeList);
        if(CollectionUtil.isNotEmpty(mergeList) && CollectionUtil.isNotEmpty(collect)) {
            for (Map.Entry<Integer, Long> listEntry : collect.entrySet()) {
                Long value = listEntry.getValue();
                //根据条数合并单元格
                if (value == 1) {
                    //一条数据不合并
                    for (ExcelHeader excelHeader : mergeList) {
                        excelHeader.setLineNum(Math.toIntExact(excelHeader.getLineNum() + value));
                    }
                } else {
                    System.out.println("合并列：合并单元列数==" + mergeList.size());
                    //合并
                    for (ExcelHeader excelHeader : mergeList) {
                        System.out.println("合并列：合并行数==" + value);
                        bigWriter.merge(excelHeader.getLineNum(),
                                Math.toIntExact(excelHeader.getLineNum() + value - 1),
                                excelHeader.getSortNum(), excelHeader.getSortNum(), null, true);
                        excelHeader.setLineNum(Math.toIntExact(excelHeader.getLineNum() + value));
                    }
                }
            }
        }
    }

    /**
     * 设置Excel表头
     * @param clazz 映射实体
     * @param bigWriter
     */
    private static List<ExcelHeader> setTheMergeHeader(Class clazz, ExcelWriter bigWriter) {
        //获取当前类字段
        Field[] fields = clazz.getDeclaredFields();
        //字段中文名称集合（获取注解@Excel的name和orderNum排序值）集合存储
        List<ExcelHeader> headerList = new ArrayList<>(fields.length);
        for(Field field: fields){
            //强制访问，越过安全检查
            field.setAccessible(true);

            //获取注解属性和字段名
            boolean annotationPresent = field.isAnnotationPresent(Excel.class);
            if(annotationPresent){
                Excel excel = field.getAnnotation(Excel.class);
                ExcelHeader header = new ExcelHeader()
                        .setSortNum(Integer.valueOf(excel.orderNum()))
                        .setHeaderName(excel.name())
                        .setParamName(field.getName())
                        .setNeedMerge(excel.needMerge());
                headerList.add(header);
            }
        }
        System.out.println("无序表头设置："+headerList);
        //表头排序
        List<ExcelHeader> sortHeaders = headerList.stream().sorted(Comparator.comparing(ExcelHeader::getSortNum)).collect(Collectors.toList());
        System.out.println("有序表头设置："+sortHeaders);

        List<ExcelHeader> mergeHeaders = new ArrayList<>();
        //组装表头
        for (ExcelHeader header : sortHeaders){
            if(header != null){
                if(header.getNeedMerge()){
                    mergeHeaders.add(header);
                }
                bigWriter.addHeaderAlias(header.getParamName(), header.getHeaderName());
            }
        }
        return mergeHeaders;
    }

    /**
     * 大文件导出实例
     *
     * @param response
     */
    public static void export(HttpServletResponse response) {
        try {
            ExcelWriter writer = ExcelUtil.getBigWriter();
            //甚至sheet的名称
            writer.renameSheet("所有数据");
            //设置head的名称， 此时的顺寻就是导出的顺序， key就是RecordInfoDetailsDTO的属性名称， value就是别名
            writer.addHeaderAlias("id", "数据ID");
            writer.addHeaderAlias("name", "名称");
            writer.addHeaderAlias("provinceName", "所属区域");
            writer.addHeaderAlias("groupId", "所属组ID");
            writer.addHeaderAlias("groupName", "所属组名称");
            writer.addHeaderAlias("status_name", "状态");
            writer.addHeaderAlias("approvalStatus_name", "审核状态");


            List<T> list = new ArrayList<>(); //查询出所有的需要导出的数据
            writer.write(list, true);
            writer.setOnlyAlias(true);
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //中文名称需要特殊处理
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("所有数据", "UTF-8") + ".xlsx");
            writer.flush(response.getOutputStream());

            writer.close();
        } catch (Exception e) {
            //如果导出异常，则生成一个空的文件
            log.info("######导出  excel异常  ：{}", e.getMessage());
        }
    }
}
