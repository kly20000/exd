package test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.exd.core.ServiceAnalyzer;
import com.exd.core.ServiceAnalyzer;

public class BaseTest2 {
	@Test
	public void Test(){
		ApplicationContext context=new ClassPathXmlApplicationContext("ApplicationContext.xml");
		ServiceAnalyzer sa = (ServiceAnalyzer)context.getBean("analyzerProxy");
		
		sa.analyze("test",null);
		//sa.analyze("sqlfile");
	}
}
