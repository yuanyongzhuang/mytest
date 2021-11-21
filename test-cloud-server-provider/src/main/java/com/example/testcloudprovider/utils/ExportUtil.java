package com.example.testcloudprovider.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class ExportUtil {
    /**
     * 导出指定目录文件文件
     * @param response
     * @param fileName 文件名
     * @param path 文件路径
     */
    public static void exportFile(HttpServletResponse response, String fileName, String path) {
        FileInputStream inStream=null;
        try {
            inStream=new FileInputStream(path);
            byte[] buf=new byte[4096];
            int readLength;
            setResponseHeader(response, fileName);
            while (((readLength=inStream.read(buf)) != -1)) {
                response.getOutputStream().write(buf, 0, readLength);
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                //获取OutputStream输出流
                OutputStream outputStream=response.getOutputStream();
                //通过设置响应头控制浏览器以UTF-8的编码显示
                response.setHeader("content-type", "text/html;charset=UTF-8");
                //将字符转换成字节数组，指定以UTF-8编码进行转换
                byte[] dataByteArr="下载失败".getBytes("UTF-8");
                //使用OutputStream流向客户端输出字节数组
                outputStream.write(dataByteArr);
                return;
            }catch (Exception ex){
                e.printStackTrace();
            }
        }finally {
            if(inStream != null){
                try {
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 设置响应头  文件类型为xlsx的   可以修改对应的后缀
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            // 清空输出流
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(fileName.getBytes("GB2312"), "8859_1")
                    + ".xlsx");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 导出excel文件到文件目录下
     * @param workBook
     * @param uri
     * @param fileName
     */
    public static void saveExcelByPath(Workbook workBook, String uri, String fileName){
        File savefile = new File(uri);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream out = null;
        try{
            out = new FileOutputStream(uri + File.separator + fileName +".xlsx");
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 导出excel大文件到文件目录下
     * @param workBook
     * @param uri
     * @param fileName
     */
    public static void saveBigExcelByPath(SXSSFWorkbook workBook, String uri, String fileName){
        File savefile = new File(uri);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream out = null;
        try{
            out = new FileOutputStream(uri + File.separator + fileName +".xlsx");
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (workBook != null) {
                // 删除临时文件，很重要，否则磁盘可能会被写满
                workBook.dispose();
            }
        }
    }
}
