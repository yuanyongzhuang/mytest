package com.example.testcloudprovider.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 子订单
 * @author yyz
 * @date 2021/12/1114:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Order {
    /**
     * 子订单id
     */
    @Excel(name = "子订单id", width = 20, orderNum = "7")
    private Integer tableId =123;

    /**
     * 商品id
     */
    @Excel(name = "商品ID", width = 20,orderNum = "8")
    private Integer packageId =458;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称",  width = 20,orderNum = "9")
    private String productName = "合并单元格测试";

    /**
     * 项目类
     */
    @Excel(name = "项目类", width = 20,orderNum = "10")
    private String directoryName = "my order type";

    /**
     * 项目
     */
    @Excel(name = "项目", width = 20,orderNum = "11")
    private String examName = "人品升级课";

    /**
     * 科目
     */
    @Excel(name = "科目", width = 20,orderNum = "12")
    private String subjectName = "biuBiuBiu";

    /**
     * 课程类型
     */
    @Excel(name = "课程类型", width = 20,orderNum = "13")
    private String courseTypeName = "laLaLa";

    /**
     * 产品类型
     */
    @Excel(name = "产品类型",  width = 20,orderNum = "14")
    private String resourceTypeName = "人品";

    /**
     * 是否签署协议
     */
    @Excel(name = "是否签署协议",  width = 20,orderNum = "15")
    private String protocolState = "未签署";

}
