package com.yaochufa.apijava.recsys;

import org.apache.mahout.cf.taste.common.TasteException;

import com.yaochufa.apijava.lang.common.bean.ItemRecSysRequestBean;
import com.yaochufa.apijava.recsys.service.RecModelService;
import com.yaochufa.apijava.recsys.util.SpringContextHelper;

public class RecSysRunner
{
public static void main(String[] args)
{
	SpringContextHelper.init();
	RecModelService recModelService=SpringContextHelper.getBean("recModelServiceImpl",RecModelService.class);
	try
	{
		ItemRecSysRequestBean requestBean=new ItemRecSysRequestBean();
		requestBean.setCity("广州");
		requestBean.setLat(23.12d);
		requestBean.setLng(113.35d);
		requestBean.setProvince("广州");
		requestBean.setSize(20);
		requestBean.setUserId(1);
		recModelService.recommend(requestBean);
	} catch (TasteException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
