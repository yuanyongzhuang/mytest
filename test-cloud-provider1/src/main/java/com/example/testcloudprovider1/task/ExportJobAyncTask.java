package com.example.testcloudprovider1.task;

import com.example.testcloudprovider1.dto.OrderRelationExportDTO;
import com.example.testcloudprovider1.dto.PaymentOrderExportDTO;
import com.example.testcloudprovider1.utils.Excel.ExcelUtil;
import com.example.testcloudprovider1.utils.Excel.ExportParam;
import com.example.testcloudprovider1.utils.ExportUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExportJobAyncTask {
    @Async
    public void asyncExportData() {
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException var8) {
            var8.printStackTrace();
        }

        List<PaymentOrderExportDTO> paymentOrderExportAll = new ArrayList();
        int process = 1;

        for(int i = 0; i < 90; ++i) {
            List<PaymentOrderExportDTO> pList = new ArrayList<>();
            PaymentOrderExportDTO dto = new PaymentOrderExportDTO();
            dto.setOrderId(123 + i);
            dto.setMobile("12345678910");
            dto.setOpportunityId(125436);
            dto.setOriginalCash(BigDecimal.valueOf(125.0D));
            dto.setShiYeBuId("1458");
            dto.setCreateDengLuZhangHao("袁永庄_42679");
            List<OrderRelationExportDTO> oList = new ArrayList();
            OrderRelationExportDTO odto = new OrderRelationExportDTO();
            odto.setTableId(741852);
            odto.setPackageId(123456);
            odto.setProductName("商品" + i);
            odto.setDirectoryName("建筑类");
            odto.setCourseTypeName("精选科");
            odto.setResourceTypeName("商品");
            odto.setProtocolState("未签署");
            oList.add(odto);
            dto.setOrderRelationExportDTOS(oList);
            pList.add(dto);
            if (pList != null) {
                paymentOrderExportAll.addAll(pList);
            }

            if (process % 10 == 0) {
                System.out.println("循环进度:" + process / 10);
            }

            ++process;
        }

        ExportParam exportParam = ExportParam.builder().type(PaymentOrderExportDTO.class).sheetName("订单导出").dataList(paymentOrderExportAll).build();
        List<ExportParam> exportParamList = new ArrayList<>();
        exportParamList.add(exportParam);
        ExportUtil.saveExcelByPath(ExcelUtil.getWorkBook(exportParamList), "D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb", "订单管理.xlsx");
    }
}
