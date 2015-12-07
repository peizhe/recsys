package com.yaochufa.apijava.recsys.mapper;

import java.util.List;

import com.yaochufa.apijava.recsys.model.ProductSimimlarity;

public interface ProductSimimlarityMapper {
    int insert(ProductSimimlarity record);

    int insertSelective(ProductSimimlarity record);

	int batchInsert(List<ProductSimimlarity> list);
}