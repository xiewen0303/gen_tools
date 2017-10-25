package com.tools.genmsg;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import com.tools.genmsg.msgcode.GenMessageCode;

/**
 * 生成器
 * Created by xiewen on 2016/7/22.
 */
public class GenJavaMsg {

    /**
     * proto文件位置
     */
    public final static String PROTO_FILE_PATH = Util.GetConfigValue("PROTO_FILE_PATH");

    /**
     * proto 安装位置
     */
    public final static String PROTO_INSTALL_PATH = Util.GetConfigValue("PROTO_INSTALL_PATH");


    /**
     * 生成后的java项目路径
     */
    public final static String JAVA_PROJECT_PATH = Util.GetConfigValue("JAVA_PROJECT_PATH");

    /**
     * 生成后的java协议文件的输出目录
     */
    public final static String JAVA_OUT_PATH = JAVA_PROJECT_PATH +"\\src";

    /**
     * 生成后的javaMessageCode路径
     */
    public final static String JAVA_MESSAGECODE_OUT_PATH = JAVA_PROJECT_PATH +"\\src\\message";

    /**
     * 生成后的javaMessageCode路径
     */
    public final static String CLIENT_MESSAGE_OUT_PATH = Util.GetConfigValue("CLIENT_PROJECT_PATH");

    /**
     * 生成协议的命令
     */
    public static String CMD_TPL = "cmd  /c {0}\\src\\protoc -I={1} --cpp_out={4} --java_out={2} {3}";

    /**
     * 生成proto协议
     */
    public void genProtoMessage() {
        //删除掉之前删除的文件
        File folder = new File(JAVA_MESSAGECODE_OUT_PATH);
        if (folder.exists()) {
            Util.deleteDir(folder);
        }


        File files = new File(PROTO_FILE_PATH);
        if (files == null) {
            System.err.println("path is not exist :"+PROTO_FILE_PATH);
            System.exit(-1);
        }
        if(!files.isDirectory()){
            System.err.println("PROTO_FILE_PATH is not isDirectory :"+PROTO_FILE_PATH);
            System.exit(-1);
        }
        File[] fileInfos = files.listFiles();
        genProtoMessageExt(fileInfos);
    }

    public void genProtoMessageExt(File[] fileInfos){
        Runtime r = Runtime.getRuntime();
        for (File file : fileInfos) {
            if(file.isDirectory()) {
                genProtoMessageExt(file.listFiles());
            }else{
                if(!file.getName().endsWith(".proto")){
                    continue;
                }
                String cmd = MessageFormat.format(CMD_TPL,PROTO_INSTALL_PATH,PROTO_FILE_PATH,JAVA_OUT_PATH,file.getPath(),CLIENT_MESSAGE_OUT_PATH);
                System.out.println(cmd);
                try {
                    Process p = r.exec(cmd);
                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream(),"gbk"));
                    String line = null;
                    boolean flagExit  = false;
                    while ((line = br.readLine()) != null) {
                        System.err.println(line);
                        flagExit = true;
                    }
                    if(flagExit){
                        System.exit(-1);
                    }

                    GenMessageCode.parseTemplate(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
