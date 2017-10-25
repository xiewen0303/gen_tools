package com.message;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * 
 * @author wyan
 * 
 */
public class MessageConfigParser {

	private final List<Controller> ctlList = new ArrayList<Controller>();
	private final List<TransitController> tcList = new ArrayList<TransitController>();
	private final List<Connection> connList = new ArrayList<Connection>(); 
	
	private static int time = 0;

	@SuppressWarnings("unchecked")
	public void parse(File xmlFile) throws Exception {

		SAXBuilder builder = new SAXBuilder(false);
		Document doc = builder.build(xmlFile);

		Element controllers = doc.getRootElement();

		List<Element> controllerEList = controllers.getChildren("controller");
		for (Element controller : controllerEList) {

			Controller ctl = new Controller();
			ctlList.add(ctl);

			String pkgServer = controller.getAttributeValue("packageServer");
			String pkgClient = controller.getAttributeValue("packageClient");
			String moduleName = controller.getAttributeValue("moduleName");
			
			int point = pkgClient.lastIndexOf(".");
			String pkgClientPre = pkgClient.substring(0, point+1);
			String pkgClientSuf = pkgClient.substring(point,pkgClient.length());
			
		
			
			ctl.setClzName(controller.getAttributeValue("type"));
			ctl.setPackageServer(pkgServer);
			ctl.setPackageClient(pkgClient);
			ctl.setComment(controller.getAttributeValue("comment"));
			ctl.setModuleName(moduleName);
			
			List<Element> messages = controller.getChildren("message");
			for (Element message : messages) {
//				String id = message.getAttributeValue("id");
//				codeModules.put(id, moduleName);
				
				Message msg = new Message();
				ctl.addMessage(msg);
				msg.setCtlName(ctl.getClzName());
				msg.setClzName(message.getAttributeValue("type"));
				msg.setCodeName(message.getAttributeValue("id"));
				msg.setPackageServer(pkgServer.concat(".message"));
				
				msg.setPackageClient(pkgClient);
				//msg.setPackageClient(pkgClientPre+"message"+pkgClientSuf);
				
				msg.setComment(message.getAttributeValue("comment"));
				msg.setCtlServerImport(ctl.getPackageServer() + "." + ctl.getClzName());
				msg.setCtlClientImport(ctl.getPackageClient() + "." + ctl.getClzName());


				msg.setMethod(message.getAttributeValue("method"));
				time = 0;
				msg.setPropList(createObjectProps(message.getChildren(), msg));
				MessageCodeGenerator.increaseCode(msg.getCodeName(),msg.getClzName(),msg.getComment(),ctl.getComment(),msg.getCodeName(),ctl.getModuleName());
				MessageCodeGenerator.addServerImport(msg.getPackageServer().concat(".").concat(msg.getClzName()));
				MessageCodeGenerator.addClientImport(msg.getPackageClient().concat(".").concat(msg.getClzName()));
			}
			List<Element> constants = controller.getChildren("constant");
			for (Element constant : constants) {
				Constant cst = new Constant();
				ctl.addConstant(cst);// add to controller
				cst.setClzName(constant.getAttributeValue("type"));
				cst.setComment(constant.getAttributeValue("comment"));
				cst.setPkgServer(pkgServer.concat(".constant"));
				cst.setPkgClient(pkgClientPre.concat("constant").concat(pkgClientSuf));
				List<Element> constantProps = constant.getChildren("property");
				for (Element propE : constantProps) {
					ConstantProp prop = new ConstantProp();
					cst.addProp(prop);
					prop.setId(propE.getAttributeValue("id"));
					prop.setType(propE.getAttributeValue("type"));
					prop.setValue(propE.getAttributeValue("value"));
					prop.setComment(propE.getAttributeValue("comment"));
				}
			}
		}
		
		/*
		 * connection
		 */
		List<Element> connectionEList = controllers.getChildren("connection");
		if (connectionEList == null)
			return;
		for (Element connection : connectionEList) {

			Connection conn = new Connection();
			connList.add(conn);

			String pkgServer = connection.getAttributeValue("packageServer");
			String pkgClient = connection.getAttributeValue("packageClient");
			

			conn.setClzName(connection.getAttributeValue("type"));
			conn.setPackageServer(pkgServer);
			conn.setPackageClient(pkgClient);
			conn.setComment(connection.getAttributeValue("comment"));

			List<Element> messages = connection.getChildren("message");
			for (Element message : messages) {

				Message msg = new Message();
				conn.addMessage(msg);
				msg.setCtlName(conn.getClzName());

				msg.setClzName(message.getAttributeValue("type"));
				msg.setCodeName(message.getAttributeValue("id"));
				msg.setPackageServer(pkgServer.concat(".message"));
				msg.setPackageClient(pkgClient.concat(".message"));
				
				//msg.setPackageClient(pkgClientPre+"message"+pkgClientSuf);
				
				msg.setComment(message.getAttributeValue("comment"));
				msg.setCtlServerImport(conn.getPackageServer() + "." + conn.getClzName());
				msg.setCtlClientImport(conn.getPackageClient() + "." + conn.getClzName());


				msg.setMethod(message.getAttributeValue("method"));
				time = 0;
				msg.setPropList(createObjectProps(message.getChildren(), msg));
				MessageCodeGenerator.increaseTransitCode(msg.getCodeName(),
						msg.getClzName(),msg.getComment(),conn.getComment(),msg.getCodeName());
				MessageCodeGenerator.addServerImport(msg.getPackageServer().concat(".")
						.concat(msg.getClzName()));
				MessageCodeGenerator.addBattleImport(msg.getPackageClient().concat(".")
						.concat(msg.getClzName()));
			}

		}
		/*
		 * transitController 跨服战役服务器
		 */
		List<Element> transitControllerEList = controllers.getChildren("transitController");
		if (transitControllerEList == null)
			return;
		for (Element controller : transitControllerEList) {

			TransitController ctl = new TransitController();
			tcList.add(ctl);

			String pkgServer = controller.getAttributeValue("packageServer");
			String pkgClient = controller.getAttributeValue("packageClient");
			

			ctl.setClzName(controller.getAttributeValue("type"));
			ctl.setPackageServer(pkgServer);
			ctl.setPackageClient(pkgClient);
			ctl.setComment(controller.getAttributeValue("comment"));

			List<Element> messages = controller.getChildren("message");
			for (Element message : messages) {

				Message msg = new Message();
				ctl.addMessage(msg);
				msg.setCtlName(ctl.getClzName());

				msg.setClzName(message.getAttributeValue("type"));
				msg.setCodeName(message.getAttributeValue("id"));
				msg.setPackageServer(pkgServer.concat(".message"));
				msg.setPackageClient(pkgClient.concat(".message"));
				
				//msg.setPackageClient(pkgClientPre+"message"+pkgClientSuf);
				
				msg.setComment(message.getAttributeValue("comment"));
				msg.setCtlServerImport(ctl.getPackageServer() + "." + ctl.getClzName());
				msg.setCtlClientImport(ctl.getPackageClient() + "." + ctl.getClzName());


				msg.setMethod(message.getAttributeValue("method"));
				time = 0;
				msg.setPropList(createObjectProps(message.getChildren(), msg));
				MessageCodeGenerator.increaseTransitCode(msg.getCodeName(),
						msg.getClzName(),msg.getComment(),ctl.getComment(),msg.getCodeName());
				MessageCodeGenerator.addServerImport(msg.getPackageServer().concat(".")
						.concat(msg.getClzName()));
				MessageCodeGenerator.addBattleImport(msg.getPackageClient().concat(".")
						.concat(msg.getClzName()));
				MessageCodeGenerator.addClientImport(msg.getPackageClient().concat(".")
						.concat(msg.getClzName()));
			}
			List<Element> constants = controller.getChildren("constant");
			for (Element constant : constants) {
				Constant cst = new Constant();
				ctl.addConstant(cst);// add to controller
				cst.setClzName(constant.getAttributeValue("type"));
				cst.setComment(constant.getAttributeValue("comment"));
				cst.setPkgServer(pkgServer.concat(".constant"));
				cst.setPkgClient(pkgClient.concat(".constant"));
				List<Element> constantProps = constant.getChildren("property");
				for (Element propE : constantProps) {
					ConstantProp prop = new ConstantProp();
					cst.addProp(prop);
					prop.setId(propE.getAttributeValue("id"));
					prop.setType(propE.getAttributeValue("type"));
					prop.setValue(propE.getAttributeValue("value"));
					prop.setComment(propE.getAttributeValue("comment"));
				}
			}
		} 
	}

	public void parse(String folderPath) throws Exception {

		String path = MessageConfigParser.class.getClassLoader().getResource(folderPath).getPath();
		File folder = new File(path);
		if (folder.isDirectory()) {
			File[] xmlFiles = folder.listFiles();
			for (File xmlFile : xmlFiles) {
				if (xmlFile.getAbsolutePath().contains(".xml")) {
					parse(xmlFile);
				}
			}
		}
		MessageCodeGenerator.refreshCodeIdHistory();
	}

	public List<Controller> getControllerlist() {
		return ctlList;
	}

	public List<TransitController> getTcList() {
		return tcList;
	}

	public List<Connection> getConnList() {
		return connList;
	}

	@SuppressWarnings("unchecked")
	private static List<BeanProp> createObjectProps(
			List<Element> outerObjectElements, Message msg) {
		List<BeanProp> props = new ArrayList<BeanProp>();
		for (Element element : outerObjectElements) {

			String name = element.getName();

			String id = element.getAttributeValue("id");
			String type = element.getAttributeValue("type");
			String comment = element.getAttributeValue("comment");
			String clientType = "";
			boolean flag = false;
			if (type==null)
				System.out.println(msg.getClzName());
			if (type.contains(".")) {
				msg.addImportServer(type);
				
				// 可能在这里修改VO的文件夹
				String ct = element.getAttributeValue("client");
				msg.addImportClient(ct); //和message同一文件夹子
				
				clientType = ct.substring(ct.lastIndexOf(".") + 1);
				
				type = type.substring(type.lastIndexOf(".") + 1);
				
				flag = true;
			}
			BeanProp prop = new BeanProp();
			props.add(prop);
			prop.setChildren(0);
			prop.setName(id);
			if (type.equals("boolean")) {
				prop.setMethod(id.substring(2, id.length()));
			} else {
				prop.setMethod(toMethodName(id));
			}
			prop.setComment(comment);

			if (name.equals("property")) {
				prop.setChildren(0);
				prop.setClz(type);
			} else if (name.equals("array")) {
				time ++;
				if (flag) {
					prop.setChildren(-1);
					prop.setClz(type);
					prop.setClientClz(clientType);
					prop.setObject(createObjectProps(element.getChildren(), msg));
				}else{
					prop.setChildren(-11);
					prop.setClz(type+"[]");
					prop.setClzOrigin(type);
				}
				if (time==1)
					msg.addImportServer("java.util.Arrays");

			} else if (name.equals("object")) {
				prop.setChildren(1);
				prop.setClz(type);
				prop.setClientClz(clientType);
				prop.setObject(createObjectProps(element.getChildren(), msg));
			}

		}
		return props;
	}

	private static String toMethodName(String propName) {
		String p = propName.substring(0, 1);
		String m = propName.replaceFirst(p, p.toUpperCase());
		return m;
	}

}
