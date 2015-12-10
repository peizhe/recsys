package com.yaochufa.apijava.recsys.etl;

import java.io.Serializable;

public class Pair <K,V> implements Serializable{

	private K first;
	private V second;
	
	public Pair(K first, V second) {
		super();
		this.first = first;
		this.second = second;
	}
	
	public K getFirst() {
		return first;
	}
	public void setFirst(K first) {
		this.first = first;
	}
	public V getSecond() {
		return second;
	}
	public void setSecond(V second) {
		this.second = second;
	}

	@Override
	public String toString() {
		return "Pair [first=" + first + ", second=" + second + "]";
	}
	
	
}