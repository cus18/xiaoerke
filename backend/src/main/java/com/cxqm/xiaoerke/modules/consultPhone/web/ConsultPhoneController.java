package com.cxqm.xiaoerke.modules.consultPhone.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.order.service.PhoneConsultDoctorRelationService;
import com.cxqm.xiaoerke.modules.order.service.SysConsultPhoneService;

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
	private ConsultPhoneOrderService consultPhoneOrderService;
	
	@Autowired
	private SysConsultPhoneService sysConsultPhoneService;
	
	@Autowired
	private PhoneConsultDoctorRelationService phoneConsultDoctorRelationService;
	
	/**
	 * 号源信息显示及保存
	 * sunxiao
	 * @param
	 * @param model
	 * sunxiao
	 */
	@RequestMapping(value = "registerForm")
	public String registerForm(HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			String pageFlag = request.getParameter("pageFlag");
			String doctorId = request.getParameter("doctorId");
			String doctorName = new String(request.getParameter("doctorName").getBytes("ISO-8859-1"),"utf-8");
			String hospital = new String(request.getParameter("hospital").getBytes("ISO-8859-1"),"utf-8");
			String phone = request.getParameter("phone");
			List<String> dateList = new ArrayList<String>();
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_YEAR, 7 * Integer.parseInt(pageFlag));
			Date firstDay = c.getTime();
			for (int i = 0; i < 7; i++) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(firstDay);
				calendar.add(calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
				Date date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				int dayForWeek = 0;
				if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
					dayForWeek = 7;
				} else {
					dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
				}
				String[] weekDays = { "一", "二", "三", "四", "五", "六", "日" };
				String date_ = DateUtils.DateToStr(date, "date");
				dateList.add(date_ + "(" + weekDays[dayForWeek - 1] + ")");
			}
			ConsulPhonetDoctorRelationVo param = new ConsulPhonetDoctorRelationVo();
			param.setDoctorId(doctorId);
			List<ConsulPhonetDoctorRelationVo> list = phoneConsultDoctorRelationService.getPhoneConsultDoctorRelationByInfo(param);
			String phoneConsultFlag = "";
			ConsulPhonetDoctorRelationVo cvo = new ConsulPhonetDoctorRelationVo();
			if(list.size()==0){
				phoneConsultFlag = "no";
				cvo.setDoctorId(doctorId);
			}else{
				phoneConsultFlag = "yes";
			}
			model.addAttribute("repeatFlag", "1");
			model.addAttribute("intervalFlag", "1");
			model.addAttribute("consulPhonetDoctorRelationVo", list.size()==0?cvo:list.get(0));
			model.addAttribute("pageFlag", pageFlag);
			model.addAttribute("dateList", dateList);
			model.addAttribute("doctorName", doctorName);
			model.addAttribute("hospital", hospital);
			model.addAttribute("phone", phone);
			model.addAttribute("phoneConsultFlag", phoneConsultFlag);
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
		result = phoneConsultDoctorRelationService.openConsultPhone(vo);
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
		Page<ConsultPhoneRegisterServiceVo> pagess = null;
		if(temp==null){
			pagess = new Page<ConsultPhoneRegisterServiceVo>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<ConsultPhoneRegisterServiceVo>(pageNo,pageSize);
		}
		Page<ConsultPhoneRegisterServiceVo> page = consultPhonePatientService.findConsultPhonePatientList(pagess,vo);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("youxiao", "待支付");
		map.put("shixiao", "已支付");
		map.put("yituikuan", "已退款");
		map.put("", "全部");
		model.addAttribute("statusList", map);
		model.addAttribute("page", page);
		model.addAttribute("consultPhone", vo);
		return "modules/consultPhone/orderList";
	}
	
}
