package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.operation.service.UserAppoinService;
import com.cxqm.xiaoerke.modules.order.dao.PatientRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorDao;
import com.cxqm.xiaoerke.modules.sys.dao.HospitalDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 用户预约统计 实现
 *
 * @author deliang
 * @version 2015-11-05
 */
@Service
@Transactional(readOnly = false)
public class UserAppoinServiceImpl implements UserAppoinService {

    @Autowired
    private HospitalDao hospitalDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private PatientRegisterServiceDao patientRegisterServiceDao;

    @Autowired
    private MongoDBService<MongoLog> logMongoDBService;

    @Autowired
    private MongoDBService<WechatAttention> attentionMongoDBService;

    @Override
    public HashMap<String, Object> getUserAppoinStatisticData(HashMap<String, Object> newMap) throws Exception {
        HashMap<String, Object> response = new HashMap<String, Object>();

        List<HospitalVo> hospitalList = hospitalDao.findAllOrderHospital(newMap);
        if (hospitalList.size() > 0 && hospitalList != null) {
            //根据医院id查询该医院下有订单存在的所有医生
            for (HospitalVo hospitalVo : hospitalList) {
                newMap.put("hospitalId", hospitalVo.getId());
                List<DoctorVo> doctorList = doctorDao.findAllOrderDoctorList(newMap);
                for (DoctorVo doctorVo : doctorList) {
                    HashMap<String, Object> searchMap = new HashMap<String, Object>();
                    searchMap.put("begin_time", newMap.get("begin_time"));
                    searchMap.put("end_time", newMap.get("end_time"));
                    searchMap.put("doctorId", doctorVo.getId());
                    searchMap.put("hospitalId", hospitalVo.getId());
                    searchMap.put("searchFlag", newMap.get("searchFlag"));
                    //获取该医生这一段时间内的所有成功的订单
                    List<PatientRegisterServiceVo> orderList = patientRegisterServiceDao.findAllPatientRegisterService(searchMap);
                    for (PatientRegisterServiceVo patientRegisterServiceVo : orderList) {
                        patientRegisterServiceVo.setOrderCreateDate(DateUtils.formatDate(patientRegisterServiceVo.getCreateDate()));
                        patientRegisterServiceVo.setVisitDate(DateUtils.formatDate(patientRegisterServiceVo.getDate()));
                    }
                    doctorVo.setdoctorList(orderList);

                }
                hospitalVo.sethospitalList(doctorList);

            }
            response.put("orderData", hospitalList);
        }
        return response;

    }


}
