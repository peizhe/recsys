package com.yaochufa.apijava.recsys.trans.itemcf;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.jblas.DoubleMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import scala.Tuple2;

import com.yaochufa.apijava.lang.common.Pair;
import com.yaochufa.apijava.recsys.mapper.SearchMapper;
import com.yaochufa.apijava.recsys.model.ProductSimimlarity;
import com.yaochufa.apijava.recsys.trans.RecommendCache;
import com.yaochufa.apijava.recsys.util.GlobalVar;

@Component
public class MartixItemCf implements Serializable {

	@Autowired
	private RecommendCache recommendCache;

	private SearchMapper searchMapper;

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
		if (!searchMapper.exists(pid)) {
			return;
		}
		LimitInsertSort<Pair<Integer, Double>> imitInsertSort = new LimitInsertSort<Pair<Integer, Double>>(
				GlobalVar.ITEMCF_REC_PRODUCT_SIZE, new SimilarityComparator());
	
		for (Tuple2<Object, double[]> tp : list) {
			int toPid = (Integer) tp._1();
			if (pid.equals(toPid)) {
				continue;
			}
			if (!searchMapper.exists(toPid)) {
				continue;
			}
			double cos = consineSimilarity(
					new DoubleMatrix(productFeature._2()),
					new DoubleMatrix(tp._2()));
			if (!Double.isNaN(cos)) {
				imitInsertSort.insert(new Pair<Integer, Double>(toPid, cos));
			}
		}
		System.out.println("---------------------------");
		System.out.println(productFeature._1());
		imitInsertSort.printResult();
		recommendCache.cacheItemRecommends(pid,
				imitInsertSort.getResult());
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
