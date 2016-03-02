package com.exd.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exd.utl.ConfigLoader;
import com.exd.utl.Encrypt;

public class WebHelper {
	private static Logger logger = LoggerFactory.getLogger(WebHelper.class);
	
	public static final String WEB_REQUEST = "exd_web_request";
	public static final String WEB_RESPONSE = "exd_web_response";
	public static final String WEB_OUT = "exd_web_out";
	public static final String WEB_REDIRECT = "exd_web_redirect";
	
	private static Properties link = null;		//  系统链接验证码配置
	
	/**获取请求参数Map*/
	public static Map<String, Object> getParameterMap(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		Enumeration<String> enumer = request.getParameterNames();
		while(enumer.hasMoreElements()){
			String key = enumer.nextElement();
			paraMap.put(key, request.getParameter(key));
		}
		paraMap.put(WebHelper.WEB_REQUEST, request);
		paraMap.put(WebHelper.WEB_RESPONSE, response);
		return paraMap;
	}
	
	/**验证链接合法性并获取请求参数Map
	 * 请求中参数xml的格式如下(传参时没有换行与标签间的空格)：
	 * <appName>
	 * 	<param name="appName" value="inc"/>
	 * 	......
	 * </appName>
	 * */
	public static Map<String, Object> getParameterMapByLink(HttpServletRequest request, HttpServletResponse response){
		String xml = request.getParameter("xml");
		String verify = request.getParameter("verify");
		Document document = checkSafeLinkDom(xml, verify);
		if(document != null){
			// 参数匹配通过，解析xml生成Map
			List<?> pNode =  document.getRootElement().elements();
			Map<String, Object> paraMap = new HashMap<String, Object>();
			for(int i=0; i<pNode.size(); i++){
				Element el = (Element)pNode.get(i);
				paraMap.put(el.attributeValue("name"), el.attributeValue("value"));
			}
			paraMap.put(WebHelper.WEB_REQUEST, request);
			paraMap.put(WebHelper.WEB_RESPONSE, response);
			return paraMap;
		}else{
			return null;
		}
	}
	
	/**
	 * 验证安全连接
	 * 如果通过，返回参数Document
	 * */
	private static Document checkSafeLinkDom(String xml, String verify){
		logger.info("[exd-req-xml] : "+xml);
		logger.info("[exd-req-verify] : "+verify);
		if(xml==null || verify == null){
			return null;
		}
		try{
			Document document = DocumentHelper.parseText(xml);
			String appName = document.getRootElement().getName();
			if(link == null){
				link = ConfigLoader.loadFromProperties("classpath:link.properties");
			}
			String checkCode = link.getProperty("link."+appName+".check_code");
			logger.info("[exd-req-checkCode] : "+checkCode);
			if(checkCode == null){
				return null;
			}
			if(Encrypt.md5(xml+checkCode,null).equals(verify)){
				return document;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**执行web请求
	 * @throws IOException */
	public static String processResult(Map<String, Object>results, HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		// 普通web输出(ajax)
		if(results.get(WEB_OUT) != null){
			response.getWriter().print(results.get(WEB_OUT));
		}
		// 重定向
		else if(results.get(WEB_REDIRECT) != null){
			return results.get(WEB_REDIRECT).toString();
		}
		return null;
	}
}
