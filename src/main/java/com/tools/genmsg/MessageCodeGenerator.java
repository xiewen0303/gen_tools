package com.tools.genmsg;
 
import org.apache.commons.collections.IterableMap;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.tools.genmsg.msgcode.CodeBean;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;


/**
 * 生成code
 * @author xiewen
 */
public class MessageCodeGenerator {

	private static short BEGIN_CODE = 100;

	//协议号常亮定义
	static Map<Short,String> historyCodes = new TreeMap<>();
	//协议名字对应的协议号
	static Map<String,String> codeClazzNames = new HashMap<>();
	//生成协议对象
	static Map<String,String> genMsgObjs = new HashMap<>();

	static{
		loadCodeIdHistory();
	}
	/**
	 * 加载messagecode生成的记录
	 */
	private static void loadCodeIdHistory() {
		FileInputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			File file = new File(GenJavaMsg.PROTO_FILE_PATH+"\\codeIdHistory.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			inputStream = new FileInputStream(file);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
			String lineStr = null;
			while((lineStr = bufferedReader.readLine())!=null){
				String[] data = lineStr.split("=");
				short code = Short.parseShort(data[0]);
				historyCodes.put(code,data[1]);
				BEGIN_CODE = code > BEGIN_CODE ? code : BEGIN_CODE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				bufferedReader.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void refreshCodeIdHistory() {
		FileOutputStream outStream = null;
		BufferedWriter bufferedWriter = null;
		try {
			File file = new File(GenJavaMsg.PROTO_FILE_PATH+"\\codeIdHistory.txt");
			outStream = new FileOutputStream(file);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(outStream,"utf-8"));
			for (Entry<Short,String> entry : historyCodes.entrySet()) {
				bufferedWriter.write(entry.getKey()+"="+entry.getValue()+"\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				bufferedWriter.flush();
				outStream.flush();
				bufferedWriter.close();
				bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Map<String,String> sortCodeClazzNames() {
		Map<String,String> result = new LinkedHashMap<>();
		Iterator<Entry<Short,String>> iterator = historyCodes.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<Short,String> entry  = iterator.next();
			String codeName = entry.getValue();
			String allName = codeClazzNames.get(codeName);
			if(allName == null) {
				System.err.println("codeName is delete"+codeName);
				iterator.remove();
				continue;
			}
			result.put(allName,codeName);
		}
		return result;
	}

	/**
	 * 生成对象用的数据  排序
	 * @return
     */
	public static Map<String,String> sortGenMsgObjs() {
		Map<String,String> result = new LinkedHashMap<>();

		for (Entry<Short,String> entry : historyCodes.entrySet()) {
			String codeName = entry.getValue();
			String allName = genMsgObjs.get(codeName);
			if(allName != null) {
				result.put(allName,codeName);
			}
		}
		return result;
	}

	public static void generateJavaMessageCode(Template template) {
		try {
			File folder = new File(GenJavaMsg.JAVA_MESSAGECODE_OUT_PATH);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(GenJavaMsg.JAVA_MESSAGECODE_OUT_PATH + "\\" + "MessageCode.java");

			VelocityContext context = new VelocityContext();
			context.put("historyCodes", historyCodes);
			context.put("codeClazzNames", sortCodeClazzNames());
			context.put("genMsgObjs", sortGenMsgObjs());


			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"utf-8"));
			if (template != null) {
				template.merge(context, writer);
			}

			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void AddCodeInfo(CodeBean codeBean){
		String ctx = codeBean.getPackageName() +"."+  codeBean.getJavaClassName()+"$";
		for (String msgName : codeBean.getMessageNames()){
			if(!historyCodes.values().contains(msgName)){
				historyCodes.put(++BEGIN_CODE ,msgName);
			}

			//协议名对应的协议号
			codeClazzNames.put(msgName,ctx + msgName);

			//生成协议对象数据
			if(msgName.contains("CS_")){
				genMsgObjs.put(msgName,codeBean.getJavaClassName()+"."+msgName);
			}
		}
	}


}
