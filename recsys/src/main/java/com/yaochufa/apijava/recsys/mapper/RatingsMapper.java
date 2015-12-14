package com.yaochufa.apijava.recsys.mapper;

import com.yaochufa.apijava.recsys.model.Ratings;
import com.yaochufa.apijava.recsys.model.RatingsKey;

public interface RatingsMapper {
    int deleteByPrimaryKey(RatingsKey key);

    int insert(Ratings record);

    int insertSelective(Ratings record);

    Ratings selectByPrimaryKey(RatingsKey key);

    int updateByPrimaryKeySelective(Ratings record);

    int updateByPrimaryKey(Ratings record);
}