package com.example.testcloudprovider.utils.SXSSFExcelUtils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.testcloudprovider.entity.ExcelHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletResponse;
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
     * @param clazz        实体类型
     * @param dataList     数据集合
     * @param sheetName     sheet名
     * @param destFilePath 文件路径
     */
    public static void exportBigExcel(Class clazz, List<?> dataList, String destFilePath, String sheetName) {
        if(!dataList.isEmpty()){
            if(dataList.get(0).getClass().equals(clazz)) {
                BigExcelWriter bigWriter = ExcelUtil.getBigWriter(destFilePath);
                //甚至sheet的名称
                bigWriter.renameSheet(sheetName);

                //设置Excel表头
                setTheHeader(clazz, bigWriter);

                bigWriter.write(dataList, true);
                // 设置所有列为自动宽度，不考虑合并单元格
                bigWriter.autoSizeColumnAll();
                //输出别名
                bigWriter.setOnlyAlias(true);
                bigWriter.close();
                log.info("导出完成!");
            } else {
                log.error("数据类型与传入的集合数据类型不一致！数据类型：{}; 集合数据类型：{}", clazz, dataList.get(0).getClass());
            }
        }else{
            log.error("数据集合为空，数据类型：{}", clazz);
        }
    }

    /**
     * 设置Excel表头
     * @param clazz 映射实体
     * @param bigWriter
     */
    private static void setTheHeader(Class clazz, BigExcelWriter bigWriter) {
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
                HashMap<String, String> fileNameMap = new HashMap<>();
                fileNameMap.put(field.getName(),excel.name());
                ExcelHeader header = new ExcelHeader().setSortNum(Integer.valueOf(excel.orderNum())).setHeaderMap(fileNameMap);
                headerList.add(header);
            }
        }
        System.out.println(headerList);
        //表头排序
        List<ExcelHeader> sortHeaders = headerList.stream().sorted(Comparator.comparing(ExcelHeader::getSortNum)).collect(Collectors.toList());
        System.out.println(sortHeaders);
        //设置表头
        for (ExcelHeader header : sortHeaders){
            if(header != null){
                for(Map.Entry<String, String> entry : header.getHeaderMap().entrySet()){
                    //设置head的名称， 此时的顺寻就是导出的顺序， key就是映射实体的属性名称， value就是别名
                    bigWriter.addHeaderAlias(entry.getKey(), entry.getValue());
                }
            }
        }
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
