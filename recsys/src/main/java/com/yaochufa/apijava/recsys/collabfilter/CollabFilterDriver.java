package com.yaochufa.apijava.recsys.collabfilter;


import java.io.Closeable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

import scala.Tuple2;

import com.yaochufa.apijava.recsys.collabfilter.etl.DataTransformer;
import com.yaochufa.apijava.recsys.collabfilter.etl.PurchaseDataTransformer;
import com.yaochufa.apijava.recsys.collabfilter.tran.ALSCollabFiterTransor;
import com.yaochufa.apijava.recsys.mapper.ProductSimimlarityMapper;
import com.yaochufa.apijava.recsys.util.GlobalVar;
import com.yaochufa.apijava.recsys.util.SpringContextHelper;


public class CollabFilterDriver implements Closeable,Serializable{

	ResultHadler resultHadler=SpringContextHelper.getBean("resultHadler", ResultHadler.class);
	ProductSimimlarityMapper productSimimlarityMapper=SpringContextHelper.getBean("productSimimlarityMapper", ProductSimimlarityMapper.class);
	
	private JavaSparkContext jssc;
	private DataTransformer dataTransformer=SpringContextHelper.getBean("purchaseDataTransformer", PurchaseDataTransformer.class);
	private Set<Integer> validProducts;
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
//		final String directoryPath = args[0];
//		if (args.length < 1) {
//			 System.err.println(
//				        "Usage: CollabFilterDriver <directoryPath>");
//				      System.exit(1);
//		}
		SpringContextHelper.init();
		String directoryPath=GlobalVar.ORDER_PATH;
		try (CollabFilterDriver cfcDriver = new CollabFilterDriver()) {
			cfcDriver.service(directoryPath);
			System.out.println("完成全部");
		}
	
	}

	public CollabFilterDriver() {
		SparkConf conf = new SparkConf().setAppName("Collaborative Filtering with Mysql").set("spark.master", GlobalVar.SPARKCONF_MASTER);
		conf.set("spark.executor.memory", "1024M");
		this.jssc = new JavaSparkContext(conf);
	}

	public void service(String directoryPath){
		JavaRDD<Rating> ratings=dataTransformer.transform(this.jssc, directoryPath);
		ALSCollabFiterTransor.Builder builder=new ALSCollabFiterTransor.Builder();
		ALSCollabFiterTransor transor=builder.build();
		MatrixFactorizationModel model=transor.tran(ratings);
//		SQLContext sqlCtx=new SQLContext(this.jssc);
//		Properties pros=new Properties();
//		pros.setProperty("user", GlobalVar.jdbcUsername);
//		pros.setProperty("password", GlobalVar.jdbcPassword);
//		printResult(itemcf.itemcf(model).cache().collect());
//		StructType schema=new StructType(new StructField[]{new StructField("product_id_a", DataTypes.IntegerType,true,Metadata.empty()),
//				new StructField("product_id_b",DataTypes.IntegerType,true,Metadata.empty()),
//				new StructField("similarity",DataTypes.FloatType,true,Metadata.empty()),
//				new StructField("created_at",DataTypes.DateType,true,Metadata.empty())
//		});
//		sqlCtx.createDataFrame(itemcf.itemcf(model), schema).write().mode(SaveMode.Append).jdbc(GlobalVar.jdbcUrl, "product_simimlarity", pros);
//		productSimimlarityMapper.batchInsert(itemcf.itemcf(model));
//		model.productFeatures().toJavaRDD().map(new FeaturesToString()).saveAsTextFile("data/productFeatures");
//		resultHadler.itemcfLocal(model.productFeatures().toJavaRDD().collect());
		List<Integer> userList=null;
//		resultHadler.userCf(model,userList);
	}
	
	<T> void printResult(List<T> list){
		for(T t:list){
			System.out.println(t.toString());
		}
	}
	
	/*
	 * For use with try-with-resources.
	 */
	@Override
	public void close() {
		this.jssc.stop();
	}
	static class FeaturesToString implements Function<Tuple2<Object, double[]>, String> {
	    
	    public String call(Tuple2<Object, double[]> element) {
	      return element._1() + "," + Arrays.toString(element._2());
	    }
	  }
}
