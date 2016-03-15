package com.exd.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exd.core.ServiceAnalyzer;

public class SocketAnalyzerThread extends Thread {
	private static Logger logger = LoggerFactory.getLogger(SocketAnalyzerThread.class);
	// 客户端的socket
	private Socket clientSocket;

	// IO句柄
	private BufferedReader sin;
	private PrintWriter sout;
	
	// service分析器
	private ServiceAnalyzer serviceAnalyzer;

	// 默认的构造函数
	public SocketAnalyzerThread() {
	}

	public SocketAnalyzerThread(Socket s, ServiceAnalyzer serviceAnalyzer) throws IOException {
		this.serviceAnalyzer = serviceAnalyzer;
		this.clientSocket = s;
		// 初始化sin和sout的句柄

		sin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);

		// 开启线程
		start();
	}

	// 线程执行的主体函数
	public void run()
	{
		try
		{
			// 用循环来监听通讯内容
			while(true)
			{
				String str = sin.readLine();
				// 如果接收到的是byebye，退出本次通讯
				if(str.equals("exit") || clientSocket.isClosed()){
					sout.println("Bye!");
					break;
				}

				Map<String, Object> commond = getCommondParameters(str);
				String serviceName = commond.get("serviceName").toString();
				if(serviceName == null){
					throw new RuntimeException("No service name exception");
				}
				Map<String, Object> out = serviceAnalyzer.analyze(serviceName, commond);

				logger.info("In Server reveived the info: [" + str + "]");
				//sout.println(str);
				if(out.size() == 0){
					sout.println("execute success !");
				}else{
					sout.println(out);
				}
				
			}
			logger.info("closing the server socket!");
		}
		catch (Exception e)
		{
			sout.println("error commond !");
		}
		finally
		{
			logger.info("close the client socket and the io.");
			try
			{
				sin.close();
				sout.close();
				clientSocket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**分析命令中的输入参数*/
	public Map<String, Object> getCommondParameters(String commondLine){
		Map<String, Object> result = new HashMap<String, Object>();
		commondLine = commondLine.trim();
		if(commondLine.startsWith("service")){
			String[] lineParts = commondLine.replaceFirst("service", "").trim()
								.replaceFirst("-","")
								.replaceAll("/s+", " ").split("-");
			for(String part : lineParts){
				int seprater = part.indexOf(" ");
				result.put(part.substring(0,seprater), part.substring(seprater+1));
			}
		}
		return result;
	}

}
