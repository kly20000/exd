package com.exd.web;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Element;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.exd.utl.IOUtil;

public class UploadService extends WebService {

	private HttpServletRequest request;
	private int size = 0;	// 限制文件大小
	private String key;		// 文件参数名称
	
	public void beforeExecute(Element xml) {
		request = getRequest();
	}
	
	public void execute() {
		try{
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			multipartRequest.setCharacterEncoding("UTF-8");
			Map<String, MultipartFile> files = multipartRequest.getFileMap();
			Set<Entry<String, MultipartFile>> set = files.entrySet();
			for (Entry<String, MultipartFile> bean : set) {
			    // 上传存储附件
				MultipartFile file = null;
				if(key == null || key.equals(bean.getKey())){
					file = bean.getValue();
				}else{
					continue;
				}
				if(size > 0 && file.getSize() > size){
					getParams().put(WebHelper.WEB_OUT, "{\"error\":\"out of size\",\"code\":4001}");
					break;
				}else{
					IOUtil.writeToFile(file.getInputStream(), getFilePath(getOutpath()));
					getParams().put(getName(), file);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setSize(String size) {
		try{
			this.size = Integer.parseInt(size);
		}catch(Exception e){
			this.size = 0;
		}
	}

	public void setKey(String key) {
		this.key = key;
	}
}
