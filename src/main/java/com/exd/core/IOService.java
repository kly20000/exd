package com.exd.core;


/**
 * 输入/输出服务解析器基类
 * @author YangWei
 * */
public abstract class IOService extends BaseService {

	private String inpath;		// 输入路径
	private String outpath;		// 输出路径
	private String file;		// 输入/出文件

	public String getInpath() {
		return inpath;
	}

	public void setInpath(String inpath) {
		this.inpath = inpath;
	}

	public String getOutpath() {
		return outpath;
	}

	public void setOutpath(String outpath) {
		this.outpath = outpath;
	}

	public String getFilePath(String path) {
		if(this.file == null) 
			return null;
		else if(path == null)
			return this.file;
		else{
			return path+"/"+this.file;
		}
	}

	public void setFile(String file) {
		this.file = file;
	}
}
