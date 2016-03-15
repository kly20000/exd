package test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.exd.core.ServiceAnalyzer;

public class BaseTest {
	@Test
	public void Test(){
		ApplicationContext context=new ClassPathXmlApplicationContext("ApplicationContext.xml");
		ServiceAnalyzer sa = (ServiceAnalyzer)context.getBean("analyzerProxy");
		
		//sa.analyze("sf-order");
		//sa.analyze("sf-order-route");
		//sa.analyze("sf-order-search");
		sa.analyze("test",null);
		//sa.analyze("sqlfile");
	}
}
