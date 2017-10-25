package com.message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
/**
 * 
 * @author wyan
 *
 */
public class MessageJavaBeanGenerator {

	public static void generateServer(String projectPath,Message msg,Template template){
        try
        {
            String beanPackage = msg.getPackageServer();

            String path = "src/"+beanPackage;
            String className = msg.getClzName();
            String messageCode = msg.getCodeName();
            path = path.replaceAll("\\.", "\\/");       
            
            File folder = new File(projectPath+path);
            if (!folder.exists()){
            	folder.mkdirs();
            }

            File file = new File(projectPath+path+"/"+ className+".java");

            VelocityContext context = new VelocityContext();
            
            context.put("ctlName",msg.getCtlName());
            context.put("package", beanPackage);
            context.put("className",className);        
            context.put("messageCode", messageCode);
            context.put("propList", msg.getPropList());
            context.put("importList", msg.getImportServer());
            context.put("ctlImport", msg.getCtlServerImport());
            context.put("comment", msg.getComment());
            context.put("method", msg.getMethod());

            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file)));

            if ( template != null)
                template.merge(context, writer);

            writer.flush();
            writer.close();
        }
        catch( Exception e )
        {
            System.out.println(e);
        }
	}
	
	public static void generateBattle(String projectPath,Message msg,Template template){
        try
        {
            String beanPackage = msg.getPackageClient();

            String path = "src/"+beanPackage;
            String className = msg.getClzName();
            String messageCode = msg.getCodeName();
            path = path.replaceAll("\\.", "\\/");       
            
            File folder = new File(projectPath+path);
            if (!folder.exists()){
            	folder.mkdirs();
            }

            File file = new File(projectPath+path+"/"+ className+".java");

            VelocityContext context = new VelocityContext();
            
            String ctlName = msg.getCtlName();
            ctlName = ctlName.replace("TransitController", "Controller");
            
            String battleCtlImport = msg.getCtlClientImport();
            battleCtlImport = battleCtlImport.replace("TransitController", "Controller");
            
            context.put("ctlName",ctlName);
            context.put("package", beanPackage);
            context.put("className",className);        
            context.put("messageCode", messageCode);
            context.put("propList", msg.getPropList());
            context.put("importList", msg.getImportClient());
            context.put("ctlImport", battleCtlImport);
            context.put("comment", msg.getComment());
            context.put("method", msg.getMethod());

            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file)));

            if ( template != null)
                template.merge(context, writer);

            writer.flush();
            writer.close();
        }
        catch( Exception e )
        {
            System.out.println(e);
        }
	}
	 
	

	public static void generateJavaVO(String projectPath, Message msg, Template voJavaTemplate) {
		
		Map<String,List<BeanProp>> classMap = new HashMap<String, List<BeanProp>>();
		
		List<BeanProp> beanProps = msg.getPropList();
		
		
		getBeanProps(classMap, beanProps);
		
			
			
		
		
			for (Entry<String, List<BeanProp>> entry : classMap.entrySet()) {
				 
				 String packagename = getServerPackageName(msg, entry.getKey());
				 if(packagename == null)continue;
				 int splitIndex = packagename.lastIndexOf(".");
				 String beanPackage = packagename.substring(0,splitIndex); 
				 String className =  packagename.substring(splitIndex+1);
				  
		  try
	        {
	         

	            String path = "src/"+beanPackage;
	          
	            path = path.replaceAll("\\.", "\\/");       
	            
	            File folder = new File(projectPath+path);
	            if (!folder.exists()){
	            	folder.mkdirs();
	            }

	            File file = new File(projectPath+path+"/"+ className+".java");

	            VelocityContext context = new VelocityContext();
	             
	            context.put("package", beanPackage);
	            context.put("className",className);       
	            
	            context.put("propList", entry.getValue()); 
	            context.put("comment", msg.getComment()); 

	            BufferedWriter writer = new BufferedWriter(
	                new OutputStreamWriter(new FileOutputStream(file)));

	            if ( voJavaTemplate != null)
	            	voJavaTemplate.merge(context, writer);

	            writer.flush();
	            writer.close();
	        }
	        catch( Exception e )
	        {
	            System.out.println(e);
	        }
		  
			}
	}

	public static void getBeanProps(Map<String, List<BeanProp>> classMap, List<BeanProp> beanProps) {
		for (BeanProp beanProp : beanProps) {
			if(beanProp.getObject().size() < 0){
				continue;
			}else{
			
			List<BeanProp>  voPropList  =	classMap.get(beanProp.getClz());
				if(voPropList == null){
					voPropList = beanProp.getObject();
					classMap.put(beanProp.getClz(), voPropList);
				}else{
					List<BeanProp> newProps = beanProp.getObject();
					
					//检查新的里面是否已经在old中存在
					for (BeanProp newProp : newProps) {
						boolean flag = true;
						for (BeanProp oldProp : voPropList) {
							if(newProp.getName().equals(oldProp.getName())){
								flag = false;
								break;
							}
						}
						if(flag){
							voPropList.add(newProp);
						}
					}
				}
				
				List<BeanProp> beanPropsTemp =	beanProp.getObject(); 
				getBeanProps(classMap, beanPropsTemp);
			}
			}
	}
	
	public static String getServerPackageName(Message message,String className){
		for (String element : message.getImportServer()) {
			if(element.contains(className)){
				return element;
			}
		}
		return null;
	}
	 


	

}
