package com.message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 
 * @author wyan
 *
 */
public class Message {

	private String ctlName;
	private String codeName;
	private String clzName;
	private String packageServer;
	private String packageClient;
	private String comment;
	private String method; 
	private List<BeanProp> propList = new ArrayList<BeanProp>();
	private Set<String> importServer = new HashSet<String>();
	private Set<String> importClient = new HashSet<String>();
	private String ctlServerImport;
	private String ctlClientImport;
	
	
	public String getCtlName() {
		
		return ctlName;
	}
	public void setCtlName(String ctlName) {
		this.ctlName = ctlName;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
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
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getCtlServerImport() {
		return ctlServerImport;
	}
	public void setCtlServerImport(String ctlServerImport) {
		this.ctlServerImport = ctlServerImport;
	}
	public String getCtlClientImport() {
		return ctlClientImport;
	}
	public void setCtlClientImport(String ctlClientImport) {
		this.ctlClientImport = ctlClientImport;
	}
	public List<BeanProp> getPropList() {
		return propList;
	}
	public void setPropList(List<BeanProp> propList) {
		this.propList = propList;
	}

	public Set<String> getImportServer() {
		return importServer;
	}
	public void setImportServer(Set<String> importServer) {
		this.importServer = importServer;
	}
	public Set<String> getImportClient() {
		return importClient;
	}
	public void setImportClient(Set<String> importClient) {
		this.importClient = importClient;
	}
	public void addProp(BeanProp prop){
		this.propList.add(prop);
	}
	public void addImportServer(String ipt){
		this.importServer.add(ipt);
	}
	public void addImportClient(String ipt){
		this.importClient.add(ipt);
	}
}
