package com.yaochufa.apijava.recsys.function.collabfilter;

import org.apache.spark.api.java.function.Function;

import redis.clients.jedis.Jedis;
import scala.Tuple2;

import com.yaochufa.apijava.recsys.constant.CollabFilterConst;
import com.yaochufa.apijava.recsys.util.RedisClient;

public class UserFetchFunction implements Function<Tuple2<Object,double[]>, Integer> {

	@Override
	public Integer call(Tuple2<Object, double[]> v1) throws Exception {
		return (Integer) v1._1();
	}

}
