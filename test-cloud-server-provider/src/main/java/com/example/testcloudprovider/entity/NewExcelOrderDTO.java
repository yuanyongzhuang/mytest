package com.example.testcloudprovider.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 新导出订单实体
 * @author yyz
 * @date 2021/12/1114:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NewExcelOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 父订单id
     */
    @Excel(name = "父订单号", width = 20, needMerge = true, orderNum = "0")
    private Integer orderId;

    /**
     * 手机号
     */
    @Excel(name = "手机号", width = 20, needMerge = true, orderNum = "1")
    private String mobile;

    /**
     * 地区大区省
     */
    @Excel(name = "地区大区省", width = 20, needMerge = true,orderNum = "2")
    private String provinceName;

    /**
     * 城市
     */
    @Excel(name = "城市", width = 20, needMerge = true,orderNum = "3")
    private String cityName;

    /**
     * 机会id
     */
    @Excel(name = "机会ID", width = 20, needMerge = true,orderNum = "4")
    private Integer opportunityId;

    /**
     * 机会创建时间
     */
    @Excel(name = "机会创建时间",exportFormat="yyyy-MM-dd HH:mm:ss", needMerge = true, width = 20,orderNum = "5")
    private String opportunityCreateDate;

    @ExcelCollection(name = "")
    private List<Order> orderRelationExportDTOS;

    /**
     * 订单金额
     */
    @Excel(name = "订单金额", width = 20, needMerge = true,orderNum = "16")
    private BigDecimal originalCash;

    /**
     * 优惠金额
     */
    @Excel(name = "优惠金额", width = 20, needMerge = true,orderNum = "17")
    private BigDecimal pauDou;

    /**
     * 应付金额
     */
    @Excel(name = "应付金额", width = 20, needMerge = true,orderNum = "18")
    private BigDecimal cash;

    /**
     * 已付金额
     */
    @Excel(name = "已付金额", width = 20, needMerge = true,orderNum = "19")
    private BigDecimal payCash;

    /**
     * 下单部门
     */
    @Excel(name = "下单部门", width = 20, needMerge = true,orderNum = "20")
    private String shiYeBuId;

    /**
     * 下单人
     */
    @Excel(name = "下单人", width = 20, needMerge = true,orderNum = "21")
    private String createDengLuZhangHao;

    /**
     * 支付时间
     */
    @Excel(name = "支付时间", width = 20, needMerge = true,orderNum = "22")
    private String openDate;

    /**
     * 订单状态
     */
    @Excel(name = "订单状态", width = 20, needMerge = true,orderNum = "23")
    private String orderState;

    /**
     * 业绩坐席
     */
    @Excel(name = "业绩坐席", width = 20, needMerge = true,orderNum = "24")
    private String performanceTbale;

    /**
     * 业绩坐席类别
     */
    @Excel(name = "业绩坐席类别", width = 20, needMerge = true,orderNum = "25")
    private String performanceTableType;

    /**
     * 业绩坐席事业部
     */
    @Excel(name = "业绩坐席事业部", width = 20, needMerge = true,orderNum = "26")
    private String performanceTableShiYeBu;

    /**
     * 业绩分摊部门
     */
    @Excel(name = "业绩分摊部门", width = 20, needMerge = true,orderNum = "27")
    private String performanceTableDept;

    /**
     * 业绩坐席主管
     */
    @Excel(name = "机会库url", width = 20, needMerge = true,orderNum = "28")
    private String performanceTableMaster;

    /**
     * 机会库url
     */
    @Excel(name = "机会当前坐席", width = 20, needMerge = true,orderNum = "29")
    private String opportunityUrl;

    /**
     * 感兴趣课程
     */
    @Excel(name = "感兴趣课程", width = 20, needMerge = true,orderNum = "30")
    private String likeClassName;

    /**
     * 首次成交坐席
     */
    @Excel(name = "首次成交坐席", width = 20, needMerge = true,orderNum = "31")
    private String firstOrderPersonName;

    /**
     * 是否是二次升班
     */
    @Excel(name = "是否是二次升班", width = 20, needMerge = true,orderNum = "6")
    private String isSecondPromotionClass;


}
