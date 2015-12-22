package com.yaochufa.apijava.recsys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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

	public static void main(String[] args) throws Exception {
//		List a=new ArrayList();
//		a.add(2);
//		a.add(0, 1);
//		for(Object s:a){
//			System.out.println(s);
//		}
		/*Integer a=129;
		Integer b=129;
		System.out.println(a==b);
		Configuration conf=HBaseConfiguration.create();
		Scan scan = new Scan();
		scan.setCacheBlocks(conf.getBoolean("a", false));*/
		Person p=new Person();
		p.setName("xuht");
		p.setItem(new Item("iphone"));
		ByteArrayOutputStream ops=new ByteArrayOutputStream();
		ObjectOutputStream oop=new ObjectOutputStream(ops);
		oop.writeObject(p);
		ByteArrayInputStream ios=new ByteArrayInputStream(ops.toByteArray());
		ObjectInputStream objis=new ObjectInputStream(ios);
		
		Person p2= (Person) objis.readObject();
		System.out.println(p2.toString());
	}
}

class Person implements Serializable{
	private String name;
	
	private Item item;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", item=" + item + "]";
	}
	
 } 

class Item implements Serializable{
	private String name;

	public Item(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Item [name=" + name + "]";
	}
	
}
