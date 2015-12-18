package com.yaochufa.apijava.recsys.shuffle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.rdd.PairRDDFunctions;

import scala.Tuple2;

/**
 * 处理订单数据 便于准备 协同过滤 以及关联规则需要的数据
 * 
 * 注意 治理协商的订单元数据 单条记录格式:
 * orderId,userd,productId_1#productId_2,timestamp,isCancled
 * 
 * @author root
 *
 */
public class CfOrderDataSHuffle implements DataShuffle {

	private static final Pattern COMMA = Pattern.compile(",");
	private String inputPath;

	public CfOrderDataSHuffle(String inputPath) {
		super();
		this.inputPath = inputPath;
	}

	@Override
	public void shuffle(JavaSparkContext jssc) {
		String fileName = new SimpleDateFormat("yyyyMMdd").format(new Date());
		Configuration conf=new HBaseConfiguration().create();
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.zookeeper.quorum", "master");
		JobConf jobConf=new JobConf(conf, this.getClass());
//		jobConf.setOutputFormat(DBOutputFormat.class);
//		jobConf.set(DBoutputFormat.,"user");
		jssc.textFile(inputPath + "/" + fileName)
				.filter(new Function<String, Boolean>() {

					@Override
					public Boolean call(String line) throws Exception {
						String[] tok = COMMA.split(line);
						return tok.length >= 4;
					}
				}).flatMapToPair(new OutputPairFlatMapFunction()).saveAsHadoopDataset(jobConf);
	}

	class OutputPairFlatMapFunction implements PairFlatMapFunction<String, ImmutableBytesWritable, Put>{

		private final Pattern COMM = Pattern.compile("#");
		private final String FORMET = "yyyyMMddHHmmss";
		private long THREE_MONTH_DIFF = 3 * 30 * 24 * 3600000;
		private long SIX_MONTH_DIFF = 6 * 30 * 24 * 3600000;
		private long TWELVE_MONTH_DIFF = 12 * 30 * 24 * 3600000;
		private long currTime = new Date().getTime();
		private final int baseOrderScore = 30;
		@Override
		public Iterable<Tuple2<ImmutableBytesWritable, Put>> call(String t)
				throws Exception {
			String arr[] = COMMA.split(t);
			String userId = arr[1];
			String[] productIds = COMMA.split(arr[2]);
			long timestamp = Long.parseLong(arr[3]);
			double score = baseOrderScore;
			score = timeBoost(score, timestamp);
			int isCancled = Integer.parseInt(arr[4]);
			score=stateBoost(score,isCancled);
			List<Tuple2<ImmutableBytesWritable, Put>> list=new ArrayList<Tuple2<ImmutableBytesWritable, Put>>();
			for (String productId : productIds) {
				Put p=new Put((userId+"_"+productId).getBytes());
				p.addColumn("cf1".getBytes(),  "score".getBytes(),String.valueOf(score).getBytes() );
				list.add(new Tuple2<ImmutableBytesWritable, Put>(new ImmutableBytesWritable(),p));
			}
			return list;
		}
		private Double timeBoost(double score, long time) {
			long diff = currTime - time;
			if (diff <= THREE_MONTH_DIFF) {
				return score * 1.2;
			} else if (diff > THREE_MONTH_DIFF && diff <= SIX_MONTH_DIFF) {
				return score;
			} else if (diff > SIX_MONTH_DIFF && diff <= TWELVE_MONTH_DIFF) {
				return score * 0.8;
			} else if (diff > TWELVE_MONTH_DIFF) {
				return score * 0.5;
			}
			return score;
		}

		private double stateBoost(double score, int isCancled) {
			return isCancled == 1 ? score * 0.5 : score;
		}
		
	}
	
	class OutputFunction implements VoidFunction<Iterator<String>> {

		private final Pattern COMM = Pattern.compile("#");
		private final String FORMET = "yyyyMMddHHmmss";
		private long THREE_MONTH_DIFF = 3 * 30 * 24 * 3600000;
		private long SIX_MONTH_DIFF = 6 * 30 * 24 * 3600000;
		private long TWELVE_MONTH_DIFF = 12 * 30 * 24 * 3600000;
		private long currTime = new Date().getTime();
		private final int baseOrderScore = 30;

		@Override
		public void call(Iterator<String> t) throws Exception {
			while (t.hasNext()) {
				String arr[] = COMMA.split(t.next());
				String userId = arr[1];
				String[] productIds = COMMA.split(arr[2]);
				long timestamp = Long.parseLong(arr[3]);
				double score = baseOrderScore;
				score = timeBoost(score, timestamp);
				int isCancled = Integer.parseInt(arr[4]);
				// score=stateBoost(score,isCancled);
				for (String productId : productIds) {
				}

			}
		}

		private Double timeBoost(double score, long time) {
			long diff = currTime - time;
			if (diff <= THREE_MONTH_DIFF) {
				return score * 1.2;
			} else if (diff > THREE_MONTH_DIFF && diff <= SIX_MONTH_DIFF) {
				return score;
			} else if (diff > SIX_MONTH_DIFF && diff <= TWELVE_MONTH_DIFF) {
				return score * 0.8;
			} else if (diff > TWELVE_MONTH_DIFF) {
				return score * 0.5;
			}
			return score;
		}

		private double stateBoost(double score, int isCancled) {
			return isCancled == 1 ? score * 0.5 : score;
		}
	}

}