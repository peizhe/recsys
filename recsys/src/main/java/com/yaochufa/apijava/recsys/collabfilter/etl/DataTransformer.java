package com.yaochufa.apijava.recsys.collabfilter.etl;

import java.io.Serializable;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.Rating;

/**
 *数据转化器
 * @author root
 *
 */
public interface DataTransformer extends Serializable {
	
	/**
	 * 
	 * @param jsc 
	 * @param directoryPath  原始数据目录
	 * @return
	 */
	JavaRDD<Rating> transform(JavaSparkContext jsc,String directoryPath);
}
