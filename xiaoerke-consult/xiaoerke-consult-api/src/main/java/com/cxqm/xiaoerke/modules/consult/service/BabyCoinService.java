package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;

import java.util.List;

/**
 * Created by wangbaowei on 16/3/10.
 */
public interface BabyCoinService {


    BabyCoinVo selectByBabyCoinVo(BabyCoinVo babyCoinVo);

    List<BabyCoinVo> selectListByBabyCoinVo(BabyCoinVo babyCoinVo);

    List<BabyCoinVo> selectSubBabyCoin(BabyCoinVo babyCoinVo);

    List<BabyCoinRecordVo> selectByBabyCoinRecordVo(BabyCoinRecordVo babyCoinRecordVo);

    int insertBabyCoinRecord(BabyCoinRecordVo record);

    int updateCashByOpenId(BabyCoinVo record);

    int updateBabyCoinInviteNumber(BabyCoinVo babyCoinVo);

    int updateBabyCoinByOpenId(BabyCoinVo record);

    int insertBabyCoinSelective(BabyCoinVo record);
}
