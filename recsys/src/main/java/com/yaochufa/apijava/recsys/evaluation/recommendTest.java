package com.yaochufa.apijava.recsys.evaluation;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.evaluation.RegressionMetrics;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

import scala.Tuple2;

import com.yaochufa.apijava.recsys.etl.DataTransformer;
import com.yaochufa.apijava.recsys.etl.PurchaseDataTransformer;
import com.yaochufa.apijava.recsys.trans.ALSCollabFiterTransor;

public class recommendTest {

		
	public static void main(String[] args) {
		testSimpleResult();
	}
	
	public static void testSimpleResult(){
		SparkConf conf = new SparkConf().setAppName("Collaborative Filtering with Mysql").set("spark.master", "local[4]");
		JavaSparkContext jssc = new JavaSparkContext(conf);
		DataTransformer tf=new PurchaseDataTransformer();
		String directoryPath="data/csv/orders_sample.csv";
		JavaRDD<Rating> ratings=tf.transform(jssc, directoryPath);
		List<Rating> res=ratings.keyBy(new Function<Rating, Integer>() {

			@Override
			public Integer call(Rating v1) throws Exception {
				// TODO Auto-generated method stub
				return v1.user();
			}
		}).lookup(1895576);
		StringBuilder ps=new StringBuilder();
		for(int i=0;i<res.size();i++){
			ps.append(res.get(i).product()+",");
			
		}
		if(ps.length()>0){
			ps.deleteCharAt(ps.length()-1);
		}
		System.out.println("uid:1895576,购买产品:"+ps.toString());
	}
	
	public static void evaluation(){
		SparkConf conf = new SparkConf().setAppName("Collaborative Filtering with Mysql").set("spark.master", "local[4]");
		JavaSparkContext jssc = new JavaSparkContext(conf);
		DataTransformer tf=new PurchaseDataTransformer();
		String directoryPath="data/csv/orders_sample.csv";
		JavaRDD<Rating> ratings=tf.transform(jssc, directoryPath);
		ALSCollabFiterTransor.Builder builder=new ALSCollabFiterTransor.Builder();
		ALSCollabFiterTransor transor=builder.build();
		MatrixFactorizationModel model=transor.tranImplicit(ratings);
		JavaPairRDD<Integer, Integer> usersProducts= ratings.mapToPair(new PairFunction<Rating, Integer, Integer>() {

			@Override
			public Tuple2<Integer, Integer> call(Rating t) throws Exception {
				// TODO Auto-generated method stub
				return new Tuple2<Integer, Integer>(t.user(),t.product());
			}
		});
		JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions= model.predict(usersProducts).mapToPair(new PairFunction<Rating, Tuple2<Integer, Integer>, Double>() {

			@Override
			public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating t)
					throws Exception {
				return new Tuple2<Tuple2<Integer,Integer>, Double>(new Tuple2<Integer, Integer>(t.user(),t.product()), t.rating());
			}
		});
		JavaPairRDD<Tuple2<Integer, Integer>, Tuple2<Double, Double>>  ratingsAndPredictions=ratings.mapToPair(new PairFunction<Rating, Tuple2<Integer, Integer>, Double>() {

			@Override
			public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating t)
					throws Exception {
				return new Tuple2<Tuple2<Integer,Integer>, Double>(new Tuple2<Integer, Integer>(t.user(),t.product()), t.rating());
			}
		}).join(predictions);
		JavaRDD<Tuple2<Object, Object>> predictedAndtrues= ratingsAndPredictions.map(new Function<Tuple2<Tuple2<Integer,Integer>,Tuple2<Double,Double>>, Tuple2<Object,Object>>() {

			@Override
			public Tuple2<Object, Object> call(
					Tuple2<Tuple2<Integer, Integer>, Tuple2<Double, Double>> v1)
					throws Exception {
				return new Tuple2<Object, Object>(v1._2()._2,v1._2()._1);
			}
		});
		RegressionMetrics regressionMetrics=new RegressionMetrics(predictedAndtrues.rdd());
		System.out.println("Mean Squared Error="+regressionMetrics.meanSquaredError());
		System.out.println("Root Mean Squared Error="+regressionMetrics.rootMeanSquaredError());
	}
}
