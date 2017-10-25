package com.message;
/**
 * 
 * @author wyan
 *
 */
public class MessageIdentified implements Comparable<MessageIdentified> {

	private int codeValue;
	private String clzName;
	private String codeKey;
	private String comment;
	private String ctlComment;
	private String codeName;
	private String moduleName; //所属模块名称
	
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public int getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(int codeValue) {
		this.codeValue = codeValue;
	}
	public String getCodeKey() {
		return codeKey;
	}
	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
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
	public String getCtlComment() {
		return ctlComment;
	}
	public void setCtlComment(String ctlComment) {
		this.ctlComment = ctlComment;
	}
	@Override
	public int hashCode() {
		return codeValue;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageIdentified other = (MessageIdentified) obj;
		if (codeValue != other.codeValue)
			return false;
		return true;
	}
	@Override
	public int compareTo(MessageIdentified m) {
		// TODO Auto-generated method stub
		return codeValue - m.codeValue;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}
