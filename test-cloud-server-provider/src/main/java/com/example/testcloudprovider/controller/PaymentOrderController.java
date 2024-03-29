package com.example.testcloudprovider.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.testcloudprovider.service.IPaymentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * <p>
 * 订单导出测试表 前端控制器
 * </p>
 *
 * @author yyz
 * @since 2021-11-20
 */
@RestController
@RequestMapping("/paymentOrder")
public class PaymentOrderController {

    @Autowired
    IPaymentOrderService paymentOrderService;

    @PostMapping({"/add"})
    public void add() {
        paymentOrderService.addList();
    }

    @PostMapping("/exportOrder")
    public void exportOrder(){
        paymentOrderService.exportOrder();
    }

    @PostMapping("/huToolExport")
    public void huToolExport(){
        paymentOrderService.huToolExport();
    }

    @PostMapping("/huToolNeedMergeExport")
    public void huToolNeedMergeExport(){
        paymentOrderService.huToolNeedMergeExport();
    }
    @PostMapping("/poiMergeExport")
    public void poiMergeExport(){
        paymentOrderService.poiMergeExport();
    }

}

