package com.exd.core;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.dom4j.Element;
/**
 * 流程控制服务解析器
 * @author YangWei
 * */
public class ExpService extends BaseService {

	private String expression;	// 条件表达式
	
	private ScriptEngineManager manager; // 脚本解析器
	
	public void beforeExecute(Element xml) {
		setServiceDom(xml);
		if(manager == null){
			manager = new ScriptEngineManager(); 
		}
	}

	public void execute() {
		Element exeEl = null;
		boolean exp = isExpression();
		getLogger().info("start executing expression : "+exp);
		if(exp){
			exeEl = getServiceDom().element("true");
		}else{
			exeEl = getServiceDom().element("false");
		}
		if(exeEl != null){
			getServiceAnalyzer().processServiceDocument(exeEl, getParams());
		}
	}
	
	/**
	 * 解析并执行expresssion
	 * */
	private boolean isExpression(){
		expression = expression.replaceAll(" and ", " && ")
								.replaceAll(" or ", " || ")
								.replaceAll(" @lt ", " < ")
								.replaceAll(" @lte ", " <= ")
								.replaceAll(" @gt ", " > ")
								.replaceAll(" @gte ", " >= ");
		getLogger().info("expression info : "+expression);
		ScriptEngine engine = manager.getEngineByName("JavaScript");  
        if(engine == null){  
            throw new RuntimeException("not found JavaScript engine!");  
        }
        Object result = "false";
		try {
			result = engine.eval(expression);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return Boolean.parseBoolean(result.toString());
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
