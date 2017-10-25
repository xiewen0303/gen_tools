package com.tools.genmsg.msgcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tools.genmsg.MessageCodeGenerator;
 

/**
 * 协议号生成器
 * Created by xiewen on 2016/7/23.
 */
public class GenMessageCode {

    /**
     * 模块名称匹配规则
     */
    static Pattern MODULENAME_PATTERN = Pattern.compile("^\\s*package\\s+");

    /**
     *  包名匹配规则
     */
    static Pattern PACKAGENAME_PATTERN = Pattern.compile("^\\s*option\\s+java_package\\s+=");

    /**
     *  className
     */
    static Pattern CLASSNAME_PATTERN = Pattern.compile("^\\s*option\\s+java_outer_classname\\s+=");

    /**
     *  message
     */
    static Pattern MESSAGE_PATTERN = Pattern.compile("^\\s*message\\s+(CS_|SC_)");

    /**
     * 协议proto的协议
     */
    public static void parseTemplate(File file) {
        CodeBean codeBean = new CodeBean();
        try {
            FileInputStream inStrean = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStrean));
            String content = null;
            while((content = bufferedReader.readLine())!=null){
                //System.out.println(content);

                //匹配模块名称
                Matcher moduleNameMatcher = MODULENAME_PATTERN.matcher(content);
                if(moduleNameMatcher.find()){
                    int endIndex = moduleNameMatcher.end();
                    String data = content.substring(endIndex).replace(";","").trim();
                    codeBean.setModuleName(data);
                }

                //匹配包名
                Matcher packageNameMatcher = PACKAGENAME_PATTERN.matcher(content);
                if(packageNameMatcher.find()){
                    int endIndex = packageNameMatcher.end();
                    String data = content.substring(endIndex).replaceAll(";|\"","").trim();
                    codeBean.setPackageName(data);
                }

                //匹配包名
                Matcher classNameMatcher = CLASSNAME_PATTERN.matcher(content);
                if(classNameMatcher.find()){
                    int endIndex = classNameMatcher.end();
                    String data = content.substring(endIndex).replaceAll(";|\"","").trim();
                    codeBean.setJavaClassName(data);
                }

                //协议名称
                Matcher messageMatcher = MESSAGE_PATTERN.matcher(content);
                if(messageMatcher.find()){
                    int endIndex = messageMatcher.end();
                    String data = content.substring(endIndex-3).replaceAll(";|\"","").trim();
                    codeBean.addMessageNames(data);
                }
            }

            //添加到生成数据中
            MessageCodeGenerator.AddCodeInfo(codeBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}