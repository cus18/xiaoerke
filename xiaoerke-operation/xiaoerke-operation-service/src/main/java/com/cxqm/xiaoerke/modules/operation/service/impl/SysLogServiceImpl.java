package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.modules.operation.dao.SysLogDao;
import com.cxqm.xiaoerke.modules.operation.service.SysLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 号源结算统计 实现
 *
 * @author Frank
 * @version 2015-11-05
 */
@Service
@Transactional(readOnly = false)
public class SysLogServiceImpl implements SysLogService {

	@Autowired
	private SysLogDao sysLogDao;
	
	@Override
	public List<HashMap<String, Object>> getUserListYesterday(String date) {
		return sysLogDao.getUserListYesterday(date);
	}

	@Override
	public HashMap<String, Object> getUserOperationStatistic(
			HashMap<String, Object> data) {
		return sysLogDao.getUserOperationStatistic(data);
	}

}
