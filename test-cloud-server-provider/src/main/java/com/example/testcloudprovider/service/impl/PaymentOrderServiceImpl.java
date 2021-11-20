package com.example.testcloudprovider.service.impl;

import com.example.testcloudprovider.entity.PaymentOrder;
import com.example.testcloudprovider.mapper.PaymentOrderMapper;
import com.example.testcloudprovider.service.IPaymentOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单导出测试表 服务实现类
 * </p>
 *
 * @author yyz
 * @since 2021-11-20
 */
@Service
public class PaymentOrderServiceImpl extends ServiceImpl<PaymentOrderMapper, PaymentOrder> implements IPaymentOrderService {

    @Override
    public void addList() {
        for(int j = 0; j <10; j++){
            System.out.println("j=="+j);
            for(int i = 0; i < 200; i++){
                System.out.println("i=="+i);
                List<PaymentOrder> orderList = new ArrayList<>();
                PaymentOrder order = new PaymentOrder();
                order.setMobile("12345678910");
                order.setProvinceName("北京");
                order.setCityName("通州");
                order.setOpportunityId(10+j+i);
                order.setOpportunityCreateDate(LocalDateTime.now());
                order.setOriginalCash(BigDecimal.valueOf(125.0D+i));
                order.setPauDou(BigDecimal.valueOf(1.0D+i));
                order.setCash(BigDecimal.valueOf(124.0D+i));
                order.setPayCash(BigDecimal.valueOf(100.0D+i));
                order.setShiYeBuId("1458"+j+i);
                order.setCreateDengLuZhangHao("袁永庄_42679");
                order.setOpenDate("2021-11-16");
                order.setOrderState("已支付");
                order.setPerformanceTbale("袁永庄");
                order.setPerformanceTableType("业务");
                order.setPerformanceTableShiYeBu("建筑");
                order.setPerformanceTableDept("技术");
                order.setPerformanceTableMaster("上帝");
                order.setOpportunityUrl("xxx/xxx/xx/xx/xx/x");
                order.setLikeClassName("建筑基建");
                order.setFirstOrderPersonName("如来");
                order.setIsSecondPromotionClass("否");
                order.setTableId(20+j+i);
                order.setPackageId(30+j+i);
                order.setProductName("商品" + j + i);
                order.setDirectoryName("建筑类");
                order.setExamName("基建");
                order.setSubjectName("二建");
                order.setCourseTypeName("精选科");
                order.setResourceTypeName("商品");
                order.setProtocolState("未签署");
                orderList.add(order);
                this.saveBatch(orderList);
            }
        }
    }
}
