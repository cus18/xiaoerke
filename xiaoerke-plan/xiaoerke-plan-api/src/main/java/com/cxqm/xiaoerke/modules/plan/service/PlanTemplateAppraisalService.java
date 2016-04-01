package com.cxqm.xiaoerke.modules.plan.service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.plan.entity.PlanTemplateAppraisal;

public interface PlanTemplateAppraisalService {

	/**
	 * 添加评论接口
	 * sunxiao
	 * @param planTemplateAppraisal
	 */
	void saveAppraisal(PlanTemplateAppraisal planTemplateAppraisal);
	
	/**
	 * 查询评论列表
	 * sunxiao
	 * @param planTemplateAppraisal
	 * @return
	 */
	Page<PlanTemplateAppraisal> findAppraisalList(Page<PlanTemplateAppraisal> page,PlanTemplateAppraisal planTemplateAppraisal);
}
