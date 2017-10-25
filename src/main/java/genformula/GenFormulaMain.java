package genformula;

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

/**
 * 读取游戏数值配置
 */
public class GenFormulaMain { 
	
	String excelPath ="";
	String fileOutputPath = "";
	
	HashMap<String,String> sheetNameList = new HashMap<String,String>();
	
	public static void main(String[] args) {
		new GenFormulaMain();
	}
	
	public GenFormulaMain() {
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
		 ResourceBundle rb = ResourceBundle.getBundle("genformula.xlsTozlib");
		 excelPath = new String(rb.getString("EXCEL_FILE_PATH").getBytes("ISO-8859-1"),"utf-8"); 
		 System.out.println("excel文件目录：\t"+excelPath);
		 fileOutputPath = new String(rb.getString("FILE_OUTPUT_PATH").getBytes("ISO-8859-1"),"utf-8");
		 System.out.println("输出文件目录：\t"+fileOutputPath);
	}

	public void load() throws Exception {
		Template	xmlTemplate = Velocity.getTemplate("src/template/formula_java.vm","gbk");
		
		//获取当前运行路径
		String path = excelPath; 
		
		if (!path.endsWith(".xls")) {
			return ;
		}
		File file = new File(path); 
			String fileName = file.getName();
			
			//当文件扩展名为xls或者xlsx时
			 
				Workbook workbook = null;
				
				try{
					workbook = Workbook.getWorkbook(file);
				}catch(Exception e){					
					System.out.println("EXCEL文件 (" + fileName + ") 处理出错" );
					e.printStackTrace();
				}

				//读取该文件内的所有标签页sheet
				Sheet[] sheets = workbook.getSheets();
				//对所有标签进行遍历
//				for (Sheet sheet : sheets) {
				if(sheets.length<=0)return;
				Sheet sheet = sheets[0];
					
					List<Map<String,String>> datas = new ArrayList<Map<String,String>>();
					 
 
					//获得配置表的行数
					int rows = sheet.getRows();
					 
					//按行进行配置赋值
					int startLine = 0;
					Cell[] rowConfigInfo = sheet.getRow(1);
					List<String> headNames = new ArrayList<String>(); 					
					for (Cell cell : rowConfigInfo) {
						headNames.add(cell.getContents());
					}
					
					if(rowConfigInfo.length>0&&rowConfigInfo[0].getContents().indexOf("~~~")!=-1){
						startLine = 3;
					}else{
						startLine = 2;
					}
					for (int i = startLine; i < rows; i++) {
						Cell[] row = sheet.getRow(i);
						Map<String, String> beanMap = new HashMap<String, String>();
						for (int j = 0; j < row.length; j++) {
							Cell cell = row[j];
							if(headNames.size()>j){
								beanMap.put(headNames.get(j), cell.getContents());
							}
						}
						datas.add(beanMap);
						 
					}
					
					  try
				        {
				            File targetFile = new File(fileOutputPath+"FormulaUtil.java");
				        	FileOutputStream fOutput = new FileOutputStream(targetFile); 
				            VelocityContext context = new VelocityContext();
				            List<Data> functionDatas = FormulaUtil.getDatas(datas);
				            context.put("functionDatas", functionDatas);
				            
				            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fOutput,"utf-8"));

				            if ( xmlTemplate != null){ 
					            xmlTemplate.merge(context, writer);
				            }
				             
				            writer.flush();
				            writer.close(); 
				        }
				        catch( Exception e )
				        {
				            e.printStackTrace();
				        }
	}
	 
}
