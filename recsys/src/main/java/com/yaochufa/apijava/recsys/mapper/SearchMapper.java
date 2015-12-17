package com.yaochufa.apijava.recsys.mapper;

import java.util.List;
import java.util.Map;

import com.yaochufa.apijava.lang.common.Pair;
import com.yaochufa.apijava.recsys.model.Search;

public interface SearchMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Search record);

    int insertSelective(Search record);

    Search selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Search record);

    int updateByPrimaryKeyWithBLOBs(Search record);

    int updateByPrimaryKey(Search record);
    
    Search selectByLinkid(Integer id);
    
    /**
     * 酒景统计
     * @param record
     * @return
     */
    List<Map<String, Object>> selectStatByPro(Search record);
    
    List<Map<String, Object>> selectStatByCity(Search record);
    
    int selectProductNumByPro(Search record);
    
    int selectProductNumByCity(Search record);

	boolean exists(int productId);

	List<Integer> selectAllWithIdList();

}