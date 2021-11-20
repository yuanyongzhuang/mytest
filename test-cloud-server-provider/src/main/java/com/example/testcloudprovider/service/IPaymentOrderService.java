package com.example.testcloudprovider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.testcloudprovider.entity.PaymentOrder;

/**
 * <p>
 * 订单导出测试表 服务类
 * </p>
 *
 * @author yyz
 * @since 2021-11-20
 */
public interface IPaymentOrderService extends IService<PaymentOrder> {

    void addList();
}
