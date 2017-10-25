package com.start;

import java.util.Properties;

import org.apache.velocity.app.Velocity;

import com.tools.gendb.GenDBMain;




public class Main {
	
	static void init(){
		Properties p = new Properties();
		p.put("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(p);
	}
    public static void main(String[] args) throws Exception {
    	init();
    	
    	//生成java - as3协议.
//    	ResourceBundle rb = ResourceBundle.getBundle("as3.outpath");
//    	new MessageGeneratorWrapper(rb.getString("JAVA_PROJECT_PATH"));
    	
    	//生成db
    	GenDBMain.genDBMain();
    	
    	//生成protobuf message
//    	ProtobufMain.genMessage();
    	
    	//生成zlib文件
//    	GenZlibMain.GenZlib();
	}
}