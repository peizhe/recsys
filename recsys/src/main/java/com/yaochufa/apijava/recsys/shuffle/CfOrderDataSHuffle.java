package com.yaochufa.apijava.recsys.shuffle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;

/**
 *  处理订单数据 便于准备 协同过滤 以及关联规则需要的数据
 *  
 *  注意 治理协商的订单元数据 单条记录格式: orderId,userd,productId_1#productId_2,timestamp,state
 * @author root
 *
 */
public class CfOrderDataSHuffle implements DataShuffle{

	private static final Pattern COMMA = Pattern.compile(",");
	private String inputPath;
	
	public CfOrderDataSHuffle(String inputPath) {
		super();
		this.inputPath = inputPath;
	}

	@Override
	public void shuffle(JavaSparkContext jssc) {
		String fileName=new SimpleDateFormat("yyyyMMdd").format(new Date());
		jssc.textFile(inputPath+"/"+fileName).filter(new Function<String, Boolean>() {
			
			@Override
			public Boolean call(String line) throws Exception {
				String[] tok = COMMA.split(line);
				return tok.length>=3;
			}
		}).foreachPartition(new VoidFunction<Iterator<String>>() {
			
			@Override
			public void call(Iterator<String> t) throws Exception {
				String[] arr=COMMA.split(",");
				
			}
		});
	}

}
