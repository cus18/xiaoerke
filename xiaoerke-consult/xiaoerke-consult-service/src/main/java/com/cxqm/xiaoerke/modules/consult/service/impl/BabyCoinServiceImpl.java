package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.consult.dao.BabyCoinDao;
import com.cxqm.xiaoerke.modules.consult.dao.BabyCoinRecordDao;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultPhoneRecordDao;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import com.cxqm.xiaoerke.modules.consult.entity.CallResponse;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultPhoneRecordVo;
import com.cxqm.xiaoerke.modules.consult.service.BabyCoinService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPhoneService;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.DoctorMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public int insertBabyCoinSelective(BabyCoinVo record){
        return babyCoinDao.insertSelective(record);
    }

}
