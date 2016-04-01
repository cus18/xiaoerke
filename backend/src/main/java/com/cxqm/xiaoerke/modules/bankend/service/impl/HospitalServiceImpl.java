package com.cxqm.xiaoerke.modules.bankend.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.bankend.service.HospitalService;
import com.cxqm.xiaoerke.modules.sys.dao.*;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorHospitalRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorLocationVo;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysHospitalContactVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    HospitalDao hospitalDao;

    @Autowired
    DoctorDao doctorDao;

    @Autowired
    DoctorHospitalRelationDao doctorHospitalRelationDao;

    @Autowired
    DoctorLocationDao doctorLocationDao;

    @Autowired
    SysHospitalContactDao sysHospitalContactDao;

    /**
     * 插入医生信息（单条）
     *
     * @param hospitalVo
     */
    @Override
    public void insertHospitalData(HospitalVo hospitalVo) {
        hospitalDao.insertHospital(hospitalVo);
    }

    @Override
    public HospitalVo getHospital(String id) {
        HospitalVo hospitalVo = new HospitalVo();
        hospitalVo.setId(id);
        //根据医院id查询医院
        hospitalVo = hospitalDao.getHospital(hospitalVo);
        return hospitalVo;
    }

    /**
     * 获取系统所有医院
     *
     * @return
     */
    @Override
    public Page<HospitalVo> findAllHospital(Page<HospitalVo> page, HospitalVo hospitalVo) {
        // 设置分页参数
        hospitalVo.setPage(page);
        // 执行分页查询
        page.setList(hospitalDao.findAllHospital(hospitalVo));
        return page;
    }

    /**
     * 医院修改操作
     */
    @Override
    public void saveEditHospital(HospitalVo hospitalVo) {
        hospitalDao.saveEditHospital(hospitalVo);
        if (null != hospitalVo.getBusinessContactName()) {
            SysHospitalContactVo sysHospitalContactVo = new SysHospitalContactVo();
            sysHospitalContactVo.setContactName(hospitalVo.getBusinessContactName());
            sysHospitalContactVo.setContactPhone(hospitalVo.getBusinessContactPhone());
            sysHospitalContactVo.setSysHospitalId(hospitalVo.getId());
            sysHospitalContactDao.updateByHospitalId(sysHospitalContactVo);
        }
    }

    /**
     * 删除医院操作
     */
    @Override
    public void deleteHospitalByHospitalId(String hopitalId) {
        hospitalDao.deleteHospitalByHospitalId(hopitalId);

        //根据医院主键删除医生信息
        doctorDao.deleteDoctorByHospitalId(hopitalId);

        //根据医院主键医生与医院关联信息
        DoctorHospitalRelationVo doctorHospitalRelationVo = new DoctorHospitalRelationVo();
        doctorHospitalRelationVo.setSysHospitalId(hopitalId);
        doctorHospitalRelationDao.deleteDoctorHospitalRelation(doctorHospitalRelationVo);

        //根据医院主键删除sys_location信息
        DoctorLocationVo doctorLocationVo = new DoctorLocationVo();
        String sysHospitalId = hopitalId;
        doctorLocationVo.setSysHospitalId(sysHospitalId);
        doctorLocationDao.deleteDoctorLocation(doctorLocationVo);
    }
}
