package com.message;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * 
 * @author wyan
 * 
 */
public class MessageGeneratorWrapper {
 
	public final static String sc_bean_java = "src/template/sc_bean_java.vm";
	public final static String cs_bean_java = "src/template/cs_bean_java.vm";
	public final static String sc_bean_as = "src/template/sc_bean_as_simple.vm";
	public final static String cs_bean_as = "src/template/cs_bean_as_simple.vm";
	
	/**
	 * 另一套as的协议写法
	 */
	public final static String CS_AS_NEW_TEMPLATE="src/template/cs_bean_as_simple_new.vm"; 
	public static final String SC_AS_NEW_TEMPLATE = "src/template/sc_bean_as_simple_new.vm";

	public final static String message_code_java_server = "src/template/message_code_java_server.vm";
	public final static String message_code_java_battle = "src/template/message_code_java_battle.vm";
	public final static String message_code_as = "src/template/message_code_as.vm";
	
	public final static String doc_message_code_as = "src/template/doc_message_code_as.vm";

	public final static String constant_java = "src/template/constant_java.vm";
	public final static String constant_as = "src/template/constant_as.vm";
	

	
	/*
	 * 跨服战役连接到GAME_WORLD,建立session
	 */
	public final static String bs_connection_java_battle = "src/template/bs_connection_java_battle.vm";
	public final static String bs_connection_java_server = "src/template/bs_connection_java_server.vm";
	/*
	 * 跨服战役协议
	 */
	public final static String bs_bean_java_battle = "src/template/bs_bean_java_battle.vm";
	public final static String sb_bean_java_battle = "src/template/sb_bean_java_battle.vm";
	public final static String bs_bean_java_server = "src/template/bs_bean_java_server.vm";
	public final static String sb_bean_java_server = "src/template/sb_bean_java_server.vm";
	
	/**
	 * 协议模块模板
	 */
	public final static String cmd_module_name="src/template/cmd_module_name.vm";
	public final static String module_name="src/template/module_name.vm";
	
	/**
	 * vo生成模板 
	 */
	public final static String VO_JAVA_TEMPLATE_VM="src/template/vo_java.vm";
	public final static String VO_AS_TEMPLATE_VM="src/template/vo_as.vm";
	private static final String AS_SERVICE_INTERFACE_VN = "src/template/as_service_interface.vm";
	

	private Template scJavaTemplate;
	private Template csJavaTemplate;
	private Template scASTemplate;
	private Template csASTemplate;
	private Template csASNewTemplate;
	private Template scASNewTemplate;
	
//	/game_message/src/template/cs_bean_as_simple_new.vm

	private String projectPath;

	private String AS3ProjectPath = AS3Project.PROJECT_PATH;
	private String AS3_CLEAR_PATH = "src/ghostcoming/protocol";
	
	private String battlePath;

	private Template javaMessageCodeServerTemplate;
	private Template javaMessageCodeBattleTemplate;
	private Template asMessageCodeTemplate;
	//private Template asMessageCodeXLSTemplate;
	

	private Template javaConstantTemplate;
	private Template asConstantTemplate;
	
	/*
	 * 跨服战役连接到GAME_WORLD,建立session
	 */
	private Template transitBattleConnectionTemplate; //发起
	private Template transitServerConnectionTemplate; //接收
	/*
	 * 跨服战役协议
	 */
	private Template bsJavaTemplateBattle;
	private Template sbJavaTemplateBattle;
	private Template bsJavaTemplateServer;
	private Template sbJavaTemplateServer;
	
	/**
	 * 协议对应模块的模板
	 */
	private Template cmdModuleNameTemplate; 
	private Template moduleNameTemplate;
	
	/**
	 * vo模板
	 */
	private Template  voJavaTemplate;
	private Template  voAsTemplate;
	/**
	 * 新as模板接口生成
	 */
	private Template asServiceTemplate;
	
	public void readTemplate() {
		try {
			scJavaTemplate = Velocity.getTemplate(sc_bean_java);
			csJavaTemplate = Velocity.getTemplate(cs_bean_java);
			
			scASNewTemplate = Velocity.getTemplate(SC_AS_NEW_TEMPLATE);
			csASNewTemplate = Velocity.getTemplate(CS_AS_NEW_TEMPLATE);
			
//			scASTemplate = Velocity.getTemplate(sc_bean_as);
			csASTemplate = Velocity.getTemplate(cs_bean_as);

			javaMessageCodeServerTemplate = Velocity.getTemplate(message_code_java_server);
//			javaMessageCodeBattleTemplate = Velocity.getTemplate(message_code_java_battle);
			asMessageCodeTemplate = Velocity.getTemplate(message_code_as);
			//asMessageCodeXLSTemplate = Velocity.getTemplate(doc_message_code_as);
//
			javaConstantTemplate = Velocity.getTemplate(constant_java);
			asConstantTemplate = Velocity.getTemplate(constant_as);
			cmdModuleNameTemplate = Velocity.getTemplate(cmd_module_name);
			moduleNameTemplate = Velocity.getTemplate(module_name);
			
			
//			
			voJavaTemplate = Velocity.getTemplate(VO_JAVA_TEMPLATE_VM);
			voAsTemplate = Velocity.getTemplate(VO_AS_TEMPLATE_VM);
			
			asServiceTemplate = Velocity.getTemplate(AS_SERVICE_INTERFACE_VN);
			
//			

			
//			/*
//			 * 跨服战役连接到GAME_WORLD,建立session
//			 */
//			transitBattleConnectionTemplate = Velocity.getTemplate(bs_connection_java_battle);
//			transitServerConnectionTemplate = Velocity.getTemplate(bs_connection_java_server);
//			/*
//			 * 跨服战役协议
//			 */
//			bsJavaTemplateBattle = Velocity.getTemplate(bs_bean_java_battle);
//			sbJavaTemplateBattle = Velocity.getTemplate(sb_bean_java_battle);
//			bsJavaTemplateServer = Velocity.getTemplate(bs_bean_java_server);
//			sbJavaTemplateServer = Velocity.getTemplate(sb_bean_java_server);
			
			
		} catch (ResourceNotFoundException rnfe) {
			System.out
					.println("Example : error : cannot find template " + rnfe);
		} catch (ParseErrorException pee) {
			System.out.println("Example : Syntax error in template :" + pee);
		}
	}

	public void generate() throws Exception {
		MessageConfigParser p = new MessageConfigParser();
		p.parse("config/message/");
		List<Controller> ctlList = p.getControllerlist();
		List<TransitController> tcList = p.getTcList();
//		List<Connection> connList = p.getConnList();
		clearOldASFiles();

		for (Controller ctl : ctlList) {

			clearOldJavaFiles(ctl.getPackageServer());// 清空
		}

		for (Controller ctl : ctlList) {

			for (Message msg : ctl.getMessageList()) {
				
				
			 	
				if (msg.getCodeName().startsWith("SC")) {
					MessageJavaBeanGenerator.generateServer(projectPath, msg, scJavaTemplate);
//					MessageASBeanGenerator.generate(AS3ProjectPath, msg, scASTemplate);
				} else if (msg.getCodeName().startsWith("CS")) {
					MessageJavaBeanGenerator.generateServer(projectPath, msg, csJavaTemplate);
//					MessageASBeanGenerator.generate(AS3ProjectPath, msg, csASTemplate);
				} 
				
				MessageJavaBeanGenerator.generateJavaVO(projectPath, msg, voJavaTemplate);
			 	MessageASBeanGenerator.generateAsVO(AS3ProjectPath, msg, voAsTemplate);
			}
			
			
			
			/**
			 * 生成client cs代码
			 */
			ClientGenData  csDatas = new ClientGenData();
			csDatas.setClassName(ctl.getClzName().substring(0,ctl.getClzName().indexOf("Ctl"))); 
			csDatas.setPackageClient(ctl.getPackageClient());
			Set<String> importList = new HashSet<String>();
			csDatas.setImportList(importList);
			List<Message> messages = ctl.getMessageList();
			for (Message message : messages) {
				if(message.getCodeName().startsWith("CS")){
					ClientMessage clientMessge = new ClientMessage();
					clientMessge.setMessageCode(message.getCodeName());
					String cmdNameStr = message.getClzName();
					char indexName = cmdNameStr.charAt(2);
					String cmdName = Character.toLowerCase(indexName)+cmdNameStr.substring(3);
					clientMessge.setCmdName(cmdName);
					clientMessge.setComment(message.getComment());
					clientMessge.setPropList(message.getPropList());
					MessageASBeanGenerator.convertAS3ArrayFormat(message.getPropList());
					
					csDatas.addCsMessage(clientMessge);
					importList.addAll(message.getImportClient());
				} 
			} 
			MessageASBeanGenerator.generateClientCS(AS3ProjectPath, csDatas, csASNewTemplate);
			
			
			/**
			 * 生成client sc代码
			 */
			ClientGenData  scDatas = new ClientGenData();
			scDatas.setClassName(ctl.getClzName().substring(0,ctl.getClzName().indexOf("Ctl"))); 
			scDatas.setPackageClient(ctl.getPackageClient());
			Set<String> scImportList = new HashSet<String>();
			scDatas.setImportList(scImportList);
			List<Message> scMessages = ctl.getMessageList();
			for (Message message : scMessages) {
				if(message.getCodeName().startsWith("SC")){
					ClientMessage clientMessge = new ClientMessage();
					clientMessge.setMessageCode(message.getCodeName());
					String cmdNameStr = message.getClzName();
					char indexName = cmdNameStr.charAt(2);
					String cmdName = Character.toLowerCase(indexName)+cmdNameStr.substring(3);
					clientMessge.setCmdName(cmdName);
					clientMessge.setComment(message.getComment());
					clientMessge.setPropList(message.getPropList());
					MessageASBeanGenerator.convertAS3ArrayFormat(message.getPropList());
					
					scDatas.addCsMessage(clientMessge);
					scImportList.addAll(message.getImportClient());
				} 
			} 
			MessageASBeanGenerator.generateClientSC(AS3ProjectPath, scDatas, scASNewTemplate);
			
			 
			//as接口service生成
			MessageASBeanGenerator.generateAS3Interface(AS3ProjectPath, scDatas,asServiceTemplate);
			
			

			for (Constant cst : ctl.getConstantList()) {
				ConstantGenerator.generateJavaServer(projectPath, cst, javaConstantTemplate);
				ConstantGenerator.generateAS3(AS3ProjectPath, cst,asConstantTemplate);
			}
		}
		
//		for (Connection conn : connList){
//			for (Message msg : conn.getMessageList()) {
//
//				if (msg.getCodeName().startsWith("BS")) {
//					MessageJavaBeanGenerator.generateServer(projectPath, msg,
//							transitServerConnectionTemplate);
//					MessageJavaBeanGenerator.generateBattle(battlePath, msg,
//							transitBattleConnectionTemplate);
//				}
//
//			}
//		}
		
		
		for (TransitController controller : tcList){
			/*
			 * FIMXE clear
			 */
		}
		
//		for (TransitController ctl : tcList){
//			for (Message msg : ctl.getMessageList()) {
//
//				if (msg.getCodeName().startsWith("SB")) {
//					MessageJavaBeanGenerator.generateServer(projectPath, msg,
//							sbJavaTemplateServer);
//					MessageJavaBeanGenerator.generateBattle(battlePath, msg,
//							sbJavaTemplateBattle);
//				} else if (msg.getCodeName().startsWith("BS")) {
//					MessageJavaBeanGenerator.generateServer(projectPath, msg,
//							bsJavaTemplateServer);
//					MessageJavaBeanGenerator.generateBattle(battlePath, msg,
//							bsJavaTemplateBattle);
//				}
//
//			}
//
//			for (Constant cst : ctl.getConstantList()) {
//				ConstantGenerator.generateJavaServer(projectPath, cst,
//						javaConstantTemplate);
//				ConstantGenerator.generateJavaBattle(battlePath, cst,
//						javaConstantTemplate);
//			}
//		}
		
		/*
		 * FIXME ....
		 */

		MessageCodeGenerator.generateJavaServer(projectPath, javaMessageCodeServerTemplate);
//		MessageCodeGenerator.generateJavaBattle(battlePath, javaMessageCodeBattleTemplate);
		MessageCodeGenerator.generateAS3(AS3ProjectPath, asMessageCodeTemplate);
		MessageCodeGenerator.generateAS3DOC(AS3ProjectPath);
		
		MessageCodeGenerator.generateCmdParserModuel(projectPath,cmdModuleNameTemplate);
		MessageCodeGenerator.generateParserModuel(projectPath,moduleNameTemplate);
	}

	private void clearOldASFiles() throws Exception {
		String path = AS3ProjectPath + AS3_CLEAR_PATH;
		File folder = new File(path);
		if (folder.exists()) {
			if (folder.isDirectory()) {
				for (File file : folder.listFiles()){
					if (file.isDirectory())
						deleteAllFile(file);
				}
			}
		}

		System.out.println(path + " cleared!");
	}

	private void clearOldJavaFiles(String pkgPath) throws Exception {
		String path = projectPath + "src/" + pkgPath + "/message";
		path = path.replaceAll("\\.", "\\/");
		File folder = new File(path);
		if (folder.exists()) {
			deleteAllFile(folder);
		}

		System.out.println(path + " cleared!");
	}

	private void deleteAllFile(File folder) {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				deleteAllFile(file);
				file.delete();
			} else {
				file.delete();
			}
		}
	}
	
	public MessageGeneratorWrapper(String projectPath) throws Exception {
		this.projectPath = projectPath;
//		this.battlePath = battlePath;
		this.readTemplate();
		this.generate();
	}

	public MessageGeneratorWrapper(String projectPath, String battlePath) throws Exception {
		this.projectPath = projectPath;
		this.battlePath = battlePath;
		this.readTemplate();
		this.generate();
	}

}
