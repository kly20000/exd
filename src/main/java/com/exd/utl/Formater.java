package com.exd.utl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.lang.StringUtils;

public class Formater {
	/**
	 * XML转JSON
	 * */
	public static String xml2Json(Object xml){
		XMLSerializer xmlSerializer = new XMLSerializer();  
	    JSON json = xmlSerializer.read(xml.toString());  
	    String jsonTxt=json.toString().replaceAll("@", "").replaceAll("#", "");
	    return jsonTxt;
	}
	/**
	 * List<Map<String, Object> 转JSON 
	 * 用于sql查询结果集
	 * */
	public static String list2Json(Object listObj){
		List<Map<String, Object>> list = (List<Map<String, Object>>)listObj;
		JSONArray jsonArray=new JSONArray();
		for(Map<String, Object> item : list){
			JSONObject bean = new JSONObject();
			for(Iterator<String> it = item.keySet().iterator(); it.hasNext();){
				String key = it.next();
				bean.put(key, item.get(key));
			}
			jsonArray.add(bean);
		}
		return jsonArray.toString();
	}
	
	/**
	 * Map<String, Object> 转url
	 * 用于HTTP参数拼接RSA校验
	 * */
	public static String map2Url(Object mapObj){
		Map<String, Object> map = (TreeMap<String, Object>)mapObj;
		String[] parameters = new String[map.size()];
		int i=0;
		for(Iterator<String> it = map.keySet().iterator(); it.hasNext();){
			String key = it.next();
			parameters[i] = key+"="+map.get(key);
			i++;
		}
		return StringUtils.join(parameters, "&");
	}
}
