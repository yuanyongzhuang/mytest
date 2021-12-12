package com.example.testcloudprovider.service.impl;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.testcloudprovider.dto.OrderRelationExportDTO;
import com.example.testcloudprovider.dto.PaymentOrderExportDTO;
import com.example.testcloudprovider.entity.ExcelPaymentOrderDTO;
import com.example.testcloudprovider.entity.NewExcelOrderDTO;
import com.example.testcloudprovider.entity.Order;
import com.example.testcloudprovider.entity.PaymentOrder;
import com.example.testcloudprovider.mapper.PaymentOrderMapper;
import com.example.testcloudprovider.service.IPaymentOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.testcloudprovider.utils.DateTimeUtil;
import com.example.testcloudprovider.utils.Excel.ExcelUtil;
import com.example.testcloudprovider.utils.Excel.ExportParam;
import com.example.testcloudprovider.utils.ExportUtil;
import com.example.testcloudprovider.utils.SXSSFExcelUtils.HuToolExcelUtil;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public void exportOrder() {
        long t0 = System.currentTimeMillis();
        List<PaymentOrder> list = this.list(new LambdaQueryWrapper<>());
        long t1 = System.currentTimeMillis();
        System.out.println("导出数量" + list.size() + "，查询时间：" + (t1 - t0));
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        //压缩临时文件，很重要，否则磁盘很快就会被写满
        workbook.setCompressTempFiles(true);
        workbook.createSheet("订单导出");
        SXSSFSheet sheet = workbook.getSheetAt(0);
        //标题
        SXSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("父订单号");
        row.createCell(1).setCellValue("手机号");
        row.createCell(2).setCellValue("地区大区省");
        row.createCell(3).setCellValue("城市");
        row.createCell(4).setCellValue("机会ID");
        row.createCell(5).setCellValue("机会创建时间");
        row.createCell(6).setCellValue("订单金额");
        row.createCell(7).setCellValue("优惠金额");
        row.createCell(8).setCellValue("应付金额");
        row.createCell(9).setCellValue("已付金额");
        row.createCell(10).setCellValue("下单部门");
        row.createCell(11).setCellValue("下单人");
        row.createCell(12).setCellValue("支付时间");
        row.createCell(13).setCellValue("订单状态");
        row.createCell(14).setCellValue("业绩坐席");
        row.createCell(15).setCellValue("业绩坐席类别");
        row.createCell(16).setCellValue("业绩坐席事业部");
        row.createCell(17).setCellValue("业绩分摊部门");
        row.createCell(18).setCellValue("业绩坐席主管");
        row.createCell(19).setCellValue("机会库url");
        row.createCell(20).setCellValue("感兴趣课程");
        row.createCell(21).setCellValue("首次成交坐席");
        row.createCell(22).setCellValue("是否是二次升班");

        int rowNumber = 1;
        for (int i = 0; i < list.size(); i++) {
            if(i == 5){
                for(int j = 0; j<3; j++){
                    SXSSFRow row1 = sheet.createRow(rowNumber);
                    row1.createCell(0).setCellValue(list.get(i).getOrderId());
                    row1.createCell(1).setCellValue(list.get(i).getMobile());
                    row1.createCell(2).setCellValue(list.get(i).getProvinceName());
                    row1.createCell(3).setCellValue(list.get(i).getCityName());
                    row1.createCell(4).setCellValue(list.get(i).getOpportunityId());
                    row1.createCell(5).setCellValue(DateTimeUtil.getDateTimeStr(list.get(i).getOpportunityCreateDate(), "yyyy-MM-dd HH:mm:ss"));

                    row1.createCell(6).setCellValue(list.get(i).getOriginalCash().toString());
                    row1.createCell(7).setCellValue(list.get(i).getPauDou().toString());
                    row1.createCell(8).setCellValue(list.get(i).getCash().toString());
                    row1.createCell(9).setCellValue(list.get(i).getPayCash().toString());
                    row1.createCell(10).setCellValue(list.get(i).getShiYeBuId());
                    row1.createCell(11).setCellValue(list.get(i).getCreateDengLuZhangHao());
                    row1.createCell(12).setCellValue(list.get(i).getOpenDate());
                    row1.createCell(13).setCellValue(list.get(i).getOrderState());
                    row1.createCell(14).setCellValue(list.get(i).getPerformanceTbale());

                    row1.createCell(15).setCellValue(list.get(i).getPerformanceTableType());
                    row1.createCell(16).setCellValue(list.get(i).getPerformanceTableDept());
                    row1.createCell(17).setCellValue(list.get(i).getPerformanceTableMaster());
                    row1.createCell(18).setCellValue(list.get(i).getOpportunityUrl());
                    row1.createCell(19).setCellValue(list.get(i).getLikeClassName());
                    row1.createCell(20).setCellValue(list.get(i).getFirstOrderPersonName());
                    row1.createCell(21).setCellValue(list.get(i).getIsSecondPromotionClass());
                    row1.createCell(22).setCellValue(list.get(i).getTableId());
                    row1.createCell(23).setCellValue(list.get(i).getPackageId());
                    row1.createCell(24).setCellValue(list.get(i).getProductName());
                    row1.createCell(25).setCellValue(list.get(i).getDirectoryName());
                    row1.createCell(26).setCellValue(list.get(i).getExamName());
                    row1.createCell(27).setCellValue(list.get(i).getCourseTypeName());
                    row1.createCell(28).setCellValue(list.get(i).getResourceTypeName());
                    row1.createCell(29).setCellValue(list.get(i).getProtocolState());
                    row1.createCell(30).setCellValue(list.get(i).getPerformanceTableShiYeBu());
                    row1.createCell(31).setCellValue(list.get(i).getSubjectName());
                    rowNumber++;
                }
            }else{
                SXSSFRow row1 = sheet.createRow(rowNumber);
                row1.createCell(0).setCellValue(list.get(i).getOrderId());
                row1.createCell(1).setCellValue(list.get(i).getMobile());
                row1.createCell(2).setCellValue(list.get(i).getProvinceName());
                row1.createCell(3).setCellValue(list.get(i).getCityName());
                row1.createCell(4).setCellValue(list.get(i).getOpportunityId());
                row1.createCell(5).setCellValue(DateTimeUtil.getDateTimeStr(list.get(i).getOpportunityCreateDate(), "yyyy-MM-dd HH:mm:ss"));

                row1.createCell(6).setCellValue(list.get(i).getOriginalCash().toString());
                row1.createCell(7).setCellValue(list.get(i).getPauDou().toString());
                row1.createCell(8).setCellValue(list.get(i).getCash().toString());
                row1.createCell(9).setCellValue(list.get(i).getPayCash().toString());
                row1.createCell(10).setCellValue(list.get(i).getShiYeBuId());
                row1.createCell(11).setCellValue(list.get(i).getCreateDengLuZhangHao());
                row1.createCell(12).setCellValue(list.get(i).getOpenDate());
                row1.createCell(13).setCellValue(list.get(i).getOrderState());
                row1.createCell(14).setCellValue(list.get(i).getPerformanceTbale());

                row1.createCell(15).setCellValue(list.get(i).getPerformanceTableType());
                row1.createCell(16).setCellValue(list.get(i).getPerformanceTableDept());
                row1.createCell(17).setCellValue(list.get(i).getPerformanceTableMaster());
                row1.createCell(18).setCellValue(list.get(i).getOpportunityUrl());
                row1.createCell(19).setCellValue(list.get(i).getLikeClassName());
                row1.createCell(20).setCellValue(list.get(i).getFirstOrderPersonName());
                row1.createCell(21).setCellValue(list.get(i).getIsSecondPromotionClass());
                row1.createCell(22).setCellValue(list.get(i).getTableId());
                row1.createCell(23).setCellValue(list.get(i).getPackageId());
                row1.createCell(24).setCellValue(list.get(i).getProductName());
                row1.createCell(25).setCellValue(list.get(i).getDirectoryName());
                row1.createCell(26).setCellValue(list.get(i).getExamName());
                row1.createCell(27).setCellValue(list.get(i).getCourseTypeName());
                row1.createCell(28).setCellValue(list.get(i).getResourceTypeName());
                row1.createCell(29).setCellValue(list.get(i).getProtocolState());
                row1.createCell(30).setCellValue(list.get(i).getPerformanceTableShiYeBu());
                row1.createCell(31).setCellValue(list.get(i).getSubjectName());
                rowNumber++;
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("编写workbook时间：" + (t2 - t1));

//                ExportParam exportParam = ExportParam.builder().type(PaymentOrderExportDTO.class).sheetName("订单导出").dataList(paymentOrderExportAll).build();
        //        System.out.println("1");
        //        List<ExportParam> exportParamList = new ArrayList<>();
        //        exportParamList.add(exportParam);
        //        System.out.println("2");
        //        ExportUtil.saveExcelByPath(ExcelUtil.getWorkBook(exportParamList), "D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb", "订单管理.xlsx");
        long t3 = System.currentTimeMillis();
        ExportUtil.saveBigExcelByPath(workbook, "D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb", "订单管理");
        System.out.println("导出完成时间：" + (t3 - t2));
        System.out.println("总完成时间：" + (t3 - t0));
    }

    @Override
    public void huToolExport() {
        long t0 = System.currentTimeMillis();
        List<PaymentOrder> list = this.list(new LambdaQueryWrapper<>());
        List<ExcelPaymentOrderDTO> dtoList = list.stream().map(m-> BeanUtil.copyProperties(m,ExcelPaymentOrderDTO.class)).collect(Collectors.toList());
        long t1 = System.currentTimeMillis();
        System.out.println("导出数量" + dtoList.size() + "，查询时间：" + (t1 - t0));
        HuToolExcelUtil.exportBigExcel(ExcelPaymentOrderDTO.class,dtoList,
                "D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb"+ File.separator +"hutool--订单管理.xlsx",
                "hutoolOrder");
        long t3 = System.currentTimeMillis();
        System.out.println("导出完成，完成时间：" + (t3 - t0));

    }

    @Override
    public void huToolNeedMergeExport() {
        long t0 = System.currentTimeMillis();
        List<PaymentOrder> list = this.list(new LambdaQueryWrapper<>());
        //模拟订单中有多个子订单
        List<NewExcelOrderDTO> newOrderList = new ArrayList<>();
        list.forEach(paymentOrder -> {
            NewExcelOrderDTO dto = new NewExcelOrderDTO();
            BeanUtils.copyProperties(paymentOrder,dto);
            List<Order> orderList = new ArrayList<>();
            Order order = new Order();
            Integer orderId = paymentOrder.getOrderId();
            if((30 > orderId && orderId > 27)){
                orderList.add(order);
                orderList.add(order);
                orderList.add(order);
            }
            orderList.add(order);
            dto.setOrderRelationExportDTOS(orderList);

            newOrderList.add(dto);

        });
        //数据分组组合 按照订单号
        List<ExcelPaymentOrderDTO>  dtoList = new ArrayList<>();
        newOrderList.forEach(newOrder ->{
            List<Order> orderRelationExportDTOS = newOrder.getOrderRelationExportDTOS();
            orderRelationExportDTOS.forEach(order -> {
                ExcelPaymentOrderDTO excelDTO = new ExcelPaymentOrderDTO();
                BeanUtils.copyProperties(newOrder,excelDTO);
                BeanUtils.copyProperties(order,excelDTO);
//                excelDTO.setTableId(order.getTableId());
//                excelDTO.setPackageId(order.getPackageId());
//                excelDTO.setProductName(order.getProductName());
//                excelDTO.setDirectoryName(order.getDirectoryName());
//                excelDTO.setExamName(order.getExamName());
//                excelDTO.setSubjectName(order.getSubjectName());
//                excelDTO.setCourseTypeName(order.getCourseTypeName());
//                excelDTO.setResourceTypeName(order.getResourceTypeName());
//                excelDTO.setProtocolState(order.getProtocolState());
                dtoList.add(excelDTO);
            });
        });
        //分组行数
        Map<Integer, Long> collect = dtoList.stream()
                .collect(Collectors.groupingBy(ExcelPaymentOrderDTO::getOrderId,LinkedHashMap::new, Collectors.counting()));


        long t1 = System.currentTimeMillis();
        System.out.println("导出数量" + dtoList.size() + "，查询时间：" + (t1 - t0));
        HuToolExcelUtil.exportNeedMergeBigExcel(ExcelPaymentOrderDTO.class,dtoList,collect,
                "D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb"+ File.separator +"hutool订单管理.xlsx",
                "hutoolOrder");
        long t3 = System.currentTimeMillis();
        System.out.println("导出完成，完成时间：" + (t3 - t0));

    }
}
