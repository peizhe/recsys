package com.yaochufa.apijava.recsys.trans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import scala.Tuple2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yaochufa.apijava.recsys.mapper.UserProductRecommendMapper;
import com.yaochufa.apijava.recsys.model.UserProductRecommend;
import com.yaochufa.apijava.recsys.util.GlobalVar;
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
	@Autowired
	private UserProductRecommendMapper userProductRecommendMapper=SpringContextHelper.getBean("userProductRecommendMapper", UserProductRecommendMapper.class);
	
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

	@Override
	public void save(MatrixFactorizationModel model,JavaSparkContext jssc) {
		
//		String outputDir="data";
//		Rating[] res=model.recommendProducts(17720, 2);
//		String p="";
//		for(Rating r:res){
//			p+=r.product()+",";
//		}
//		System.out.println("17720:"+p);
//		model.userFeatures().toJavaRDD().map(new FeaturesToString()).saveAsTextFile(
//		        outputDir + "/userFeatures");
//		    model.productFeatures().toJavaRDD().map(new FeaturesToString()).saveAsTextFile(
//		        outputDir + "/productFeatures");
//		    System.out.println("Final user/product features written to " + outputDir);
		JavaRDD<String> users=jssc.textFile(GlobalVar.USER_PATH);
		List<String> userList=users.collect();
		int e=10000;
		int i=0;
		int len=userList.size();
		List<String> temp=null;
		do{
			int endIx=i+e;
			if(endIx<=len){
				temp=userList.subList(i, endIx);
			}else{
				temp=userList.subList(i, len);
			}
			List<UserProductRecommend> list=new ArrayList<UserProductRecommend>();
			for(String _uid:temp){
				if(StringUtils.isNotBlank(_uid)){
					int uid=Integer.parseInt(_uid);
					Rating[] rating=null;
					try {
						rating=model.recommendProducts(uid, 50);
					} catch (Exception e2) {
						System.out.println(uid);
						continue;
					}
				
					UserProductRecommend re=resToEntitys(rating,uid);
					if(re!=null){
						list.add(re);
					}
				}
			}
			userProductRecommendMapper.batchInsert(list);
			i+=e;
		}while(i<len);
		
	
		
		
		
		
	
		
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
