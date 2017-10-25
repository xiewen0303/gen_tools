package com.jcraft.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.jcraft.jzlib.DeflaterOutputStream;

public class Test {
	
	public static void main(String[] args)  throws Exception {
		
		InputStream input = new FileInputStream("D:/小.zip");
		
		DeflaterOutputStream zOutput = new DeflaterOutputStream(new FileOutputStream("D:/testbak.gz"));
		int len = input.available();
		byte[] datas = new byte[len];
		input.read(datas); 
		zOutput.write(datas);
		input.close();
		
		
	}
}
