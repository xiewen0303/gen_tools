package com.message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
 


/**
 * 
 * @author wyan
 * 
 */
public class MessageCodeGenerator {
	static final String url = MessageMain.rb.getString("DB_URL");  
    static final String user = MessageMain.rb.getString("DB_USER");  
    static final String pwd = MessageMain.rb.getString("DB_PWD");  
    Connection cn = null;  
    Statement stm = null;  

	public final static String BEAN_PACKAGE_CLIENT = "ghostcoming.protocol";
	public final static String BEAN_PACKAGE_SERVER = "com.benniu.game.message.generated";
	public final static String BEAN_PACKAGE_BATTLE = "com.benniu.game.battle.message.generated";
	public final static String CMD_MODULE_PATH = "config/protertiesfile";
	public final static String MODULE_PATH = "com.benniu.game.message";
	private static Set<MessageIdentified> codeSet = new TreeSet<MessageIdentified>();
	private static List<String> importServerList = new ArrayList<String>();
	private static List<String> importBattleList = new ArrayList<String>();
	private static List<String> importClientList = new ArrayList<String>();
	public static TreeMap<Integer,String> hCodeHistory = new TreeMap<Integer,String>();
	public static Set<MessageIdentified> transitCodeSet = new TreeSet<MessageIdentified>();
	private final static int BASE_TRANSIT_CODE = 100000;
	private static int transitCode = BASE_TRANSIT_CODE + 1;
	static{
	loadCodeIdHistory();
	}

	/**
	 * 加载messagecode生成的记录
	 */
	private static void loadCodeIdHistory() {
//		 try {
////			 String path = MessageCodeGenerator.class.getClassLoader().getResource(AS3Project.PROJECT_PATH+ "codeIdHistory.txt").getPath();			 
////			  InputStream in = MessageCodeGenerator.class.getResourceAsStream("codeIdHistory.txt");			 
////			  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));  			  
//			 File file = new File(AS3Project.PROJECT_PATH+ "codeIdHistory.txt");
//			  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
//			  String str = "";
//			  while((str = br.readLine()) != null){				  
//					  String[] codeHistoryItem = str.split("\t");
//					  hCodeHistory.put(Integer.valueOf(codeHistoryItem[0]), codeHistoryItem[1]);
//			  }
//			  br.close();
//		} catch (Exception e) {		
//			e.printStackTrace();
//		}    
		Connection cn  = getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		 try {			  			 
			 String sql = "select * from codeproto";
			 pstmt = cn.prepareStatement(sql);
			 rs = pstmt.executeQuery();
			 while(rs.next()){
				 int code = rs.getInt("code");
				 String protoClass = rs.getString("protoclass");
				 hCodeHistory.put(code,protoClass);
			 }
		 }catch(Exception e){			
			 try {
				rs.close();
				pstmt.close();
				cn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 e.printStackTrace();
		 }
	}
	
	
	public static void refreshCodeIdHistory() {
		Connection cn  = getConnection();
		PreparedStatement pstmt = null;
		 PreparedStatement pstmt2 = null;
		try{			 
			 String sql2 = "delete from codeproto";
			 String sql = "insert into codeproto(code,protoclass) values";
			 pstmt2 = cn.prepareStatement(sql2);
			 pstmt2.execute();
			 int count = 0;
			 for(Entry<Integer, String> entry:hCodeHistory.entrySet()){
				 sql += "("+entry.getKey()+",'"+entry.getValue()+"')";
				 count ++;
				 if(count!=hCodeHistory.size()){
					 sql += ",";
				 }
			 }
			 System.out.println(sql);
			 pstmt = cn.prepareStatement(sql);
			 pstmt.execute();
		}catch(Exception e){
			try {
				pstmt.close();
				pstmt2.close();
				cn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}			
			e.printStackTrace();
		}
//		 File file = new File(AS3Project.PROJECT_PATH+ "codeIdHistory.txt");
//		//获取当前路径对应的文件夹
//		FileOutputStream fileOut;
//		try {
//			fileOut = new FileOutputStream(file);
//			String content = "";
//			Integer lastKey = hCodeHistory.lastKey();
//			for(Entry<Integer, String> entry:hCodeHistory.entrySet()){
//				content = content + entry.getKey()+ "\t" + entry.getValue();
//				if(entry.getKey()!=lastKey){
//					content +="\r\n";
//				}
//			}
//			fileOut.write(new String(content).getBytes());
//			fileOut.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	
	public static void increaseCode(String messageCode, String clzName, String comment, String ctlName, String codeName,String moduleName) {
		MessageIdentified mi = new MessageIdentified();
		int currentCode = 0;
		if(hCodeHistory.containsValue(codeName)){
			for(Entry<Integer, String> entry:hCodeHistory.entrySet()){
				if(entry.getValue().equals(codeName)){
					currentCode = entry.getKey();
					break;
				}
			}
		}else{
			currentCode = hCodeHistory.lastKey()+1;
			hCodeHistory.put(currentCode, codeName);
		}
		mi.setCodeValue(currentCode);
		mi.setClzName(clzName);
		mi.setCodeKey(messageCode);
		mi.setComment(comment);
		mi.setCtlComment(ctlName);
		mi.setCodeName(codeName);
		mi.setModuleName(moduleName);
		codeSet.add(mi);
	}
	
	public static void increaseTransitCode(String messageCode, String clzName, String comment, String ctlName, String codeName) {
		MessageIdentified mi = new MessageIdentified();
		mi.setCodeValue(transitCode++);
		mi.setClzName(clzName);
		mi.setCodeKey(messageCode);
		mi.setComment(comment);
		mi.setCtlComment(ctlName);
		mi.setCodeName(codeName);
		transitCodeSet.add(mi);
	}
	
		
	
	public static void addServerImport(String ipt) {
		importServerList.add(ipt);
	}
	public static void addBattleImport(String ipt) {
		importBattleList.add(ipt);
	}
	public static void addClientImport(String ipt){
		importClientList.add(ipt);
	}

	public static void generateJavaServer(String projectPath, Template template) {
		try {
			String path = "src/" + BEAN_PACKAGE_SERVER;
			path = path.replaceAll("\\.", "\\/");

			File folder = new File(projectPath + path);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(projectPath + path + "/" + "MessageCode.java");

			VelocityContext context = new VelocityContext();

			Set<MessageIdentified> tempSet = new TreeSet<MessageIdentified>();
			tempSet.addAll(codeSet);
			tempSet.addAll(transitCodeSet);
			
			context.put("package", BEAN_PACKAGE_SERVER);
			context.put("codeSet", tempSet);
			context.put("importList", importServerList);

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));

			if (template != null)
				template.merge(context, writer);

			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void generateJavaBattle(String projectPath, Template template) {
		try {
			String path = "src/" + BEAN_PACKAGE_BATTLE;
			path = path.replaceAll("\\.", "\\/");

			File folder = new File(projectPath + path);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(projectPath + path + "/" + "MessageCode.java");

			VelocityContext context = new VelocityContext();

			context.put("package", BEAN_PACKAGE_BATTLE);
			context.put("codeSet", transitCodeSet);
			context.put("importList", importBattleList);

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));

			if (template != null)
				template.merge(context, writer);

			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateAS3(String projectPath, Template template) {
		try {
			String path ="src/"+BEAN_PACKAGE_CLIENT;
			path = path.replaceAll("\\.", "\\/");

			File folder = new File(projectPath + path);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(projectPath + path + "/" + "MessageCode.as");

			VelocityContext context = new VelocityContext();

			context.put("package", BEAN_PACKAGE_CLIENT);
			context.put("codeSet", codeSet);
			context.put("importList", importClientList);

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));

			if (template != null)
				template.merge(context, writer);

			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	public static void generateAS3DOC(String projectPath){
		ArrayList<ArrayList<String>> gridData = new ArrayList<ArrayList<String>> ();
		for (MessageIdentified mi : codeSet){
			ArrayList<String> row = new ArrayList<String>();
			row.add(""+mi.getCodeValue());
			row.add(mi.getCodeKey());
			row.add(mi.getClzName());
			row.add(mi.getModuleName());
			row.add(mi.getComment());
			row.add(mi.getCtlComment());
			gridData.add(row);
		}
		String path = BEAN_PACKAGE_CLIENT;
		path = path.replaceAll("\\.", "\\/");

		ExcelReport.export(projectPath  + "MessageCode.xls", gridData);
	}
	public static void generateAS3DOC(String projectPath, Template template) {
		try {
			String path = BEAN_PACKAGE_CLIENT;
			path = path.replaceAll("\\.", "\\/");

			File folder = new File(projectPath + path);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(projectPath + path + "/" + "MessageCode.txt");

			VelocityContext context = new VelocityContext();

			context.put("codeSet", codeSet);


			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file),"UTF-8"));

			if (template != null)
				template.merge(context, writer);

			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	public static Connection getConnection(){
		Connection cn = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");  
			cn =  DriverManager.getConnection(url, user, pwd); 
		}catch(Exception e){
			e.printStackTrace();			
		}
       return cn;
	}
	public static void main(String[] args) {
		Connection cn  = getConnection();
		 try {			  
			 PreparedStatement pstmt = null;  

			 File file = new File("E:\\Message\\codeIdHistory.txt");

			  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			  String str = "";
			  while((str = br.readLine()) != null){				  
					  String[] c = str.split("\t");
					  Integer code = Integer.parseInt(c[0]);
					  String classs = c[1];
					  String sql = "insert into codeproto(code,protoclass) values(?,?)";
					  pstmt = cn.prepareStatement(sql);  
			          pstmt.setInt(1,code);
			          pstmt.setString(2,classs);
			          pstmt.execute();
			  }
			  br.close();
		} catch (Exception e) {		
			e.printStackTrace();
		}    
	}

	public static void  generateCmdParserModuel(String projectPath, Template template) {
		try {
			String path = "src/" + CMD_MODULE_PATH;
			path = path.replaceAll("\\.", "\\/");

			File folder = new File(projectPath + path);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(projectPath + path + "/" + "cmdmodulename.properties");

			VelocityContext context = new VelocityContext();

			Set<MessageIdentified> tempSet = new TreeSet<MessageIdentified>();
			tempSet.addAll(codeSet);
			tempSet.addAll(transitCodeSet);
			
			context.put("codeSet", tempSet);
		 

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));

			if (template != null)
				template.merge(context, writer);

			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void generateParserModuel(String projectPath, Template template) {
		
		try {
			String path = "src/" + MODULE_PATH;
			path = path.replaceAll("\\.", "\\/");

			File folder = new File(projectPath + path);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(projectPath + path + "/" + "ModuleName.java");

			VelocityContext context = new VelocityContext();
			
			Set<ModuleInfo> datas = new HashSet<>();
			for (MessageIdentified messageIdentified : codeSet) {
				ModuleInfo moduleInfo = new ModuleInfo();
				moduleInfo.setModule(messageIdentified.getModuleName().toUpperCase());
				moduleInfo.setModuleValue(messageIdentified.getModuleName());
				moduleInfo.setDesc(messageIdentified.getComment());
				datas.add(moduleInfo);
			}
			
			context.put("datas", datas);
		 

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));

			if (template != null)
				template.merge(context, writer);

			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
