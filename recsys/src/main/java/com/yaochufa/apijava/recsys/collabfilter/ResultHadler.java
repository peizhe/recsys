package com.yaochufa.apijava.recsys.collabfilter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.jblas.DoubleMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import scala.Tuple2;

import com.yaochufa.apijava.lang.common.Pair;
import com.yaochufa.apijava.recsys.function.collabfilter.FilterInActiveUser;
import com.yaochufa.apijava.recsys.function.collabfilter.ForEacheUserFunction;
import com.yaochufa.apijava.recsys.function.collabfilter.RecommendForEachUserTask;
import com.yaochufa.apijava.recsys.function.collabfilter.UserFetchFunction;
import com.yaochufa.apijava.recsys.mapper.SearchMapper;
import com.yaochufa.apijava.recsys.model.ProductSimimlarity;

@Component
public class ResultHadler implements Serializable {

	
	private SearchMapper searchMapper;
	
	@Resource(name="javaSerRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	private String COLLAB_FILTER_USER_CACHE="recsys:collab_filter_user:";
	private String COLLAB_FILTER_ITEM_CACHE="recsys:collab_filter_item:";
	private Set<Integer> validProduct;
	
	@Autowired
	public ResultHadler(SearchMapper searchMapper) {
		super();
		this.searchMapper = searchMapper;
		List<Integer> ids=this.searchMapper.selectAllWithIdList();
		validProduct=new HashSet<Integer>(ids);
	}

	public void userCf(MatrixFactorizationModel model,List<Integer> userList) {
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
//		JavaRDD<String> users=jssc.textFile(GlobalVar.USER_PATH);
//		List<String> userList=users.collect();
		int e=10000;
		int i=0;
		int len=userList.size();
		
		List<Integer> temp=null;
		do{
			int endIx=i+e;
			if(endIx<=len){
				temp=userList.subList(i, endIx);
			}else{
				temp=userList.subList(i, len);
			}
			for(Integer uid:temp){

				Rating[] rating=null;
				try {
					rating=model.recommendProducts(uid, 100);
				} catch (Exception e2) {
					System.out.println(uid);
					Map<String,Float> map=new HashMap<String,Float>();
					for(Rating r:rating){
						map.put(Integer.toString(r.product()), Double.valueOf(r.rating()).floatValue());
					}
					redisTemplate.opsForHash().putAll(COLLAB_FILTER_ITEM_CACHE+uid,map);
					redisTemplate.expire(COLLAB_FILTER_ITEM_CACHE+uid, 3, TimeUnit.DAYS);
					continue;
				}
			
//				UserProductRecommend re=resToEntitys(rating,uid);
//				if(re!=null){
//					list.add(re);
//				}
				if(rating!=null&&rating.length>0){
					Map<String,Float> map=new HashMap<String,Float>();
					for(Rating r:rating){
						map.put(Integer.toString(r.product()), Double.valueOf(r.rating()).floatValue());
					}
					redisTemplate.opsForHash().putAll(COLLAB_FILTER_USER_CACHE+uid,map);
					redisTemplate.expire(COLLAB_FILTER_USER_CACHE+uid, 3, TimeUnit.DAYS);
				}	
				
			}
			i+=e;
		}while(i<len);
		
	}
	
	public void userCf(final MatrixFactorizationModel model,JavaRDD<Tuple2<Object, double[]>>  userList) {
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
//		JavaRDD<String> users=jssc.textFile(GlobalVar.USER_PATH);
//		List<String> userList=users.collect();
		List<Integer> users=userList.map(new UserFetchFunction()).filter(new FilterInActiveUser()).collect();
		ExecutorService ex=Executors.newFixedThreadPool(4);
		for(Integer userId :users){
			ex.execute(new RecommendForEachUserTask(userId, model));
		}
		try {
			ex.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ex.shutdown();
		
		
	}
	
	
	
	/**
	 * 计算两个向量的余弦相似度
	 * 
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	public double consineSimilarity(DoubleMatrix vec1, DoubleMatrix vec2) {
		return vec1.dot(vec2) / (vec1.norm2() * vec2.norm2());
	}

	public double consineSimilarity(Tuple2<DoubleMatrix, DoubleMatrix> t) {
		return consineSimilarity(t._1(), t._2());
	}

	public List<ProductSimimlarity> itemcf(MatrixFactorizationModel model) {
		final Date date = new java.util.Date();
		JavaRDD<Tuple2<Integer, DoubleMatrix>> productMatrixs = model
				.productFeatures()
				.toJavaRDD()
				.map(new Function<Tuple2<Object, double[]>, Tuple2<Integer, DoubleMatrix>>() {

					@Override
					public Tuple2<Integer, DoubleMatrix> call(
							Tuple2<Object, double[]> v1) throws Exception {
						// TODO Auto-generated method stub
						return new Tuple2<Integer, DoubleMatrix>((Integer) v1
								._1(), new DoubleMatrix(v1._2()));
					}
				});
		return productMatrixs
				.cartesian(productMatrixs)
				.filter(new Function<Tuple2<Tuple2<Integer, DoubleMatrix>, Tuple2<Integer, DoubleMatrix>>, Boolean>() {

					@Override
					public Boolean call(
							Tuple2<Tuple2<Integer, DoubleMatrix>, Tuple2<Integer, DoubleMatrix>> v1)
							throws Exception {
						return v1._1._1 != v1._2()._1();
					}
				})
				.mapToPair(
						new PairFunction<Tuple2<Tuple2<Integer, DoubleMatrix>, Tuple2<Integer, DoubleMatrix>>, Tuple2<Integer, Integer>, Tuple2<DoubleMatrix, DoubleMatrix>>() {

							@Override
							public Tuple2<Tuple2<Integer, Integer>, Tuple2<DoubleMatrix, DoubleMatrix>> call(
									Tuple2<Tuple2<Integer, DoubleMatrix>, Tuple2<Integer, DoubleMatrix>> t)
									throws Exception {
								Tuple2<Integer, Integer> key = null;
								Tuple2<DoubleMatrix, DoubleMatrix> val = null;
								if (t._1()._1() < t._2()._1()) {
									key = new Tuple2<Integer, Integer>(t._1()
											._1(), t._2()._1());
									val = new Tuple2<DoubleMatrix, DoubleMatrix>(
											t._1()._2(), t._2()._2());
								} else {
									key = new Tuple2<Integer, Integer>(t._2()
											._1(), t._1()._1());
									val = new Tuple2<DoubleMatrix, DoubleMatrix>(
											t._2()._2(), t._1()._2());
								}
								return new Tuple2<Tuple2<Integer, Integer>, Tuple2<DoubleMatrix, DoubleMatrix>>(
										key, val);
							}
						})
				.distinct()
				.map(new Function<Tuple2<Tuple2<Integer, Integer>, Tuple2<DoubleMatrix, DoubleMatrix>>, ProductSimimlarity>() {

					@Override
					public ProductSimimlarity call(
							Tuple2<Tuple2<Integer, Integer>, Tuple2<DoubleMatrix, DoubleMatrix>> tp)
							throws Exception {
						ProductSimimlarity entity = new ProductSimimlarity();
						// entity.setCreatedAt(date);
						entity.setProductIdA(tp._1()._1());
						entity.setProductIdB(tp._1()._2());
						entity.setSimilarity((float) consineSimilarity(tp._2()));
						entity.setCreatedAt(date);
						return entity;
					}
				}).collect();
	}

	public void itemcfLocal(List<Tuple2<Object, double[]>> list) {
		for (Tuple2<Object, double[]> tup : list) {
			computerSimilarity(tup, list);
		}
	}

	private void computerSimilarity(Tuple2<Object, double[]> productFeature,
			List<Tuple2<Object, double[]>> list) {
		Integer pid = (Integer) productFeature._1();
		if (!validProduct.contains(pid)) {
			return;
		}
//		LimitInsertSort<Pair<Integer, Double>> imitInsertSort = new LimitInsertSort<Pair<Integer, Double>>(
//				GlobalVar.ITEMCF_REC_PRODUCT_SIZE, new SimilarityComparator());
//	
		Map<String,Float> map=new HashMap<String,Float>();
		for (Tuple2<Object, double[]> tp : list) {
			Integer toPid = (Integer) tp._1();
			if (pid.equals(toPid)) {
				continue;
			}
			if (!validProduct.contains(pid)) {
				continue;
			}
			double cos = consineSimilarity(
					new DoubleMatrix(productFeature._2()),
					new DoubleMatrix(tp._2()));
			if (!Double.isNaN(cos)) {
//				imitInsertSort.insert(new Pair<Integer, Double>(toPid, cos));
				map.put(toPid.toString(),Double.valueOf(cos).floatValue());
			}
		}
		redisTemplate.opsForHash().putAll(COLLAB_FILTER_ITEM_CACHE+pid,map);
		redisTemplate.expire(COLLAB_FILTER_ITEM_CACHE+pid, 3, TimeUnit.DAYS);
//		System.out.println(productFeature._1());
//		imitInsertSort.printResult();
//		recommendCache.cacheItemRecommends(pid,
//				imitInsertSort.getResult());
	}

	private class SimilarityComparator implements
			Comparator<Pair<Integer, Double>> {

		@Override
		public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
			double diff = o1.getSecond() - o2.getSecond();
			return diff > 0 ? 1 : diff == 0 ? 0 : -1;
		}

	}

	/*
	 * foreachPartition(new
	 * VoidFunction<Iterator<Tuple2<Tuple2<Integer,Integer>,
	 * Tuple2<DoubleMatrix,DoubleMatrix>>>>() {
	 * 
	 * @Override public void call( Iterator<Tuple2<Tuple2<Integer, Integer>,
	 * Tuple2<DoubleMatrix, DoubleMatrix>>> t) throws Exception {
	 * Tuple2<Tuple2<Integer, Integer>, Tuple2<DoubleMatrix, DoubleMatrix>>
	 * tp=null; while(t.hasNext()){ tp=t.next(); ProductSimilarity entity=new
	 * ProductSimilarity(); entity.setCreatedAt(date);
	 * entity.setProductIdA(tp._1()._1()); entity.setProductIdB(tp._1()._2());
	 * entity.setSimilarity((float) consineSimilarity(tp._2()));
	 * productSimilarityDao.insert(entity); } } })
	 */
}
