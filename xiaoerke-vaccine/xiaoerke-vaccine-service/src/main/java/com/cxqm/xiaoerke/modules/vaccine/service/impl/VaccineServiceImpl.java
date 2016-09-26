package com.cxqm.xiaoerke.modules.vaccine.service.impl;


import com.cxqm.xiaoerke.modules.vaccine.dao.VaccineBabyInfoDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyInfoVo;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wangbaowei on 15/12/16.
 */
public class VaccineServiceImpl implements VaccineService {
    @Autowired
    private VaccineBabyInfoDao vaccineBabyInfoDao;

    @Override
    public VaccineBabyInfoVo selectByVaccineBabyInfoVo(VaccineBabyInfoVo vaccineBabyInfoVo){
        return vaccineBabyInfoDao.selectByVaccineBabyInfoVo(vaccineBabyInfoVo);
    }

    @Override
    public int insertSelective(VaccineBabyInfoVo record){
        return vaccineBabyInfoDao.insertSelective(record);
    }



}
