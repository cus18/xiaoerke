package com.cxqm.xiaoerke.modules.bankend.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
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

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;

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
    public void saveEditHospital(HospitalVo hospitalVo,SysHospitalContactVo contactVo) {
        hospitalDao.saveEditHospital(hospitalVo);
        if ("2".equals(hospitalVo.getHospitalType())) {
            sysHospitalContactDao.updateByHospitalId(contactVo);
            if(StringUtils.isNotNull(contactVo.getHospitalPic())){
                if(!contactVo.getHospitalPic().contains("oss-cn-beijing.aliyuncs.com")){
                    try {
                        uploadHospitalImage(contactVo.getSysHospitalId(),contactVo.getHospitalPic());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if(StringUtils.isNotNull(contactVo.getClinicItemsPic())){
                if(!contactVo.getClinicItemsPic().contains("oss-cn-beijing.aliyuncs.com")){
                    try {
                        uploadHospitalImage(contactVo.getSysHospitalId()+"clinicItems",contactVo.getClinicItemsPic());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void uploadHospitalImage(String id , String src) throws Exception{
        File file = new File(System.getProperty("user.dir").replace("bin", "webapps")+ URLDecoder.decode(src, "utf-8"));
        FileInputStream inputStream = new FileInputStream(file);
        long length = file.length();
        //上传图片至阿里云
        OSSObjectTool.uploadFileInputStream(id, length, inputStream, OSSObjectTool.BUCKET_DOCTOR_PIC);
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
