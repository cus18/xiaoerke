package com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.impl;

import com.cxqm.xiaoerke.modules.nonRealTimeConsult.dao.NonRealTimeConsultRecordDao;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.dao.NonRealTimeConsultSessionDao;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultRecordVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultSessionVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.NonRealTimeConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
@Service
@Transactional(readOnly = false)
public class NonRealTimeConsultServiceImpl implements NonRealTimeConsultService {

    @Autowired
    private NonRealTimeConsultSessionDao nonRealTimeConsultSessionDao;

    @Autowired
    private NonRealTimeConsultRecordDao nonRealTimeConsultRecordDao;

    @Override
    public List<NonRealTimeConsultSessionVo> selectByNonRealTimeConsultSessionVo(NonRealTimeConsultSessionVo realTimeConsultSessionVo){
        return nonRealTimeConsultSessionDao.selectByNonRealTimeConsultSessionVo(realTimeConsultSessionVo);
    }
    @Override
    public List<NonRealTimeConsultRecordVo> selectSessionRecordByVo(NonRealTimeConsultRecordVo nonRealTimeConsultRecordVo){
        return nonRealTimeConsultRecordDao.selectSessionRecordByVo(nonRealTimeConsultRecordVo);
    }


}
