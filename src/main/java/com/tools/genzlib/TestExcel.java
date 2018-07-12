package com.tools.genzlib;

	import jxl.Workbook;
	import jxl.write.Label;
	import jxl.write.WritableSheet;
	import jxl.write.WritableWorkbook;
	import jxl.write.WriteException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
	import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;

	/**
	 * @Author changle
	 * @Time 17/8/1.
	 * @Desc json转变为Excel演示
	 */
	public class TestExcel {
	    public static void main(String[] args) {
	        parseJsonToExcel("C:/Users/aaa/Desktop/tttt/jsonToExcel-demo.xls", "C:/Users/aaa/Desktop/tttt/config.json");
	    }

	    static void parseJsonToExcel(String saveFileName, String rootNodeName) {
	        try {
	        	
	        	StringBuffer sb = new StringBuffer();
	        	
	        	
	        	BufferedReader br = new BufferedReader(new  InputStreamReader(new FileInputStream(new File(rootNodeName)), "utf-8")); 
	        	String t = null;
	        	while((t=br.readLine()) != null){
	        		sb.append(t);
	        	}
	        	
	        	Map<String,Object> jsonObjectTemp  = (Map)JSONObject.parseObject(sb.toString());
	        	
	        	Map<String,List<Map<String,Object>>>  jsonObject = new HashMap<>();
	        	
	        	for (Entry<String, Object> iterable_element : jsonObjectTemp.entrySet()) {
	        		try {
						Map<String,Object> tt11 = 	(Map<String,Object>)iterable_element.getValue();
						List<Map<String,Object>> sss = (List<Map<String,Object>>)tt11.get("data");
						jsonObject.put(iterable_element.getKey(), sss);
					} catch (Exception e) {
						
						Map<String,Object> tt11 = 	(Map<String,Object>)iterable_element.getValue();
						Map<String,Object> sss = (Map<String,Object>)tt11.get("data");
						
						 List<Map<String,Object>> ttsfe =   new ArrayList<>();
						for (Object e111 : sss.values()) {
							ttsfe.add((Map<String,Object>)e111);
						}
						jsonObject.put(iterable_element.getKey(),ttsfe);
					}
				}
	        	
	            
	            createExcel(jsonObject, rootNodeName);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public static void createExcel(Map<String,List<Map<String,Object>>> apiJarInfos, String rootNodeName) throws WriteException, IOException {
	        int t = 0;
	        for (Entry<String, List<Map<String,Object>>> iterable_element : apiJarInfos.entrySet()) {
	        	
        	   File filewrite = new File("C:/Users/aaa/Desktop/tttt/"+iterable_element.getKey().replace("_json", "")+".xls");
	           filewrite.createNewFile();
	           OutputStream os = new FileOutputStream(filewrite);
	           //创建工作薄
		        WritableWorkbook workbook = Workbook.createWorkbook(os);
	        	
	        	//创建新的一页
		        WritableSheet sheet = workbook.createSheet(iterable_element.getKey().replace("_json", ""), t++);
		        List<String> jsonObjectHeader = new ArrayList<>(iterable_element.getValue().get(0).keySet());
		        String[] headers = createTableHeader(jsonObjectHeader, sheet);
		        
		        List<Map<String,Object>> contents   =   iterable_element.getValue();
		       
		        for (int i = 1; i <= contents.size(); i++) {
		        	int j = 0;
		        	Map<String,Object> rowData = contents.get(i-1);
		            for (String key : headers) {
		                try {
							Label cellValue = new Label(j, i, rowData.get(key)==null? "":rowData.get(key).toString());
							sheet.addCell(cellValue);
							j++;
						} catch (Exception e) {
							e.printStackTrace();
						}
		            }
		        }
		        
		        //把创建的内容写入到输出流中，并关闭输出流
		        workbook.write();
		        workbook.close();
		        os.close();
			}
	       
	    }

	    static String[] createTableHeader(List<String> jsonObjectHeader, WritableSheet sheet) throws WriteException {
	        //遍历JSONObject中的key
	        String[] headers = new String[jsonObjectHeader.size()];
	        
	        for (int i = 0; i < jsonObjectHeader.size(); i++) {
	        	 //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
	            String headerName = jsonObjectHeader.get(i);
	            Label cell = new Label(i, 0, headerName);
	            sheet.addCell(cell);
	            headers[i] = headerName;
			}
	        return headers;
	    }
	}
