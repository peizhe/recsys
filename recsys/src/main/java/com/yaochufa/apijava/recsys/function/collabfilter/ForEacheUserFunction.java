package com.yaochufa.apijava.recsys.function.collabfilter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

import redis.clients.jedis.Jedis;
import scala.Tuple2;

import com.yaochufa.apijava.recsys.util.RedisClient;

public class ForEacheUserFunction implements VoidFunction<Iterator<Tuple2<Object,double[]>>>{

	private   int recNum=100;
	private MatrixFactorizationModel model;
	private String COLLAB_FILTER_USER_CACHE="recsys:collab_filter_user:";
	public ForEacheUserFunction(MatrixFactorizationModel model,int recNum) {
		super();
		this.model = model;
		this.recNum=recNum;
	}

	@Override
	public void call(Iterator<Tuple2<Object, double[]>> t) throws Exception {
		Jedis jedis=RedisClient.getJedis();
		try {
			while(t.hasNext()){
				Tuple2<Object, double[]> tp=t.next();
				int uid=(int) tp._1();
				Rating[] rating =model.recommendProducts(uid, recNum);
				if(rating!=null&&rating.length>0){
					Map<String,String> map=new HashMap<String,String>();
					for(Rating r:rating){
						map.put(Integer.toString(r.product()), Double.valueOf(r.rating()).floatValue()+"");
					}
					
					jedis.hmset(COLLAB_FILTER_USER_CACHE+uid, map);
					jedis.pexpire(COLLAB_FILTER_USER_CACHE+uid, 3*24*3600*1000L);
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			RedisClient.returnResource(jedis);
		}
		
		
	}

}
