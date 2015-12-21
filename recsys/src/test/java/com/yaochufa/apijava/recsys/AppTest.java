package com.yaochufa.apijava.recsys;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public static void main(String[] args) {
//		List a=new ArrayList();
//		a.add(2);
//		a.add(0, 1);
//		for(Object s:a){
//			System.out.println(s);
//		}
		Integer a=129;
		Integer b=129;
		System.out.println(a==b);
		Configuration conf=HBaseConfiguration.create();
		Scan scan = new Scan();
		scan.setCacheBlocks(conf.getBoolean("a", false));
	}
}
