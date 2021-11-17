package com.example.testcloudprovider.task;

import com.example.testcloudprovider.dto.OrderRelationExportDTO;
import com.example.testcloudprovider.dto.PaymentOrderExportDTO;
import com.example.testcloudprovider.entity.ExportJob;
import com.example.testcloudprovider.enums.ExportJobProvessEnum;
import com.example.testcloudprovider.enums.ExportJobSourceEnum;
import com.example.testcloudprovider.service.IExportJobService;
import com.example.testcloudprovider.utils.DateTimeUtil;
import com.example.testcloudprovider.utils.Excel.ExcelUtil;
import com.example.testcloudprovider.utils.Excel.ExportParam;
import com.example.testcloudprovider.utils.ExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Component
@Slf4j
public class ExportJobAsync2Task {

    @Autowired
    IExportJobService exportJobService;

    ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public void asyncExportData() {

        long start = System.currentTimeMillis();

        //插入记录
        ExportJob exportJob = new ExportJob()
                .setCreateUserGroupId(123542)
                .setCreateTime(LocalDateTime.now())
                .setExportSource(ExportJobSourceEnum.ORDER_JOB.getValue())
                .setFileUri("D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb")
                .setFileName("订单管理")
                .setProcess(ExportJobProvessEnum.STARTED.getValue())
                .setExportClause("1234586584855");
        exportJob = exportJobService.addExportJob(exportJob);
        log.info("数据导出进行中，任务id[{}],文件名[{}]",exportJob.getId(),exportJob.getFileName());

        String startDate = "2021-09-01";
        String endDate = "2021-11-16";
        //总天数
        int difference = (int) (DateTimeUtil.getDifference(DateTimeUtil.getStrToDate(startDate), DateTimeUtil.getStrToDate(endDate)) + 1);

        List<PaymentOrderExportDTO> paymentOrderExportAll = new ArrayList();
        String dateStr = startDate;
        //时间段
        List<String> dateList = new ArrayList<>();
        for(int i = 0; i < difference; i++){
            dateStr = (i == 0 ? dateStr:DateTimeUtil.getDateStrPlusDays(DateTimeUtil.getStrToDate(dateStr),1,"yyyy-MM-dd"));
            dateList.add(dateStr);
        }
        //拆分每段条数
        int size = 10;
        List<List<String>> groupList = splitListStr(dateList,size);
//        List<List<Callable<List<PaymentOrderExportDTO>>>> groupList = splitList(dateList,size);
        System.out.println("时间拆分段数"+(groupList.size())+"拆分时间个数"+dateList.size());
        //总数据集合
        int process;
        for(int i = 0; i < groupList.size(); i++){
            List<String> dateSplitList = groupList.get(i);
            List<Future<List<PaymentOrderExportDTO>>> futureTasks = new ArrayList<>();
            dateSplitList.forEach(new Consumer<String>() {
                @Override
                public void accept(String dateStr) {
                    futureTasks.add(threadPool.submit(new Callable<List<PaymentOrderExportDTO>>() {
                        @Override
                        public List<PaymentOrderExportDTO> call() throws Exception {
                            try {
                                Thread.sleep(3000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return extracted(dateStr);
                        }
                    }));
                }
            });
            System.out.println("dateSplitList:"+futureTasks.size());
            futureTasks.forEach(task -> {
                try {
                    List<PaymentOrderExportDTO> paymentOrderExportDTOS = task.get(10, TimeUnit.SECONDS);
                    System.out.println(paymentOrderExportDTOS);
                    paymentOrderExportAll.addAll(paymentOrderExportDTOS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            });
            process = 1+i;
            System.out.println("process="+process);
            exportJob.setProcess(process);
            exportJobService.updateById(exportJob);

        }

        ExportParam exportParam = ExportParam.builder().type(PaymentOrderExportDTO.class).sheetName("订单导出").dataList(paymentOrderExportAll).build();
        List<ExportParam> exportParamList = new ArrayList<>();
        exportParamList.add(exportParam);
        ExportUtil.saveExcelByPath(ExcelUtil.getWorkBook(exportParamList), "D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb", "订单管理");
        log.info("数据导出完成，任务id[{}],导出目录[{}],文件名[{}]",exportJob.getId(),exportJob.getFileUri(),exportJob.getFileName());
        exportJob.setProcess(ExportJobProvessEnum.OVER.getValue());
        exportJobService.updateById(exportJob);

        System.out.println("完成时间"+(System.currentTimeMillis() - start));
    }

    /**
     * 拆分list
     * @param dateList  被拆分集合
     * @param size 每段条数
     * @return
     */
    private List<List<String>> splitListStr(List<String> dateList, int size) {
        //拆分集合
        List<List<String>> groupList = new ArrayList<>();
        int listSize = dateList.size();
        //子集合的长度
        int toIndex = size;
        for (int i = 0; i < dateList.size(); i += size) {
            if (i + size > listSize) {
                toIndex = listSize - i;
            }
            List<String> newList = dateList.subList(i, i + toIndex);
            groupList.add(newList);
        }
        return groupList;
    }
    /**
     * 拆分list
     * @param dateList  被拆分集合
     * @param size 每段条数
     * @return
     */
    private List<List<Callable<List<PaymentOrderExportDTO>>>> splitList(List<Callable<List<PaymentOrderExportDTO>>> dateList, int size) {
        //拆分集合
        List<List<Callable<List<PaymentOrderExportDTO>>>> groupList = new ArrayList<>();
        int listSize = dateList.size();
        //子集合的长度
        int toIndex = size;
        for (int i = 0; i < dateList.size(); i += size) {
            if (i + size > listSize) {
                toIndex = listSize - i;
            }
            List<Callable<List<PaymentOrderExportDTO>>> newList = dateList.subList(i, i + toIndex);
            groupList.add(newList);
        }
        return groupList;
    }

    private List<PaymentOrderExportDTO> extracted(String i) {
        List<PaymentOrderExportDTO> pList = new ArrayList<>();
        PaymentOrderExportDTO dto = new PaymentOrderExportDTO();
        dto.setOrderId(123);
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
        return pList;
    }
}
