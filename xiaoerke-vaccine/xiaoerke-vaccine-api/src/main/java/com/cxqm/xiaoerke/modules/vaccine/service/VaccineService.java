package com.cxqm.xiaoerke.modules.vaccine.service;


import com.cxqm.xiaoerke.modules.vaccine.entity.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wangbaowei on 15/12/16.
 */

public interface VaccineService {

    VaccineBabyInfoVo selectByVaccineBabyInfoVo(VaccineBabyInfoVo vaccineBabyInfoVo);

    int insertSelective(VaccineBabyInfoVo record);

    List<VaccineStationVo> selectByVaccineStationVo(VaccineStationVo record);

    List<HashMap<String,Object>> getUserWillVaccination(HashMap<String, Object> searchMap);

    int insertVaccineSendMessage(VaccineSendMessageVo record);

    List<VaccineSendMessageVo> selectSendMessageInfo(VaccineSendMessageVo record);

    int updateByPrimaryKeyWithBLOBs(VaccineSendMessageVo record);

    List<VaccineInfoWithBLOBsVo> findVaccineList(VaccineInfoWithBLOBsVo vo);

    void saveVaccineInfo(VaccineInfoWithBLOBsVo vo);

    void deleteVaccine(VaccineInfoWithBLOBsVo vo);

    void saveVaccineStationInfo(VaccineStationVo vo,List<VaccineStationRelVo> relList,String relid);

    List<VaccineStationVo> findVaccineStationList(VaccineStationVo vo);

    List<VaccineStationRelVo> findVaccineStationRelList(VaccineStationRelVo vo);

    int insertVaccineBabyRecord(VaccineBabyRecordVo record);

    List<VaccineBabyRecordVo> selectByVaccineBabyRecord(VaccineBabyRecordVo record);

    List<VaccineSendMessageVo> selectByVaccineSendMessageInfo(VaccineSendMessageVo record);

    void deleteVaccineStation(VaccineStationVo vo);

    List<VaccineScanCodeVo> findVaccineScanCodeList(VaccineScanCodeVo vo);

    VaccineScanCodeVo getVaccineScanCodeByInfo(VaccineScanCodeVo vo);

    void updateVaccineScanCodeInfo(VaccineScanCodeVo vo);

    void deleteVaccineScanCodeById(Integer id);
}
