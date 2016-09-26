package com.cxqm.xiaoerke.modules.vaccine.service;


import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyInfoVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineStationVo;

import java.util.List;

/**
 * Created by wangbaowei on 15/12/16.
 */
public interface VaccineService {

    VaccineBabyInfoVo selectByVaccineBabyInfoVo(VaccineBabyInfoVo vaccineBabyInfoVo);

    int insertSelective(VaccineBabyInfoVo record);

    List<VaccineStationVo> selectByVaccineStationVo(VaccineStationVo record);
}
