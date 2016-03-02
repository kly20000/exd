package com.exd.utl;

import java.lang.reflect.Method;
import java.util.Properties;

public class Coder {
	
	private static Properties prop;		// 可编程配置
	
	private static Coder instance;		// 实例
	
	public Coder(String coderPath){
		if(prop == null){
			prop = ConfigLoader.loadFromProperties("classpath:com/exd/core/coder.properties");
		}
		if(coderPath != null){
			prop.putAll(ConfigLoader.loadFromProperties(coderPath));
		}
	}
	
	public static void initCoder(String coderPath){
		if(instance == null){
			instance = new Coder(coderPath);
		}
	}
	
	/**获取coder实例*/
	public static Coder getInstance(){
		return instance;
	}
	
	/**获取可编程方法返回值(单一参数)*/
	public Object getValue(String method, Object value) {
		try {
			String clazz = prop.getProperty(method);
			Method me = Class.forName(clazz).getMethod(method, Object.class);
			return me.invoke(null, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**获取可编程方法返回值（双参数）*/
	public Object getValue(String method, Object value, Object help) {
		try {
			String clazz = prop.getProperty(method);
			Method me = Class.forName(clazz).getMethod(method, Object.class, Object.class);
			return me.invoke(null, value, help);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
