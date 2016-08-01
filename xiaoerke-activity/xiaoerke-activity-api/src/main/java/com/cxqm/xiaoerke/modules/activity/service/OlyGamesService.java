package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGameDetailVo;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;

/**
 * Created by deliang on 16/8/01.
 */
public interface OlyGamesService {

    OlyBabyGamesVo selectByOlyBabyGamesVo(OlyBabyGamesVo olyBabyGamesVo);

    int insertOlyBabyGamesVo(OlyBabyGamesVo record);

    int insertOlyBabyGameDetailVo(OlyBabyGameDetailVo record);

    int updateOlyBabyGamesByOpenId(OlyBabyGamesVo record);
}
