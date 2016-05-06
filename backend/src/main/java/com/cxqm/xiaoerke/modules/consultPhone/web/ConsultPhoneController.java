package com.cxqm.xiaoerke.modules.consultPhone.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.excel.ExportExcel;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneManuallyConnectVo;
import com.cxqm.xiaoerke.modules.order.exception.CancelOrderException;
import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.order.service.PhoneConsultDoctorRelationService;
import com.cxqm.xiaoerke.modules.order.service.SysConsultPhoneService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 电话咨询Controller
 * @author sunxiao
 * @version 2016-3-22
 */
@Controller(value = "ConsultPhoneController")
@RequestMapping(value = "${adminPath}/consultPhone")
public class ConsultPhoneController extends BaseController {

	@Autowired
	private ConsultPhonePatientService consultPhonePatientService;

	@Autowired
	private SysConsultPhoneService sysConsultPhoneService;

	@Autowired
	private PhoneConsultDoctorRelationService phoneConsultDoctorRelationService;

	/**
	 * 号源信息显示及保存
	 * sunxiao
	 * @param
	 * @param model
	 */
	@RequestMapping(value = "registerForm")
	public String registerForm(HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			String pageFlag = request.getParameter("pageFlag");
			String doctorId = request.getParameter("doctorId");
			String doctorName = new String(request.getParameter("doctorName").getBytes("ISO-8859-1"),"utf-8");
			String hospital = new String(request.getParameter("hospital").getBytes("ISO-8859-1"),"utf-8");
			String phone = request.getParameter("phone");
			Map<String, Object> resultMap = sysConsultPhoneService.getRegisterInfo(doctorId,pageFlag);

			model.addAttribute("repeatFlag", "1");
			model.addAttribute("intervalFlag", "1");
			model.addAttribute("pageFlag", pageFlag);
			model.addAttribute("doctorName", doctorName);
			model.addAttribute("hospital", hospital);
			model.addAttribute("phone", phone);
			model.addAttribute("consulPhonetDoctorRelationVo", resultMap.get("consulPhonetDoctorRelationVo"));
			model.addAttribute("dateList", resultMap.get("dateList"));
			model.addAttribute("phoneConsultFlag", resultMap.get("phoneConsultFlag"));
			model.addAttribute("serverType", resultMap.get("serverType"));
			model.addAttribute("serverLength", resultMap.get("serverLength"));
			model.addAttribute("beginTimeList", resultMap.get("beginTimeList"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "modules/consultPhone/registerForm";
	}

	/**
	 * 添加号源页面切换时间时查询号源
	 * @param
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "getRegisterTime")
	public String getRegisterTime(HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		String date = request.getParameter("date");
		String doctorId = request.getParameter("doctorId");
		result = sysConsultPhoneService.getRegisterTime(doctorId,date);
		return result.toString();
	}

	/**
	 * 开通电话咨询
	 * sunxiao
	 * @param
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "openConsultPhone")
	public String openConsultPhone(ConsulPhonetDoctorRelationVo vo ,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		vo.setCreateTime(new Date());
		vo.setState("1");
		vo.setUpdateTime(new Date());
		try {
			result = phoneConsultDoctorRelationService.openConsultPhone(vo);
			result.put("result", "suc");
		} catch (Exception e) {
			result.put("result", "fail");
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 添加电话咨询号源
	 * sunxiao
	 * @param
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "addRegister")
	public String addRegister(SysConsultPhoneServiceVo vo ,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		String time = request.getParameter("time");
		String times = request.getParameter("times");
		List<String> timeList = new ArrayList<String>();
		if(times!=null){
			String[] timeArray = times.split(";");
			for(String s : timeArray){
				timeList.add(s);
			}
		}
		if(time!=null){
			timeList.add(time);
		}
		Map<String, String> ret = null;
		try {
			ret = sysConsultPhoneService.addRegisters(vo,timeList,request.getParameter("date"),request.getParameter("repeat"));
			result.put("result", "suc");
			result.put("reason", ret.get("backend"));
		} catch (Exception e) {
			result.put("result", "fail");
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 删除号源
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "removeRegister")
	public synchronized String removeRegister(HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		String times = request.getParameter("times");
		String time = request.getParameter("time");
		SysConsultPhoneServiceVo vo = new SysConsultPhoneServiceVo();
		vo.setSysDoctorId(request.getParameter("sysDoctorId"));
		List<String> timeList = new ArrayList<String>();
		if(times!=null){
			String[] timeArray = times.split(";");
			for(String s : timeArray){
				timeList.add(s);
			}
		}
		if(time!=null){
			timeList.add(time);
		}
		try {
			sysConsultPhoneService.deleteRegisters(vo, timeList, request.getParameter("date"), request.getParameter("operRepeat"));
			result.put("result", "suc");
		} catch (Exception e) {
			result.put("result", "fail");
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 删除号源时判断受影响的号源
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "judgeRepeatEffect")
	public String judgeRepeatEffect(HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		String date = request.getParameter("date");
		String pageFlag = request.getParameter("pageFlag");
		String operRepeat = request.getParameter("operRepeat");
		String timeParam = request.getParameter("times");
		String doctorId = request.getParameter("sysDoctorId");
		String re = sysConsultPhoneService.judgeRepeatEffect(date, timeParam, doctorId,operRepeat);
		result.put("reason", re);
		result.put("pageFlag", pageFlag);
		return result.toString();
	}

	/**
	 * 电话咨询订单列表
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequiresPermissions("consultPhone:consultPhoneOrderList")
	@RequestMapping(value = "consultPhoneOrderList")
	public String consultPhoneOrderList(ConsultPhoneRegisterServiceVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		String returnUrl = "";
		if("remove".equals(request.getParameter("stateFlag"))){
			vo.setState("4");
			returnUrl = "modules/consultPhone/removedOrderList";
		}else{
			returnUrl = "modules/consultPhone/orderList";
		}
		Page<ConsultPhoneRegisterServiceVo> pagess = null;
		if(temp==null){
			pagess = new Page<ConsultPhoneRegisterServiceVo>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<ConsultPhoneRegisterServiceVo>(pageNo,pageSize);
		}
		Page<ConsultPhoneRegisterServiceVo> page = consultPhonePatientService.findConsultPhonePatientList(pagess,vo);
		Map<String, Object> payStateMap = new LinkedHashMap<String, Object>();
		payStateMap.put("0", "待支付");
		payStateMap.put("1", "已支付");
		payStateMap.put("4", "已退款");
		payStateMap.put("6", "未支付");
		payStateMap.put("", "全部");
		Map<String, Object> stateMap = new LinkedHashMap<String, Object>();
		stateMap.put("0", "待支付");
		stateMap.put("1", "待接通");
		stateMap.put("2", "待评价");
		stateMap.put("3", "待分享");
		stateMap.put("4", "已取消");
		stateMap.put("5", "已完成");
		stateMap.put("6", "超时取消");
		stateMap.put("", "全部");
		model.addAttribute("statusList", stateMap);
		model.addAttribute("payStatusList", payStateMap);
		model.addAttribute("page", page);
		model.addAttribute("pageCount", page.getCount());
		model.addAttribute("consultPhone", vo);
		return returnUrl;
	}

	/**
	 * 手动拨号
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "getNewOrderCount")
	public String getNewOrderCount(HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		String state = request.getParameter("state");
		int count = consultPhonePatientService.getNewOrderCount(state);
		result.put("orderCount",count);
		return result.toString();
	}

	/**
	 * 导出电话咨询订单数据
	 * sunxiao
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "export", method= RequestMethod.POST)
	public String exportFile(ConsultPhoneRegisterServiceVo vo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "订单数据"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
			List<ConsultPhoneRegisterServiceVo> list = consultPhonePatientService.getAllConsultPhoneRegisterListByInfo(vo);
			new ExportExcel("订单数据", ConsultPhoneRegisterServiceVo.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/register/patientRegisterList";
	}

	/**
	 * 电话咨询退费表单
	 * @param
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "refundConsultPhoneFeeForm")
	public String refundConsultPhoneFeeForm(ConsultPhoneRegisterServiceVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		model.addAttribute("vo", vo);
		return "modules/consultPhone/refundConsultPhoneFeeForm";
	}

	/**
	 * 取消预约
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "cancelOrder")
	public synchronized String cancelOrder(HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		String id = request.getParameter("id");
		String cancelReason = request.getParameter("cancelReason");
		try {
			consultPhonePatientService.cancelOrder(Integer.valueOf(id), cancelReason,"0");
			result.put("result","suc");
		} catch (CancelOrderException e) {
			result.put("result","fail");
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 电话咨询手动接通页
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequiresPermissions("consultPhone:consultPhoneOrderList")
	@RequestMapping(value = "manuallyConnectForm")
	public String manuallyConnectForm(ConsultPhoneRegisterServiceVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		Map map = consultPhonePatientService.manuallyConnectFormInfo(vo);
		model.addAttribute("consultPhone", vo);
		model.addAttribute("map", map);
		return "modules/consultPhone/phoneInterruptHandle";
	}

	/**
	 * 手动拨号
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "timingDial")
	public String timingDial(ConsultPhoneManuallyConnectVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		String surplusTimeStr = request.getParameter("surplusTimeStr");
		vo.setSurplusTime(Long.valueOf(surplusTimeStr.split(":")[0])*60*1000 + Long.valueOf(surplusTimeStr.split(":")[1])*1000);
		try {
			result = consultPhonePatientService.manuallyConnect(vo);
		}catch (Exception e){
			e.printStackTrace();
			result.put("result", "操作失败！");
		}
		return result.toString();
	}
}
