package com.cxqm.xiaoerke.modules.plan.dao;

import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.plan.entity.NutritionEvaluate;
/**
 * 营养管理dao
 * Created by sunxiao on 16/3/3.
 */
@MyBatisDao
public interface NutritionManagementDao {

	/**
	 * 保存营养评估报告
	 * sunxiao
	 * @param planInfo
	 */
    int insert(NutritionEvaluate nutritionEvaluate);

    /**
	 * 保存营养评估报告
	 * sunxiao
	 * @param planInfo
	 */
    int insertSelective(NutritionEvaluate nutritionEvaluate);

    /**
	 * 获取营养评估报告
	 * sunxiao
	 * @param planInfo
	 */
    NutritionEvaluate selectByPrimaryKey(Long id);

    /**
	 * 修改营养评估报告
	 * sunxiao
	 * @param planInfo
	 */
    int updateByPrimaryKeySelective(NutritionEvaluate nutritionEvaluate);

    /**
	 * 获取营养评估报告
	 * sunxiao
	 * @param planInfo
	 */
	public List<NutritionEvaluate> getEvaluateListByInfo(Map<String, Object> params);
	
}