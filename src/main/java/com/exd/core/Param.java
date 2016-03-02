package com.exd.core;

import java.util.Map;

import com.exd.utl.Coder;

public class Param{
	private String scope;	// 空间名
	private String name;	// 参数名
	private String value;	// 参数值
	
	private String ref;		// 参数引用
	private String method;	// 取值方法（反射）
	
	private String target;	// 批处理引用关键字（暂停用）
	private String type;	// 批处理引用类型（暂停用）
	
	private String format;	// 内容格式化类型
	private String encode;	// 内容加密类型
	private String decode;	// 解密类型
	private String skey;	// 加/解密密钥
	
	private Coder coder;	// 可编程实例

	private Map<String, Object> paraMap;	// 服务参数Map
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		Object originValue = null;
		if(ref != null){
			Object obj = paraMap.get(ref);
			if(obj == null){
				return;
			}else{
				originValue = getParamValue(obj, method);
			}
		}
		if(originValue==null){
			if(value == null) return;
			if(value.startsWith("@")){
				value = coder.getValue(value.substring(1), skey).toString();
			}
			originValue = value;
		}
		if(format != null) originValue = coder.getValue(format, originValue).toString();
		if(encode != null) originValue = coder.getValue(encode, originValue, skey).toString();
		if(decode != null) originValue = coder.getValue(decode, originValue, skey).toString();
		this.value = originValue.toString();
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public String getScopeName() {
		return scope==null?name:scope+"."+name;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public void setParaMap(Map<String, Object> paraMap) {
		this.paraMap = paraMap;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getDecode() {
		return decode;
	}
	public void setDecode(String decode) {
		this.decode = decode;
	}
	public String getSkey() {
		return skey;
	}
	public void setSkey(String skey) {
		this.skey = skey;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	private Object getParamValue(Object obj, String mehod){
		if(method == null){
			if(obj instanceof String){
				return obj.toString();
			}else{
				return obj;
			}
		}else{
			try {
				return obj.getClass().getMethod(mehod).invoke(obj).toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return obj.toString();
		}
	}
	
	public void setCoder(Coder coder) {
		this.coder = coder;
	}
}
