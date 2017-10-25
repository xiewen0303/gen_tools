package com.message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

public class ConstantGenerator {

	public static void generateJavaServer(String projectPath,Constant constant,Template template) throws Exception{
        String beanPackage = constant.getPkgServer();

        String path = "src/"+beanPackage;
        String className = constant.getClzName();

        path = path.replaceAll("\\.", "\\/");       
        
        File folder = new File(projectPath+path);
        if (!folder.exists()){
        	folder.mkdirs();
        }

        File file = new File(projectPath+path+"/"+ className+".java");

        VelocityContext context = new VelocityContext();
        
        context.put("package", beanPackage);
        context.put("className",className);        
        context.put("propList", constant.getPropList());
        context.put("comment", constant.getComment());

        BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(file)));

        if ( template != null)
            template.merge(context, writer);

        writer.flush();
        writer.close();

	}
	
	public static void generateJavaBattle(String projectPath,Constant constant,Template template) throws Exception{
        String beanPackage = constant.getPkgClient();

        String path = "src/"+beanPackage;
        String className = constant.getClzName();

        path = path.replaceAll("\\.", "\\/");       
        
        File folder = new File(projectPath+path);
        if (!folder.exists()){
        	folder.mkdirs();
        }

        File file = new File(projectPath+path+"/"+ className+".java");

        VelocityContext context = new VelocityContext();
        
        context.put("package", beanPackage);
        context.put("className",className);        
        context.put("propList", constant.getPropList());
        context.put("comment", constant.getComment());

        BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(file)));

        if ( template != null)
            template.merge(context, writer);

        writer.flush();
        writer.close();

	}
	
	public static void generateAS3(String projectPath,Constant constant,Template template) throws Exception{
        String beanPackage = constant.getPkgClient();
        
        String path = beanPackage;
        String className = constant.getClzName();

        path = "src/"+path.replaceAll("\\.", "\\/");       
        
        File folder = new File(projectPath+path);
        if (!folder.exists()){
        	folder.mkdirs();
        }

        File file = new File(projectPath+path+"/"+ className+".as");

        VelocityContext context = new VelocityContext();
        
        context.put("package", beanPackage);
        context.put("className",className);        
        context.put("propList", constant.getPropList());
        context.put("comment", constant.getComment());

        BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(file)));

        if ( template != null)
            template.merge(context, writer);

        writer.flush();
        writer.close();

	}
}
