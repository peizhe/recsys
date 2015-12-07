package com.yaochufa.apijava.recsys.util;

public class StringUtils {

	public static boolean isNotBlank(String s){
		return s!=null&&"".endsWith(s.trim());
	}
}
