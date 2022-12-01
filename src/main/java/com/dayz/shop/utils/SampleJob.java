package com.dayz.shop.utils;

import com.dayz.shop.service.ClearSetService;
import com.dayz.shop.service.ClearVipService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class SampleJob extends QuartzJobBean {

	private ClearVipService clearVipService;
	private ClearSetService clearSetService;

	private String name;

	@Autowired
	public SampleJob(ClearVipService clearVipService, ClearSetService clearSetService) {
		this.clearVipService = clearVipService;
		this.clearSetService = clearSetService;
	}

	// Внедряем свойство данных "name" задания
	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		//TODO
	}

}