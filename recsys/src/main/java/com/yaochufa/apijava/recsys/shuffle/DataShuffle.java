package com.yaochufa.apijava.recsys.shuffle;

import java.io.Serializable;

import org.apache.spark.api.java.JavaSparkContext;

public interface DataShuffle extends Serializable {

	public void shuffle(JavaSparkContext jssc);
}
