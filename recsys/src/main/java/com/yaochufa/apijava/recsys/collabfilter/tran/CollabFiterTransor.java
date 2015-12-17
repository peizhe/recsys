package com.yaochufa.apijava.recsys.collabfilter.tran;

import java.io.Serializable;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

public interface CollabFiterTransor extends Serializable{
	MatrixFactorizationModel tran(JavaRDD<Rating> ratings);
	
	MatrixFactorizationModel tranImplicit(JavaRDD<Rating> ratings);

}
