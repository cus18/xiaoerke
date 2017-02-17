package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import com.cxqm.xiaoerke.modules.consult.entity.RedPacketInfoVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 16/3/10.
 */
public interface BabyCoinService {


    BabyCoinVo selectByBabyCoinVo(BabyCoinVo babyCoinVo);

    BabyCoinVo getBabyCoin(HashMap<String, Object> response, String openId);

    List<BabyCoinVo> selectListByBabyCoinVo(BabyCoinVo babyCoinVo);

    List<BabyCoinVo> selectSubBabyCoin(BabyCoinVo babyCoinVo);

    List<BabyCoinRecordVo> selectByBabyCoinRecordVo(BabyCoinRecordVo babyCoinRecordVo);

    int insertBabyCoinRecord(BabyCoinRecordVo record);

    int updateCashByOpenId(BabyCoinVo record);

    int updateBabyCoinInviteNumber(BabyCoinVo babyCoinVo);

    int updateBabyCoinByOpenId(BabyCoinVo record);

    int insertBabyCoinSelective(BabyCoinVo record);

    int giveBabyCoin(String openid,Long count);

    Map<String,Object> redPacketShare(String openid, String packetId);

    String redPacketInit(RedPacketInfoVo re);

//    String redPacketInfo(String packetId);
}
