package com.yaochufa.apijava.recsys.util;

import java.io.Serializable;

public class HbasePostConverter implements Serializable{

	public  convert(triple: (Int, String, Int)) = {
	      val p = new Put(Bytes.toBytes(triple._1))
	      p.addColumn(Bytes.toBytes("basic"),Bytes.toBytes("name"),Bytes.toBytes(triple._2))
	      p.addColumn(Bytes.toBytes("basic"),Bytes.toBytes("age"),Bytes.toBytes(triple._3))
	      (new ImmutableBytesWritable, p)
	}
}
