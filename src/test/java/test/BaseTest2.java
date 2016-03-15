package test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.exd.core.ServiceAnalyzer;

public class BaseTest2 {
	@Test
	public void Test(){
		ApplicationContext context=new ClassPathXmlApplicationContext("ApplicationContext.xml");
		/*QuartzAnalyzer sa = (QuartzAnalyzer)context.getBean("quartzAnalyzer");
		sa.init();*/
		ServiceAnalyzer sa = (ServiceAnalyzer)context.getBean("analyzerProxy");
		sa.analyze("alipay-query", null);
	}
	
	public static void main(String[] args){
		ApplicationContext context=new ClassPathXmlApplicationContext("ApplicationContext.xml");
	}
}
