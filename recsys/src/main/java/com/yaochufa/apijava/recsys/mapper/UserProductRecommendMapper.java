package com.yaochufa.apijava.recsys.mapper;

import java.util.List;

import com.yaochufa.apijava.recsys.model.UserProductRecommend;

public interface UserProductRecommendMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserProductRecommend record);

    int insertSelective(UserProductRecommend record);

    UserProductRecommend selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UserProductRecommend record);

    int updateByPrimaryKey(UserProductRecommend record);
    
    int batchInsert(List<UserProductRecommend> list);
}