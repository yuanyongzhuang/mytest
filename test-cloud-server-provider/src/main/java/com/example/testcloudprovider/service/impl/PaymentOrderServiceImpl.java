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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
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
        List<PaymentOrder> orderList = new ArrayList<>();
        for(int j = 0; j <200000; j++){
            System.out.println("j=="+j);
            PaymentOrder order = new PaymentOrder();
            order.setMobile("12345678910"+j);
            order.setProvinceName("北京"+j);
            order.setCityName("通州"+j);
            order.setOpportunityId(10+j);
            order.setOpportunityCreateDate(LocalDateTime.now());
            order.setOriginalCash(BigDecimal.valueOf(125.0D+j));
            order.setPauDou(BigDecimal.valueOf(1.0D+j));
            order.setCash(BigDecimal.valueOf(124.0D+j));
            order.setPayCash(BigDecimal.valueOf(100.0D+j));
            order.setShiYeBuId("1458"+j);
            order.setCreateDengLuZhangHao("袁永庄_42679"+j);
            order.setOpenDate("2021-11-16"+j);
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
            order.setTableId(20+j);
            order.setPackageId(30+j);
            order.setProductName("商品" + j);
            order.setDirectoryName("建筑类");
            order.setExamName("基建");
            order.setSubjectName("二建");
            order.setCourseTypeName("精选科");
            order.setResourceTypeName("商品");
            order.setProtocolState("未签署");
            orderList.add(order);
        }
        this.saveBatch(orderList);
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
//        List<Integer> ids = new ArrayList<>();
//        ids.add(25);
//        ids.add(26);
//        ids.add(27);
//        ids.add(28);
//        ids.add(29);
//        ids.add(30);
//        ids.add(31);
//        ids.add(32);
//        List<PaymentOrder> list = this.list(new LambdaQueryWrapper<PaymentOrder>().in(PaymentOrder::getOrderId,ids));
        List<PaymentOrder> list = this.list(new LambdaQueryWrapper<PaymentOrder>());
        //模拟订单中有多个子订单
        List<NewExcelOrderDTO> newOrderList = new ArrayList<>();
        list.forEach(paymentOrder -> {
            NewExcelOrderDTO dto = new NewExcelOrderDTO();
            BeanUtils.copyProperties(paymentOrder,dto);
            List<Order> orderList = new ArrayList<>();
            Order order = new Order();
            Integer orderId = paymentOrder.getOrderId();
            List<Order> orders = new ArrayList<>();

            if((202025 == orderId || orderId == 202030 || orderId == 202035 || orderId == 202038)){
                for(int i =0; i < 4; i++){
                    Order order1 = new Order();
                    order1.setTableId(i+201);
                    order1.setPackageId(i+300);
                    order1.setProductName("wwwww"+i);
                    orders.add(order1);
                }
            }else{
                orders.add(order);
            }
            for(Order order2: orders){
                orderList.add(order2);
            }
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
                dtoList.add(excelDTO);
            });
        });
//        //分组行数
//        LinkedHashMap<Integer, Long> collect = dtoList.stream()
//                .collect(Collectors.groupingBy(ExcelPaymentOrderDTO::getOrderId,LinkedHashMap::new, Collectors.counting()));


        long t1 = System.currentTimeMillis();
        System.out.println("导出数量" + dtoList.size() + "，查询时间：" + (t1 - t0));
//        HuToolExcelUtil.exportNeedMergeBigExcel(ExcelPaymentOrderDTO.class,dtoList,collect,
//                "D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb"+ File.separator +"hutool订单管理"+t0+".xlsx",
//                "hutoolOrder");
        HuToolExcelUtil.exportBigExcel(ExcelPaymentOrderDTO.class,dtoList,
                "D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb"+ File.separator +"hutool订单管理"+t0+".xlsx",
                "hutoolOrder");
        long t3 = System.currentTimeMillis();
        System.out.println("导出完成，完成时间：" + (t3 - t0));

    }

    @Override
    public void poiMergeExport() {
        long t0 = System.currentTimeMillis();
        List<PaymentOrder> list = this.list(new LambdaQueryWrapper<PaymentOrder>());
        long t1 = System.currentTimeMillis();
        //模拟订单中有多个子订单
        List<NewExcelOrderDTO> newOrderList = new ArrayList<>();
        list.forEach(paymentOrder -> {
            NewExcelOrderDTO dto = new NewExcelOrderDTO();
            BeanUtils.copyProperties(paymentOrder,dto);
            List<Order> orderList = new ArrayList<>();
            Order order = new Order();
            Integer orderId = paymentOrder.getOrderId();
            List<Order> orders = new ArrayList<>();

            if((202025 == orderId || orderId == 202030 || orderId == 202035 || orderId == 202038)){
                for(int i =0; i < 4; i++){
                    Order order1 = new Order();
                    order1.setTableId(i+201);
                    order1.setPackageId(i+300);
                    order1.setProductName("wwwww"+i);
                    orders.add(order1);
                }
            }else{
                orders.add(order);
            }
            for(Order order2: orders){
                orderList.add(order2);
            }
            dto.setOrderRelationExportDTOS(orderList);

            newOrderList.add(dto);
        });

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(true);
        workbook.createSheet("订单导出");
        SXSSFSheet sheet = workbook.getSheetAt(0);
        //设置表头
        setHeader(sheet);

        int process = 1;
        for(NewExcelOrderDTO  newExcelOrderDTO: newOrderList){
            //excel赋值
            process = extracted(sheet, process, newExcelOrderDTO);

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
        ExportUtil.saveBigExcelByPath(workbook, "D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb", "订单管理"+t3);
        System.out.println("导出完成时间：" + (t3 - t2));
        System.out.println("总完成时间：" + (t3 - t0));

    }
    @SuppressWarnings("unchecked")
    private int extracted(SXSSFSheet sheet, int process, NewExcelOrderDTO dto) {
        int index = process;
        List<Order> orderRelationExportDTOS = dto.getOrderRelationExportDTOS();
        if(orderRelationExportDTOS.size() > 1){
            System.out.println("order id:"+dto.getOrderId()+"=== zi order size:"+orderRelationExportDTOS.size());
            int i = 0;
            for(Order subOrder: orderRelationExportDTOS){
                System.out.println("order id:"+dto.getOrderId()+"=== zi order size:"+i);

                SXSSFRow row = sheet.createRow(index);
                if(i == 0){
                    CellRangeAddress cra0=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            0/*从某一列开始*/, 0/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra0);
                    row.createCell(0).setCellValue(dto.getOrderId());
                    CellRangeAddress cra1=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            1/*从某一列开始*/, 1/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra1);
                    row.createCell(1).setCellValue(dto.getMobile());
                    CellRangeAddress cra2=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            2/*从某一列开始*/, 2/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra2);
                    row.createCell(2).setCellValue(dto.getProvinceName());
                    CellRangeAddress cra3=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            3/*从某一列开始*/, 3/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra3);
                    row.createCell(3).setCellValue(dto.getCityName());
                    CellRangeAddress cra4=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            4/*从某一列开始*/, 4/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra4);
                    row.createCell(4).setCellValue(dto.getOpportunityId() == null? 0:dto.getOpportunityId());
                    CellRangeAddress cra5=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            5/*从某一列开始*/, 5/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra5);
                    row.createCell(5).setCellValue(dto.getOpportunityCreateDate());

                    CellRangeAddress cra15=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            15/*从某一列开始*/, 15/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra15);
                    row.createCell(15).setCellValue(dto.getOriginalCash().doubleValue());
                    CellRangeAddress cra16=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            16/*从某一列开始*/, 16/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra16);
                    row.createCell(16).setCellValue(dto.getPauDou().doubleValue());
                    CellRangeAddress cra17=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            17/*从某一列开始*/, 17/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra17);
                    row.createCell(17).setCellValue(dto.getCash().doubleValue());
                    CellRangeAddress cra18=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            18/*从某一列开始*/, 18/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra18);
                    row.createCell(18).setCellValue(dto.getPayCash().doubleValue());
                    CellRangeAddress cra19=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            19/*从某一列开始*/, 19/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra19);
                    row.createCell(19).setCellValue(dto.getShiYeBuId());
                    CellRangeAddress cra20=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            20/*从某一列开始*/, 20/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra20);
                    row.createCell(20).setCellValue(dto.getCreateDengLuZhangHao());
                    CellRangeAddress cra21=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            21/*从某一列开始*/, 21/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra21);
                    row.createCell(21).setCellValue(dto.getOpenDate());
                    CellRangeAddress cra22=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            22/*从某一列开始*/, 22/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra22);
                    row.createCell(22).setCellValue(dto.getOrderState());
                    CellRangeAddress cra23=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            23/*从某一列开始*/, 23/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra23);
                    row.createCell(23).setCellValue(dto.getPerformanceTableType());
                    CellRangeAddress cra24=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            24/*从某一列开始*/, 24/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra24);
                    row.createCell(24).setCellValue(dto.getPerformanceTableShiYeBu());
                    CellRangeAddress cra25=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            25/*从某一列开始*/, 25/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra25);
                    row.createCell(25).setCellValue(dto.getPerformanceTableDept());
                    CellRangeAddress cra26=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            26/*从某一列开始*/, 26/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra26);
                    row.createCell(26).setCellValue(dto.getPerformanceTableMaster());
                    CellRangeAddress cra27=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            27/*从某一列开始*/, 27/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra27);
                    row.createCell(27).setCellValue(dto.getOpportunityUrl());
                    CellRangeAddress cra28=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            28/*从某一列开始*/, 28/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra28);
                    row.createCell(28).setCellValue(dto.getLikeClassName());
                    CellRangeAddress cra29=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            29/*从某一列开始*/, 29/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra29);
                    row.createCell(29).setCellValue(dto.getFirstOrderPersonName());
                    CellRangeAddress cra30=new CellRangeAddress(index/*从第几行开始*/, index + orderRelationExportDTOS.size() - 1/*到第几行*/,
                            30/*从某一列开始*/, 30/*到第几列*/);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra30);
                    row.createCell(30).setCellValue(dto.getIsSecondPromotionClass());
                }

                row.createCell(6).setCellValue(subOrder.getTableId());
                row.createCell(7).setCellValue(subOrder.getPackageId());
                row.createCell(8).setCellValue(subOrder.getProductName());
                row.createCell(9).setCellValue(subOrder.getDirectoryName());
                row.createCell(10).setCellValue(subOrder.getExamName());
                row.createCell(11).setCellValue(subOrder.getSubjectName());
                row.createCell(12).setCellValue(subOrder.getCourseTypeName());
                row.createCell(13).setCellValue(subOrder.getResourceTypeName());
                row.createCell(14).setCellValue(subOrder.getProtocolState());

                i++;
                index++;
            }
        }else{
            Order subOrder = orderRelationExportDTOS.get(0);
            SXSSFRow row = sheet.createRow(index);
            row.createCell(0).setCellValue(dto.getOrderId());
            row.createCell(1).setCellValue(dto.getMobile());
            row.createCell(2).setCellValue(dto.getProvinceName());
            row.createCell(3).setCellValue(dto.getCityName());
            row.createCell(4).setCellValue(dto.getOpportunityId() == null? 0:dto.getOpportunityId());
            row.createCell(5).setCellValue(dto.getOpportunityCreateDate());

            row.createCell(6).setCellValue(subOrder.getTableId());
            row.createCell(7).setCellValue(subOrder.getPackageId());
            row.createCell(8).setCellValue(subOrder.getProductName());
            row.createCell(9).setCellValue(subOrder.getDirectoryName());
            row.createCell(10).setCellValue(subOrder.getExamName());
            row.createCell(11).setCellValue(subOrder.getSubjectName());
            row.createCell(12).setCellValue(subOrder.getCourseTypeName());
            row.createCell(13).setCellValue(subOrder.getResourceTypeName());
            row.createCell(14).setCellValue(subOrder.getProtocolState());

            row.createCell(15).setCellValue(dto.getOriginalCash().doubleValue());
            row.createCell(16).setCellValue(dto.getPauDou().doubleValue());
            row.createCell(17).setCellValue(dto.getCash().doubleValue());
            row.createCell(18).setCellValue(dto.getPayCash().doubleValue());
            row.createCell(18).setCellValue(dto.getShiYeBuId());
            row.createCell(20).setCellValue(dto.getCreateDengLuZhangHao());
            row.createCell(21).setCellValue(dto.getOpenDate());
            row.createCell(22).setCellValue(dto.getOrderState());
            row.createCell(23).setCellValue(dto.getPerformanceTableType());
            row.createCell(24).setCellValue(dto.getPerformanceTableShiYeBu());
            row.createCell(25).setCellValue(dto.getPerformanceTableDept());
            row.createCell(26).setCellValue(dto.getPerformanceTableMaster());
            row.createCell(27).setCellValue(dto.getOpportunityUrl());
            row.createCell(28).setCellValue(dto.getLikeClassName());
            row.createCell(29).setCellValue(dto.getFirstOrderPersonName());
            row.createCell(30).setCellValue(dto.getIsSecondPromotionClass());
            index++;
        }
        return index;
    }

    private void setHeader(SXSSFSheet sheet) {
        SXSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("父订单号");
        row.createCell(1).setCellValue("手机号");
        row.createCell(2).setCellValue("地区大区省");
        row.createCell(3).setCellValue("城市");
        row.createCell(4).setCellValue("机会ID");
        row.createCell(5).setCellValue("机会创建时间");
        row.createCell(6).setCellValue("子订单id");
        row.createCell(7).setCellValue("商品id");
        row.createCell(8).setCellValue("商品名称");
        row.createCell(9).setCellValue("项目类");
        row.createCell(10).setCellValue("项目");
        row.createCell(11).setCellValue("科目");
        row.createCell(12).setCellValue("课程类型");
        row.createCell(13).setCellValue("产品类型");
        row.createCell(14).setCellValue("是否签署协议");
        row.createCell(15).setCellValue("订单金额");
        row.createCell(16).setCellValue("优惠金额");
        row.createCell(17).setCellValue("应付金额");
        row.createCell(18).setCellValue("已付金额");
        row.createCell(18).setCellValue("下单部门");
        row.createCell(20).setCellValue("下单人");
        row.createCell(21).setCellValue("支付时间");
        row.createCell(22).setCellValue("订单状态");
        row.createCell(23).setCellValue("业绩坐席");
        row.createCell(24).setCellValue("业绩坐席类别");
        row.createCell(25).setCellValue("业绩坐席事业部");
        row.createCell(26).setCellValue("业绩分摊部门");
        row.createCell(27).setCellValue("业绩坐席主管");
        row.createCell(28).setCellValue("机会库url");
        row.createCell(29).setCellValue("感兴趣课程");
        row.createCell(30).setCellValue("首次成交坐席");
    }
}
