package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.CoopThirdBabyInfoVo;

import java.util.List;

/**
 * 合作方宝宝信息接口
 * Created by jiangzhongge on 2016-8-15.
 */
public interface CoopThirdBabyInfoService {

    int addCoopThirdBabyInfo(CoopThirdBabyInfoVo coopThirdBabyInfoVo);

    List<CoopThirdBabyInfoVo> findCoopThirdBabyInfo(CoopThirdBabyInfoVo coopThirdBabyInfoVo);

}
