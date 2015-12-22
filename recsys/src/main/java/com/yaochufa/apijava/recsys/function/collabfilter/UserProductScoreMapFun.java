package com.yaochufa.apijava.recsys.function.collabfilter;

import java.util.regex.Pattern;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.Rating;

public class UserProductScoreMapFun implements Function<Result, Rating>{

	private final static Pattern RE=Pattern.compile("_");
	
	
	@Override
	public Rating call(Result v1) throws Exception {
		String[] user_product=RE.split(Bytes.toString(v1.getRow()));
		double score=Double.parseDouble(new String(v1.getValue("cf1".getBytes(), "score".getBytes())));
		return new Rating(Integer.parseInt(user_product[0]), Integer.parseInt(user_product[1]), score);
	}

}
