package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultEvaluateCoopVo;

import java.util.List;

/**
 * 合作方评价信息Dao
 * Created by jiangzhongge on 2016-8-24.
 */
@MyBatisDao
public interface ConsultEvaluateCoopDao {

    int saveConsultEvaluateCoops(ConsultEvaluateCoopVo consultEvaluateCoopVo);

    List<ConsultEvaluateCoopVo> findConsultEvaluateCoops (ConsultEvaluateCoopVo consultEvaluateCoopVo);

}
