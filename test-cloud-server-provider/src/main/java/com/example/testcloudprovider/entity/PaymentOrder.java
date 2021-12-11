package com.example.testcloudprovider.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单导出测试表
 * </p>
 *
 * @author yyz
 * @since 2021-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PaymentOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 父订单id
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 地区大区省
     */
    private String provinceName;

    /**
     * 城市
     */
    private String cityName;

    /**
     * 机会id
     */
    private Integer opportunityId;

    /**
     * 机会创建时间
     */
    private LocalDateTime opportunityCreateDate;

    /**
     * 订单金额
     */
    private BigDecimal originalCash;

    /**
     * 优惠金额
     */
    private BigDecimal pauDou;

    /**
     * 应付金额
     */
    private BigDecimal cash;

    /**
     * 已付金额
     */
    private BigDecimal payCash;

    /**
     * 下单部门
     */
    private String shiYeBuId;

    /**
     * 下单人
     */
    private String createDengLuZhangHao;

    /**
     * 支付时间
     */
    private String openDate;

    /**
     * 订单状态
     */
    private String orderState;

    /**
     * 业绩坐席
     */
    private String performanceTbale;

    /**
     * 业绩坐席类别
     */
    private String performanceTableType;

    /**
     * 业绩坐席事业部
     */
    private String performanceTableShiYeBu;

    /**
     * 业绩分摊部门
     */
    private String performanceTableDept;

    /**
     * 业绩坐席主管
     */
    private String performanceTableMaster;

    /**
     * 机会库url
     */
    private String opportunityUrl;

    /**
     * 感兴趣课程
     */
    private String likeClassName;

    /**
     * 首次成交坐席
     */
    private String firstOrderPersonName;

    /**
     * 是否是二次升班
     */
    private String isSecondPromotionClass;

    //==========================================================================
    /**
     * 子订单id
     */
    private Integer tableId;

    /**
     * 商品id
     */
    private Integer packageId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 项目类
     */
    private String directoryName;

    /**
     * 项目
     */
    private String examName;

    /**
     * 科目
     */
    private String subjectName;

    /**
     * 课程类型
     */
    private String courseTypeName;

    /**
     * 产品类型
     */
    private String resourceTypeName;

    /**
     * 是否签署协议
     */
    private String protocolState;
    //================================================================================================


}
