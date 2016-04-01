package com.cxqm.xiaoerke.modules.healthRecords.service;


import com.cxqm.xiaoerke.modules.healthRecords.entity.BabyIllnessInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;

import java.util.List;

/**
 * Created by wangbaowei on 16/1/20.
 */

public interface HealthRecordsService {

  BabyBaseInfoVo selectByPrimaryKey(Integer id);

  List<BabyBaseInfoVo> getUserBabyInfolist(String userId);

  List<BabyBaseInfoVo> getUserBabyInfolistByOpenId(String openId);

  int insertBabyInfo(BabyBaseInfoVo bean);

  int updateBabyInfo(BabyBaseInfoVo bean);
  
  int deleteBabyInfo(Integer id);

  int insertBabyIllnessInfo(BabyIllnessInfoVo bean);

  void uploadPic(String key,String mediaId);

  List<BabyBaseInfoVo> selectUserBabyInfo(String openId);


}
