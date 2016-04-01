/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.register.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.service.NotificationService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;

/**
 * 号源Controller
 * @author sunxiao
 * 2015-7-30
 */
@Controller(value = "BackendRegisterController")
@RequestMapping(value = "${adminPath}/register")
public class RegisterController extends BaseController {

	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private NotificationService notificationService;
	
	/**
	 * 号源信息显示及保存
	 * @param user
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("register:registerForm")
	@RequestMapping(value = "registerForm")
	public String registerForm(HttpServletRequest request,HttpServletResponse response, Model model) {
		String doctorId = request.getParameter("id");
		String pageFlag = request.getParameter("pageFlag");
		Map<String, Object> map = registerService.returnRegisterInfo(doctorId,pageFlag);
		if(map!=null){
			model.addAttribute("dateList",map.get("dateList"));
			model.addAttribute("locationList",map.get("locationList"));
			model.addAttribute("registerServiceVo", map.get("registerServiceVo"));
			model.addAttribute("repeatFlag", map.get("repeatFlag"));
			model.addAttribute("intervalFlag", map.get("intervalFlag"));
			model.addAttribute("beginTimeList", map.get("timeList"));
			model.addAttribute("distimeList", map.get("distimeList"));
			model.addAttribute("pageFlag", pageFlag);
		} else {
			return "modules/register/noRegisterLocation";
		}
		Calendar cal = Calendar.getInstance();
		boolean showFloatLayerFlag = false;
		if(cal.get(Calendar.HOUR_OF_DAY)>=20){
			showFloatLayerFlag = true;
		}
		model.addAttribute("showFloatLayerFlag", showFloatLayerFlag);
		return "modules/register/registerForm";
	}
	
	/**
	 * 添加号源页面切换时间和地点时查询号源
	 * @param user
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "getRegisterTime")
	public String getRegisterTime(HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		String date = request.getParameter("date");
		String doctorId = request.getParameter("sysDoctorId");
		String locationId = request.getParameter("locationId");
		result = registerService.getRegisterTime(doctorId,date,locationId);
		return result.toString();
	}
	
	/**
	 * 修改号源价格，服务类型页面
	 * @param user
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "operRegisterForm")
	public String operRegisterForm(RegisterServiceVo registerServiceVo,HttpServletRequest request,HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		String operFlag = request.getParameter("operFlag");
		String delFlag = request.getParameter("delFlag");
		model.addAttribute("time", request.getParameter("time"));
		model.addAttribute("date", request.getParameter("date"));
		if("oper".equals(operFlag)){
			Map<String, Object> params = new HashMap<String, Object>(8);
	    	params.put("sysDoctorId", request.getParameter("sysDoctorId"));
	    	params.put("locationId", request.getParameter("locationId"));
	    	params.put("date", request.getParameter("date"));
	    	params.put("time", request.getParameter("time"));
	    	List<String> statusList = new ArrayList<String>();
	    	statusList.add("0");
	    	statusList.add("1");
	    	params.put("statusList", statusList);
	    	List<RegisterServiceVo> list = registerService.getRegisterListByInfo(params);
	    	model.addAttribute("registerServiceVo", list.get(0));
			if("del".equals(delFlag)){
				return "modules/register/delRegister";
			}else{
				return "modules/register/updateRegister";
			}
		}else{
			return "modules/register/addRegister";
		}
		
	}
	
	/**
	 * 保存单个或多个号源
	 * @param user
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "addRegister")
	public String addRegister(HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		RegisterServiceVo vo = new RegisterServiceVo();
		String time = request.getParameter("time");
		String times = request.getParameter("times");
		vo.setSysDoctorId(request.getParameter("sysDoctorId"));
		vo.setSysHospitalId(request.getParameter("sysHospitalId"));
		vo.setPrice(Float.parseFloat(request.getParameter("price")));
		vo.setServiceType(request.getParameter("serverType"));
		vo.setLocationId(request.getParameter("locationId"));
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
		Map<String, String> ret = registerService.addRegister(vo,timeList,request.getParameter("date"),request.getParameter("operInterval"));
		result.put("suc", "suc");
		result.put("reason", ret.get("backend"));
		return result.toString();
	}
	
	/**
	 * 修改号源
	 * @param user
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "updateRegister")
	public String updateRegister(HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		RegisterServiceVo vo = new RegisterServiceVo();
		vo.setId(request.getParameter("registerId"));
		vo.setSysDoctorId(request.getParameter("sysDoctorId"));
		vo.setPrice(Float.parseFloat(request.getParameter("price")));
		vo.setServiceType(request.getParameter("serverType"));
		vo.setLocationId(request.getParameter("locationId"));
		registerService.updateRegister(vo,request.getParameter("time"),request.getParameter("date"),request.getParameter("operRepeat"));
		result.put("suc", "suc");
		return result.toString();
	}
	
	/**
	 * 删除号源
	 * @param user
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "removeRegister")
	public String removeRegister(HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		String times = request.getParameter("times");
		String time = request.getParameter("time");
		String pageFlag = request.getParameter("pageFlag");
		RegisterServiceVo vo = new RegisterServiceVo();
		vo.setId(request.getParameter("registerId"));
		vo.setSysDoctorId(request.getParameter("sysDoctorId"));
		vo.setSysHospitalId(request.getParameter("sysHospitalId"));
		vo.setLocationId(request.getParameter("locationId"));
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
		registerService.deleteRegisters(vo,timeList,request.getParameter("date"),request.getParameter("operRepeat"),"kefu");
		result.put("suc", "suc");
		return result.toString();
	}
	
	/**
	 * 即将没有号源的列表
	 * @param
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("register:willNoRegisterList")
	@RequestMapping(value = "willNoRegisterList")
	public String willNoRegisterList(RegisterServiceVo registerServiceVo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		Page<RegisterServiceVo> pagess = null;
		if(temp==null){
			pagess = new Page<RegisterServiceVo>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<RegisterServiceVo>(pageNo,pageSize);
		}
		Page<RegisterServiceVo> page = registerService.findWillNoRegisterList(pagess);
		model.addAttribute("page", page);
		return "modules/register/willNoRegisterList";
	}
	
	/**
	 * 删除号源时判断受影响的号源
	 * @param user
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
		String locationId = request.getParameter("locationId");
		String doctorId = request.getParameter("sysDoctorId");
		String re = registerService.judgeRepeatEffect(date, timeParam, locationId, doctorId,operRepeat);
		result.put("reason", re);
		result.put("pageFlag", pageFlag);
		return result.toString();
	}
	
	/**
	 * 号源列表
	 * @param
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("register:registerList")
	@RequestMapping(value = "registerList")
	public String registerList(RegisterServiceVo registerServiceVo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		Page<RegisterServiceVo> pagess = null;
		if(temp==null){
			pagess = new Page<RegisterServiceVo>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<RegisterServiceVo>(pageNo,pageSize);
		}
		String orderBy = request.getParameter("orderBy");
		if(null!=orderBy&&!"".equals(orderBy)){
			String[] orderBys = request.getParameter("orderBy").split(" ");
			pagess.setOrderBy("CONVERT("+orderBys[0]+" USING gbk)"+orderBys[1]);
		}
		Map map = registerService.findRegisterList(pagess, registerServiceVo);
		Page<RegisterServiceVo> page = (Page<RegisterServiceVo>) map.get("page");
		page.setOrderBy(orderBy);
		Map<String, Object> priceMap = new HashMap<String, Object>();
		priceMap.put("", "全部的");
		priceMap.put("100", "少于100");
		priceMap.put("100-300", "100-300");
		priceMap.put("300", "多于300");
		Map<String, Object> relationTypeMap = new HashMap<String, Object>();
		relationTypeMap.put("1", "公立医院");
		relationTypeMap.put("2", "合作机构");
		relationTypeMap.put("", "全部的");
		model.addAttribute("illLevel1List", map.get("illLevel1List"));
        model.addAttribute("page", page);
        model.addAttribute("priceMap", priceMap);
        model.addAttribute("relationTypeMap", relationTypeMap);
        model.addAttribute("hospitalList", map.get("hospital"));
        model.addAttribute("doctorNameList", map.get("doctorList"));
        model.addAttribute("registerServiceVo", registerServiceVo);
        return "modules/register/registerList";
	}
	
	/**
	 * 取消号源
	 * @param user
	 * @param model
	 * sunxiao
	 */
	@RequiresPermissions("register:deleteRegister")
	@RequestMapping(value = "deleteRegister")
	public String deleteRegister(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String registerId = request.getParameter("id");
		registerService.deleteRegister(registerId);
		addMessage(redirectAttributes, "删除号源成功！");
		return "redirect:" + adminPath + "/register/registerList?repage";
	}
	/**
	 * 批量取消号源
	 * @param
	 * @param
	 * @return
	 */
	@RequiresPermissions("user")
    @ResponseBody
	@RequestMapping(value = "deleteRegisterArray")
	public String deleteRegisterArray(String tArray) {
		String[] registerIds = tArray.split("-");
		for (int i = 0; i < registerIds.length; i++) {
			registerService.deleteRegister(registerIds[i]);
		}
		return "123";
	}
	
}
