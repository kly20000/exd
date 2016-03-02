package com.exd.core;

import org.dom4j.Element;
/**
 * 核心服务解析器
 * @author YangWei
 * */
public class CoreService extends BaseService{
	
	public void beforeExecute(Element xml) {
		this.setServiceDom(null);
	}

	public void execute() {
		getServiceAnalyzer().analyze(this.getName(), getParams());
	}
}
