package com.cxqm.xiaoerke.modules.bankend.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.bankend.service.DoctorService;
import com.cxqm.xiaoerke.modules.order.dao.PatientRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.RegisterServiceDao;
import com.cxqm.xiaoerke.modules.search.service.util.RdsDataSourceJDBC;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorDao;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorHospitalRelationDao;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorLocationDao;
import com.cxqm.xiaoerke.modules.sys.dao.HospitalDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorHospitalRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorLocationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

@Service
@Transactional(readOnly = false)
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    DoctorDao doctorDao;

    @Autowired
    RegisterServiceDao registerServiceDao;

    @Autowired
    HospitalDao hospitalDao;

    @Autowired
    DoctorLocationDao doctorLocationDao;

    @Autowired
    DoctorHospitalRelationDao doctorHospitalRelationDao;

    @Autowired
    AccountService accountService;

    @Autowired
    private PatientRegisterServiceDao patientRegisterServiceDao;

    @Autowired
    RdsDataSourceJDBC rdsDataSourceJDBC;

    /**
     * @param page
     * @param doctorVo
     * @return
     * @author zdl
     * 获取医生信息
     */
    @Override
    public Page<DoctorVo> findDoctor(Page<DoctorVo> page, DoctorVo doctorVo) {
        // 设置分页参数
        doctorVo.setPage(page);
        // 执行分页查询
        page.setList(doctorDao.findDoctorByNameOrByHospitalName(doctorVo));
        return page;
    }

    /**
     * @param doctorVo
     * @return
     * @author zdl
     * 根据doctorVo中的参数获取医生的基本信息
     */
    @Override
    public List<DoctorVo> findDoctorDetailInfo(DoctorVo doctorVo) {
        List<DoctorVo> doctorVos = doctorDao.findDoctorVoDetailInfo(doctorVo);
        if (doctorVos != null) {
            return doctorVos;
        }
        return doctorVos ;
    }

    /**
     * 删除医生操作
     * @param doctorId
     * @return
     * @author zdl
     * 删除医生（慎用）
     */
    @Override
    public void deleteDoctorByDoctorId(String doctorId) {

        if(StringUtils.isNotNull(doctorId)){

            //删除医生的账户信息
            // 根据doctorId查询userid
            HashMap<String,Object> hashMap = new HashMap<String, Object>();
            hashMap.put("doctorId",doctorId);
            List<HashMap<String,Object>> list = doctorDao.findDoctorByDoctorId(hashMap);
            String userId=(String)list.get(0).get("userId");
            accountService.deleteAccountInfo(userId);

            doctorDao.deleteDoctorByDoctorId(doctorId);
            //删除doctor_hospital_relation表信息
            DoctorHospitalRelationVo doctorHospitalRelationVo = new DoctorHospitalRelationVo();
            doctorHospitalRelationVo.setSysDoctorId(doctorId);

            doctorHospitalRelationDao.deleteDoctorHospitalRelation(doctorHospitalRelationVo);

            //删除sys_doctor_location表信息
            DoctorLocationVo doLocationVo = new DoctorLocationVo();
            doLocationVo.setSysDoctorId(doctorId);
            doctorLocationDao.deleteDoctorLocation(doLocationVo);

            //删除sys_register_service表信息
            DoctorLocationVo doctorLocationVo = new DoctorLocationVo();
            doctorLocationVo.setSysDoctorId(doctorId);
            registerServiceDao.deleteRegisterServiceBySysDoctorId(doctorLocationVo);
        }

    }

    /**
     * 医生信息修改后保存操作
     */
    @Override
    public void saveEditDoctor(DoctorVo doctorVo) {
        //修改医生信息
        doctorDao.saveEditDoctor(doctorVo);
        //修改医生所属用户的信息
        doctorDao.saveEditUser(doctorVo);
        //修改rds上的医生表
        if(doctorVo.getIsDisplay().equals("hidden")){
//            rdsDataSourceJDBC.deletedoctor(doctorVo);
        }else{
//            rdsDataSourceJDBC.deletedoctor(doctorVo);
            HashMap<String, Object> doctorMap = new HashMap<String, Object>();
            doctorMap.put("sysDoctorId",doctorVo.getId());
            doctorMap.put("sys_user_id",doctorVo.getSysUserId());
            doctorMap.put("doctorName",doctorVo.getDoctorName());
            doctorMap.put("create_date",new Date());
            doctorMap.put("career_time",new Date());
            doctorMap.put("personal_details",doctorVo.getPersonDetails());
            doctorMap.put("fans_number",doctorVo.getFansNumber());
            doctorMap.put("experince",doctorVo.getExperience());
            doctorMap.put("hospitalName", doctorVo.getHospital());

//            rdsDataSourceJDBC.insertDoctorToRds(doctorMap,"no");
        }
    }

    /**
     * 查询所有与该医生有关的医院
     */
    @Override
    public String findAllHospitalByDoctorId(DoctorVo doctorVo) {
        StringBuffer str = new StringBuffer();
        //医生信息
        List<DoctorVo> list = hospitalDao.findAllHospitalByDoctorId(doctorVo);
        for (DoctorVo vo : list) {
            str.append(vo.getHospital() + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        return str.toString();
    }

    /**
     * 获取医生所关联的医院
     *
     * @param doctorVo
     * @return
     */
    @Override
    public List<String> findAllHospitalListByDoctorId(DoctorVo doctorVo) {
        List<String> arrayList = new ArrayList<String>();
        //医生信息
        List<DoctorVo> list = hospitalDao.findAllHospitalByDoctorId(doctorVo);
        for (DoctorVo vo : list) {
            arrayList.add(vo.getHospital());
        }
        return arrayList;
    }

    /**
     * 查询当前医生与当前医院的关联信息
     * @return
     */
    @Override
    public DoctorHospitalRelationVo findDoctorHospitalRelation(String hospitalName,String doctorId) {
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("hospitalName",hospitalName.trim());
        //根据医院的名称查询医院的主键
        HashMap<String,Object> resultMap=hospitalDao.findHospitalIdByHospitalName(hashMap);
        String hospitalId = (String)resultMap.get("hospitalId");
        hashMap.put("hospitalId",hospitalId);
        hashMap.put("doctorId",doctorId);
        //查询医生与医院关系
        DoctorHospitalRelationVo doctorHospitalRelationVo = doctorHospitalRelationDao.findDoctorHospitalRelation(hashMap);
        //查询医生与就诊地址关系
        List<DoctorLocationVo> dlvlist = doctorLocationDao.getDoctorLocationInfo(hashMap);
        StringBuilder sb = new StringBuilder("");//用户存放就诊地址
        StringBuilder sb1 = new StringBuilder("");//用于存放就诊路线
        for(DoctorLocationVo doctorLocationVo : dlvlist){
            sb.append(doctorLocationVo.getId()+"S|S"+doctorLocationVo.getLocation()+"S|S"+doctorLocationVo.getPrice()+"S|S"+doctorLocationVo.getKindlyReminder()).append("S|X");
            if(doctorLocationVo.getRoute()!=null){
                sb1.append(doctorLocationVo.getRoute().replace("1、停车", "").replace("2、地铁", "").replace("3、公交", "")).append("S|X");
            }else{
                sb1.append(doctorLocationVo.getRoute()).append("S|X");
            }
        }
        doctorHospitalRelationVo.setLocation(sb.toString());//将所有就诊地址返回（如果就诊地址有多个即在当前医院有多个就诊地址则用‘S|X’分隔）
        doctorHospitalRelationVo.setRoute(sb1.toString());//将所有路线返回，如果返回了路线属于不同的就诊地址用‘S|X’分隔
        return  doctorHospitalRelationVo;
    }

    /**
     * 新增医生与医院的关系
     * @param hashMap
     */
    @Override
    public void  saveDoctorHospitalRelation(HashMap<String,Object> hashMap,List<DoctorLocationVo> list){
        doctorHospitalRelationDao.insertDoctorAndHospitalRelation(hashMap);//插入关系表数据
        for(DoctorLocationVo vo : list){
            doctorLocationDao.insertDoctorLocation(vo);
        }
    }

    /**
     * 根据医生与医院关系表主键查询医生信息
     *
     * @param doctorHospitalRelationId
     * @param doctorHospitalRelationId
     * @return
     */
    @Override
    public DoctorHospitalRelationVo findDoctorHospitalRelationById(String doctorHospitalRelationId) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("id", doctorHospitalRelationId);
        DoctorHospitalRelationVo DoctorHospitalRelationVo = doctorHospitalRelationDao.findDoctorHospitalRelation(hashMap);
        return DoctorHospitalRelationVo;
    }

    /**
     * 删除医生的就诊地址
     *
     * @param doctorHospitalRelationVo
     */
    @Override
    public void deleteDoctorLocation(DoctorHospitalRelationVo doctorHospitalRelationVo) {
        DoctorLocationVo doctorLocationVo = new DoctorLocationVo();
        doctorLocationVo.setSysHospitalId(doctorHospitalRelationVo.getSysHospitalId());
        doctorLocationVo.setSysDoctorId(doctorHospitalRelationVo.getSysDoctorId());
        doctorLocationDao.deleteDoctorLocation(doctorLocationVo);
    }

    /**
     * 查询当前医生与当前医院的关联信息
     * @return
     */
    @Override
    public DoctorHospitalRelationVo findDoctorHospitalRelationByIds(String hospitalId,String doctorId) {
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("hospitalId", hospitalId);
        hashMap.put("doctorId", doctorId);
        DoctorHospitalRelationVo doctorHospitalRelationVo = doctorHospitalRelationDao.findDoctorHospitalRelation(hashMap);
        List<DoctorLocationVo> dlvlist = doctorLocationDao.getDoctorLocationInfo(hashMap);
        StringBuilder sb = new StringBuilder("");
        for(DoctorLocationVo doctorLocationVo : dlvlist){
            sb.append(doctorLocationVo.getLocation()).append("S|X");
        }

        if(doctorHospitalRelationVo != null)
            doctorHospitalRelationVo.setPlaceDetail(sb.toString());

        return  doctorHospitalRelationVo;
    }

    /**
     * 删除医生与医院的关联关系
     *
     * @param doctorHospitalRelationId 关联表主键
     */
    @Override
    public void deleteDoctorHospitalRelation(String doctorHospitalRelationId, DoctorHospitalRelationVo doctorHospitalRelationVo) {
        //删除医生在此医院出诊的location 信息
        deleteDoctorLocation(doctorHospitalRelationVo);
        doctorHospitalRelationVo.setId(doctorHospitalRelationId);
        doctorHospitalRelationDao.deleteDoctorHospitalRelation(doctorHospitalRelationVo);
    }

    /**
     * “当前医生与当前医院的关联信息”修改后保存操作
     * @return 123
     */
    @Override
    public void saveDoctorHospitalRelation(DoctorHospitalRelationVo doctorHospitalRelationVo,Map<String, Object> map,Map<String, Object> oldmap,Map<String, Object> tempmap)  {
        DoctorLocationVo doLocationVo = new DoctorLocationVo();
        doLocationVo.setHospitalName(doctorHospitalRelationVo.getHospitalName());
        doLocationVo.setDoctorName(doctorHospitalRelationVo.getDoctorName());
        doLocationVo.setSysDoctorId(doctorHospitalRelationVo.getSysDoctorId());
        doLocationVo.setSysHospitalId(doctorHospitalRelationVo.getSysHospitalId());
        for(String id : map.keySet()){

            String[] str = ((String)map.get(id)).split("S\\|S");

            doLocationVo.setId(IdGen.uuid());
            doLocationVo.setLocation(str[0]);
            doLocationVo.setRoute(str[1]);
            doLocationVo.setPrice(Float.valueOf(str[2]));
            doLocationVo.setKindlyReminder(str[3]);
            doctorLocationDao.insertDoctorLocation(doLocationVo);
        }
        for(String id : oldmap.keySet()){
            DoctorLocationVo vo = new DoctorLocationVo();
            vo.setId(id);
            //根据location_id删除号源信息
//            registerServiceDao.deleteRegisterServiceByLocationId(vo);
            doctorLocationDao.deleteDoctorLocation(vo);
        }
        for(String id : tempmap.keySet()){
            String[] location = ((String)tempmap.get(id)).split("S\\|S");
            doLocationVo.setId(id);
            doLocationVo.setLocation(location[0]);
            doLocationVo.setRoute(location[1]);
            System.out.println(location[2]);
            doLocationVo.setPrice(Float.valueOf(location[2]));
            int a=((String)tempmap.get(id)).split("S\\|S").length;
            if(((String)tempmap.get(id)).split("S\\|S").length > 3){
                doLocationVo.setKindlyReminder(((String)tempmap.get(id)).split("S\\|S")[3]);
            }
            doctorLocationDao.updateDoctorLocation(doLocationVo);
        }
        doctorHospitalRelationDao.saveDoctorHospitalRelation(doctorHospitalRelationVo);
    }

    @Override
    public void uploadDoctorPic(String key, Long length, InputStream in) {
        OSSObjectTool.uploadFileInputStream(key, length, in, OSSObjectTool.BUCKET_DOCTOR_PIC);
    }

}
