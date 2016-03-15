package com.exd.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exd.core.ServiceAnalyzer;


public class SocketAnalyzerServer implements Server{

	private static Logger logger = LoggerFactory.getLogger(SocketAnalyzerServer.class);
	private ServerSocket server = null;
	private int port;
	private boolean startOnBoot;
	private ServiceAnalyzer serviceAnalyzer;
	
	@Override
	public void init() {
		try {
			server = new ServerSocket(port);
			logger.info("Server has bean started !");
			if(startOnBoot){
				service();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			destroy();
		}
	}

	@Override
	public void destroy() {
		try {
			if(server != null){
				server.close();
				logger.info("Server has bean shutdown !!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void service() {
		try{
			while(true){
				//阻塞,直到有客户端连接
				Socket socket = server.accept();
				//通过构造函数，启动线程
				new SocketAnalyzerThread(socket, serviceAnalyzer);
			}
		}
		catch(IOException e){
			destroy();
		}
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setStartOnBoot(boolean startOnBoot) {
		this.startOnBoot = startOnBoot;
	}

	public void setServiceAnalyzer(ServiceAnalyzer serviceAnalyzer) {
		this.serviceAnalyzer = serviceAnalyzer;
	}

}
