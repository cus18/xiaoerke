package com.cxqm.xiaoerke.modules.vaccine.service.impl;


import com.cxqm.xiaoerke.modules.vaccine.dao.*;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyInfoVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyRecordVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineSendMessageVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineStationVo;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaodeliang on 16/09/26.
 */
@Service
@Transactional(readOnly = false)
public class VaccineServiceImpl implements VaccineService {
    @Autowired
    private VaccineBabyInfoDao vaccineBabyInfoDao;

    @Autowired
    private VaccineStationDao VaccineStationDao;

    @Autowired
    private VaccineStationRelDao vaccineStationRelDao;

    @Autowired
    private VaccineSendMessageDao vaccineSendMessageDao;

    @Autowired
    private VaccineBabyRecordDao vaccineBabyRecordDao;

    @Override
    public VaccineBabyInfoVo selectByVaccineBabyInfoVo(VaccineBabyInfoVo vaccineBabyInfoVo){
        return vaccineBabyInfoDao.selectByVaccineBabyInfoVo(vaccineBabyInfoVo);
    }

    @Override
    public int insertSelective(VaccineBabyInfoVo record){
        return vaccineBabyInfoDao.insertSelective(record);
    }

    @Override
    public List<VaccineStationVo> selectByVaccineStationVo(VaccineStationVo record){
        return VaccineStationDao.selectByVaccineStationVo(record);
    }

    @Override
    public List<HashMap<String,Object>> getUserWillVaccination(HashMap<String, Object> searchMap){
        return vaccineStationRelDao.getUserWillVaccination(searchMap);
    }

    @Override
    public int insertVaccineSendMessage(VaccineSendMessageVo record){
        return vaccineSendMessageDao.insertSelective(record);
    }

    @Override
    public int insertVaccineBabyRecord(VaccineBabyRecordVo record){
        return vaccineBabyRecordDao.insertSelective(record);
    }

    @Override
    public List<VaccineBabyRecordVo> selectByVaccineBabyRecord(VaccineBabyRecordVo record){
        return vaccineBabyRecordDao.selectByVaccineBabyRecordVo(record);
    }

    @Override
    public List<VaccineSendMessageVo> selectByVaccineSendMessageInfo(VaccineSendMessageVo record){
        return vaccineSendMessageDao.selectByVaccineSendMessage(record);
    }











}
