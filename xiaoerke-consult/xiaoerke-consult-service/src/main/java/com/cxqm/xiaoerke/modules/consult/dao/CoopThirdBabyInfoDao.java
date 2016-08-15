package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.CoopThirdBabyInfoVo;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-8-15.
 */
@MyBatisDao
public interface CoopThirdBabyInfoDao {

        int addCoopThirdBabyInfo(CoopThirdBabyInfoVo coopThirdBabyInfoVo);

        List<CoopThirdBabyInfoVo> findCoopThirdBabyInfo(CoopThirdBabyInfoVo coopThirdBabyInfoVo);

}
