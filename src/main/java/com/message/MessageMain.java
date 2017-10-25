package com.message;

import java.util.ResourceBundle;

public class MessageMain {
	
	public static ResourceBundle rb = ResourceBundle.getBundle("outpath");
	
    public static void main(String[] args) throws Exception {
    	
    	String java_project_path = rb.getString("JAVA_PROJECT_PATH"); 

    	new MessageGeneratorWrapper(java_project_path);
	}
}
