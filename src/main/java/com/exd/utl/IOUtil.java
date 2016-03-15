package com.exd.utl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOUtil {
	/**读取文件到输入流
	 * @throws FileNotFoundException */
	public static InputStream readFromFile(String path) throws FileNotFoundException{
		if(path.startsWith("classpath:")){
			String realPath = path.replaceFirst("classpath:", "");
			return IOUtil.class.getClassLoader().getResourceAsStream(realPath);
		}else if(path.startsWith("context:")){
			String realPath = path.replaceFirst("context:", "");
			return IOUtil.class.getClassLoader().getResourceAsStream("../../"+realPath);
		}else{
			return new FileInputStream(path);
		}
	}
	/**输入写到输出流*/
	public static void copy(InputStream in, OutputStream out){
		try {
			byte[] data = new byte[512];
			for(int size = in.read(data); size > -1; size=in.read(data)){
				out.write(data,0,size);
			}
			in.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**输出流转输入流*/
	public static InputStream readFromOutputStream(OutputStream out){
		ByteArrayOutputStream bos = (ByteArrayOutputStream)out;
		InputStream in = new BufferedInputStream(new ByteArrayInputStream(bos.toByteArray()));
		try {
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;
	}
	/**writer 转 输入流*/
	public static InputStream readFromWriter(Writer out){
		return null;
	}
	/**输出到文件*/
	public static void writeToFile(InputStream in, String path){
		try{
			File file = new File(path);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdir();
			}
			FileOutputStream fos = new FileOutputStream(file, true);
			byte[] data = new byte[512];
			for(int flag = in.read(data); flag > -1; flag = in.read(data)){
				fos.write(data,0,flag);
			}
			in.close();
			fos.flush();
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**删除文件*/
	public static void deleteFile(String path){
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
	}
	/**输出Reader*/
	public static OutputStream writeToReader(Reader in){
		try {
			OutputStream out = new ByteArrayOutputStream();
			BufferedReader br = new BufferedReader(in);
			String content = null;
			do{
				content  = br.readLine();
				out.write(content.getBytes());
			}while(content != null);
			return out;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
