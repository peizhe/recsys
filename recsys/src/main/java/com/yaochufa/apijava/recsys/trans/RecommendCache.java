package com.yaochufa.apijava.recsys.trans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.yaochufa.apijava.recsys.etl.Pair;


@Component
public class RecommendCache {

	@Resource(name="javaSerRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	private String COLLAB_FILTER_USER_CACHE="recsys:collab_filter_user";
	private String COLLAB_FILTER_ITEM_CACHE="recsys:collab_filter_item:";
	private int recNum=30;
	
	public void cacheUserRecommends(final MatrixFactorizationModel model,JavaRDD<Integer> users,final Set<String> filterSet){
		users.foreachPartition(new VoidFunction<Iterator<Integer>>() {
			@Override
			public void call(Iterator<Integer> it) throws Exception {
				Map<String,Pair<Double,Boolean>> map=new HashMap<String,Pair<Double,Boolean>>();
				
				while(it.hasNext()){
					int userId=it.next();
					Rating[] ratings=model.recommendProducts(userId,recNum);
					if(ratings!=null&&ratings.length>0){
						for(Rating r:ratings){
							String key=r.product()+":"+userId;
							map.put(key, new Pair<Double,Boolean>(r.rating(),filterSet.contains(key)));
						}
					
					}
				
				}
				redisTemplate.opsForHash().putAll(COLLAB_FILTER_USER_CACHE, map);
				redisTemplate.expire(COLLAB_FILTER_USER_CACHE, 3, TimeUnit.DAYS);
			}
			
		});
	}

	public void cacheItemRecommends(int productId,
			List<Pair<Integer, Double>> result) {
		redisTemplate.opsForValue().set(COLLAB_FILTER_ITEM_CACHE+productId, result);
		redisTemplate.expire(COLLAB_FILTER_ITEM_CACHE+productId, 3, TimeUnit.DAYS);
	}
	
	
}
