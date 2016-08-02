package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGameDetailVo;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;

import java.util.List;
import java.util.Map;

/**
 * Created by deliang on 16/8/01.
 */
public interface OlyGamesService {

    OlyBabyGamesVo selectByOlyBabyGamesVo(OlyBabyGamesVo olyBabyGamesVo);

    /**
     * 获取所有奖品列表
     * sunxiao
     * @return
     */
    List<Map<String,Object>> getOlyGamePrizeList(Map<String, Object> prizeMap);
    int insertOlyBabyGamesVo(OlyBabyGamesVo record);

    int insertOlyBabyGameDetailVo(OlyBabyGameDetailVo record);

    int updateOlyBabyGamesByOpenId(OlyBabyGamesVo record);

    String getUserQRCode(String id);
}
