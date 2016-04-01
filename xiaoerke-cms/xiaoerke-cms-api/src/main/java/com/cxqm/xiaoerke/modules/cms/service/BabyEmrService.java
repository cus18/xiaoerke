package com.cxqm.xiaoerke.modules.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.cms.dao.BabyEmrDao;
import com.cxqm.xiaoerke.modules.cms.entity.BabyEmrVo;

/**
 * 评论Service
 * @author sunxiao
 * @version 2015-11-27
 */
@Service
@Transactional(readOnly = false)
public class BabyEmrService {
	
	@Autowired
    private BabyEmrDao babyEmrDao;
	
	public void save(BabyEmrVo bev){
		babyEmrDao.saveBabyEmr(bev);
	}
	
	public void update(BabyEmrVo bev){
		babyEmrDao.updateBabyEmr(bev);
	}
	
	public List<BabyEmrVo> getBabyEmrList(String openid){
		List<BabyEmrVo> list = babyEmrDao.getBabyEmrList(openid);
		if(list == null){
			list = new ArrayList<BabyEmrVo>();
		}
		return list;
	}

}
