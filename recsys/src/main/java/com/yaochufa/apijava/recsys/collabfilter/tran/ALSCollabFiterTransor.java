package com.yaochufa.apijava.recsys.collabfilter.tran;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.data.redis.core.RedisTemplate;

import scala.Tuple2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yaochufa.apijava.recsys.mapper.UserProductRecommendMapper;
import com.yaochufa.apijava.recsys.model.UserProductRecommend;
import com.yaochufa.apijava.recsys.util.SpringContextHelper;

public class ALSCollabFiterTransor implements CollabFiterTransor{

	/*
	 * ratings: RDD[Rating], rank: Int, iterations: Int, lambda: Double,
	 * blocks: Int, alpha: Double, seed: Long
	 */
	private  int rank;
	private  int iterations;
	private  double lambda ;
	private  int blocks;
	private  double alpha ;
	
//	private UserProductRecommendDao dao=new UserProductRecommendDao();
	private UserProductRecommendMapper userProductRecommendMapper=SpringContextHelper.getBean("userProductRecommendMapper", UserProductRecommendMapper.class);
	@Resource(name="javaSerRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	public ALSCollabFiterTransor(int rank, int iterations, double lambda,
			int blocks, double alpha) {
		super();
		this.rank = rank;
		this.iterations = iterations;
		this.lambda = lambda;
		this.blocks = blocks;
		this.alpha = alpha;
	}

	@Override
	public MatrixFactorizationModel tran(JavaRDD<Rating> ratings) {
		return ALS.train(ratings.rdd(), rank, iterations, lambda,blocks);
	}

	@Override
	public MatrixFactorizationModel tranImplicit(JavaRDD<Rating> ratings) {
		return ALS.trainImplicit(ratings.rdd(), rank, iterations, lambda,blocks,alpha);
	}


	
	private UserProductRecommend resToEntitys(Rating[] rating,int uid) {
		if(rating!=null&&rating.length>0){
			UserProductRecommend e=new UserProductRecommend();
			e.setUserId(uid);
			JSONArray pids=new JSONArray();
			JSONObject temp=null;
			for(Rating r:rating){
				temp=new JSONObject();
				temp.put("pid", r.product());
				temp.put("rating", r.rating());
				pids.add(temp);
			}
			e.setProductIds( pids.toJSONString());
			return e;
		}
		return null;
	}


	public static class Builder{
		private static int RANK_DEF = 10;
		private static int ITERATIONS_DFF = 10;
		private static double LAMBDA_DFF = 0.01;
		private static int BLOCKS_DFF = -1;
		private static double ALPHA_DFF = 0.01;
		private  int rank;
		private  int iterations;
		private  double lambda ;
		private  int blocks;
		private  double alpha ;
		
		
		
		public Builder() {
			super();
			this.rank = RANK_DEF;
			this.iterations = ITERATIONS_DFF;
			this.lambda = LAMBDA_DFF;
			this.blocks = BLOCKS_DFF;
			this.alpha = ALPHA_DFF;
		}



		public int getRank() {
			return rank;
		}



		public Builder setRank(int rank) {
			this.rank = rank;
			return this;
		}



		public int getIterations() {
			return iterations;
		}



		public Builder setIterations(int iterations) {
			this.iterations = iterations;
			return this;
		}



		public double getLambda() {
			return lambda;
		}



		public Builder setLambda(double lambda) {
			this.lambda = lambda;
			return this;
		}



		public int getBlocks() {
			return blocks;
		}



		public Builder setBlocks(int blocks) {
			this.blocks = blocks;
			return this;
		}



		public double getAlpha() {
			return alpha;
		}



		public Builder setAlpha(double alpha) {
			this.alpha = alpha;
			return this;
		}



		public ALSCollabFiterTransor build(){
			return new ALSCollabFiterTransor(rank, iterations, lambda, blocks, alpha);
		}
	}
	static class FeaturesToString implements Function<Tuple2<Object, double[]>, String> {
	    
	    public String call(Tuple2<Object, double[]> element) {
	      return element._1() + "," + Arrays.toString(element._2());
	    }
	  }

}
