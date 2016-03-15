package com.exd.core;

import java.util.Iterator;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时服务执行控制入口
 * @author YangWei
 * */
public class QuartzAnalyzer{
	
	private ServiceAnalyzer serviceAnalyzer;
	
	private Map<String, String> cron;
	
	/**初始化定时服务*/
	public void init(){
		if(cron == null || cron.size()==0){
			return;
		}
		for(Iterator<String> it = cron.keySet().iterator(); it.hasNext();){
			String serviceName = it.next();
			String time = cron.get(serviceName);
			startQuartz(serviceName, time);
		}
	}
	
	public void startQuartz(String sn, String cronTime){
		try {
			//得到默认的调度器
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			//开始调度任务
			scheduler.start();
			//定义当前调度器的具体作业对象
			JobDetail jobDetail = JobBuilder.newJob(QuartzService.class)
					.withIdentity(sn, "cronTriggerGrounp-"+sn)
					.build();
	        Map<String, Object> map = jobDetail.getJobDataMap();
	        map.put("serviceName", sn);
	        map.put("analyzer", serviceAnalyzer);
	        //作业的触发器
	        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
	        		.withIdentity("cronTrigger-"+sn, "cronTriggerGrounp-"+sn)
	        		.startNow()
	        		.withSchedule(CronScheduleBuilder.cronSchedule(cronTime)) //在任务调度器中，使用任务调度器的 CronScheduleBuilder 来生成一个具体的 CronTrigger 对象
	        		.build();
	        
	        //注册作业和触发器
	     	scheduler.scheduleJob(jobDetail, cronTrigger);
	     	
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setServiceAnalyzer(ServiceAnalyzer serviceAnalyzer) {
		this.serviceAnalyzer = serviceAnalyzer;
	}

	public void setCron(Map<String, String> cron) {
		this.cron = cron;
	}

}
