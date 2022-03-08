package com.example.testcloudprovider.controller;

import com.example.testcloudprovider.service.IExportJobService;
import com.example.testcloudprovider.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping({"/export"})
public class ExportController {

    @Autowired
    IExportJobService exportJobService;

    @GetMapping({"/download"})
    public void download(HttpServletResponse response) {
        FileInputStream inStream = null;

        try {
            inStream = new FileInputStream("D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb" + File.separator + "订单管理.xlsx");
            byte[] buf = new byte[4096];
            setResponseHeader(response, "订单管理.xlsx");

            int readLength;
            while((readLength = inStream.read(buf)) != -1) {
                response.getOutputStream().write(buf, 0, readLength);
            }

            return;
        } catch (Exception var18) {
            var18.printStackTrace();

            try {
                OutputStream outputStream = response.getOutputStream();
                response.setHeader("content-type", "text/html;charset=UTF-8");
                byte[] dataByteArr = "下载失败".getBytes("UTF-8");
                outputStream.write(dataByteArr);
            } catch (Exception var17) {
                var18.printStackTrace();
                return;
            }
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Exception var16) {
                    var16.printStackTrace();
                }
            }

        }

    }

    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            String var10002 = new String(fileName.getBytes("GB2312"), "8859_1");
            response.setHeader("Content-Disposition", "attachment;filename=" + var10002 + ".xlsx");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    @PostMapping({"/exportOrder"})
    @ResponseBody
    public String exportOrder() {
        return this.exportJobService.export();
    }

    public static void main(String[] args) {
//        String lastMonth = DateTimeUtil.getLastMonth();
//        String entryTimeStr = DateTimeUtil.getDateTimeStr(DateTimeUtil.getStrToDateTime("2022-01-15 00:00:00"), "yyyyMM");
//        System.out.println(Integer.valueOf(lastMonth)-Integer.valueOf(entryTimeStr));

            String text = "huhantain,taiyangshen,nodeli";
            List<String> list1 = new ArrayList<>();
            Collections.addAll(list1,text);
            System.out.println(list1);
    }
}
