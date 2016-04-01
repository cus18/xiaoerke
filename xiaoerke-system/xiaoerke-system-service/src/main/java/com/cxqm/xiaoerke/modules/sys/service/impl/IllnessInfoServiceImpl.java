package com.cxqm.xiaoerke.modules.sys.service.impl;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorDao;
import com.cxqm.xiaoerke.modules.sys.dao.HospitalDao;
import com.cxqm.xiaoerke.modules.sys.dao.IllnessDao;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;
import com.cxqm.xiaoerke.modules.sys.service.IllnessInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class IllnessInfoServiceImpl implements IllnessInfoService {

    @Autowired
    private IllnessDao illnessDao;

	@Override
	@SystemServiceLog(description = "00000046")//获取疾病一级分类
	public Map<String,Object> listFirstIllness(Map<String,Object> params)
	{
		HashMap<String, Object> response = new HashMap<String, Object>();

		String currentPage = ((String) params.get("pageNo"));
		String pageSize = ((String) params.get("pageSize"));
		String orderBy = (String) params.get("orderBy");
		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage,
				pageSize);
		HashMap<String, Object> illnessInfo = new HashMap<String, Object>();
		illnessInfo.put("orderBy", orderBy);

		Page<IllnessVo> resultPage = illnessDao.findFirstIllness(illnessInfo, page);

		response.put("pageNo", resultPage.getPageNo());
		response.put("pageSize", resultPage.getPageSize());
		long tmp = FrontUtils.generatorTotalPage(resultPage);
		response.put("pageTotal", tmp + "");
		List<IllnessVo> list = resultPage.getList();
		List<HashMap<String, Object>> illnessDataList = new ArrayList<HashMap<String, Object>>();
		if (list != null && !list.isEmpty()) {
			for (IllnessVo illness : list) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("illnessName", illness.getLevel_1());
				illnessDataList.add(dataMap);
			}
		}
		response.put("illnessData", illnessDataList);

		return response;
	}

	@SystemServiceLog(description = "00000047")////获取一级疾病下的二级分类
	public Map<String,Object> listSecondIllness(Map<String,Object> params)
	{

		HashMap<String, Object> response = new HashMap<String, Object>();

		String illnessName = (String) params.get("illnessName");

		List<IllnessVo> list = illnessDao.findSecondIllnessByFirst(illnessName);

		List<HashMap<String, Object>> illnessListData = new ArrayList<HashMap<String, Object>>();
		if (list != null && !list.isEmpty()) {
			for (IllnessVo illness : list) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("illnessSecondId", illness.getId());
				dataMap.put("illnessSecondName", illness.getLevel_2());
				illnessListData.add(dataMap);
			}
		}
		response.put("illnessListData", illnessListData);
		return response;
	}

	@Override
	public List<IllnessVo> getFirstIllnessList() {
		return illnessDao.getFirstIllnessList();
	}

	@Override
	public List<IllnessVo> findSecondIllnessByName(String illnessName) {
		return illnessDao.findSecondIllnessByName(illnessName);
	}

}
