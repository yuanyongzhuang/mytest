package com.example.testcloudprovider.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * Excel列名实体
 * @author yyz
 * @date 2021/11/2614:15
 */
@Data
@Accessors(chain = true)
public class ExcelHeader {

    /**
     * excelHeader顺序
     */
    Integer sortNum;

    /**
     * Map<属性名，别名>
     */
//    Map<String,String> headerMap;

    /**
     * excel header 名称
     */
    String headerName;

    /**
     * 字段参数名
     */
    String paramName;

    /**
     * 是否合并单元格 true合并 false不合并
     */
    Boolean needMerge;

    /**
     * 合并行号
     */
    Integer lineNum = 1;
}
