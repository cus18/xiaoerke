/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.order.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.wechat.service.WeChatInfoService;

/**
 * 订单Controller
 *
 * @author sunxiao
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "order/doctor")
public class OrderDoctorController extends BaseController {

	@Autowired
	private PatientRegisterService patientRegisterService;

	@Autowired
	private WeChatInfoService weChatInfoService;
	
    @RequestMapping(value = "/reminderOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getReminderOrder(@RequestBody Map<String, Object> params) throws Exception {
		return patientRegisterService.getReminderOrder(params);
    }

	/**
	 * 获取医生某天的出诊信息
	 * params:{"doctorId":"fjewiofnwe","date":"2015-09-25","type":"appointment"}
	 *
	 * response:
	 * [
	 *   {"beginTime":"14:00","endTime":"14:15","price":"200","babyName":"刘涛","illness":"肚子不舒服","position":"1"},
	 *   {"beginTime":"14:00","endTime":"14:15","price":"200","babyName":"刘涛","illness":"肚子不舒服","position":"1"},
	 *   {"beginTime":"14:00","endTime":"14:15","price":"200","babyName":"刘涛","illness":"肚子不舒服","position":"1"}
	 * ]
	 * 1表示主任医生，2表示副主任医生
	 *
	 * @return 返回信息
	 */
	@RequestMapping(value = "/settlementInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> SettlementInfo(@RequestBody Map<String, Object> params) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		String userId = UserUtils.getUser().getId();
		String doctorId = (String) params.get("doctorId");
		String date = (String) params.get("date");
		if(params.get("type").equals("appointment"))
		{
			patientRegisterService.findDoctorSettlementAppointmentInfoByDate(doctorId,userId,date,response);
			
			String recommendedFee = Global.getConfig("sys.recommended.fee");
	        int recommendedFeeInt =  Global.RECOMMENDED_FEE;
	        try {
				recommendedFeeInt = Integer.parseInt(recommendedFee);
			} catch (NumberFormatException e) {}
	        
			List<Map<String, Object>> fanses = weChatInfoService.findAttentions(userId, date);
	        for(Map<String, Object> fanes : fanses ) {
	        	fanes.put("price", recommendedFeeInt);
	        }
	        
	        response.put("attention",fanses);
	        response.put("attentionTotal", fanses.size());
		}
		return response;
	}
}
