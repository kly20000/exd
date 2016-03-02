package com.exd.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;

import com.exd.utl.Coder;
import com.exd.utl.ConfigLoader;

/**
 * 服务执行控制入口
 * @author YangWei
 * */
public class ServiceAnalyzer {
	
	private Properties analyzers;
	
	private Map<String, Document> serviceDocuments;	// 服务池
	
	private Map<String, Object> sources;	// 资源池
	
	private String extendPath;		// 扩展功能配置路径
	
	private String servicePath;		// 服务配置路径
	
	private String coderPath;		// 扩展编程接口配置路径
	
	private boolean factoryMode;	// 生产模式

	/**初始化解析器*/
	public void init(){
		initAnalyzerConfig();
		/**初始化扩展编程接口*/
		Coder.initCoder(coderPath);
	}
	
	/**加载解析器配置*/
	protected void initAnalyzerConfig(){
		if(analyzers == null){
			analyzers = ConfigLoader.loadFromProperties("classpath:com/exd/core/analyzer.properties");
		}
		if(extendPath != null){
			analyzers.putAll(ConfigLoader.loadFromProperties(extendPath));
		}	
	}
	
	/**执行解析*/
	public Map<String, Object> analyze(String serviceName, Map<String, Object> datas){
		Document serviceDom = getServiceDocument(serviceName);
		return processServiceDocument(serviceDom.getRootElement(), datas);
	}
	
	/**执行服务文档*/
	public Map<String, Object> processServiceDocument(Element serviceEl, Map<String, Object> datas){
		if(datas == null){
			datas = new HashMap<String, Object>();
		}
		List<Element> serviceElements = serviceEl.elements();
		for(int i=0; i<serviceElements.size(); i++){
			Element el = serviceElements.get(i);
			String analyzerName = el.getName();
			Service service = generateServiceProcesser(analyzerName);
			service.setParams(datas);
			service.setServiceAnalyzer(this);
			service.loadConfig(el);
			service.execute();
		}
		return datas;
	}
	
	/**创建服务执行器*/
	protected Service generateServiceProcesser(String analyzerName){
		try {
			Service service = (Service)(Class.forName(analyzers.getProperty("analyzer."+analyzerName)).newInstance());
			return service;
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setExtendPath(String extendPath) {
		this.extendPath = extendPath;
	}

	/**获取服务档案*/
	public Document getServiceDocument(String serviceName) {
		if(serviceDocuments == null){
			serviceDocuments = new HashMap<String, Document>();
		}
		Document serviceDom = ConfigLoader.loadFromXml(servicePath+"/"+serviceName+".xml");
		if(!isFactoryMode()){
			return serviceDom;
		}
		if(serviceDocuments.get(serviceName) == null){
			serviceDocuments.put(serviceName, serviceDom);
		}
		return serviceDocuments.get(serviceName);
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public boolean isFactoryMode() {
		return factoryMode;
	}

	public void setFactoryMode(boolean factoryMode) {
		this.factoryMode = factoryMode;
	}

	public void setCoderPath(String coderPath) {
		this.coderPath = coderPath;
	}

	public Object getSources(String srcName) {
		return sources.get(srcName);
	}

	public void setSources(Map<String, Object> sources) {
		this.sources = sources;
	}

}
