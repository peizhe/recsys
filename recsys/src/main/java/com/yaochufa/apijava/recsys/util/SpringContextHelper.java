package com.yaochufa.apijava.recsys.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class SpringContextHelper{
	
	private static ApplicationContext context;
	
	
	public static void init(){
		context=new ClassPathXmlApplicationContext("classpath:spring/applicationContext*.xml");
	}
	

	public static Object getBean(String name) {
		return context.getBean(name);
	}
	
	public static <T> T getBean(String name, Class<T> requiredType){
		return context.getBean(name,requiredType);
	}

	public static ApplicationContext getContext() {
		return context;
	}
	
	public static void main(String[] args)
	{
		
	}

}
