package com.yaochufa.apijava.recsys.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yaochufa.apijava.recsys.collabfilter.CollabFilterDriver;
import com.yaochufa.apijava.recsys.util.GlobalVar;

@Component
public class CollabFilterTask {

	private final static Logger LOGGER=LoggerFactory.getLogger(CollabFilterTask.class);
	
	public  void run(){
		LOGGER.info("基于用户购买行为协同过滤 task started！");
		try (CollabFilterDriver cfcDriver = new CollabFilterDriver()) {
			cfcDriver.service();
			
		}
		LOGGER.info("基于用户购买行为协同过滤 task end！");
	}
}
