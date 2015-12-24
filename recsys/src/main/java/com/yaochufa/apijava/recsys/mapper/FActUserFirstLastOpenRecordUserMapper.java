package com.yaochufa.apijava.recsys.mapper;

import java.util.List;
import java.util.Map;

import com.yaochufa.apijava.recsys.model.FActUserFirstLastOpenRecordUser;

public interface FActUserFirstLastOpenRecordUserMapper {
    int insert(FActUserFirstLastOpenRecordUser record);

    int insertSelective(FActUserFirstLastOpenRecordUser record);

	List<Map<String, Object>> selectActiveUser(
			FActUserFirstLastOpenRecordUser pa);
}