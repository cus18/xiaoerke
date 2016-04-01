package com.cxqm.xiaoerke.modules.plan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.plan.entity.PlanTemplateAppraisal;
import com.cxqm.xiaoerke.modules.plan.service.PlanTemplateAppraisalService;
import com.cxqm.xiaoerke.modules.plan.dao.PlanTemplateAppraisalDao;

@Service
@Transactional(readOnly = false)
public class PlanTemplateAppraisalServiceImpl implements PlanTemplateAppraisalService{
	
	@Autowired
	private PlanTemplateAppraisalDao planTemplateAppraisalDao;

	/**
	 * 添加评论实现类
	 * sunxiao
	 * @param planTemplateAppraisal
	 */
	@Override
	public void saveAppraisal(PlanTemplateAppraisal planTemplateAppraisal) {
		// TODO Auto-generated method stub
		planTemplateAppraisalDao.insertSelective(planTemplateAppraisal);
	}

	/**
	 * 查询评论列表
	 * sunxiao
	 * @param planTemplateAppraisal
	 * @return
	 */
	@Override
	public Page<PlanTemplateAppraisal> findAppraisalList(Page<PlanTemplateAppraisal> page,PlanTemplateAppraisal planTemplateAppraisal) {
		// TODO Auto-generated method stub
		Page<PlanTemplateAppraisal> pageList = planTemplateAppraisalDao.findAppraisalListByInfo(page,planTemplateAppraisal);
		return pageList;
	}

}
