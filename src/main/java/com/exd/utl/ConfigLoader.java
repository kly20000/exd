package com.exd.utl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
/**
 * 服务配置加载工具
 * @author YangWei
 * */
public class ConfigLoader {
	/**
	 * 加载xml配置文件
	 * */
	public static Document loadFromXml(String xmlPath){
		Document document = null;
		SAXReader reader = new SAXReader();  
		try{
			document = reader.read(IOUtil.readFromFile(xmlPath));
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
		return document;
	}

	/**
	 * 加载properties配置文件
	 * */
	public static Properties loadFromProperties(String propPath){
		Properties prop = new Properties();
		try {
			prop.load(IOUtil.readFromFile(propPath));
		} catch (FileNotFoundException e) {
			prop.put("errorMessage", "File not found");
		} catch (IOException e) {
			prop.put("errorMessage", "File io exception");
		}
		return prop;
	}
}
