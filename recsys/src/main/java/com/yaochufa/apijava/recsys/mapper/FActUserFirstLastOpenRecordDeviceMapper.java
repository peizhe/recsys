package com.yaochufa.apijava.recsys.mapper;

import java.util.List;
import java.util.Map;

import com.yaochufa.apijava.recsys.model.FActUserFirstLastOpenRecordDevice;
import com.yaochufa.apijava.recsys.model.FActUserFirstLastOpenRecordUser;

public interface FActUserFirstLastOpenRecordDeviceMapper {
    int insert(FActUserFirstLastOpenRecordDevice record);

    int insertSelective(FActUserFirstLastOpenRecordDevice record);
    
    List<Map<String, Object>> selectActiveDevice(
    		FActUserFirstLastOpenRecordDevice pa);
    
}