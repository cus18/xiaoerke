package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultEvaluateCoopVo;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionVo;

import java.util.List;

/**
 * 运营推广dao
 *
 * Created by sunxiao on 2016-9-19.
 */
@MyBatisDao
public interface OperationPromotionDao {

    List<OperationPromotionVo> findKeywordRoleList(OperationPromotionVo vo);

    void saveKeywordRole(OperationPromotionVo vo);
}
