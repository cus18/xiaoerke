package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRewardDataVo;

import java.util.List;

/**
 * Created by jiangzhongge on 2017-3-27.
 */
public interface PunchCardRewardDataService {

    List<PunchCardRewardDataVo> getMoreDataBySelective(PunchCardRewardDataVo record);

    int batchInsertPunchCardData(List list);

}
