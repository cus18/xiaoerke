package com.cxqm.xiaoerke.modules.sys.web;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.entity.UnCertifiedDoctorInfo;
import com.cxqm.xiaoerke.modules.sys.service.UnCertifiedDoctorInfoService;
/**
 * 未审核医生
 * @author 张博
 * @version 2015年9月23日
 */
@Controller
@RequestMapping(value = "${xiaoerkePath}")
public class UnCertifiedDoctorInfoController {

	 @Autowired
	 private UnCertifiedDoctorInfoService unCertifiedDoctorInfo;
	
	 @RequestMapping(value = "/unCertifiedDoctorInfo/save", method = {RequestMethod.POST, RequestMethod.GET})
	 public @ResponseBody
	    Map<String, Object>  saveUnCertifiedDoctorInfo(@RequestBody Map<String, Object> params){
		 	UnCertifiedDoctorInfo  doctorInfo=new UnCertifiedDoctorInfo();
	        HashMap<String, Object> response = new HashMap<String, Object>();
	        doctorInfo.setName((String)params.get("name"));
	        doctorInfo.setCreatedTime(new Date());
	        doctorInfo.setDepartment(params.get("department").toString());
	        doctorInfo.setHospital(params.get("hospital").toString());
	        doctorInfo.setUpdatedTime(new Date());
	        doctorInfo.setMobile(params.get("mobile").toString());
	        Object createdBy = params.get("created_by");
	        if( createdBy != null && !"".equals(createdBy) ){
	        doctorInfo.setCreatedBy(params.get("created_by").toString());
	        }
	        Page<UnCertifiedDoctorInfo> page=unCertifiedDoctorInfo.findPageUnCertifiedDoctorInfo(new Page<UnCertifiedDoctorInfo>(),doctorInfo);
	        if(page.getList().size()>0){
	        	UnCertifiedDoctorInfo udi=new UnCertifiedDoctorInfo();
	        	udi.setId(page.getList().get(0).getId());
	        	udi.setUpdatedTime(new Date());
	        	unCertifiedDoctorInfo.updateByPrimaryKeySelective(udi);
	        	 response.put("status", 0);
	        }else{
		        int i=unCertifiedDoctorInfo.insertUnCertifiedDoctorInfo(doctorInfo);
		        response.put("status", 1);
	        }
	        return response;
	 }
}
	 
