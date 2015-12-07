package com.yaochufa.apijava.recsys.util;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class GlobalVar {
	
	public static Configuration properties = null;

	static {
		try {
			properties = new PropertiesConfiguration("spark_recommend.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}


	
	public static final String jdbcDriver =  properties
			.getString("jdbc.driverClassName");
	
	public static final String jdbcUrl =  properties
			.getString("jdbc.url");
	
	public static final String jdbcUsername =  properties
			.getString("jdbc.username");
	
	public static final String jdbcPassword =  properties
			.getString("jdbc.password");
	
	public static final String USER_PATH =  properties
			.getString("user.path");
	public static final String ORDER_PATH =  properties
			.getString("order.path");
	public static final String SPARKCONF_MASTER =  properties
			.getString("sparkConf.master");
}
