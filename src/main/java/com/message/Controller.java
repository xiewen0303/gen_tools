package com.message;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author wyan
 *
 */
public class Controller{

	private String clzName;
	private String packageServer;
	private String packageClient;
	private String comment;
	private String moduleName;
	private List<Message> messageList = new ArrayList<Message>();
	private List<Constant> constantList = new ArrayList<Constant>();
	
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getClzName() {
		return clzName;
	}
	public void setClzName(String clzName) {
		this.clzName = clzName;
	}
	public String getPackageServer() {
		return packageServer;
	}
	public void setPackageServer(String packageServer) {
		this.packageServer = packageServer;
	}
	public String getPackageClient() {
		return packageClient;
	}
	public void setPackageClient(String packageClient) {
		this.packageClient = packageClient;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<Message> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}
	public void addMessage(Message message){
		this.messageList.add(message);
	}
	public List<Constant> getConstantList() {
		return constantList;
	}
	public void setConstantList(List<Constant> constantList) {
		this.constantList = constantList;
	}
	public void addConstant(Constant constant){
		this.constantList.add(constant);
	}
	
}
