/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.order.web;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.wechat.service.WeChatInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单Controller
 *
 * @author sunxiao
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "")
public class OrderDoctorController extends BaseController {

	@Autowired
	private PatientRegisterService patientRegisterService;

    @Autowired
    DoctorInfoService doctorInfoService;

    @Autowired
    private ConsultPhoneOrderService consultPhoneOrderService;

	@Autowired
	private WeChatInfoService weChatInfoService;

	@RequestMapping(value = "/order/doctor/reminderOrder", method = {RequestMethod.POST, RequestMethod.GET})
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
	@RequestMapping(value = "/order/doctor/settlementInfo", method = {RequestMethod.POST, RequestMethod.GET})
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

    /**
     * 每日清单
     * @params {}
     * @return {}
     */
    @RequestMapping(value="/order/doctor/getDayList")
    public Map<String,Object> getDayList(Map<String,Object> params){
        Map<String,Object> response = new HashMap<String, Object>();
        String userId = UserUtils.getUser().getId();
        String doctorId = (String) params.get("doctorId");
        if(doctorId == null || "".equals(doctorId)){
            Map<String,Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("id",UserUtils.getUser().getId());
            doctorId = (String)doctorInfoService.getDoctorIdByUserIdExecute(paramsMap).get("id");
        }
        String date = (String) params.get("date");

        //预约挂号
        HashMap<String, Object> apppintment = new HashMap<String, Object>();

        HashMap<String, Object> appointResponse = new HashMap<String, Object>();
        patientRegisterService.findDoctorSettlementAppointmentInfoByDate(doctorId,userId,date,appointResponse);
        apppintment.put("totalNum", response.get("appointmentTotal"));
        apppintment.put("totalPrice", response.get("totalAppPrice"));
        List<HashMap<String, Object>> doctorSettlementList = (List<HashMap<String, Object>>) appointResponse.get("appointment");
        List<HashMap<String, Object>> timeList = new ArrayList<HashMap<String, Object>>();
        for(HashMap<String, Object> appoint:doctorSettlementList){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("time",appoint.get("beginTime"));
            map.put("name",appoint.get("babyName"));
            map.put("price",appoint.get("price"));
            timeList.add(map);
        }
        apppintment.put("timeList", timeList);
        response.put("appointment",apppintment);

        //电话咨询
        HashMap<String, Object> phoneConsult = new HashMap<String, Object>();
        HashMap<String, Object> phoneConsultResponse = consultPhoneOrderService.getSettlementPhoneConsultInfoByDate(doctorId,date);
        response.put("phoneConsult",phoneConsultResponse);

        return response;
    }
}
