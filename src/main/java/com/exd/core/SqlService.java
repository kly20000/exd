package com.exd.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.jdbc.core.JdbcTemplate;

import com.exd.utl.IOUtil;
/**
 * SQL服务解析器
 * @author YangWei
 * */
public class SqlService extends BaseService{

	private JdbcTemplate jdbcTemplate;

	private String sqlContent;		// sql语句
	private String source;			// sql文件地址
	private String separator = ";";	// sql文件分隔符
	
	/**
	 * 解析sql标签服务
	 * */
	public void beforeExecute(Element xml) {
		// 解析sql语句
		if(xml.element("content") != null){
			sqlContent = parseAttribute(xml.element("content").getText());
		}
		setJdbcTemplate((JdbcTemplate)this.getServiceAnalyzer().getSources("datasource"));
	}

	/**
	 * 多结果集参数转换
	 * */
	protected Object transformResult(Object res, String key, String type){
		String seprator = "string".equals(type) ? "'" : "";
		if(res instanceof List){
			List<Map<String, String>> result = (List<Map<String, String>>)res;
			StringBuilder sb = new StringBuilder();
			for(Map<String, String> row : result){
				sb.append(",");
				sb.append(seprator);
				sb.append(row.get(key));
				sb.append(seprator);
			}
			return sb.length()>0? sb.substring(1):"";
		}
		return res.toString();
	}
	
	/**
	 * 执行sql标签服务
	 * */
	public void execute() {
		// 执行sql文件
		if(this.source != null){
			getLogger().info("start executing sql file "+getName()+" : "+this.source);
			executeSqlFile();
		}
		// 执行单挑sql语句
		else{
			getLogger().info("start executing sql "+getName()+" : "+this.sqlContent);
			if(this.sqlContent.trim().startsWith("select")){
				getParams().put(getName(), this.jdbcTemplate.queryForList(this.sqlContent));
				getLogger().info(getParams().get(getName()).toString());
			}
			else{
				this.jdbcTemplate.update(this.sqlContent);
			}
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 执行sql文件
	 * */
	private void executeSqlFile(){
		try {
			List<String> sqls = new ArrayList<String>();
			int index = 0;
			BufferedReader br = new BufferedReader(new InputStreamReader(IOUtil.readFromFile(source)));
			StringBuilder sb = new StringBuilder();
			for(String line = br.readLine(); line != null; line=br.readLine()){
				line = line.trim();
				if(line.startsWith("--") || "".equals(line)) {
					continue;
				}
				if(line.lastIndexOf(separator) == line.length()-1){
					sb.append(line);
					sqls.add(sb.toString());
					index ++ ;
					sb = new StringBuilder("");
					if(index > 99){
						jdbcTemplate.batchUpdate(sqls.toArray(new String[0]));
						sqls.clear();
						index = 0;
					}
				}else{
					sb.append(line);
					sb.append(" ");
					continue;
				}
			}
			if(sqls.size() > 0){
				jdbcTemplate.batchUpdate(sqls.toArray(new String[0]));
				sqls.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setSource(String source) {
		this.source = source;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
}
