package com.exd.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.dom4j.Element;

import com.exd.utl.IOUtil;

public class OutService extends IOService {

	private String outputPath;	// 输出文件路径

	public void beforeExecute(Element xml) {
		this.outputPath = getFilePath(getOutpath());
	}

	public void execute() {
		getParams().clear();
		StringBuilder sb = new StringBuilder();
		for(Iterator<String> it = this.getLocalParams().keySet().iterator(); it.hasNext();){
			String name = it.next();
			String value = this.getLocalParams().get(name);
			getParams().put(name, value);
			sb.append(value);
			sb.append("\r\n");
		}
		if(outputPath != null){
			writeToFile(sb);
		}
	}
	
	/**将内容写入文件*/
	public void writeToFile(Object content){
		InputStream is = new ByteArrayInputStream(content.toString().getBytes());
		IOUtil.writeToFile(is, outputPath);
	}
}
