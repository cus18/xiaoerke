package com.cxqm.xiaoerke.modules.plan.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.plan.entity.PlanTemplate;

@MyBatisDao
public interface PlanTemplateDao {
    int deleteByPrimaryKey(Short id);

    int insert(PlanTemplate record);

    int insertSelective(PlanTemplate record);

    PlanTemplate selectByPrimaryKey(Short id);

    int updateByPrimaryKeySelective(PlanTemplate record);

    int updateByPrimaryKey(PlanTemplate record);
    
    /**
     * 获取模板列表
     * 张博
     * @return
     */
    public List<Map<String, Object>> getPlanTemplateList(@Param("type")String type);
    
}