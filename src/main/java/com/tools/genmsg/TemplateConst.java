package com.tools.genmsg;

import com.message.*;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author xiewen
 * 
 */
public class TemplateConst {

	public final static String message_code = "src/template/msg_code.vm";

	private static Template messageCodeTemplate;

	static{
		readTemplate();
	}

	static void readTemplate() {
		try {
			messageCodeTemplate = Velocity.getTemplate(message_code,"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Template getMessageCodeTemplate(){
		return messageCodeTemplate;
	}
}
