package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultDoctorInfoDao;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultPhoneRecordDao;
import com.cxqm.xiaoerke.modules.consult.entity.CallResponse;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultPhoneRecordVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPhoneService;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 咨询医生信息实现类
 * @author sunxiao
 * 2016-04-26
 */

@Service
public class ConsultDoctorInfoServiceImpl implements ConsultDoctorInfoService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ConsultDoctorInfoDao consultDoctorInfoDao;

    @Override
    public int saveConsultDoctorInfo(ConsultDoctorInfoVo vo) {
        return 0;
    }

    @Override
    public Map getConsultDoctorInfo(User user) {
        Map map = new HashMap();
        List<User> list = userInfoService.getUserListByInfo(user);
        Map param = new HashMap();
        param.put("userId",user.getId());
        List<ConsultDoctorInfoVo> doctorList = getConsultDoctorByInfo(param);
        map.put("user",list.get(0));
        map.put("doctor",doctorList.get(0));
        return map;
    }

    @Override
    public List<ConsultDoctorInfoVo> getConsultDoctorByInfo(Map map) {
        List<ConsultDoctorInfoVo> list = new ArrayList<ConsultDoctorInfoVo>();
        List<ConsultDoctorInfoVo> doctorList = consultDoctorInfoDao.getConsultDoctorByInfo(map);
        if(doctorList.size()==0){
            list.add(new ConsultDoctorInfoVo());
        }else{
            list.add(doctorList.get(0));
        }
        return list;
    }

    @Override
    public int consultDoctorInfoOper(ConsultDoctorInfoVo vo) {
        Map param = new HashMap();
        int count = 0;
        param.put("userId", vo.getUserId());
        List<ConsultDoctorInfoVo> doctorList = consultDoctorInfoDao.getConsultDoctorByInfo(param);
        if(doctorList.size()==0){
            count = consultDoctorInfoDao.insertSelective(vo);
        }else{
            vo.setId(doctorList.get(0).getId());
            count = consultDoctorInfoDao.updateByPrimaryKeySelective(vo);
        }
        return count;
    }
}
