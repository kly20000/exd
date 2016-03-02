package com.exd.core;

import org.dom4j.Element;

import com.exd.utl.HttpUtil;
/**
 * HTTP服务解析器
 * @author YangWei
 * */
public class HttpService extends BaseService{

	private String url; 	// 请求地址
	private String type; 	// 请求方式
	
	/**
	 * 解析http标签服务
	 * */
	public void beforeExecute(Element xml) {
		url = parseAttribute(url);
	}

	/**
	 * 执行http标签服务
	 * */
	public void execute() {
		getParams().put(getName(), ("POST".equalsIgnoreCase(type)?
					HttpUtil.doPost(url, this.getLocalParams()):
					HttpUtil.doGet(url, this.getLocalParams())));
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setType(String type) {
		this.type = type;
	}
}
