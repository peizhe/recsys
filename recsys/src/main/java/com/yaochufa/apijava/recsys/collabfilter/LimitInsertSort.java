package com.yaochufa.apijava.recsys.collabfilter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class LimitInsertSort<T> {
	private List<T> result;
	private int pos;
	private int sortLimit;
	private Comparator<T> comparator;
	
	public LimitInsertSort(int sortLimit,Comparator<T> comparator){
		this.result=new ArrayList<T>(sortLimit);
		this.comparator=comparator;
		this.sortLimit=sortLimit;
	}
	
	public void insert(T e){
		int startOff=pos;
		T pre=null;
		while(startOff>=0){
			if(startOff==0){
				if(pos<sortLimit-1){
					pos++;
				}
				result.add(0, e);
				return;
			}
			pre=result.get(startOff-1);
			if(comparator.compare(e, pre)<=0){
				if(pos<sortLimit-1){
					pos++;
				}
				result.add(startOff, e);
				return;
			}
			startOff--;
		}
	}
	
	public List<T> getResult(){
		int limit=result.size();
		if(limit>sortLimit){
			limit=sortLimit;
		}
		return new ArrayList<T>(result.subList(0, limit));
	}
	
	public void printResult(){
		int limit=result.size();
		if(limit>sortLimit){
			limit=sortLimit;
		}
		for(int i=0;i<limit;i++){
			System.out.println(result.get(i));
		}
	}
}
