package com.message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 
 * @author wind
 *
 */
public class ClientGenData {

	private String className;
	private String packageClient;
	private String comment;
	private Set<String> importList = new HashSet<String>();
	private List<ClientMessage> csMessageList = new ArrayList<ClientMessage>();
	
	
	public Set<String> getImportList() {
		return importList;
	}
	public void setImportList(Set<String> importList) {
		this.importList = importList;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<ClientMessage> getCsMessageList() {
		return csMessageList;
	}
	public void setCsMessageList(List<ClientMessage> csMessageList) {
		this.csMessageList = csMessageList;
	}
	public void addCsMessage(ClientMessage clientMessage){
		this.csMessageList.add(clientMessage);
	}
	public void setPackageClient(String packageClient) {
		this.packageClient = packageClient;
	}
	public String getPackageClient() {
		return packageClient;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	} 
}
