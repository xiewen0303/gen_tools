package com.message;

import java.util.ArrayList;
import java.util.List;

public class Constant {
	
	private String pkgServer;
	private String pkgClient;
	private String clzName;
	private String comment;
	private List<ConstantProp> propList = new ArrayList<ConstantProp>();

	public String getPkgServer() {
		return pkgServer;
	}
	public void setPkgServer(String pkgServer) {
		this.pkgServer = pkgServer;
	}
	public String getPkgClient() {
		return pkgClient;
	}
	public void setPkgClient(String pkgClient) {
		this.pkgClient = pkgClient;
	}
	public String getClzName() {
		return clzName;
	}
	public void setClzName(String clzName) {
		this.clzName = clzName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<ConstantProp> getPropList() {
		return propList;
	}
	public void setPropList(List<ConstantProp> propList) {
		this.propList = propList;
	}
	public void addProp(ConstantProp prop){
		this.propList.add(prop);
	}
	
}
