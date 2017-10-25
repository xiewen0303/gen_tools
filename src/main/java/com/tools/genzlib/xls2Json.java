package com.tools.genzlib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.jdom.JDOMException;

import com.alibaba.fastjson.JSONArray;

/**
 * 读取游戏数值配置
 */
public class xls2Json {

    String excelPath ="";
    String fileOutputPath = "";

    public static void main(String[] args) {
        new xls2Json();
    }

    public xls2Json() {
        try {
            init();
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关联实体类的名称与excel表格
     * @throws Exception
     * @throws JDOMException
     */
    public void init() throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("genzlib.xlsTozlib");
        excelPath = new String(rb.getString("EXCEL_PATH").getBytes("ISO-8859-1"),"utf-8");
        System.out.println("excel文件目录：\t"+excelPath);
        fileOutputPath = new String(rb.getString("FILE_OUTPUT_PATH").getBytes("ISO-8859-1"),"utf-8");
        System.out.println("输出文件目录：\t"+fileOutputPath);
    }


    /**
     *
     * @return
     */
    public  List<Map<String,Object>> getSheetValue(Sheet sheet) {
        System.out.println("生成表："+sheet.getSheetName());
        List<Map<String,Object>> result = new ArrayList<>();

        int firstRowIndex = 0;				//sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();
//      int coloumNum = sheet.getRow(2).getRowNum();//获得总列数,包括空这着的列

        Row keyRow = sheet.getRow(1);
        int rowNum = keyRow.getLastCellNum();
        String[] keys =  new String[rowNum];
        String[] types =  new String[rowNum];

        for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex ++){
            Row row = sheet.getRow(rIndex);
            if(row != null) {
                Map<String,Object> rowMap = new HashMap<>();
                for(int cIndex = 0; cIndex < rowNum; cIndex ++){
                    Cell cell = row.getCell(cIndex);
                    String value = "";

                    if(cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        value = cell.getStringCellValue();
                    }
                    if(cIndex == 0 &&(value == null || "".equals(value.trim()))){
                        continue;
                    }

                    if(rIndex == 0){
                        keys[cIndex] = value;
                    }else if(rIndex == 2){
                        types[cIndex] = value;
                    }else if (rIndex > 2){
                       try {
                            if(null == keys[cIndex] || "".equals(keys[cIndex])){
                                continue;
                            }
                            rowMap.put(keys[cIndex], getTypeValue(types[cIndex],value));
                       } catch (Exception e) {
                            System.err.println("sheetName:"+sheet.getSheetName()+"\t"+"index:"+cIndex+"\tkey:【"+keys[cIndex]+"】\ttype is error:【"+types[cIndex]+"】\tvalue:"+value);
                            e.printStackTrace();
                       }
                    }
                }

                if(rIndex > 2){
                    result.add(rowMap);
                }
            }
        }
        return result;
    }

    //获得对应的类型数据
    public Object getTypeValue(String type,String value)  throws Exception {
        switch (type){
            case "string":
                return value;
            case "int":
                if ("".equals(value)){
                    return 0;
                }
                return Integer.parseInt(value);
            default:
               throw new Exception("配置的类型不存在，请检查是否在【int/String】中");
        }
    }


    public void load() throws Exception {
        Template xmlTemplate = Velocity.getTemplate("src/template/xls_to_xml.vm");

        //获取当前运行路径
        String path = excelPath;

        //获取当前路径对应的文件夹
        File folder = new File(path);

        //对该文件夹内的所有文件进行遍历
        if (folder.listFiles() == null) {
            System.err.println("发生严重错误，未能读取EXCEL文件路径");
            return;
        }

        HashMap<String, File> files = new HashMap<String, File>();

        for (File file : folder.listFiles()) {
            //获取文件夹内的文件名称和文件对象
            files.put(file.getName(), file);
        }


        for (File file : files.values()) {
            String fileName = file.getName();

            Workbook workbook = null;

            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            } else {
                System.err.println("您的文档格式不正确！fileName:"+fileName);
                continue;
            }

            //对所有标签进行遍历
            Iterator<Sheet> iterator = workbook.sheetIterator();
            while (iterator.hasNext()) {
                Sheet sheet = iterator.next();
                if(sheet.getSheetName().contains("注释")){
                    continue;
                }
                List<Map<String, Object>> datas = getSheetValue(sheet);

                //生成json文件
                String dataStr = JSONArray.toJSONString(datas);
                byte[] bytes = dataStr.getBytes("utf-8");
                File jsonFile = new File(fileOutputPath +"//"+ sheet.getSheetName() + ".json");
                OutputStream output = new FileOutputStream(jsonFile);
                output.write(bytes);
                output.flush();
                output.close();
            }
        }
    }
}