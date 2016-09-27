package com.cxqm.xiaoerke.modules.vaccine.service;


import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyInfoVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyRecordVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineSendMessageVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineStationVo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaodeliang on 16/09/26.
 */

public interface VaccineService {

    VaccineBabyInfoVo selectByVaccineBabyInfoVo(VaccineBabyInfoVo vaccineBabyInfoVo);

    int insertSelective(VaccineBabyInfoVo record);

    List<VaccineStationVo> selectByVaccineStationVo(VaccineStationVo record);

    List<HashMap<String,Object>> getUserWillVaccination(HashMap<String, Object> searchMap);

    int insertVaccineSendMessage(VaccineSendMessageVo record);

    int insertVaccineBabyRecord(VaccineBabyRecordVo record);

    List<VaccineBabyRecordVo> selectByVaccineBabyRecord(VaccineBabyRecordVo record);
}
