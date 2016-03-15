package com.exd.core;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时服务
 * @author YangWei
 * */
public class QuartzService implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		JobDataMap dm = arg0.getMergedJobDataMap();
		ServiceAnalyzer serviceAnalyzer = (ServiceAnalyzer)dm.get("analyzer");
		String serviceName = dm.getString("serviceName");
		serviceAnalyzer.analyze(serviceName, null);
	}
}
