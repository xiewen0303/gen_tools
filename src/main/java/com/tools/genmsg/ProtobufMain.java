package com.tools.genmsg;

/**
 * 生成协议
 * Created by xiewen on 2016/7/22.
 */
public class ProtobufMain {

    public static void genMessage() {


        GenJavaMsg genJavaMsg = new GenJavaMsg();
        genJavaMsg.genProtoMessage();

        MessageCodeGenerator messageCodeGenerator = new MessageCodeGenerator();
        //生成java的协议常量类
        messageCodeGenerator.generateJavaMessageCode(TemplateConst.getMessageCodeTemplate());
        //生成协议号的历史记录
        messageCodeGenerator.refreshCodeIdHistory();



    }
}