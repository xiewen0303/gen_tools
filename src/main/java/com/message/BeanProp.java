package com.message;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wyan
 *
 */
public class BeanProp implements Cloneable {

	private String clz;
	private String clientClz;
	private String name;
	private String method;
	private String comment;
	private List<BeanProp> object = new ArrayList<BeanProp>();
	private String clzOrigin;

	private int children = 0;
	public String getClz() {
		return clz;
	}
	public void setClz(String clz) {
		this.clz = clz;
	}
	public String getClientClz() {
		return clientClz;
	}
	public void setClientClz(String clientClz) {
		this.clientClz = clientClz;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	//Object
	public void addPropToObject(BeanProp beanProp){
		this.object.add(beanProp);
	}
	
	public List<BeanProp> getObject() {
		return object;
	}
	public void setObject(List<BeanProp> object) {
		this.object = object;
	}

	public int getChildren() {
		return children;
	}
	public void setChildren(int children) {
		this.children = children;
	}
	public String getClzOrigin() {
		return clzOrigin;
	}
	public void setClzOrigin(String clzOrigin) {
		this.clzOrigin = clzOrigin;
	}
	
	@Override
	public BeanProp clone() throws CloneNotSupportedException {
		return (BeanProp)super.clone();
	}
	
}
