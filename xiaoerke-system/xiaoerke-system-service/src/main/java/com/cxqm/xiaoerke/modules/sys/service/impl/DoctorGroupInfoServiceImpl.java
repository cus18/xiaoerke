package com.cxqm.xiaoerke.modules.sys.service.impl;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.dao.*;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorGroupVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.service.DoctorGroupInfoService;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = false)
public class DoctorGroupInfoServiceImpl implements DoctorGroupInfoService {

    @Autowired
    private DoctorGroupInfoDao doctorGroupInfoDao;

    @Override
    public HashMap<String,Object> findPageAllDoctorGroup(Page<HashMap<String, Object>> page,HashMap<String, Object> param)
    {
        HashMap<String,Object> response = new HashMap<String,Object>();
        Page<DoctorGroupVo> resultPage = doctorGroupInfoDao.findPageAllDoctorGroup(page,param);
        List<HashMap<String,Object>> doctorGroupList = new ArrayList<HashMap<String, Object>>();
        for(DoctorGroupVo resultMap :resultPage.getList()){
            HashMap<String,Object> doctorGroup = new HashMap<String,Object>();
            Integer doctorGroupId = resultMap.getDoctorGroupId();
            String doctorId = resultMap.getDoctorId();
            List<DoctorVo> doctorList = doctorGroupInfoDao.findDoctorListInDoctorGroup(doctorGroupId);
            String list="";
            int times = 0;
            for(DoctorVo doctorMap:doctorList){
                if(times==4){
                    list = list.substring(0,list.length()-1);
                    break;
                }
                list = list + doctorMap.getId() + ";";
                times++;
            }
//            list = doctorId + ";" + list;
            doctorGroup.put("doctorGroupId",resultMap.getDoctorGroupId());
            doctorGroup.put("doctorGroupName",resultMap.getName());
            doctorGroup.put("description",resultMap.getDescription());
            doctorGroup.put("expertise",resultMap.getExpertise());
            doctorGroup.put("doctorIdList",list);
            doctorGroupList.add(doctorGroup);
        }
        response.put("doctorGroupList",doctorGroupList);
        return response;
    }

    @Override
    public HashMap<String,Object> getDoctorGroupInfo(String doctorGroupId){
        HashMap<String,Object> response = new HashMap<String, Object>();
        DoctorGroupVo doctorGroupInfo = doctorGroupInfoDao.getDoctorGroupInfo(Integer.parseInt(doctorGroupId));
        response.put("doctorGroupId",doctorGroupInfo.getDoctorGroupId());
        response.put("doctorGroupName",doctorGroupInfo.getName());
        response.put("description",doctorGroupInfo.getDescription());
        response.put("expertise", doctorGroupInfo.getExpertise());
        response.put("doctorId",doctorGroupInfo.getDoctorId());
        return response;
    }

    @Override
    public Page<HashMap<String, Object>> findDoctorByDoctorGroup(HashMap<String, Object> doctorMap, Page<HashMap<String, Object>> page) {
        Page<HashMap<String, Object>> pageVo = doctorGroupInfoDao.findDoctorByDoctorGroup(doctorMap, page);
        return pageVo;
    }

    @Override
    public DoctorGroupVo getDoctorGroupInfoByDoctor(String doctorId){
        return doctorGroupInfoDao.getDoctorGroupInfoByDoctor(doctorId);
    }

    /**
     * 激活医生电话咨询时修改sys_doctor_group表
     * sunxiao
     */
	@Override
	public void updateSysDoctorGroup(Map<String, Object> map) {
		// TODO Auto-generated method stub
		doctorGroupInfoDao.updateSysDoctorGroup(map);
	}

}
