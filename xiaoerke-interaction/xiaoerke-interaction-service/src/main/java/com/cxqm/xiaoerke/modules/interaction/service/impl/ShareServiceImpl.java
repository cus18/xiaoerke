package com.cxqm.xiaoerke.modules.interaction.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.interaction.service.ShareService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;

/**
 * Created by wangbaowei on 15/11/5.
 */
@Service
@Transactional(readOnly = false)
public class ShareServiceImpl implements ShareService{
    
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private HospitalInfoService hospitalInfoService;
	
	@Autowired
	private PatientRegisterService patientRegisterService;
	
	//获取分享信息详情 
	@Override
    public HashMap<String, Object> findShareDetailInfo(HashMap<String, Object> hashMap) {
        HashMap<String, Object> map = messageService.findShareDetailInfoExecute(hashMap);
        return map;
    }
    
	@Override
    public HashMap<String, Object> getMyShareDetail(Map<String, Object> params)
    {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("patientRegisterServiceId", params.get("patientRegisterServiceId"));
        HashMap<String, Object> response = messageService.findShareDetailInfoExecute(hashMap);
        //获取科室
        String departmentName = hospitalInfoService.getDepartmentFullName((String) response.get("doctorId"),
                (String) response.get("hospitalId"));
        response.put("departmentName", departmentName);
        return response;
    }
	
	@Override
	@SystemServiceLog(description = "00000013")//用户分享
	public Map<String, Object> orderShareOperation(Map<String, Object> params, HttpSession session, HttpServletRequest request)
	{
		params.put("urlPath", "baodf.com/xiaoerke-appoint");
		params.put("openId", WechatUtil.getOpenId(session,request));
		String patientRegisterServiceId = (String) params.get("patient_register_service_id");
		if (patientRegisterServiceId == null || "".equals(patientRegisterServiceId)) {
			patientRegisterServiceId = IdGen.uuid();
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> excuteMap = new HashMap<String, Object>();

		//分享处理
		shareHandle(patientRegisterServiceId, excuteMap);

		response.put("patient_register_service_id", patientRegisterServiceId);
		response.put("status", '1');
		return response;
	}
	
	private void shareHandle(String patientRegisterServiceId, HashMap<String, Object> excuteMap) {
		//完成分享    需传入参数register_no
		excuteMap.put("patientRegisterServiceId", patientRegisterServiceId);
		patientRegisterService.completeShareExecute(excuteMap);
	}
    
}
