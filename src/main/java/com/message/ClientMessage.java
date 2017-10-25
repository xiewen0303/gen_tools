package com.message;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author wind
 *
 */
public class ClientMessage {
 
	private String messageCode;  
	private String comment; 
	private String cmdName;
	private List<BeanProp> propList = new ArrayList<BeanProp>();
	
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<BeanProp> getPropList() {
		return propList;
	}
	public void setPropList(List<BeanProp> propList) {
		this.propList = propList;
	}
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
}
