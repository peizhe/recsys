package com.yaochufa.apijava.recsys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaochufa.apijava.lang.common.bean.ItemRecSysRequestBean;
import com.yaochufa.apijava.lang.common.bean.ItemVo;
import com.yaochufa.apijava.recsys.core.cf.ItemCfRecommender;
import com.yaochufa.apijava.recsys.core.cf.UserCfRecommender;
import com.yaochufa.apijava.recsys.core.cf.UserCfRescorer;
import com.yaochufa.apijava.recsys.mapper.SearchMapper;

@Service
public class RecModelServiceImpl implements RecModelService
{

	@Autowired
	private UserCfRecommender userCfRecommender;
	@Autowired
	private ItemCfRecommender itemCfRecommender;
	private SearchMapper searchMapper;
	
	@Override
	public List<ItemVo> recommend(ItemRecSysRequestBean requestBean)
			throws TasteException
	{
		IDRescorer  userCfRescorer=new 	UserCfRescorer(requestBean.getLat(), requestBean.getLng());
		List<RecommendedItem> list= userCfRecommender.recommend(requestBean.getUserId(), requestBean.getSize(), userCfRescorer);
		return parseToItemVoList(list);
	}

	@Override
	public List<ItemVo> mostSimilarItems(ItemRecSysRequestBean requestBean)
			throws TasteException
	{
		List<ItemVo> result=new ArrayList<ItemVo>();
		itemCfRecommender.mostSimilarItems(requestBean.getProductId(), requestBean.getSize());
		return result;
	}
	
	private List<ItemVo> parseToItemVoList(List<RecommendedItem> list)
	{
		if(list!=null&&list.size()>0){
			return searchMapper.selectByRecommendedItems(list);
		}else{
			return new ArrayList<ItemVo>(0);
		}
	}


}
