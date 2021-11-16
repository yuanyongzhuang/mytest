package com.example.testcloudprovider.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: lee
 * @create: 2021-07-06 14:21
 **/
@Data
public class PaymentOrderExportDTO {

    @Excel(name = "父订单号", width = 20, needMerge = true, orderNum = "0")
    private Integer orderId;

    @Excel(name = "手机号", width = 20, needMerge = true, orderNum = "1")
    private String mobile;

    @Excel(name = "地区大区省", width = 20, needMerge = true,orderNum = "2")
    private String provinceName;

    @Excel(name = "城市", width = 20, needMerge = true,orderNum = "3")
    private String cityName;

    @Excel(name = "机会ID", width = 20, needMerge = true,orderNum = "4")
    private Integer opportunityId;

    @Excel(name = "机会创建时间",exportFormat="yyyy-MM-dd HH:mm:ss", needMerge = true, width = 20,orderNum = "5")
    private Date opportunityCreateDate;

    @ExcelCollection(name = "",orderNum = "6")
    private List<OrderRelationExportDTO> orderRelationExportDTOS;

    @Excel(name = "订单金额", width = 20, needMerge = true,orderNum = "15")
    private BigDecimal originalCash;

    @Excel(name = "优惠金额", width = 20, needMerge = true,orderNum = "16")
    private BigDecimal payDou;

    @Excel(name = "应付金额", width = 20, needMerge = true,orderNum = "17")
    private BigDecimal cash;

    @Excel(name = "已付金额", width = 20, needMerge = true,orderNum = "18")
    private BigDecimal payCash;

    @Excel(name = "下单部门", width = 20, needMerge = true,orderNum = "19")
    private String shiYeBuId;

    @Excel(name = "下单人", width = 20, needMerge = true,orderNum = "20")
    private String createDengLuZhangHao;

    @Excel(name = "支付时间", width = 20, needMerge = true,orderNum = "21")
    private String openDate;

    @Excel(name = "订单状态", width = 20, needMerge = true,orderNum = "22")
    private String orderState;

    @Excel(name = "业绩坐席", width = 20, needMerge = true,orderNum = "23")
    private String performanceTable;


    @Excel(name = "业绩坐席类别", width = 20, needMerge = true,orderNum = "24")
    private String performanceTableType;

    @Excel(name = "业绩坐席事业部", width = 20, needMerge = true,orderNum = "25")
    private String performanceTableShiYeBu;

    @Excel(name = "业绩分摊部门", width = 20, needMerge = true,orderNum = "26")
    private String performanceTableDept;


    @Excel(name = "业绩坐席主管", width = 20, needMerge = true,orderNum = "27")
    private String performanceTableMaster;


    @Excel(name = "机会当前坐席", width = 20, needMerge = true,orderNum = "28")
    private String currentTable;

    @Excel(name = "机会当前坐席类型", width = 20, needMerge = true,orderNum = "29")
    private String currentTableType;

    @Excel(name = "机会当前坐席主管", width = 20, needMerge = true,orderNum = "30")
    private String currentTableMaster;


    @Excel(name = "机会库url", width = 20, needMerge = true,orderNum = "31")
    private String opportunityUrl;


    @Excel(name = "感兴趣课程", width = 20, needMerge = true,orderNum = "32")
    private String likeClassName;

    @Excel(name = "首次成交坐席", width = 20, needMerge = true,orderNum = "33")
    private String firstOrderPersonName;

    @Excel(name = "是否是二次升班", width = 20, needMerge = true,orderNum = "34")
    private String isSecondPromotionClass;

}
