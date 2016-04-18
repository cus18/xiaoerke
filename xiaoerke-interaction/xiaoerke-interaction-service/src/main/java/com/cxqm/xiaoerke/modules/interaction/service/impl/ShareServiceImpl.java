package com.cxqm.xiaoerke.modules.interaction.service.impl;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.interaction.service.ShareService;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbaowei on 15/11/5.
 */
@Service
@Transactional(readOnly = false)
public class ShareServiceImpl implements ShareService{
    
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ConsultPhonePatientService consultPhonePatientService;
	
	@Autowired
	private PatientRegisterService patientRegisterService;

    @Autowired
    private ConsultPhoneOrderService consultPhoneOrderService;
	
	//获取分享信息详情 
	@Override
    public HashMap<String, Object> findShareDetailInfo(HashMap<String, Object> hashMap) {
        HashMap<String, Object> map = messageService.findShareDetailInfoExecute(hashMap);
        return map;
    }
    
	@Override
    public HashMap<String, Object> getMyShareDetail(Map<String, Object> params)
    {
        String type = (String)params.get("type");
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("patientRegisterServiceId", params.get("patientRegisterServiceId"));
        HashMap<String, Object> response = null;
        if(type != null && "phone".equals(type)){

            response = messageService.findPhoneConsultShareDetailInfoExecute(hashMap);
            ConsultPhoneRegisterServiceVo vo =  new ConsultPhoneRegisterServiceVo();
            vo.setId((Integer)params.get("patientRegisterServiceId"));
            vo.setUpdateTime(new Date());
            vo.setState("5");
            consultPhonePatientService.updateOrderInfoBySelect(vo);
        }else{
            response = messageService.findShareDetailInfoExecute(hashMap);
        }

        //获取科室
//        String departmentName = hospitalInfoService.getDepartmentFullName((String) response.get("doctorId"),
//                (String) response.get("hospitalId"));
//        response.put("departmentName", departmentName);
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
        String type = (String) params.get("type");

		//分享处理
		shareHandle(patientRegisterServiceId, excuteMap, type);

		response.put("patient_register_service_id", patientRegisterServiceId);
		response.put("status", '1');
		return response;
	}
	
	private void shareHandle(String patientRegisterServiceId, HashMap<String, Object> excuteMap,String type) {
		//完成分享    需传入参数register_no
		excuteMap.put("patientRegisterServiceId", patientRegisterServiceId);
        if(type != null && "phone".equals(type)){
            excuteMap.put("state","5");
            consultPhoneOrderService.changeConsultPhoneRegisterServiceState(excuteMap);
        }else{
            patientRegisterService.completeShareExecute(excuteMap);
        }
	}
    
}
