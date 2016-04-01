package com.cxqm.xiaoerke.modules.plan.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.plan.entity.PlanTemplateTask;

@MyBatisDao
public interface PlanTemplateTaskDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PlanTemplateTask record);

    int insertSelective(PlanTemplateTask record);

    PlanTemplateTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlanTemplateTask record);

    int updateByPrimaryKey(PlanTemplateTask record);
    
    /**
     * 获取模板列表
     * 张博
     * @return
     */
    public List<Map<String, Object>> getPlanTemplateTaskListByPlanTemplateId(@Param("templateId")String templateId);
}