package com.cxqm.xiaoerke.modules.sys.service;


import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;

import java.util.List;
import java.util.Map;

@Transactional(readOnly = false)
public interface IllnessInfoService {

	Map<String,Object> listFirstIllness(Map<String,Object> params);

	Map<String,Object> listSecondIllness(Map<String,Object> params);

	List<IllnessVo> getFirstIllnessList();
	
	List<IllnessVo> findSecondIllnessByName(String illnessName);
}
