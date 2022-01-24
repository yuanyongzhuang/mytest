package com.example.testcloudprovider.utils.poi;

import com.example.testcloudprovider.entity.PoiModel;
import com.example.testcloudprovider.utils.DateTimeUtil;
import com.example.testcloudprovider.utils.ExportUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * poi  excel
 */
public class PoiUtils {

    /**
     *  导出 动态合并单元格
     * @param request req
     * @param response res
     * @param title 标题数组
     * @param titleHead Excel标题
     * @param widthAttr 单元格宽度
     * @param maps 数据
     * @param mergeIndex 要合并的列   数组
     * @return String 返回类型
     */
    public static String createExcel(HttpServletRequest request, HttpServletResponse response, String[] title, String titleHead ,
                                     int[] widthAttr, Map<String/*sheet名*/, List<Map<String/*对应title的值*/, String>>> maps, int[] mergeIndex){

        if(title.length==0){
            return null;
        }

        //初始化excel模板
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = null;
        int n = 0;
        //循环sheet页
        for(Map.Entry<String, List<Map<String/*对应title的值*/, String>>> entry : maps.entrySet()){
            //实例化sheet 对象并且设置sheet名称，book对象
            try{
                sheet = workbook.createSheet();
                workbook.setSheetName(n, entry.getKey());
                workbook.setSelectedTab(0);
            }catch (Exception e){
                e.printStackTrace();
            }
            //设置样式 头 cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            //水平方向的对齐方式
            CellStyle cellStyle_head = style(0,workbook);
            // 导出时间
            CellStyle cellStyle_export = style(3, workbook);
            // 标题
            CellStyle cellStyle_title = style(1, workbook);
            // 正文
            CellStyle cellStyle = style(2, workbook);
            //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
            CellRangeAddress c1 = new CellRangeAddress(0,0,0,title.length-1);
            sheet.addMergedRegion(c1);
            CellRangeAddress c2 = new CellRangeAddress(1,1,0,title.length-1);
            sheet.addMergedRegion(c2);
            //在sheet里创建第一行，参数为行索引（excel的行），可以是0-65535之间的任何一个
            Row row0 = sheet.createRow(0);
            //创建单元格（excel的单元格，参数为列索引，可以是0-255之间的任何一个）
            Cell cell1 = row0.createCell(0);
            //设置单元格内容 标题
            cell1.setCellValue("兴为科技");
            cell1.setCellStyle(cellStyle_head);
            //设置合并单元格边框
            setRegionStyle(sheet, c1, cellStyle_head);
            setRegionStyle(sheet, c2, cellStyle_export);
            //设置列宽
            for(int i = 0; i < widthAttr.length; i++){
                sheet.setColumnWidth((short)i,(short) widthAttr[i] * 200);
            }
            //在sheet里创建第二行
            Row row1 = sheet.createRow(1);
            //创建单元格（excel的单元格，参数为列索引，可以是0-255之间的任何一个）
            Cell cell2 = row1.createCell(0);
            //设置单元格内容 标题
            cell2.setCellValue("导出时间："+ DateTimeUtil.getStrToDate("yyyy-MM-dd HH:mm:ss"));
            cell2.setCellStyle(cellStyle_export);
            //初始化标题，填值标题行（第一行）
            Row row2 = sheet.createRow(2);
            for(int i = 0; i < title.length; i++){
                //创建单元格，指定类型
                Cell cell_1 = row2.createCell(i,CellType.STRING);
                //设定标题的值
                cell_1.setCellValue(title[i]);
                //设定标题样式
                cell_1.setCellStyle(cellStyle_title);
            }
            //得到当前sheet下的数据集合
            List<Map<String/*对应title的值*/,String>> list = entry.getValue();
            //便利该数据集合
            List<PoiModel> poiModels = Lists.newArrayList();
            if(workbook != null){
                Iterator iterator = list.iterator();
                //第四行开始，前面几行已经塞入标题了
                    int index = 3;
                while(iterator.hasNext()){
                    Row row = sheet.createRow(index);
                    //获取当前这行的map，该map中以key，value的形式存着一行值
                    @SuppressWarnings("unchecked")
                    Map<String, String> map =(Map<String, String>) iterator.next();
                    //循环列数，给当前行
                    for(int i = 0; i < title.length; i++){
                        String old = "";
                        //old存的是上一行同一位置的单元的值，第一行是最上一行，所以从第二行开始记
                        if(index > 3){
                            old = poiModels.get(i) == null ? "": poiModels.get(i).getContent();
                        }
                        //循环需要合并的列
                        for(int j = 0; j < mergeIndex.length; j++){
                            //因为标题行前还有2行 所以index从3开始 也就是第四行
                            if(index == 3){
                                //记录第一行的开始行和开始列
                                PoiModel poiModel = new PoiModel();
                                poiModel.setOldContent(map.get(title[i]));
                                poiModel.setContent(map.get(title[i]));
                                poiModel.setRowIndex(3);
                                poiModel.setCellIndex(i);
                                poiModels.add(poiModel);
                                break;
                            }else if(i > 0 && mergeIndex[j] == i){
                                //这里i>0也是因为第一列已经是最前一列了，只能从第二列开始
                                //当前同一列的内容于上一行同一列不同时，把那以上的合并，或者在当前元素一样的情况下，前一列的元素并不一样，这种情况也合并
                                //如果不需要考虑当前行于上一行内容相同，但时他们的前一列内容不一样则不合并的情况，把下面条件中
                                // ||poiModels.get(i).getContent().equals(map.get(title[i])) && !poiModels.get(i - 1).getOldContent().equals(map.get(title[i-1]))
                                // 去掉就行
                                if(!poiModels.get(i).getContent().equals(map.get(title[i]))
                                        || poiModels.get(i).getContent().equals(map.get(title[i]))
                                        && !poiModels.get(i - 1).getOldContent().equals(map.get(title[i-1]))){
                                    //当前行的当前列于上一行的当前列的内容不一致时，则把当前行以上的合并
                                    CellRangeAddress cra = new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/,index-1/*到第几行*/,
                                            poiModels.get(i).getCellIndex()/*从某一列开始*/,poiModels.get(i).getCellIndex()/*到第几列*/);
                                    //在sheet里增加合并单元格
                                    sheet.addMergedRegion(cra);
                                    //重新记录该列的内容为当前内容，行标记改为当前行标记，列标记则为当前列
                                    poiModels.get(i).setContent(map.get(title[i]));
                                    poiModels.get(i).setRowIndex(index);
                                    poiModels.get(i).setCellIndex(i);
                                }
                            }
                            //处理第一列的情况
                            if(mergeIndex[j] == i && i == 0 && !poiModels.get(i).getContent().equals(map.get(title[i]))){
                                /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                                CellRangeAddress cra=new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/,
                                        poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                                /*重新记录该列的内容为当前内容，行标记改为当前行标记*/
                                poiModels.get(i).setContent(map.get(title[i]));
                                poiModels.get(i).setRowIndex(index);
                                poiModels.get(i).setCellIndex(i);
                            }
                            //最后一行没有后续的行于之比较，所有当到最后一行时则直接合并对应列的相同内容 +2是因为标题行前面还用2行
                            if(mergeIndex[j] == i && index == list.size()+2){
                                CellRangeAddress cra = new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index/*到第几行*/,
                                        poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                            }
                        }
                        Cell cell = row.createCell(i,CellType.STRING);
                        cell.setCellValue(map.get(title[i]));
                        cell.setCellStyle(cellStyle);
                        //在每一个单元格处理完成后，把这个单元格内容设置为old内容
                        poiModels.get(i).setOldContent(old);
                    }
                    index++;
                }
            }
            n++;
        }
        ExportUtil.saveExcelByPath(workbook,"D:" + File.separator + "AAAAA_yyz_wrod" + File.separator + "java" + File.separator + "bbb", "订单管理.xlsx");
        return "导出成功";
    }

    /**
     * 设定文件  样式
     * @param index index 0:头 1：标题 2：正文
     * @param workbook workbook
     * @return HSSFCellStyle    返回类型
     */
    public static CellStyle style(int index, Workbook workbook){
        CellStyle cellStyle = null;
        if(index == 0){
            //设置头样式
            cellStyle = workbook.createCellStyle();
            //设置字体大小 位置居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //生成一个字体
            Font font = workbook.createFont();
            //设置字体
            font.setFontName("宋体");
            //字体颜色 黑色
            font.setColor(Font.COLOR_NORMAL);
            //设置字体高度
            font.setFontHeightInPoints((short) 12);
            //字体增粗
            font.setBold(true);
            //上下居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //背景白色
            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
            //填充
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //边框下
            cellStyle.setBorderBottom(BorderStyle.THIN);
            //边框左
            cellStyle.setBorderLeft(BorderStyle.THIN);
            //边框右
            cellStyle.setBorderRight(BorderStyle.THIN);
            //边框上
            cellStyle.setBorderTop(BorderStyle.THIN);
            //单元格对齐格式
            cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            cellStyle.setFont(font);
        }
        if(index == 1){
            //标题
            cellStyle = workbook.createCellStyle();
            //设置字体大小 位置居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //生成一个字体
            Font font_title = workbook.createFont();
            //设置字体
            font_title.setFontName("宋体");
            //字体颜色 黑色
            font_title.setColor(Font.COLOR_NORMAL);
            //设置字体高度
            font_title.setFontHeightInPoints((short) 10);
            //字体增粗
            font_title.setBold(true);
            //上下居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //背景白色
            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
            //填充
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //边框下
            cellStyle.setBorderBottom(BorderStyle.THIN);
            //边框左
            cellStyle.setBorderLeft(BorderStyle.THIN);
            //边框右
            cellStyle.setBorderRight(BorderStyle.THIN);
            //边框上
            cellStyle.setBorderTop(BorderStyle.THIN);
            //单元格对齐格式
            cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            cellStyle.setFont(font_title);
        }
        if(index == 2){
            //正文
            cellStyle = workbook.createCellStyle();
            //设置字体大小 位置居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //生成一个字体
            Font font_title = workbook.createFont();
            //设置字体
            font_title.setFontName("宋体");
            //字体颜色 黑色
            font_title.setColor(Font.COLOR_NORMAL);
            //设置字体高度
            font_title.setFontHeightInPoints((short) 10);
            //字体增粗
//            font_title.setBold(true);
            //自动换行
            cellStyle.setWrapText(true);
            //上下居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //背景白色
//            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
            //填充
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //边框下
            cellStyle.setBorderBottom(BorderStyle.THIN);
            //边框左
            cellStyle.setBorderLeft(BorderStyle.THIN);
            //边框右
            cellStyle.setBorderRight(BorderStyle.THIN);
            //边框上
            cellStyle.setBorderTop(BorderStyle.THIN);
            //单元格对齐格式
            cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            cellStyle.setFont(font_title);
        }
        if(index == 3){
            //时间
            cellStyle = workbook.createCellStyle();
            //设置字体大小 位置居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //生成一个字体
            Font font_title = workbook.createFont();
            //设置字体
            font_title.setFontName("宋体");
            //字体颜色 黑色
            font_title.setColor(Font.COLOR_NORMAL);
            //设置字体高度
            font_title.setFontHeightInPoints((short) 10);
            //字体增粗
            font_title.setBold(true);
            //上下居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //背景白色
            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
            //填充
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //边框下
            cellStyle.setBorderBottom(BorderStyle.THIN);
            //边框左
            cellStyle.setBorderLeft(BorderStyle.THIN);
            //边框右
            cellStyle.setBorderRight(BorderStyle.THIN);
            //边框上
            cellStyle.setBorderTop(BorderStyle.THIN);
            //单元格对齐格式
            cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            cellStyle.setFont(font_title);
        }
        if(index == 4){
            //时间
            cellStyle = workbook.createCellStyle();
            //设置字体大小 位置居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //上下居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //填充
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //边框下
            cellStyle.setBorderBottom(BorderStyle.THIN);
            //边框左
            cellStyle.setBorderLeft(BorderStyle.THIN);
            //边框右
            cellStyle.setBorderRight(BorderStyle.THIN);
            //边框上
            cellStyle.setBorderTop(BorderStyle.THIN);
            //单元格对齐格式
            cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        }
        return cellStyle;
    }

    /**
     * 合并单元格后边框不显示问题
     * @param sheet sheet页
     * @param region 单元格范围
     * @param cs 样式
     */
    public static void setRegionStyle(Sheet sheet, CellRangeAddress region, CellStyle cs){
        for(int i = region.getFirstRow(); i <= region.getLastRow(); i++){
            Row row = CellUtil.getRow(i,sheet);
            for(int j = region.getFirstColumn(); j <= region.getLastColumn(); j++){
                Cell cell = CellUtil.getCell(row,(short) j);
                cell.setCellStyle(cs);
            }
        }
    }
}



























































