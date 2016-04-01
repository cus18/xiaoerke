package com.cxqm.xiaoerke.modules.bankend.service.impl;

import com.cxqm.xiaoerke.modules.bankend.service.DoctorIllnessRelationService;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorIllnessRelationDao;
import com.cxqm.xiaoerke.modules.sys.dao.HospitalDao;
import com.cxqm.xiaoerke.modules.sys.dao.IllnessDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorIllnessRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class DoctorIllnessRelationServiceImpl implements DoctorIllnessRelationService {

    @Autowired
    HospitalDao hospitalDao;

    @Autowired
    IllnessDao illnessDao;

    @Autowired
    DoctorIllnessRelationDao doctorIllnessRelationDao;

    /**
     * 根据一类疾病和二类疾病查询疾病信息（单条）
     *
     * @param doctorIllnessRelationVo
     */
    //根据一类疾病和二类疾病查询疾病信息表主键
    @Override
    public IllnessVo findSysIllnessInfo(DoctorIllnessRelationVo doctorIllnessRelationVo) {
        IllnessVo illnessVo = illnessDao.findSysIllnessInfo(doctorIllnessRelationVo);
        if (illnessVo != null) {
            return illnessVo;
        }
        return null;
    }

    //往doctor_illness_relation中插入数据（单条）
    @Override
    public void insertDoctorIllnessRelationData(DoctorIllnessRelationVo doctorIllnessRelationVo) {
        doctorIllnessRelationDao.insertDoctorIllnessRelationData(doctorIllnessRelationVo);
    }

    //校验，根据医生的主键和疾病主键查询此该医生是否已经关联了该疾病
    @Override
    public DoctorIllnessRelationVo findDoctorIllnessRelationInfo(DoctorIllnessRelationVo doctorIllnessRelationVo) {
        return doctorIllnessRelationDao.findDoctorIllnessRelationInfo(doctorIllnessRelationVo);
    }

    //根据doctorId删除医生与疾病的所有关联信息（先删除，后保存）
    @Override
    public void deleteDoctorAndIllnessRelation(DoctorIllnessRelationVo doctorIllnessRelationVo) {
        doctorIllnessRelationDao.deleteDoctorAndIllnessRelation(doctorIllnessRelationVo);
    }
}
