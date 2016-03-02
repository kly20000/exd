package com.exd.core;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Element;

import com.exd.utl.ConfigLoader;
/**
 * Property 类
 * @author YangWei
 * @version 1.0
 * */
public class PropertyService extends IOService {
	
	private static Map<String, Properties> propertiesMap;
	
	private Properties prop = null;
	
	private String key = null;	// 文件池中的键名称
	
	private String inputPath;	// 读取路径
	
	public void beforeExecute(Element xml) {
		this.inputPath = getFilePath(getInpath());
		if(propertiesMap == null){
			propertiesMap = new HashMap<String, Properties>();
		}
	}

	public void execute() {
		if(inputPath != null){
			getLogger().info("start loading property file : "+inputPath);
			if(key != null && propertiesMap.containsKey(key)){
				prop = propertiesMap.get(key);
			}else{
				prop = ConfigLoader.loadFromProperties(inputPath);
				propertiesMap.put(key, prop);
			}
			Enumeration<?> names = prop.propertyNames();
			while(names.hasMoreElements()){
				String key = names.nextElement().toString();
				getParams().put(getName()+"."+key, prop.get(key).toString());
			}
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
