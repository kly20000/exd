package com.exd.core;

import java.util.Map;

import org.dom4j.Element;
/**
 * 服务接口
 * @author YangWei
 * */
public interface Service {
	/**
	 * 加载配置文件
	 * */
	public void loadConfig(Element xml);
	/**
	 * 执行服务
	 * */
	public void execute();
	/**
	 * 设置参数
	 * */
	public void setParams(Map<String, Object> params);
	
	/**
	 * 设置解析器
	 * */
	public void setServiceAnalyzer(ServiceAnalyzer analyzer);
}
