package com.yaochufa.apijava.recsys.function.collabfilter;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

import redis.clients.jedis.Jedis;

import com.yaochufa.apijava.recsys.constant.CollabFilterConst;
import com.yaochufa.apijava.recsys.util.RedisClient;

public class RecommendForEachUserTask implements Runnable{

	private int userId;
	private MatrixFactorizationModel model;

	public RecommendForEachUserTask(int userId, MatrixFactorizationModel model) {
		super();
		this.userId = userId;
		this.model = model;
	}

	@Override
	public void run() {
		Rating[] rating =model.recommendProducts(userId, CollabFilterConst.RECOMMEND_PRODUCT_NUM);
		if(rating!=null&&rating.length>0){
			Map<String,String> map=new HashMap<String,String>();
			for(Rating r:rating){
				map.put(Integer.toString(r.product()), Double.valueOf(r.rating()).floatValue()+"");
			}
			Jedis jedis=RedisClient.getJedis();
			try {
				jedis.hmset(CollabFilterConst.COLLAB_FILTER_USER_CACHE+userId, map);
				jedis.pexpire(CollabFilterConst.COLLAB_FILTER_USER_CACHE+userId, 3*24*3600*1000L);
			} finally{
				RedisClient.returnResource(jedis);
			}
			
		}	
		
	}

}
