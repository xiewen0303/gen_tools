package com.tools.genmsg.msgcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成code实体
 * Created by xiewen on 2016/7/23.
 */
public class CodeBean {
    //模块名
    private String moduleName;

    //包名
    private String packageName;

    //Class名称
    private String javaClassName;

    //协议名称
    private List<String> messageNames = new ArrayList<>();

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getMessageNames() {
        return messageNames;
    }

    public void addMessageNames(String messageName) {
        this.messageNames.add(messageName);
    }

    public void setMessageNames(List<String> messageNames) {
        this.messageNames = messageNames;
    }

    public String getJavaClassName() {
        return javaClassName;
    }

    public void setJavaClassName(String javaClassName) {
        this.javaClassName = javaClassName;
    }
}
