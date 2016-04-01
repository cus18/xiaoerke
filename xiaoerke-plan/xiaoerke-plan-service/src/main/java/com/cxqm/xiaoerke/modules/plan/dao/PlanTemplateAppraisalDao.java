package com.cxqm.xiaoerke.modules.plan.dao;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.plan.entity.PlanTemplateAppraisal;

@MyBatisDao
public interface PlanTemplateAppraisalDao {
    int deleteByPrimaryKey(Long id);

    int insert(PlanTemplateAppraisal record);

    int insertSelective(PlanTemplateAppraisal record);

    PlanTemplateAppraisal selectByPrimaryKey(Long id);
    
    Page<PlanTemplateAppraisal> findAppraisalListByInfo(Page<PlanTemplateAppraisal> page,PlanTemplateAppraisal planTemplateAppraisal);

    int updateByPrimaryKeySelective(PlanTemplateAppraisal record);

    int updateByPrimaryKey(PlanTemplateAppraisal record);
}