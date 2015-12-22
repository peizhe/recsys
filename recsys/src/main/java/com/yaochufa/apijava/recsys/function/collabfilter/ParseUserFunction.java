package com.yaochufa.apijava.recsys.function.collabfilter;

import org.apache.hadoop.hbase.client.Result;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.Rating;

import scala.Tuple2;

public class ParseUserFunction implements Function<Tuple2<Object,double[]>, Integer>{

	@Override
	public Integer call(Tuple2<Object, double[]> v1) throws Exception {
		return (Integer) v1._1();
	}



}
