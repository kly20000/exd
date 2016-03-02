package com.exd.web;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Element;

import com.exd.utl.IOUtil;

public class WebOutService extends WebService{
	
	private String srcName;										// 资源文件名
	private String contentType = "application/octet-stream";	// 下载类型
	
	private String inputPath;	// 读取文件路径
	
	private HttpServletResponse response;

	public void beforeExecute(Element xml) {
		response = getResponse();
		inputPath = getFilePath(getInpath());
	}
	
	public void execute() {
		try{
			if(inputPath == null){
				WebHelper.processResult(getParams(), response);
			}else{
				InputStream in = IOUtil.readFromFile(inputPath);
				// 清空response
		        response.reset();
		        // 设置response的Header
		        response.addHeader("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode(srcName,"utf-8"));
		        response.addHeader("Content-Length", "" + in.available());
		        response.setContentType(contentType);
		        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				IOUtil.copy(in, toClient);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public static void main(String[] args){
		System.out.println(Integer.parseInt("aa"));
	}
}
