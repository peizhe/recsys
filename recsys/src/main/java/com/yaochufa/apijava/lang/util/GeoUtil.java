package com.yaochufa.apijava.lang.util;

public class GeoUtil
{
	/** 
	 * google maps的脚本里代码 
	 */    
	private static final double EARTH_RADIUS = 6378.137; 
	
	/** 
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米 
	 */ 
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) 
	{ 
	    double radLat1 = rad(lat1); 
	    double radLat2 = rad(lat2); 
	    double a = radLat1 - radLat2; 
	    double b = rad(lng1) - rad(lng2); 
	    double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2))); 
	    s = s * EARTH_RADIUS; 
	    s = Math.round(s * 10000) / 10000d; 
	    return s; 
	} 

	private static double rad(double d) 
	{ 
	     return d * Math.PI / 180.0; 
	}  

	public static void main(String[] args)
	{
		System.out.println(getDistance(29.490295,106.486654,29.615467,106.581515));
	}
}
