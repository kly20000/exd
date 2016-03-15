package com.exd.net;


/**
 * 网络服务端
 * @author yangwei
 * */
public interface Server {
	
	public void init();
	
	public void service();
	
	public void destroy();
}
