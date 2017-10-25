package com.tools.genzlib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jdom.JDOMException;

import com.jcraft.jzlib.DeflaterOutputStream;

/**
 * 读取游戏数值配置
 */
public class GenZlibMain { 
	
	String excelPath ="";
	String fileOutputPath = "";
	
	HashMap<String,String> sheetNameList = new HashMap<String,String>();
	
	public static void GenZlib() {
		new GenZlibMain();
	}
	
	public GenZlibMain() {
		try {
			init();
			load();
//			clear();
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

	public void load() throws Exception {
		Template	xmlTemplate = Velocity.getTemplate("src/template/xls_to_xml.vm");
		
		//获取当前运行路径
		String path = excelPath;
		//获取当前路径对应的文件夹
		File folder = new File(path);
		//对该文件夹内的所有文件进行遍历
		if(folder.listFiles()==null) {
//			logger.error("发生严重错误，未能读取EXCEL文件路径");
			return;
		}
		HashMap<String,File> files = new HashMap<String,File>();
		
		for (File file : folder.listFiles()) {
			//获取文件夹内的文件名称和文件对象
			files.put( file.getName(), file);
		}
		
		
		for (File file : files.values()) {
			String fileName = file.getName();
			
			//当文件扩展名为xls或者xlsx时
			if (fileName.endsWith(".xls")) {
				
				Workbook workbook = null;
				
				try{
					workbook = Workbook.getWorkbook(file);
				}catch(Exception e){					
					System.out.println("EXCEL文件 (" + fileName + ") 处理出错" );
					e.printStackTrace();	
					continue;
				}

				//读取该文件内的所有标签页sheet
				Sheet[] sheets = workbook.getSheets();
				//对所有标签进行遍历
				for (Sheet sheet : sheets) { 
					
					
					List<Map<String,String>> datas = new ArrayList<Map<String,String>>();
					
					//获取标签名
					String sheetName = sheet.getName();
					
					if(sheetNameList.get(sheetName)!=null){
						System.err.println("sheet名为:"+sheetName+"在["+fileName+","+sheetNameList.get(sheetName)+"]里面都存在,请不要有重复的sheet名！");
						System.exit(0);
					}
					sheetNameList.put(sheetName,fileName);
 
					//获得配置表的行数
					int rows = sheet.getRows();
					
					int n = 1;
					//按行进行配置赋值
					int startLine = 0;
					Cell[] rowConfigInfo = sheet.getRow(1);
					List<String> headNames = new ArrayList<String>(); 
					for (Cell cell : rowConfigInfo) {
						if(cell.getContents()!= null && !"".equals(cell.getContents())){
							headNames.add(cell.getContents());
						}
					}
					if(headNames.size()==0) {continue;}
					headNames.remove(0);
					headNames.add(0, "id");
					
					if(rowConfigInfo.length>0&&rowConfigInfo[0].getContents().indexOf("~~~")!=-1){
						startLine = 3;
					}else{
						startLine = 2;
					}
					for (int i = startLine; i < rows; i++) {
						Cell[] row = sheet.getRow(i);
						Map<String, String> beanMap = new HashMap<String, String>();
						if(row.length==0||row[0]==null ||"".equals(row[0].getContents())||row[0].getContents() == null){
							continue;
						}
						
						for (int j = 0; j < row.length; j++) {
							Cell cell = row[j];
							if(headNames.size()>j){
								beanMap.put(headNames.get(j), cell.getContents());
							}
						}
						datas.add(beanMap);
						n++;
					}
					
					
					
					  try
				        {   
				            File targetFile = new File(fileOutputPath+sheetName+".xml");
				            
				        	DeflaterOutputStream zOutput = new DeflaterOutputStream(new FileOutputStream(fileOutputPath+sheetName+".tpl"));
				        	FileOutputStream fOutput = new FileOutputStream(targetFile); 
				        	
				            VelocityContext context = new VelocityContext();
				            context.put("datas", datas);
				            
				            BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(zOutput));
				            BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(fOutput));

				            if ( xmlTemplate != null){
				            	xmlTemplate.merge(context, writer1);
					            xmlTemplate.merge(context, writer2);
				            }
				            	
				            
				            writer1.flush();
				            writer2.flush();
				            zOutput.flush();
				            
				            writer1.close();
				            writer2.close();
							zOutput.close();
				        }
				        catch( Exception e )
				        {
				            System.out.println(e);
				        }
					  	
					  	
//						DeflaterOutputStream zOutput = new DeflaterOutputStream(new FileOutputStream("testbak.gz"));
						
//						int len = input.available();
//						byte[] datas = new byte[len];
//						input.read(datas); 
//						zOutput.write(datas);
//						input.close();
						
						
					  	
//					  	生成json文件
//					    String dataStr = JSONArray.toJSONString(datas);
//					  	byte[] bytes = dataStr.getBytes("utf-8");
//					  	File jsonFile = new File(fileOutputPath+sheetName+"bak.json");
//					  	OutputStream  output = new FileOutputStream(jsonFile);
//					  	output.write(bytes);
//					  	output.flush();
//					  	output.close();
						} 
				} 
		}
	}
	 
}
