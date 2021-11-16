package com.example.testcloudprovider.dto;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * @author: lee
 * @create: 2021-07-06 14:44
 **/
@Data
@ExcelTarget("PaymentOrderExportDTO")
public class OrderRelationExportDTO {

    @Excel(name = "子订单id", width = 20, needMerge = true,orderNum = "7")
    private Integer tableId;

    @Excel(name = "商品ID", width = 20,orderNum = "8")
    private Integer packageId;

    @Excel(name = "商品名称",  width = 20,orderNum = "9")
    private String productName;

    @Excel(name = "项目类", width = 20,orderNum = "10")
    private String directoryName;

    @Excel(name = "项目", width = 20,orderNum = "11")
    private String examName;

    @Excel(name = "科目", width = 20,orderNum = "12")
    private String subjectName;

    @Excel(name = "课程类型", width = 20,orderNum = "13")
    private String courseTypeName;

    @Excel(name = "产品类型",  width = 20,orderNum = "14")
    private String resourceTypeName;

    @Excel(name = "是否签署协议",  width = 20,orderNum = "15")
    private String protocolState;

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName==null?"--":subjectName;
    }

    public void setByOrderRelation() {
        this.packageId = 123456;
    }
}
