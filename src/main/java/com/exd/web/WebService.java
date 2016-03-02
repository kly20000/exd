package com.exd.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exd.core.IOService;

abstract class WebService extends IOService{
	
	public HttpServletRequest getRequest(){
		return (HttpServletRequest)getParams().get(WebHelper.WEB_REQUEST);
	}
	
	public HttpServletResponse getResponse(){
		return (HttpServletResponse)getParams().get(WebHelper.WEB_RESPONSE);
	}
}
