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
import java.util.Set;
import java.util.Map.Entry;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
/**
 * 
 * @author wyan
 *
 */
public class MessageASBeanGenerator {

	public static void generate(String projectPath,Message msg,Template template){
        try
        {
        	
            String beanPackage = msg.getPackageClient();
//  
//			beanPackage = beanPackage.replace(".", ":");
//		
//            String[] tempPkgs = beanPackage.split(":");
//            int l = tempPkgs.length;
//            int t = l -2;
//            beanPackage = "";
//            for (int i=0; i<t; i++){
//            	beanPackage += tempPkgs[i]+".";
//            }
//            beanPackage += tempPkgs[l-1]+".";
//            beanPackage += tempPkgs[l-2];
//          

            String path = beanPackage;
            String className = msg.getClzName();
            String messageCode = msg.getCodeName();
            path = path.replaceAll("\\.", "\\/");       
            
            File folder = new File(projectPath+path);
            if (!folder.exists()){
            	folder.mkdirs();
            }

            File file = new File(projectPath+path+"/"+ className+".as");

            VelocityContext context = new VelocityContext();
            
            convertAS3ArrayFormat(msg.getPropList());


            
            
            context.put("ctlName",msg.getCtlName());
            context.put("package", beanPackage);
            context.put("className",className);       
            context.put("method", msg.getMethod());
            context.put("messageCode", messageCode);
            context.put("propList", msg.getPropList());
            context.put("importList", msg.getImportClient());
            context.put("ctlImport", msg.getCtlClientImport());
            context.put("comment", msg.getComment());
            
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
	
	public static void convertAS3ArrayFormat(List<BeanProp> list){
        for (BeanProp prop : list){
        	String clz = prop.getClz();
        	if (prop.getChildren()==PropChildrenConstant.OBJECT_ARRAY){
        		clz = prop.getClientClz();
//        		prop.setClzOrigin("Msg"+clz+"VO");
//        		clz = "Vector.<Msg"+clz+"VO>";
        		prop.setClzOrigin(clz);
        		clz = "Vector.<"+clz+">";
        		prop.setClz(clz);
        	}else if (prop.getChildren()==PropChildrenConstant.SIMPLE_ARRAY){
        		clz = clz.substring(0,clz.length()-2);
        		clz = "Vector.<"+clz+">";
        		prop.setClz(clz);
        	}else if (prop.getChildren()==PropChildrenConstant.OBJECT){
        		clz = prop.getClientClz();
//        		clz = "Msg"+clz+"VO";
        		prop.setClz(clz);
        		
        	}
        	
        	if (prop.getChildren() == PropChildrenConstant.OBJECT || prop.getChildren() == PropChildrenConstant.OBJECT_ARRAY){
        		convertAS3ArrayFormat(prop.getObject());
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
	
	public static void generateAsVO(String projectPath, Message msg, Template voAsTemplate) {
 
		
	Map<String,List<BeanProp>> classMap = new HashMap<String, List<BeanProp>>();
		
		List<BeanProp> beanProps = msg.getPropList();
		
		getBeanProps(classMap, beanProps);
		
//		for (BeanProp beanProp : beanProps) { 
//			if(beanProp.getObject().size() <= 0)continue;
//			List<BeanProp>  voPropList  =	classMap.get(beanProp.getClz());
// 
//				if(voPropList == null){
//					voPropList = beanProp.getObject();
//					classMap.put(beanProp.getClz(), voPropList);
//				}else{ 
//					List<BeanProp> newProps = beanProp.getObject();
//					
//					//检查新的里面是否已经在old中存在
//					for (BeanProp newProp : newProps) {
//						boolean flag = true;
//						for (BeanProp oldProp : voPropList) {
//							if(newProp.getName().equals(oldProp.getName())){
//								flag = false;
//								break;
//							}
//						}
//						if(flag){
//							voPropList.add(newProp);
//						}
//					}
//				}
//			}
		
			for (Entry<String, List<BeanProp>> entry : classMap.entrySet()) {
				 
				 String packagename = getClientPackageName(msg, entry.getKey());
				 if(packagename == null)continue;
				 int splitIndex = packagename.lastIndexOf(".");
				 String beanPackage = packagename.substring(0,splitIndex); 
				 String className =  packagename.substring(splitIndex+1);
				 
//				 BeanProp props = entry.getValue();
		  try
	        {
	         

	            String path = "src/"+beanPackage;
	          
	            path = path.replaceAll("\\.", "\\/");       
	            
	            File folder = new File(projectPath+path);
	            if (!folder.exists()){
	            	folder.mkdirs();
	            }

	            File file = new File(projectPath+path+"/"+ className+".as");

	            VelocityContext context = new VelocityContext();
	             
	            context.put("package", beanPackage);
	            context.put("className",className);   
	            
	            List<BeanProp> list = new  ArrayList<BeanProp>();
	            for (BeanProp beanProp : entry.getValue()) {
	            	list.add(beanProp.clone());
				}
	            
	            MessageASBeanGenerator.convertAS3ArrayFormat(list);
	            context.put("propList", list); 
	            context.put("comment", msg.getComment()); 

	            BufferedWriter writer = new BufferedWriter(
	                new OutputStreamWriter(new FileOutputStream(file)));

	            if ( voAsTemplate != null)
	            	voAsTemplate.merge(context, writer);

	            writer.flush();
	            writer.close();
	        }
	        catch( Exception e )
	        {
	            System.out.println(e);
	        }
		  
			}
	}
	
	public BeanProp clone(BeanProp beanProp){
		try {
			  beanProp.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getClientPackageName(Message message,String className){
		for (String element : message.getImportClient()) {
			if(element.contains(className)){
				return element;
			}
		}
		return null;
	}

	public static void generateClientSC(String aS3ProjectPath, ClientGenData datas, Template csASTemplate) {
		 try
	        {
		String beanPackage = datas.getPackageClient();
        String path = beanPackage;
        
        String className = datas.getClassName();
        
        path = "src/"+path.replaceAll("\\.", "\\/");       
        
        File folder = new File(aS3ProjectPath+path);
        if (!folder.exists()){
        	folder.mkdirs();
        }

        File file = new File(aS3ProjectPath+path+"/SC"+ className+".as");

        VelocityContext context = new VelocityContext();
        
//      convertAS3ArrayFormat(msg.getPropList());　
        
        context.put("package", beanPackage);
        context.put("className",className);
        context.put("scMessageList", datas.getCsMessageList());
        context.put("importList", datas.getImportList()); 
        context.put("comment", datas.getComment());
        
        BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(file)));

        if ( csASTemplate != null)
        	csASTemplate.merge(context, writer);

        writer.flush();
        writer.close();
    }
    catch( Exception e )
    {
        System.out.println(e);
    }
	}
	
	
	public static void generateClientCS(String aS3ProjectPath, ClientGenData datas, Template csASTemplate) {
		 try
	        {
		String beanPackage = datas.getPackageClient();
       String path = beanPackage;
       
       String className = datas.getClassName();
       
       path = "src/"+path.replaceAll("\\.", "\\/");       
       
       File folder = new File(aS3ProjectPath+path);
       if (!folder.exists()){
       	folder.mkdirs();
       }

       File file = new File(aS3ProjectPath+path+"/CS"+ className+".as");

       VelocityContext context = new VelocityContext();
       
//     convertAS3ArrayFormat(msg.getPropList());　
       
       context.put("package", beanPackage);
       context.put("className",className);
       context.put("csMessageList", datas.getCsMessageList());
       context.put("importList", datas.getImportList()); 
       context.put("comment", datas.getComment());
       
       BufferedWriter writer = new BufferedWriter(
           new OutputStreamWriter(new FileOutputStream(file)));

       if ( csASTemplate != null)
       	csASTemplate.merge(context, writer);

       writer.flush();
       writer.close();
   }
   catch( Exception e )
   {
       System.out.println(e);
   }
	}

	public static void generateAS3Interface(String aS3ProjectPath, ClientGenData datas, Template asServiceInterfaceVn) {
		 try
	        {
		String beanPackage = datas.getPackageClient();
     String path = beanPackage;
     
     String className = datas.getClassName();
     
     path ="src/"+ path.replaceAll("\\.", "\\/");       
     
     File folder = new File(aS3ProjectPath+path);
     if (!folder.exists()){
     	folder.mkdirs();
     }

     File file = new File(aS3ProjectPath+path+"/I"+ className+"Service.as");

     VelocityContext context = new VelocityContext();
     
//   convertAS3ArrayFormat(msg.getPropList());　
     
     context.put("package", beanPackage);
     context.put("className",className);
     context.put("scMessageList", datas.getCsMessageList());
     context.put("importList", datas.getImportList()); 
     context.put("comment", datas.getComment());
     
     BufferedWriter writer = new BufferedWriter(
         new OutputStreamWriter(new FileOutputStream(file)));

     if ( asServiceInterfaceVn != null)
    	 asServiceInterfaceVn.merge(context, writer);

     writer.flush();
     writer.close();
 }
 catch( Exception e )
 {
     System.out.println(e);
 } 
	}
}
