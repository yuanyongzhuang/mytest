package com.example.testcloudprovider.mapper;

import com.example.testcloudprovider.entity.PaymentOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单导出测试表 Mapper 接口
 * </p>
 *
 * @author yyz
 * @since 2021-11-20
 */
@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrder> {

}
