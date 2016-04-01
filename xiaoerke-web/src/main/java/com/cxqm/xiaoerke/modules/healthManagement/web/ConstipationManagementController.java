package com.cxqm.xiaoerke.modules.healthManagement.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.cms.entity.Article;
import com.cxqm.xiaoerke.modules.cms.service.ArticleService;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfo;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfoTask;
import com.cxqm.xiaoerke.modules.plan.entity.PlanTemplateAppraisal;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoService;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoTaskConfirmService;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoTaskService;
import com.cxqm.xiaoerke.modules.plan.service.PlanMessageService;
import com.cxqm.xiaoerke.modules.plan.service.PlanTemplateAppraisalService;
import com.cxqm.xiaoerke.modules.plan.service.PlanTemplateService;
import com.cxqm.xiaoerke.modules.plan.service.PlanTemplateTaskService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;


@Controller
@RequestMapping(value = "plan")
public class ConstipationManagementController {

	@Autowired
	private PlanInfoTaskConfirmService planInfoTaskConfirmService;
	
	@Autowired
	private PlanInfoService planInfoService;
	
	@Autowired
	private PlanInfoTaskService planInfoTaskService;
	
	@Autowired
	private PlanTemplateAppraisalService planTemplateAppraisalService;
	
	@Autowired
	private PlanTemplateService planTemplateService;
	
	@Autowired
	private PlanTemplateTaskService planTemplateTaskService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private PlanMessageService planMessageService;

	@Autowired
	private SystemService systemService;
	
	
	/**
	 * 任务打卡
	 * 张博
	 * @param params(openid;userid;planinfoid)
	 * @return
	 */
	@RequestMapping(value = "/pushTicket", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  pushTicket(@RequestBody Map<String, Object> params){
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			if(params.get("plan_info_task_id")==null){
				map.put("resultCode", 1);
				map.put("resultMsg", "plan_info_task_id为空");
				return map;
			}
			String[] planInfoTaskIdArray=params.get("plan_info_task_id").toString().split(",");
			for (int i = 0; i < planInfoTaskIdArray.length; i++) {
				long planInfoTaskId=Long.valueOf(planInfoTaskIdArray[i]);
				planInfoTaskConfirmService.punchTicket(planInfoTaskId);
				LogUtils.saveLog(Servlets.getRequest(), "00000067" + planInfoTaskId);//任务打卡
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
	 * 取消计划
	 * sunxiao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cancelPlan", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> cancelPlan(@RequestParam long id, HttpServletRequest request) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			PlanInfo planInfo = new PlanInfo();
			planInfo.setId(id);
			planInfoService.cancelPlan(planInfo);
			response.put("resultCode", 0);
			response.put("resultMsg", "OK");
			LogUtils.saveLog(Servlets.getRequest(), "取消计划id:" + id);
		}catch(Exception e){
			e.printStackTrace();
			response.put("resultCode", 1);
			response.put("resultMsg", "取消计划异常！");
		}
		return response;
	}
	
	/**
	 * 获取打卡详情
	 * sunxiao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/confirmDetail", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> confirmDetail(@RequestParam long planInfoId,@RequestParam String type, HttpServletRequest request) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			PlanInfoTask planInfoTask = new PlanInfoTask();
			planInfoTask.setPlanInfoId(planInfoId);
			planInfoTask.setType(type);
			response = planInfoTaskConfirmService.confirmDetail(planInfoTask);
			response.put("resultCode", 0);
			response.put("resultMsg", "OK");
		}catch(Exception e){
			e.printStackTrace();
			response.put("resultCode", 1);
			response.put("resultMsg", "获取打卡详情异常！");
		}
		return response;
	}
	
	/**
	 * 获取任务列表中的打卡情况
	 * sunxiao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/taskListConfirm", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> taskListConfirm(@RequestParam long planInfoId, HttpServletRequest request) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			response = planInfoTaskConfirmService.taskListConfirm(String.valueOf(planInfoId));
			response.put("resultCode", 0);
			response.put("resultMsg", "OK");
		}catch(Exception e){
			e.printStackTrace();
			response.put("resultCode", 1);
			response.put("resultMsg", e.getMessage());
		}
		return response;
	}
		
	/**
	 * 获取打卡情况
	 * sunxiao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getConfirmSituation", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> getConfirmSituation(@RequestParam long planId, HttpServletRequest request) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			PlanInfo planInfo = new PlanInfo();
			planInfo.setId(planId);
			String result = planInfoTaskConfirmService.getConfirmSituation(planInfo);
			response.put("resultCode", 0);
			response.put("resultMsg", "OK");
			response.put("resultType", result);
		}catch(Exception e){
			e.printStackTrace();
			response.put("resultCode", 1);
			response.put("resultMsg", e.getMessage());
		}
		return response;
	}
	
	/**
	 * 保存评论
	 * sunxiao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/appraisal/saveAppraisal", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> saveAppraisal(@RequestBody Map<String, Object> params,  HttpServletRequest request,HttpSession session) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			String openId = WechatUtil.getOpenId(session,request);
			Map<String,Object> parameter = systemService.getWechatParameter();
			String token = (String)parameter.get("token");
			String nickName = (String)session.getAttribute("wechatName");
	        
	        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openId+"&lang=zh_CN";
			String param="";
			String json=WechatUtil.post(strURL, param, "GET");
			JSONObject jasonObject = JSONObject.fromObject(json);
	        Map<String, Object> jsonMap = (Map) jasonObject;
	        
			PlanTemplateAppraisal planTemplateAppraisal = new PlanTemplateAppraisal();
			planTemplateAppraisal.setPlanTemplateId(Short.valueOf(String.valueOf(params.get("planTemplateId"))));
			planTemplateAppraisal.setAppraisal((String) params.get("appraisal"));
			planTemplateAppraisal.setCreateBy(UserUtils.getUser().getId());
			planTemplateAppraisal.setCreateTime(new Date());
			planTemplateAppraisal.setUserId(UserUtils.getUser().getId());
			planTemplateAppraisal.setName(nickName);
			planTemplateAppraisal.setHeadImgUrl((String) jsonMap.get("headimgurl"));
			planTemplateAppraisal.setOpenId(openId);
			planTemplateAppraisalService.saveAppraisal(planTemplateAppraisal);
			response.put("resultCode", 0);
			response.put("resultMsg", "OK");
			LogUtils.saveLog(Servlets.getRequest(), "00000068","保存评论planTemplateId:" + params.get("planTemplateId"));
		}catch(Exception e){
			e.printStackTrace();
			response.put("resultCode", 1);
			response.put("resultMsg", "保存评论异常！");
		}
		return response;
	}
	
	/**
	 * 查询评论列表
	 * sunxiao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/appraisal/appraisalList", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> appraisalList(@RequestParam short id, @RequestParam int pageNo,@RequestParam int pageSize,HttpServletRequest request) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			PlanTemplateAppraisal planTemplateAppraisal = new PlanTemplateAppraisal();
			planTemplateAppraisal.setPlanTemplateId(id);
			Page<PlanTemplateAppraisal> page = planTemplateAppraisalService.findAppraisalList(new Page<PlanTemplateAppraisal>(pageNo,pageSize),planTemplateAppraisal);
			List<HashMap<String,Object>> appraisalList = new ArrayList();
			for(PlanTemplateAppraisal p : page.getList())
			{
				HashMap<String,Object> appraisalMap = new HashMap<String,Object>();
				appraisalMap.put("appraisalId", (p.getId()));
				appraisalMap.put("appraisalContent", (p.getAppraisal()));
				appraisalMap.put("nickName", (p.getName()));
				appraisalMap.put("createDate",(DateUtils.DateToStr(p.getCreateTime(),"datetime")));
				appraisalMap.put("picUrl",p.getHeadImgUrl());
				appraisalList.add(appraisalMap);
			}
			response.put("resultCode", 0);
			response.put("resultMsg", "OK");
			response.put("appraisalList", appraisalList);
			response.put("total", page.getCount());
		}catch(Exception e){
			e.printStackTrace();
			response.put("resultCode", 1);
			response.put("resultMsg", "查询评论列表异常！");
		}
		return response;
	}
	
	/**
	 * 获取任务详情
	 * sunxiao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getTasksInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> getTasksInfo(@RequestParam String type,HttpServletRequest request) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			response = planInfoTaskService.getTasksInfo(type);
			response.put("resultCode", 0);
			response.put("resultMsg", "OK");
		}catch(Exception e){
			e.printStackTrace();
			response.put("resultCode", 1);
			response.put("resultMsg", "获取任务详情异常！");
		}
		return response;
	}
	
	/**
	 * 提醒设置
	 * sunxiao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatePlanTask", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> updatePlanTask(@RequestBody List<Map<String, Object>> params,HttpServletRequest request,HttpSession session) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			String userId=UserUtils.getUser().getId();
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			List<String> list = new ArrayList<String>();
			list.add("ongoing");
			list.add("pending");
			planInfoMap.put("userId",userId);
			planInfoMap.put("planTemplateId",1);
			planInfoMap.put("statusList",list);
			List<Map<String, Object>>  planInfoList=planInfoService.getPlanInfoByUserId(planInfoMap);
			String openId = WechatUtil.getOpenId(session,request);
			if(planInfoList.size()==0){
				PlanInfo p = new PlanInfo();
				for(int i=0;i<params.size();i++){
					if(i==0){
						p.setName("便秘计划");
						p.setStartTime(new Date());
						Calendar c = Calendar.getInstance();
						c.setTime(new Date());
						if("day".equals(params.get(i).get("period_unit"))){
							c.add(Calendar.DAY_OF_YEAR, Integer.parseInt((String)params.get(i).get("period")));
						}else if("week".equals(params.get(i).get("period_unit"))){
							c.add(Calendar.WEEK_OF_YEAR, Integer.parseInt((String)params.get(i).get("period")));
						}else if("month".equals(params.get(i).get("period_unit"))){
							c.add(Calendar.MONTH, Integer.parseInt((String)params.get(i).get("period")));
						}else if("year".equals(params.get(i).get("period_unit"))){
							c.add(Calendar.YEAR, Integer.parseInt((String)params.get(i).get("period")));
						}
						p.setPlanTemplateId(Short.parseShort((String)params.get(i).get("planTemplateId")));
						p.setEndTime(c.getTime());
						p.setCreateTime(new Date());
						p.setCreateBy(userId);
						p.setUpdateTime(new Date());
						p.setUpdateBy(userId);
						p.setUserId(userId);
						p.setOpenId(openId);
						planInfoService.savePlanInfo(p);
						LogUtils.saveLog(Servlets.getRequest(), "00000070" + p.getId());//保存计划id
					}
					params.get(i).remove("id");
					params.get(i).put("planInfoId", p.getId());
					params.get(i).put("createTime", new Date());
					params.get(i).put("updateTime", new Date());
					params.get(i).put("createBy", userId);
					params.get(i).put("updateBy", userId);
					planInfoTaskService.savePlanTask(params.get(i));
				}
			}else{
				if("ongoing".equals(planInfoList.get(0).get("status"))){
					for(Map<String, Object> map : params){
						planInfoTaskService.updatePlanTask(map);
						LogUtils.saveLog(Servlets.getRequest(), "00000105", map.toString());
					}
				}
				if("pending".equals(planInfoList.get(0).get("status"))){
					PlanInfo p = new PlanInfo();
					for(int i=0;i<params.size();i++){
						if(i==0){
							p.setStartTime(new Date());
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							if("day".equals(params.get(i).get("period_unit"))){
								c.add(Calendar.DAY_OF_YEAR, Integer.parseInt((String)params.get(i).get("period")));
							}else if("week".equals(params.get(i).get("period_unit"))){
								c.add(Calendar.WEEK_OF_YEAR, Integer.parseInt((String)params.get(i).get("period")));
							}else if("month".equals(params.get(i).get("period_unit"))){
								c.add(Calendar.MONTH, Integer.parseInt((String)params.get(i).get("period")));
							}else if("year".equals(params.get(i).get("period_unit"))){
								c.add(Calendar.YEAR, Integer.parseInt((String)params.get(i).get("period")));
							}
							p.setId((Long)planInfoList.get(0).get("id"));
							p.setEndTime(c.getTime());
							p.setUpdateTime(new Date());
							p.setUpdateBy(userId);
							p.setStatus("ongoing");
							planInfoService.updatePlanInfoById(p);
						}
						params.get(i).remove("id");
						params.get(i).put("planInfoId", p.getId());
						params.get(i).put("createTime", new Date());
						params.get(i).put("updateTime", new Date());
						params.get(i).put("createBy", userId);
						params.get(i).put("updateBy", userId);
						planInfoTaskService.savePlanTask(params.get(i));
					}
				}
			}
	        
			response.put("resultCode", 0);
			response.put("resultMsg", "OK");
		}catch(Exception e){
			e.printStackTrace();
			response.put("resultCode", 1);
			response.put("resultMsg", e.getMessage());
		}
		return response;
	}
	
	/**
	 * 修改计划更新时间，用于周期提示打卡情况
	 * sunxiao
	 * @param planInfoId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatePlanInfoForCycleTip", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> updatePlanInfoForCycleTip(@RequestParam long planInfoId,HttpServletRequest request,HttpSession session) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			PlanInfo planInfo = new PlanInfo();
			planInfo.setId(planInfoId);
			planInfo.setUpdateTime(new Date());
			planInfoService.updatePlanInfoById(planInfo);
			response.put("resultCode", 0);
			response.put("resultMsg", "OK");
		}catch(Exception e){
			e.printStackTrace();
			response.put("resultCode", 1);
			response.put("resultMsg", e.getMessage());
		}
		return response;
	}
		
		
	/**
	 * 获取任务列表
	 * 张博
	 * @param
	 * @return
	 */
	@SystemServiceLog(description = "00000065")
	@RequestMapping(value = "/getTasksList", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getTasksList(){
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			String userId=UserUtils.getUser().getId();
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			planInfoMap.put("userId",userId);
			planInfoMap.put("planTemplateId",1);
			planInfoMap.put("status","ongoing");
			List<Map<String, Object>>  planInfoList=planInfoService.getPlanInfoByUserId(planInfoMap);
			if(planInfoList.size()==0){
				map.put("task", null);
				map.put("resultCode", 0);
				map.put("resultMsg", "OK");
				return map;
			}
			Map<String,Object> resultObjectMap=new HashMap<String,Object>();
			for (int i = 0; i < planInfoList.size(); i++) {
				List<Map<String, Object>> planInfoTaskList=planInfoTaskService.getPlanInfoTaskListByPlanInfoId(planInfoList.get(i).get("id").toString());
				List<Map<String,Object>> planList=new ArrayList<Map<String,Object>>();
				for (Map<String, Object> planInfoTask : planInfoTaskList) {
					Map<String,Object> plan=new HashMap<String,Object>();
					String planInfoTaskId=planInfoTask.get("id").toString();
					plan.put("id", planInfoTaskId);
					plan.put("timeHappen", planInfoTask.get("time_happen"));
					plan.put("remindMe", planInfoTask.get("remind_me").toString().equals("0")?false:true);
					plan.put("type",planInfoTask.get("type").toString());
					planList.add(plan);
				}
				resultObjectMap.put("id", planInfoList.get(i).get("id"));
				resultObjectMap.put("planTemplateId", planInfoList.get(i).get("plan_template_id"));
				resultObjectMap.put("planTask",planList);
				resultObjectMap.put("update_time", planInfoList.get(i).get("update_time"));
				resultObjectMap.put("server_time", new Date());
			}
			map.put("task", resultObjectMap);
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
	 * 获取模板任务列表
	 * 张博
	 * @param
	 * @return
	 */
	@SystemServiceLog(description = "00000066")////获取任务模板列表
	@RequestMapping(value = "/getTemplateTasks", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getTemplateTasks(@RequestBody Map<String, Object> params){
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			List<Map<String, Object>>  planTemplateList=planTemplateService.getPlanTemplateList(params.get("type").toString());
			Map<String,Object> resultObjectMap=new HashMap<String,Object>();
			for (int i = 0; i < planTemplateList.size(); i++) {
				String PlanTemplateId=planTemplateList.get(i).get("id").toString();
				List<Map<String, Object>> PlanTemplateTask=planTemplateTaskService.getPlanTemplateTaskListByPlanTemplateId(PlanTemplateId);
				List<Map<String,Object>> planList=new ArrayList<Map<String,Object>>();
				for (Map<String, Object> PlanTemplate : PlanTemplateTask) {
					Map<String,Object> plan=new HashMap<String,Object>();
					String planInfoTaskId=PlanTemplate.get("id").toString();
					plan.put("id", planInfoTaskId);
					plan.put("timeHappen", PlanTemplate.get("time_happen"));
					plan.put("type",PlanTemplate.get("type").toString());
					plan.put("remindMe", false);
					planList.add(plan);
				}
				resultObjectMap.put("PlanTemplateId",PlanTemplateId);
				resultObjectMap.put("PlanTemplate",planList);
				resultObjectMap.put("period",planTemplateList.get(i).get("period").toString());
				resultObjectMap.put("period_unit",planTemplateList.get(i).get("period_unit").toString());
			}
			map.put("tasks", resultObjectMap);
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
	 * 获取食材
	 * 张博
	 * @param
	 * @return
	 */
	@SystemServiceLog(description = "获取食材列表")
	@RequestMapping(value = "/getFoodList", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getFoodList(){
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			String userId=UserUtils.getUser().getId();
			String food=planInfoService.getUserLastFoodList(userId);
			if(food==null||food.equals("")){
				food=Global.getConfig("plan.food");
			}
			map.put("foodList", food.split(","));
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
	 * 保存食材
	 * 张博
	 * @param
	 * @return
	 */
	@SystemServiceLog(description = "00000069")//保存食材
	@RequestMapping(value = "/saveFoodList", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  saveFoodList(@RequestBody Map<String, Object> params){
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			String food=params.get("foodList").toString();
			String taskId=params.get("taskId").toString();
			PlanInfo planInfo=new PlanInfo();
			planInfo.setId(Long.parseLong(taskId));
			planInfo.setNotice(food);
			planInfoService.updatePlanInfoById(planInfo);
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
	 * 获取饮食打卡
	 * 张博
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getDietList", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getDietList(@RequestBody Map<String, Object> params){
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			String userId=UserUtils.getUser().getId();
			String food=planInfoService.getUserLastFoodList(userId);
			List<Article> list=articleService.findArticleListByType(food);
			if(list.size()==0){
				map.put("Diet", null);
				map.put("resultCode", 0);
				map.put("resultMsg", "OK");
				return map;
			}
			List<Map<String ,Object>> resultArticleList=new ArrayList<Map<String ,Object>>();
			for (Article article : list) {
				Map<String ,Object> resultMap=new HashMap<String, Object>();
				resultMap.put("id", article.getId());
				resultMap.put("image", article.getImage());
				resultMap.put("content", article.getArticleData().getContent());
				resultArticleList.add(resultMap);
			}
			LogUtils.saveLog(Servlets.getRequest(), "获取饮食打卡");
			map.put("Diet", resultArticleList);
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
	 * 获取饮食打卡
	 * 张博
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/SendSMS", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  SendSMS() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			planMessageService.TimingSendWechatMessage();
			planMessageService.TimingSendWechatMessageByPunchTicket();
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
	 * 保存购买链接记录
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/saveShopping", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	void saveShopping(@RequestBody Map<String, Object> params){
		String userId = UserUtils.getUser().getId();
		Map<String, Object> planInfoMap = new HashMap<String, Object>();
		planInfoMap.put("userId",userId);
		planInfoMap.put("planTemplateId",1);
		planInfoMap.put("status","ongoing");
		List<Map<String,Object>> list = planInfoService.getPlanInfoByUserId(planInfoMap);
		if(list!=null){
			if(list.size()!=0){
				PlanInfoTask planInfoTask = new PlanInfoTask();
				planInfoTask.setPlanInfoId((Long) list.get(0).get("id"));
				planInfoTask.setType("motherMust");
				List<PlanInfoTask> taskList = planInfoTaskService.getPlanInfoTaskByInfo(planInfoTask);
				PlanInfo p = new PlanInfo();
				Object notice = list.get(0).get("notice");
				if(notice==null||!((String)notice).contains((String)params.get("type"))){
					p.setId((Long) list.get(0).get("id"));
					p.setNotice((String)params.get("type")+","+(notice==null?"":(String)notice));
					planInfoService.updatePlanInfoById(p);
					LogUtils.saveLog(Servlets.getRequest(), "00000100",String.valueOf(list.get(0).get("id")));
					if(p.getNotice().split(",").length==4&&taskList.get(0).getRemindMe()){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("remindMe", false);
						map.put("id", taskList.get(0).getId());
						planInfoTaskService.updatePlanTask(map);
						LogUtils.saveLog(Servlets.getRequest(), "00000101",String.valueOf(taskList.get(0).getId()));
					}
				}
			}
		}
		LogUtils.saveLog(Servlets.getRequest(), "00000102",params.get("type")+":"+params.get("href").toString());
	}

	/**
	 * 判断用户制定计划等级及反馈情况
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getquestion", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	void getQuestion(@RequestBody Map<String, Object> params){
		String userId = UserUtils.getUser().getId();
		LogUtils.saveLog(Servlets.getRequest(), "00000103",params.get("type").toString()+userId);
	}

}
