package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionTemplateVo;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface OperationPromotionTemplateDao {

    List<OperationPromotionTemplateVo> findTemplateListByInfo(Map map);

    int deleteByPrimaryKey(String id);

    int insertSelective(OperationPromotionTemplateVo record);

    int updateByPrimaryKeySelective(OperationPromotionTemplateVo record);

}