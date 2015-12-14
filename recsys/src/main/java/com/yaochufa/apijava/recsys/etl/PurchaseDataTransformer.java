package com.yaochufa.apijava.recsys.etl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.stereotype.Component;

import com.yaochufa.apijava.lang.common.Pair;

import scala.Tuple2;

@Component
public class PurchaseDataTransformer implements DataTransformer {

	private static final Pattern COMMA = Pattern.compile(",");
	private long THREE_MONTH_DIFF=3*30*24*3600000;
	private long SIX_MONTH_DIFF=6*30*24*3600000;
	private long TWELVE_MONTH_DIFF=12*30*24*3600000;
	
	
	@Override
	public JavaRDD<Rating> transform(JavaSparkContext jsc,
			String directoryPath) {
		JavaRDD<String> originData= jsc.textFile(directoryPath);
		final long now =System.currentTimeMillis();
		 JavaRDD<Rating>  ratings=originData.filter(new Function<String, Boolean>() {
			
			@Override
			public Boolean call(String line) throws Exception {
				String[] tok = COMMA.split(line);
				return tok.length==3;
			}
		}).mapToPair(new PairFunction<String, Pair<Integer, Integer>, Double>() {

			private final String FORMET="yyy-MM-dd HH:mm:ss";
			
			@Override
			public Tuple2<Pair<Integer, Integer>, Double> call(String line)
					throws Exception {
				String[] tok = COMMA.split(line);
				try {
					Pair<Integer, Integer> k=new Pair<Integer, Integer>(Integer.parseInt(tok[0].substring(1, tok[0].length()-1)), Integer.parseInt(tok[1].substring(1, tok[1].length()-1)));
					Double v=implicitScore(tok[2].substring(1, tok[2].length()-1));
					return new Tuple2<Pair<Integer,Integer>, Double>(k, v);
				} catch (Exception e) {
					System.out.println(tok[0]+","+tok[1]+","+tok[2]);
					e.printStackTrace();
					return new Tuple2<Pair<Integer,Integer>, Double>(new Pair<Integer, Integer>(0, 0), 0d);
				}
				
			}

			private Double implicitScore(String time) {
				double score=1d;
				long date=0L;
				try {
					date=new SimpleDateFormat(FORMET).parse(time).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return score;
				}
				long diff=now-date;
				if(diff<=THREE_MONTH_DIFF){
					return score*1.3;
				}
				if(diff>THREE_MONTH_DIFF&&diff<=SIX_MONTH_DIFF){
					return score*1.2;
				}
				if(diff>SIX_MONTH_DIFF&&diff<=TWELVE_MONTH_DIFF){
					return score*1.1;
				}
				if(diff>TWELVE_MONTH_DIFF){
					return score;
				}
				return score;
			}
		}).reduceByKey(new Function2<Double, Double, Double>() {
			
			@Override
			public Double call(Double v1, Double v2) throws Exception {
				// TODO Auto-generated method stub
				return v1+v2;
			}
		}).map(new Function<Tuple2<Pair<Integer,Integer>,Double>, Rating>() {

			@Override
			public Rating call(Tuple2<Pair<Integer, Integer>, Double> v1)
					throws Exception {
				 int uid = v1._1.getFirst();
			      int pid = v1._1.getSecond();
			      double rating = v1._2;
			      return new Rating(uid, pid, rating);
			}
		});
		return ratings;
	}

}
