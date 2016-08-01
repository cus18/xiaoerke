package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.dao.OlyBabyGamesDao;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by deliang on 16/8/01.
 */

@Service
public class OlyGamesServiceImpl implements OlyGamesService {

    @Autowired
    private OlyBabyGamesDao olyBabyGamesDao;
    @Override
    public OlyBabyGamesVo selectByOlyBabyGamesVo(OlyBabyGamesVo olyBabyGamesVo) {
        return olyBabyGamesDao.selectByOlyBabyGamesVo(olyBabyGamesVo);
    }
}
