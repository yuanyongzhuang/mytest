package com.example.testcloudprovider1.utils.Excel;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import lombok.Builder;

import java.util.List;

/**
 * @author WendyYang
 * @date 10:09 2020/10/13
 * @desc 导出sheet参数
 */
@Builder
public class ExportParam {
    /**
     * 实体类型
     */
    private Class<?> type;
    /**
     * sheet名称
     */
    private String sheetName;
    private final ExportParams exportParam;
    /**
     * 数据集合
     */
    private List<?> dataList;

    public ExportParams getExportParam() {
        return new ExportParams(null, sheetName, ExcelType.XSSF);
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<?> getDataList() {
        return dataList;
    }

    public void setDataList(List<?> dataList) {
        this.dataList = dataList;
    }
}