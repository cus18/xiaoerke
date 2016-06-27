package com.cxqm.xiaoerke.modules.healthManagement.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfo;
import com.cxqm.xiaoerke.modules.plan.service.NutritionManagementService;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;

/**
 * 营养管理控制器
 * Created by sunxiao on 16/2/23.
 */

@Controller
@RequestMapping(value = "nutrition")
public class NutritionManagementController {

	@Autowired
	private PlanInfoService planInfoService;
	
	@Autowired
	private NutritionManagementService nutritionManagementService;

	/**
	 * 保存宝宝信息
	 * sunxiao
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/saveBabyInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  saveBabyInfo(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			planInfoMap.put("userId",UserUtils.getUser().getId());
			planInfoMap.put("status","pending");
			planInfoMap.put("planTemplateId",2);
			List<Map<String,Object>> manageList = planInfoService.getPlanInfoByUserId(planInfoMap);
			for(Map<String,Object> temp : manageList){
				PlanInfo planInfo=new PlanInfo();
				planInfo.setId(Long.valueOf(String.valueOf(temp.get("id"))));
				planInfo.setStatus("ongoing");
				planInfo.setExtraInfo((String)params.get("gender")+";"+(String)params.get("birthday")+";"+String.valueOf(params.get("height"))+";"+String.valueOf(params.get("weight")));
				planInfoService.updatePlanInfoById(planInfo);
			}
			
			map.put("resultCode", 0);
			map.put("resultMsg", "OK");
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
			return map;
		}
	}
	
	/**
	 * 判断用户创建了哪个管理
	 * sunxiao
	 * @param params
	 * @return 返回用户创建的全部管理，用;分隔
	 */
	@RequestMapping(value = "/judgeUserManage", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  judgeUserManage(@RequestParam Map<String, Object> params,HttpServletRequest request,HttpServletResponse response){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			StringBuffer sb = new StringBuffer();
			if(!"healthManagement1".equals(CookieUtils.getCookie(request, "healthManagement"))){
				sb.append("yes;");
				CookieUtils.setCookie(response, "healthManagement", "healthManagement1", 3600*24*365);
			}else{
				sb.append("no;");
			}
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			List<String> list = new ArrayList<String>();
			list.add("ongoing");
			planInfoMap.put("userId",UserUtils.getUser().getId());
			planInfoMap.put("statusList",list);
			List<Map<String,Object>> manageList = planInfoService.getPlanInfoByUserId(planInfoMap);
			for(Map<String,Object> temp : manageList){
				if(!sb.toString().contains(String.valueOf(temp.get("plan_template_id")))){
					sb.append(String.valueOf(temp.get("plan_template_id"))).append(";");
				}
			}
			map.put("resultCode", 0);
			map.put("resultMsg", sb.toString());
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
			return map;
		}
	}
	
	/**
	 * 保存用户管理信息,点击首页加号时添加
	 * sunxiao
	 * @return
	 */
	@RequestMapping(value = "/saveManagementInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  saveManagementInfo(@RequestParam short planTemplateId,HttpServletRequest request,HttpSession session){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		Map<String ,Object> map=new HashMap<String, Object>();
		String userId=UserUtils.getUser().getId();
		String openId = WechatUtil.getOpenId(session,request);
		try {
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			List<String> list = new ArrayList<String>();
			list.add("ongoing");
			list.add("pending");
			planInfoMap.put("planTemplateId",planTemplateId);
			planInfoMap.put("userId",userId);
			planInfoMap.put("statusList",list);
			List<Map<String,Object>> manageList = planInfoService.getPlanInfoByUserId(planInfoMap);
			if(manageList.size()==0){
				PlanInfo p = new PlanInfo();
				if(planTemplateId==1){
					p.setName("便秘管理");
				}else if(planTemplateId==2){
					p.setName("营养管理");
				}
				p.setPlanTemplateId(planTemplateId);
				p.setStatus("pending");
				p.setCreateTime(new Date());
				p.setCreateBy(userId);
				p.setUpdateTime(new Date());
				p.setUpdateBy(userId);
				p.setUserId(userId);
				p.setOpenId(openId);
				p.setNotice("yes");
				planInfoService.savePlanInfo(p);
			}
			map.put("resultCode", 0);
			map.put("resultMsg", "OK");
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
			return map;
		}
	}
	
	/**
	 * 返回宝宝信息，extra_info中包括宝宝的性别，生日，身高，体重；用;分隔
	 * sunxiao
	 * @return
	 */
	@RequestMapping(value = "/getBabyInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getBabyInfo(){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			planInfoMap.put("userId",UserUtils.getUser().getId());
			planInfoMap.put("status","ongoing");
			planInfoMap.put("planTemplateId",2);
			List<Map<String,Object>> list = planInfoService.getPlanInfoByUserId(planInfoMap);
			if(list.size()!=0){
				map.put("babyInfo", list.get(0).get("extra_info"));
				map.put("switch", list.get(0).get("notice"));
				map.put("id", list.get(0).get("id"));
			}
			map.put("resultCode", 0);
			map.put("resultMsg", "OK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 修改宝宝信息
	 * sunxiao
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/updateBabyInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  updateBabyInfo(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			planInfoMap.put("id", params.get("id"));
			List<Map<String,Object>> list = planInfoService.getPlanInfoByUserId(planInfoMap);
			String extraInfo = (String) list.get(0).get("extra_info");
			String[] extraInfos = extraInfo.split(";");
			PlanInfo planInfo = new PlanInfo();
			if(null!=params.get("gender")){
				extraInfos[0] = (String)params.get("gender");
			}
			if(null!=params.get("birthday")){
				extraInfos[1] = (String)params.get("birthday");
			}
			if(null!=params.get("height")){
				extraInfos[2] = String.valueOf(params.get("height"));
			}
			if(null!=params.get("weight")){
				extraInfos[3] = String.valueOf(params.get("weight"));
			}
			planInfo.setExtraInfo(extraInfos[0]+";"+extraInfos[1]+";"+extraInfos[2]+";"+extraInfos[3]);
			planInfo.setId(Long.valueOf(String.valueOf(params.get("id"))));
			planInfoService.updatePlanInfoById(planInfo);
			map.put("resultCode", 0);
			map.put("resultMsg", "OK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 获取一天的食谱
	 * sunxiao
	 * @return
	 */
	@RequestMapping(value = "/getRecipes", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getRecipes(@RequestParam String birthday){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			map = nutritionManagementService.getRecipes(birthday);
			map.put("resultCode", 0);
			map.put("resultMsg", "OK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 获取一天的食谱
	 * sunxiao
	 * @return
	 */
	@RequestMapping(value = "/getTodayRead", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getTodayRead(){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			map = nutritionManagementService.getTodayRead();
			map.put("resultCode", 0);
			map.put("resultMsg", "OK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 保存或修改评估
	 * sunxiao
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/saveUpdateEvaluate", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  saveUpdateEvaluate(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			params.put("userId", UserUtils.getUser().getId());
			nutritionManagementService.saveUpdateEvaluate(params);
			map.put("resultCode", 0);
			map.put("resultMsg", "OK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
		}
		return map;
	}
		
	/**
	 * 获取评估报告
	 * sunxiao
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getEvaluate", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getEvaluate(@RequestParam String flag){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", UserUtils.getUser().getId());
			param.put("flag",flag);
			map = nutritionManagementService.getEvaluate(param);
			map.put("resultCode", 0);
			map.put("resultMsg", "OK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 修改是否发送营养管理消息
	 * sunxiao
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/updateSendWechatMessage", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  updateSendWechatMessage(@RequestParam String flag){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", UserUtils.getUser().getId());
			param.put("flag",flag);
			nutritionManagementService.updateSendWechatMessage(param);
			map.put("resultCode", 0);
			map.put("resultMsg", "OK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 重新评估
	 * sunxiao
	 * @return
	 */
	@RequestMapping(value = "/reEvaluate", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  reEvaluate(){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			nutritionManagementService.reEvaluate(UserUtils.getUser().getId());
			map.put("resultCode", 0);
			map.put("resultMsg", "OK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
		}
		return map;
	}
	
}
