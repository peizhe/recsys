package com.yaochufa.apijava.recsys.service;

import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;

import com.yaochufa.apijava.lang.common.bean.ItemRecSysRequestBean;
import com.yaochufa.apijava.lang.common.bean.ItemVo;

public interface RecModelService
{
	List<ItemVo> recommend(ItemRecSysRequestBean requestBean) throws TasteException;
	
	List<ItemVo> mostSimilarItems(ItemRecSysRequestBean requestBean) throws TasteException;
}
