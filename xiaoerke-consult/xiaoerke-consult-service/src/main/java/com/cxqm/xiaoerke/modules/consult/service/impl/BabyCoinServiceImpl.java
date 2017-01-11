package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.modules.consult.dao.BabyCoinDao;
import com.cxqm.xiaoerke.modules.consult.dao.BabyCoinRecordDao;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import com.cxqm.xiaoerke.modules.consult.service.BabyCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by deliang on 16/09/05.
 */

@Service
public class BabyCoinServiceImpl implements BabyCoinService {

    @Autowired
    private BabyCoinDao babyCoinDao;

    @Autowired
    private BabyCoinRecordDao babyCoinRecordDao;

    @Override
    public BabyCoinVo selectByBabyCoinVo(BabyCoinVo babyCoinVo){
        return babyCoinDao.selectByBabyCoinVo(babyCoinVo);
    }

    @Override
    public List<BabyCoinVo> selectListByBabyCoinVo(BabyCoinVo babyCoinVo){
        return babyCoinDao.selectListByBabyCoinVo(babyCoinVo);
    }

    @Override
    public List<BabyCoinVo> selectSubBabyCoin(BabyCoinVo babyCoinVo){
        return babyCoinDao.selectSubBabyCoin(babyCoinVo);
    }

    @Override
    public List<BabyCoinRecordVo> selectByBabyCoinRecordVo(BabyCoinRecordVo babyCoinRecordVo){
        return babyCoinRecordDao.selectByBabyCoinRecordVo(babyCoinRecordVo);
    }

    @Override
    public int insertBabyCoinRecord(BabyCoinRecordVo record){
        return babyCoinRecordDao.insertSelective(record);
    }

    @Override
    public int updateCashByOpenId(BabyCoinVo record){
        return babyCoinDao.updateCashByOpenId(record);
    }

    @Override
    public int updateBabyCoinInviteNumber(BabyCoinVo babyCoinVo){
        return babyCoinDao.updateBabyCoinInviteNumber(babyCoinVo);
    }

    @Override
    public int updateBabyCoinByOpenId(BabyCoinVo record){
        return babyCoinDao.updateByOpenId(record);
    }

    @Override
    public int insertBabyCoinSelective(BabyCoinVo record){
        return babyCoinDao.insertSelective(record);
    }

}
