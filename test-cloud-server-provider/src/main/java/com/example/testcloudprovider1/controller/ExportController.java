package com.example.testcloudprovider1.controller;

import com.example.testcloudprovider1.service.IExportJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@RestController
@RequestMapping({"/export"})
public class ExportController {
    @Autowired
    IExportJobService exportJobService;

    public ExportController() {
    }

    @GetMapping({"/download"})
    public void download(HttpServletResponse response) {
        FileInputStream inStream = null;

        try {
            inStream = new FileInputStream("D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb" + File.separator + "订单管理 (7).xls");
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
}
