package com.message;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
 
public class ExcelReport { 
     
    private static WritableCellFormat wcf_value;//表格数据样式 
    private static WritableCellFormat wcf_value_left; 
    private static WritableCellFormat wcf_key;//表头样式 
    private static WritableCellFormat wcf_name_left;//表名样式 
    private static WritableCellFormat wcf_name_right;//表名样式 
    private static WritableCellFormat wcf_name_center;//表名样式 
    private static WritableCellFormat wcf_title;//页名称样式  
    private static WritableCellFormat wcf_percent_float; 
     
    private static final int MAXCOLS=7;//表名称样式 
     
    static{ 
        try { 
            //WritableFont wf_value = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD); 
            WritableFont wf_key = new jxl.write.WritableFont(WritableFont.createFont("微软雅黑"), 10,WritableFont.BOLD); 
            WritableFont wf_value = new jxl.write.WritableFont(WritableFont.createFont("微软雅黑"), 10,WritableFont.NO_BOLD); 
            wcf_value = new WritableCellFormat(wf_value); 
            wcf_value.setAlignment(jxl.format.Alignment.CENTRE); 
            wcf_value.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
            wcf_value.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); 
             
            wcf_value_left = new WritableCellFormat(wf_value); 
            wcf_value_left.setAlignment(jxl.format.Alignment.LEFT); 
            wcf_value_left.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
            wcf_value_left.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); 
            wcf_value_left.setWrap(true); 
             
            //WritableFont wf_key = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD); 
            wcf_key = new WritableCellFormat(wf_key); 
            wcf_key.setAlignment(jxl.format.Alignment.CENTRE); 
            wcf_key.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); 
             
            wcf_name_left = new WritableCellFormat(wf_key); 
            wcf_name_left.setAlignment(Alignment.LEFT); 
            wcf_name_left.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
             
            wcf_name_right = new WritableCellFormat(wf_key); 
            wcf_name_right.setAlignment(Alignment.RIGHT); 
             
            wcf_name_center = new WritableCellFormat(wf_key); 
            wcf_name_center.setAlignment(Alignment.CENTRE); 
             
            jxl.write.NumberFormat wf_percent_float = new jxl.write.NumberFormat("0.00");  
            wcf_percent_float= new jxl.write.WritableCellFormat(wf_value,wf_percent_float); 
            wcf_percent_float.setAlignment(jxl.format.Alignment.CENTRE); 
            wcf_percent_float.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
            wcf_percent_float.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); 
             
             
            //WritableFont wf_title = new WritableFont(WritableFont.ARIAL,14,WritableFont.BOLD); 
            WritableFont wf_title = new jxl.write.WritableFont(WritableFont.createFont("微软雅黑"), 14,WritableFont.BOLD); 
            wcf_title = new WritableCellFormat(wf_title); 
            wcf_title.setAlignment(Alignment.CENTRE); 
             
             
             
             
        } catch (WriteException e) {             
            e.printStackTrace(); 
        } 
         
         
    } 
     
    //生成Excel文件 
    @SuppressWarnings("deprecation")
	public void genarateExcel(File file, ArrayList<ArrayList<String>> gridData)throws Exception{ 
        WritableWorkbook wb = Workbook.createWorkbook(file); 
        WritableSheet ws = wb.createSheet("通讯消息文档",0); 
         
        int startRowNum=0;//起始行 
        int startColNum=0;//起始列 
        //int maxColSize = gridData.size();//最大列数 
         
        //设置列宽 
        ws.setColumnView(0, 18); 
        ws.setColumnView(1, 35); 
        ws.setColumnView(2, 30); 
        ws.setColumnView(3, 60); 
        ws.setColumnView(4, 30); 

         
         
        ws.addCell(new Label(startColNum,startRowNum,"协议列表说明",wcf_title)); 
        ws.mergeCells(startColNum,startRowNum, 5,0); 
        startColNum=0; 
        startRowNum++;       
         
        ws.addCell(new Label(startColNum,startRowNum, "列表生成时间" + new Date().toLocaleString(),wcf_name_right)); 
        ws.mergeCells(startColNum,startRowNum, 5,1); 
        startColNum=0; 
        startRowNum++; 
        generateCells(ws,startRowNum,startColNum,1,MAXCOLS); 
         
        ws.addCell(new Label(startColNum++,startRowNum,"协议ID号",wcf_key)); 
        ws.addCell(new Label(startColNum++,startRowNum,"协议名称",wcf_key)); 
        ws.addCell(new Label(startColNum++,startRowNum,"协议类名",wcf_key)); 
        ws.addCell(new Label(startColNum++,startRowNum,"所属模块",wcf_key)); 
        ws.addCell(new Label(startColNum++,startRowNum,"协议用途",wcf_key)); 
        ws.addCell(new Label(startColNum++,startRowNum,"备注",wcf_key)); 
         
        startRowNum++;       
        startColNum=0; 
         
        for(int i=0;i<gridData.size();i++){ 
            int rowIndx = startRowNum+i; 
            ws.addCell(new Label(startColNum,rowIndx,gridData.get(i).get(startColNum++),wcf_value_left)); 
            ws.addCell(new Label(startColNum,rowIndx,gridData.get(i).get(startColNum++),wcf_value_left)); 
            ws.addCell(new Label(startColNum,rowIndx,gridData.get(i).get(startColNum++),wcf_value_left)); 
            ws.addCell(new Label(startColNum,rowIndx,gridData.get(i).get(startColNum++),wcf_value_left)); 
            ws.addCell(new Label(startColNum,rowIndx,gridData.get(i).get(startColNum++),wcf_value_left)); 
            ws.addCell(new Label(startColNum,rowIndx,gridData.get(i).get(startColNum++),wcf_value_left)); 
            rowIndx ++;
            startColNum=0; 
        } 
         
         
        wb.write(); 
        wb.close(); 
    } 
     
 
    //生成空单元格 
    public static void generateCells(WritableSheet ws,int startRows,int startColNums,int rows,int cols){ 
        for(int r=0;r<rows;r++){ 
            for(int c = 0;c<cols;c++){ 
                try { 
                    ws.addCell(new Label(startColNums+c,startRows+r,"")); 
                } catch (Exception e) { 
                    e.printStackTrace(); 
                }  
            } 
        } 
    } 
     
    /**
     * @param filePath
     * @param gridData
     */
    public static void export(String filePath,ArrayList<ArrayList<String>> gridData){
        File file = new File(filePath); 
        try {
			file.createNewFile();
		    new ExcelReport().genarateExcel(file,gridData); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }

     
}