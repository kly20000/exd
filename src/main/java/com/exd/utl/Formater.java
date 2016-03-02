package com.exd.utl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

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
}
