package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;

import java.util.List;

/**
 * Created by wangbaowei on 16/3/10.
 */
public interface BabyCoinService {


    BabyCoinVo selectByBabyCoinVo(BabyCoinVo babyCoinVo);

    List<BabyCoinRecordVo> selectByBabyCoinRecordVo(BabyCoinRecordVo babyCoinRecordVo);

    int insertBabyCoinRecord(BabyCoinRecordVo record);

    int updateCashByOpenId(BabyCoinVo record);

    int updateBabyCoinByOpenId(BabyCoinVo record);

    int insertBabyCoinSelective(BabyCoinVo record);
}
