package com.message;

import java.nio.ByteBuffer;
/**
 * 
 * @author wyan
 *
 */
public class MessageUtil {
	public static String readString(ByteBuffer buffer) throws Exception{
		short stringLength = 0;
		byte[] stringBytes;
		stringLength = buffer.getShort();
		stringBytes = new byte[stringLength];
		buffer.get(stringBytes);
		return new String(stringBytes,"UTF-8");
	}
	
	public static void writeString(ByteBuffer buffer, String content) throws Exception{
		byte[] bytes = content.getBytes("UTF-8");
		buffer.putShort((short)bytes.length);
		buffer.put(bytes);
	}
	
	public static int getLength(String content) throws Exception{
		return 2 + content.getBytes("UTF-8").length;
	}
}
