package com.yaochufa.apijava.recsys.function.collabfilter;

import org.apache.spark.api.java.function.Function;

import redis.clients.jedis.Jedis;

import com.yaochufa.apijava.recsys.constant.CollabFilterConst;
import com.yaochufa.apijava.recsys.util.RedisClient;

public class FilterInActiveUser implements  Function<Integer, Boolean>{

	@Override
	public Boolean call(Integer uid) throws Exception {
		Jedis jedis=null;
		try {
			 jedis=RedisClient.getJedis();
			if(!jedis.exists(CollabFilterConst.ACTIVE_USER_KEY_PRIFIX+uid)){
				return false;
			}
		}finally{
			RedisClient.returnResource(jedis);
		}
		return true;
	}
	
}
