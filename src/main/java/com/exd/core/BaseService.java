package com.exd.core;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exd.utl.Coder;
/**
 * 服务解析基类
 * @author YangWei
 * @version 1.0
 * */
public abstract class BaseService implements Service {
	private static Logger logger = LoggerFactory.getLogger(BaseService.class);
	private Element serviceDom;					// 服务元素
	private String name;						// 服务名称
	private Map<String, Object> params;			// 全局参数
	private Map<String, String> localParams;	// 服务本地定义参数(全局参数的子集)
	
	private ServiceAnalyzer serviceAnalyzer;	// 本地解析器

	public String getName() {
		return name;
	}

	public void setName(String namne) {
		this.name = namne;
	}

	public Map<String, Object> getParams() {
		return params;
	}
	
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public static Logger getLogger() {
		return logger;
	}

	public Element getServiceDom() {
		return serviceDom;
	}

	public void setServiceDom(Element serviceDom) {
		this.serviceDom = serviceDom;
	}
	
	public Map<String, String> getLocalParams() {
		return localParams;
	}
	
	/**
	 * 处理param元素
	 * */
	protected Map<String, String> analyzeParamEl(Element element){
		Map<String, String> localParam = null;
		Param paramBean = null;
		for(Iterator<?> it  = element.elementIterator("param"); it.hasNext();){
			Element param = (Element)it.next();
			paramBean = new Param();
			paramBean.setScope(name);
			paramBean.setParaMap(this.getParams());
			paramBean.setCoder(Coder.getInstance());
			mappingAttributeValue(paramBean, param);
			if(paramBean.getValue() == null){
				paramBean.setValue(parseAttribute(param.getText()));
			}
			if(localParam == null){
				localParam = new LinkedHashMap<String, String>();
			}
			localParam.put(paramBean.getName(), paramBean.getValue());
			this.getParams().put(paramBean.getScopeName(), paramBean.getValue());
		}
		return localParam;
	}
	
	/**解析属性变量*/
	protected String parseAttribute(String attr){
		if(attr == null){
			attr = "";
		}
		else if(attr.indexOf("$")>=0){
			String regEx="\\$\\{\\s*\\w+\\.?\\w+\\s*\\}";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(attr);
			while(matcher.find()){
				String placeSrc = matcher.group();
				String placeName = placeSrc.replaceAll("\\$\\{\\s*|\\s*\\}", "").trim();
				Object replaceWith = getParams().get(getName()+"."+placeName) == null ? 
										getParams().get(placeName) : 
										getParams().get(getName()+"."+placeName);
			    if(replaceWith==null) replaceWith="";
			   
			    attr = attr.replace(placeSrc, replaceWith.toString());
			}
		}
		return attr.replaceAll("\t|\n", "").trim();
	}
	
	/**
	 * 批量赋值服务属性值
	 * */
	public void mappingAttributeValue(Object obj, Element xml){
		for(Iterator<?> it = xml.attributeIterator(); it.hasNext();){
			Attribute attr = (Attribute)it.next();
			String name = attr.getName();
			String value = parseAttribute(attr.getValue());
			String setMethod = "set"+name.replaceFirst(name.substring(0,1), name.substring(0,1).toUpperCase());
			try {
				obj.getClass().getMethod(setMethod, String.class).invoke(obj, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 预实现接口方法loadConfig
	 * */
	public void loadConfig(Element xml) {
		mappingAttributeValue(this, xml);
		this.localParams = analyzeParamEl(xml);
		beforeExecute(xml);
	}
	public abstract void beforeExecute(Element xml);
	
	public void setServiceAnalyzer(ServiceAnalyzer analyzer){
		this.serviceAnalyzer = analyzer;
	}

	public ServiceAnalyzer getServiceAnalyzer() {
		return serviceAnalyzer;
	}
}
