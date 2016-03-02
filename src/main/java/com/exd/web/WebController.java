package com.exd.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.exd.core.ServiceAnalyzer;

@Controller
public class WebController {
	@Autowired
	private ServiceAnalyzer analyzer;
	
	@RequestMapping(value="/exdSafeService",method=RequestMethod.POST)
	public String executeService(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String, Object> paraMap = WebHelper.getParameterMapByLink(request, response);
		return service(paraMap, response);
	}
	
	@RequestMapping(value="/exdService")
	public String executeCommonService(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String, Object> paraMap = WebHelper.getParameterMap(request, response);
		return service(paraMap, response);
	}
	
	/**执行exd服务
	 * @param paraMap 从请求中获取的参数Map， serviceName必须有
	 * */
	private String service(Map<String, Object> paraMap, HttpServletResponse response) throws IOException{
		if(paraMap == null){
			return null;
		}
		Map<String, Object> result = analyzer.analyze(paraMap.get("serviceName").toString(), paraMap);
		return WebHelper.processResult(result, response);
	}
}
