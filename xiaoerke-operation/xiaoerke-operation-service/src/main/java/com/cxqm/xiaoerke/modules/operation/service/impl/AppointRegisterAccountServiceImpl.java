package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.operation.service.AppointRegisterAccountService;
import com.cxqm.xiaoerke.modules.operation.service.DataStatisticService;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.sys.dao.HospitalDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 号源结算统计 实现
 *
 * @author deliang
 * @version 2015-11-05
 */
@Service
@Transactional(readOnly = false)
public class AppointRegisterAccountServiceImpl implements AppointRegisterAccountService {

    @Autowired
    private HospitalDao hospitalDao;

    @Autowired
    private DoctorInfoService doctorInfoService;

    @Autowired
    private DataStatisticService dataStatisticService;

    @Override
    public HashMap<String, Object> getYYStatisticData(HashMap<String, Object> newMap) throws Exception {
        HashMap<String, Object> response = new HashMap<String, Object>();

        List<HospitalVo> hospitalList = hospitalDao.findAllOrderHospital(newMap);

        if (hospitalList.size() > 0 && hospitalList != null) {

            for (HospitalVo hospitalVo : hospitalList) {
                newMap.put("hospitalId", hospitalVo.getId());
                List<DoctorVo> doctorList = doctorInfoService.findAllOrderDoctorList(newMap);
                for (DoctorVo doctorVo : doctorList) {
                    HashMap<String, Object> searchMap = new HashMap<String, Object>();
                    searchMap.put("begin_time", newMap.get("begin_time"));
                    searchMap.put("end_time", newMap.get("end_time"));
                    searchMap.put("doctorId", doctorVo.getId());
                    searchMap.put("hospitalId", hospitalVo.getId());
                    searchMap.put("searchFlag", newMap.get("searchFlag"));

                    List<PatientRegisterServiceVo> orderList = dataStatisticService.findAllPatientRegisterService(searchMap);
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
