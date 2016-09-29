package com.cxqm.xiaoerke.modules.vaccine.service.impl;


import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.vaccine.dao.*;
import com.cxqm.xiaoerke.modules.vaccine.entity.*;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    private VaccineStationDao vaccineStationDao;

    @Autowired
    private VaccineStationRelDao vaccineStationRelDao;

    @Autowired
    private VaccineSendMessageDao vaccineSendMessageDao;

    @Autowired
    private VaccineBabyRecordDao vaccineBabyRecordDao;

    @Autowired
    VaccineInfoDao vaccineInfoDao;

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
        return vaccineStationDao.selectByVaccineStationVo(record);
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

    @Override
    public int updateByPrimaryKeyWithBLOBs(VaccineSendMessageVo record){
        return vaccineSendMessageDao.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public List<VaccineInfoWithBLOBsVo> findVaccineList(VaccineInfoWithBLOBsVo vo) {
        return vaccineInfoDao.findVaccineList(vo);
    }

    @Override
    public void saveVaccineInfo(VaccineInfoWithBLOBsVo vo) {
        if(StringUtils.isNotNull(vo.getId()+"")){
            List<VaccineInfoWithBLOBsVo> list = vaccineInfoDao.findVaccineList(vo);
            if(list.size()==0){
                vo.setCreateTime(new Date());
                vaccineInfoDao.insertSelective(vo);
            }else{
                vo.setUpdateTime(new Date());
                vaccineInfoDao.updateByPrimaryKeySelective(vo);
            }
        }else{
            vo.setCreateTime(new Date());
            vaccineInfoDao.insertSelective(vo);
        }
    }

    @Override
    public void deleteVaccine(VaccineInfoWithBLOBsVo vo) {
        vaccineInfoDao.deleteByPrimaryKey(vo.getId());
    }

    @Override
    public List<VaccineStationRelVo> findVaccineStationRelList(VaccineStationRelVo vo) {
        return vaccineStationRelDao.findVaccineStationRelList(vo);
    }

    @Override
    public List<VaccineStationVo> findVaccineStationList(VaccineStationVo vo) {
        return vaccineStationDao.selectByVaccineStationVo(vo);
    }

    @Override
    public void saveVaccineStationInfo(VaccineStationVo vo,List<VaccineStationRelVo> relList,String relid) {
        if(StringUtils.isNotNull(vo.getId() + "")){
            vo.setUpdateTime(new Date());
            vaccineStationDao.updateByPrimaryKeySelective(vo);
            String[] relids = relid.split(",");
            for(String id : relids){
                if(StringUtils.isNotNull(id)){
                    VaccineStationRelVo param = new VaccineStationRelVo();
                    param.setId(Integer.parseInt(id));
                    vaccineStationRelDao.deleteVaccineStationRel(param);
                }
            }
        }else{
            vo.setCreateTime(new Date());
            vaccineStationDao.insertSelective(vo);
        }
        if(relList.size()!=0){
            for(VaccineStationRelVo temp:relList){
                temp.setVaccineStationId(vo.getId());
            }
            vaccineStationRelDao.saveVaccineStationRelList(relList);
        }
    }

    @Override
    public void deleteVaccineStation(VaccineStationVo vo) {
        vaccineStationDao.deleteByPrimaryKey(vo.getId());
        VaccineStationRelVo relVo = new VaccineStationRelVo();
        relVo.setVaccineStationId(vo.getId());
        vaccineStationRelDao.deleteVaccineStationRel(relVo);
    }
}
