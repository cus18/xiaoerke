package com.cxqm.xiaoerke.modules.vaccine.service.impl;


import com.cxqm.xiaoerke.modules.vaccine.dao.VaccineBabyInfoDao;
import com.cxqm.xiaoerke.modules.vaccine.dao.VaccineStationDao;
import com.cxqm.xiaoerke.modules.vaccine.dao.VaccineStationRelDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyInfoVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineStationVo;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wangbaowei on 15/12/16.
 */
public class VaccineServiceImpl implements VaccineService {
    @Autowired
    private VaccineBabyInfoDao vaccineBabyInfoDao;

    @Autowired
    private VaccineStationDao VaccineStationDao;

    @Autowired
    private VaccineStationRelDao vaccineStationRelDao;

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





}
