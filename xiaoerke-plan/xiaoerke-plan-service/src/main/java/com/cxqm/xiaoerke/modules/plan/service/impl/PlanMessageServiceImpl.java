package com.cxqm.xiaoerke.modules.plan.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.cxqm.xiaoerke.modules.plan.entity.HealthPlanAddItemVo;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.cms.dao.BabyEmrDao;
import com.cxqm.xiaoerke.modules.cms.entity.Article;
import com.cxqm.xiaoerke.modules.cms.entity.BabyEmrVo;
import com.cxqm.xiaoerke.modules.cms.entity.Category;
import com.cxqm.xiaoerke.modules.cms.service.ArticleService;
import com.cxqm.xiaoerke.modules.operation.service.BaseDataService;
import com.cxqm.xiaoerke.modules.plan.dao.NutritionManagementDao;
import com.cxqm.xiaoerke.modules.plan.entity.NutritionEvaluate;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfo;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoService;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoTaskConfirmService;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoTaskService;
import com.cxqm.xiaoerke.modules.plan.service.PlanMessageService;
import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
@Service
@Transactional(readOnly = false)
public class PlanMessageServiceImpl implements PlanMessageService{

	@Autowired
	private PlanInfoService planInfoService;
	
	@Autowired
	private PlanInfoTaskService planInfoTaskService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BabyEmrDao babyEmrDao;
	
	@Autowired
	private BaseDataService baseDataService;
	
	@Autowired
	private PlanInfoTaskConfirmService planInfoTaskConfirmService;

	@Autowired
	private MongoDBService<HealthPlanAddItemVo> healthPlanAddItemVoMongoDBService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private NutritionManagementDao nutritionManagementDao;
	
	//宝大夫  待办任务提醒模板ID
	private final static String wechatMeesageTemplate="iB7QiSsYsnX8MN8MulkCcP4vIFkd31FBjXvgbgT3SAs";
	
	//营养管理提醒模板ID
	private final static String nutritionManagementWechatMeesageTemplate="Fobqkp0rVNNSSLEwJnNf7qUns9gjjMMfd725HwNe7q0";
	
	//小儿科 待办任务提醒模板ID
//	private final static String wechatMeesageTemplate="czH0xRFx3AzNRMm1bAcjqhwgz2ru9XTi7OhI6aZLedA";
	
//	短信：
//
//	7:00早上总提醒： 
//
//		亲爱的麻麻，宝大夫为您帮宝宝（小富）解决便秘，今日您需要做7：10给宝宝按摩；
//		7：30给宝宝排便；8：00，12：00，17：00的宝宝三餐；
//		10：00和15：30的运动，便秘问题可轻可重，贵在坚持哦！
	public void everyMorningSendSMS(){
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			String userId=null;
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			planInfoMap.put("userId",userId);
			planInfoMap.put("planTemplateId",1);
			planInfoMap.put("status","ongoing");
			List<Map<String, Object>>  planInfoList=planInfoService.getPlanInfoByUserId(planInfoMap);
			if(planInfoList.size()==0){
				return;
			}
			for (int i = 0; i < planInfoList.size(); i++) {
				User user=userDao.get(planInfoList.get(i).get("user_id").toString());
				if(user==null){
					continue;
				}
				String phone=user.getPhone();
				if(phone==null){
					continue;
				}
				String contentMessage="亲爱的麻麻，宝大夫为您帮宝宝解决便秘，今日您需要做";
				if(user.getOpenid()!=null&&!user.getOpenid().equals("")){
					List<BabyEmrVo> babyEmrVoList=babyEmrDao.getBabyEmrList(user.getOpenid());
					if(babyEmrVoList.size()>0){
					String babyName=babyEmrVoList.get(0).getBabyName();
					contentMessage="亲爱的麻麻，宝大夫为您帮"+babyName+"解决便秘，今日您需要做";
					}
				}
				List<Map<String, Object>> planInfoTaskList=planInfoTaskService.getPlanInfoTaskListByPlanInfoId(planInfoList.get(i).get("id").toString());
				SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
				String defecate="";
				String massage="";
				String food="";
				String sport="";
				for (Map<String, Object> planInfoTask : planInfoTaskList) {
					if(planInfoTask.get("remind_me").toString().equals("0")){
						continue;
					}
					String type= planInfoTask.get("type").toString();
					 if(type.equals("defecate")){
						 defecate+=" "+sdf.format(sdf.parse(planInfoTask.get("time_happen").toString()));
					 }else if(type.equals("massage")){
						 massage+=" "+sdf.format(sdf.parse(planInfoTask.get("time_happen").toString()));
					 }else if(type.equals("food")){
						 food+=" "+sdf.format(sdf.parse(planInfoTask.get("time_happen").toString()));
					 }else if(type.equals("sport")){
						 sport+=" "+sdf.format(sdf.parse(planInfoTask.get("time_happen").toString()));
					 }
				}
				if(massage.equals("")&&defecate.equals("")&&food.equals("")&&sport.equals("")){
					continue;
				}
				if(!massage.equals("")){
					contentMessage+=massage+"给宝宝推拿,";
				}
				if(!defecate.equals("")){
					contentMessage+=defecate+"给宝宝排便,";
				}
				if(!food.equals("")){
					contentMessage+=food+"给宝宝三餐,";
				}
				if(!sport.equals("")){
					contentMessage+=sport+"的运动,";
				}
				contentMessage+="便秘问题可轻可重，贵在坚持哦!";
				ChangzhuoMessageUtil.sendMsg(phone,contentMessage);
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("resultCode", 1);
			map.put("resultMsg", e.getMessage());
		}
	}
	
	public void TimingSendWechatMessage(){
			SendWechatMessage();
	}
	
	public void TimingSendWechatMessageByPunchTicket(){
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String userId=null;
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			planInfoMap.put("userId",userId);
			planInfoMap.put("planTemplateId",1);
			planInfoMap.put("status","ongoing");
			List<Map<String, Object>>  planInfoList=planInfoService.getPlanInfoByUserId(planInfoMap);
			String openid="";
			String title="";
			String planType="";
			String remark="";
			String url="";
			Date now=new Date();
			for (Map<String, Object> map : planInfoList) {
				Date planDate=sdf.parse(map.get("end_time").toString());
				if(planDate.getTime()>=now.getTime()){
					continue;
				}
				String id=map.get("id").toString();
				PlanInfo planInfo=new PlanInfo();
				planInfo.setId(Long.valueOf(id));
				String type=planInfoTaskConfirmService.getConfirmSituation(planInfo);
				if(type.equals("none")){
					title="3天未进入：";
					planType="亲爱的宝妈，您已经三天没有打卡（推拿卡）了，宝宝排便怎么样了，给宝大夫反馈一下吧";
					openid=map.get("open_id").toString();
					if(openid==null||openid.equals("")){
						continue;
					}
					url="http://s3.baodf.com/xiaoerke-healthPlan/ctp#/constipationFollow/none,"+id;
					String token=baseDataService.findWechatToken();
					WechatMessageUtil.templateModel(title, planType, "无", "", "", remark, token, url, openid, wechatMeesageTemplate);
				}else if(type.equals("all")){
					title="3天连续打卡：";
					planType="恭喜宝妈，您连续（又）坚持打卡3天了，宝宝好些了吗，给宝大夫反馈一下吧";
					url="http://s3.baodf.com/xiaoerke-healthPlan/ctp#/constipationFollow/all,"+id;
					openid=map.get("open_id").toString();
					if(openid==null||openid.equals("")){
						continue;
					}
					String token=baseDataService.findWechatToken();
					WechatMessageUtil.templateModel(title, planType, "无", "", "", remark, token, url, openid, wechatMeesageTemplate);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SendWechatMessage(){
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
			String userId=null;
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			planInfoMap.put("userId",userId);
			planInfoMap.put("planTemplateId",1);
			planInfoMap.put("status","ongoing");
			List<Map<String, Object>>  planInfoList=planInfoService.getPlanInfoByUserId(planInfoMap);
			if(planInfoList.size()==0){
				return;
			}
			String openid="";
			String title="";
			String planType="";
			String remark="";
			String defecateURL="";
			String foodURL="";
			String sportURL="";
			String buyURL="";
			for (int i = 0; i < planInfoList.size(); i++) {
				if(planInfoList.get(i).get("open_id")==null){
					continue;
				}
				 openid=planInfoList.get(i).get("open_id").toString();
				 if(openid==null||openid.equals("")){
						continue;
					}
				String babyName="";
				if(openid!=null&&!openid.equals("")){
					List<BabyEmrVo> babyEmrVoList=babyEmrDao.getBabyEmrList(openid);
					if(babyEmrVoList.size()>0){
					 babyName="("+babyEmrVoList.get(0).getBabyName()+")";
					}
				}else{
					continue;
				}
				String planInfoID=planInfoList.get(i).get("id").toString();
				List<Map<String, Object>> planInfoTaskList=planInfoTaskService.getPlanInfoTaskListByPlanInfoId(planInfoID);
				String defecate="";
				String food="";
				String sport="";
				String buy="";
				String now=sdf.format(new Date());
				for (Map<String, Object> planInfoTask : planInfoTaskList) {
					if(planInfoTask.get("remind_me").toString().equals("0")){
						continue;
					}
					String type= planInfoTask.get("type").toString();
					if(type.equals("defecate")||type.equals("massage")){
						defecateURL="http://s3.baodf.com/xiaoerke-healthPlan/ctp#/constipationKnead/1,"+planInfoID+",defecate";
					 if(type.equals("defecate")){
						 String time=sdf.format(sdf.parse(planInfoTask.get("time_happen").toString()));
						 if(!time.equals(now)){
							 continue;
						 }
						 defecate+=" "+time;
					 }
					}else{
						String time=sdf.format(sdf.parse(planInfoTask.get("time_happen").toString()));
						 if(!time.equals(now)){
							 continue;
						 }
						if(type.equals("food")){
							foodURL="http://s3.baodf.com/xiaoerke-healthPlan/ctp#/constipationFood/1,"+planInfoID+",food";
							food+=" "+time;
						}else if(type.equals("sport")){
							sportURL="http://s3.baodf.com/xiaoerke-healthPlan/ctp#/constipationSports/1,"+planInfoID+",sport";
							sport+=" "+time;
						}else if(type.equals("motherMust")){
							buyURL="http://s3.baodf.com/xiaoerke-healthPlan/ctp#/cMotherNecessary";
							buy+=" "+time;
						}
					 }
					}
				if(defecate.equals("")&&food.equals("")&&sport.equals("")&&buy.equals("")){
					continue;
				}
				if(!defecate.equals("")){
//				亲爱的宝妈，到了该给宝宝（小富）做便前按摩的时间了，7：30要准时给宝宝排便哦，
//				给宝宝配些舒缓的音乐或视频，宝宝会更好的排便哦。
					title="按摩、排便提醒：";
					if(babyName.equals("")){
						planType="亲爱的宝妈，到了该给宝宝做便前推拿的时间了，"+defecate+"要准时给宝宝排便哦，"
								+ "给宝宝配些舒缓的音乐或视频，宝宝会更好的排便哦。";
					}else{
					planType="亲爱的宝妈，到了该给宝宝"+babyName+"做便前推拿的时间了，"+defecate+"要准时给宝宝排便哦，"
							+ "给宝宝配些舒缓的音乐或视频，宝宝会更好的排便哦。";
					}
					String token=baseDataService.findWechatToken();
					WechatMessageUtil.templateModel(title, planType, "无", "", "", remark, token, defecateURL, openid, wechatMeesageTemplate);
				}
				if(!food.equals("")){
					title="吃饭提醒：";
					Calendar cal = Calendar.getInstance();
					int hour = cal.get(Calendar.HOUR_OF_DAY);
					if (hour >= 0 && hour < 10) {
						if(babyName.equals("")){
							planType="亲爱的宝妈，到了该给宝宝做早饭的时间了，看一下今天的食物吧。";
						}else{
							planType="亲爱的宝妈，到了该给"+babyName+"做早饭的时间了，看一下今天的食物吧。";
						}
					} else if (hour >= 10 && hour < 13) {
						if(babyName.equals("")){
							planType="亲爱的宝妈，到了该给宝宝做中饭的时间了，看一下今天的食物吧。";
						}else{
							planType="亲爱的宝妈，到了该给"+babyName+"做中饭的时间了，看一下今天的食物吧。";
						}
					} else if (hour >= 13 && hour <= 23) {
						if(babyName.equals("")){
							planType="亲爱的宝妈，到了该给宝宝做晚饭的时间了，看一下今天的食物吧。";
						}else{
							planType="亲爱的宝妈，到了该给"+babyName+"做晚饭的时间了，看一下今天的食物吧。";
						}
					}
					String token=baseDataService.findWechatToken();
					WechatMessageUtil.templateModel(title, planType, "无", "", "", remark, token, foodURL, openid, wechatMeesageTemplate);
				}
				if(!sport.equals("")){
					title="运动提醒：";
					if(babyName.equals("")){
						planType="亲爱的宝妈，到了该让宝宝做运动的时间了，看一下今天的运动吧。";
					}else{
						planType="亲爱的宝妈，到了该让"+babyName+"做运动的时间了，看一下今天的运动吧。";
					}
					String token=baseDataService.findWechatToken();
					WechatMessageUtil.templateModel(title, planType, "无", "", "", remark, token, sportURL, openid, wechatMeesageTemplate);
				}
				if(!buy.equals("")){
					title="必备物品提醒：";
					if(babyName.equals("")){
						planType="亲爱的宝妈，你还有些必备物品没有给宝宝准备呢，快来看一下吧。";
					}else{
						planType="亲爱的宝妈，你还有些必备物品没有给"+babyName+"准备呢，快来看一下吧。";
					}
					String token=baseDataService.findWechatToken();
					WechatMessageUtil.templateModel(title, planType, "无", "", "", remark, token, buyURL, openid, wechatMeesageTemplate);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<HealthPlanAddItemVo> findHealthPlanAddItem(HealthPlanAddItemVo healthPlanAddItemVo) throws Exception{
		Query queryRecord = new Query();
		queryRecord.addCriteria(Criteria.where("openid").is(healthPlanAddItemVo.getOpenid()));
		List<HealthPlanAddItemVo> healthPlanAddItemVos = healthPlanAddItemVoMongoDBService.queryList(queryRecord);
		return healthPlanAddItemVos;
	}

	@Override
	public int insertHealthPlanAddItem(HealthPlanAddItemVo healthPlanAddItemVo) throws Exception{
		healthPlanAddItemVo.setCreateDate(new Date());
		return healthPlanAddItemVoMongoDBService.insert(healthPlanAddItemVo);

	}
	
	/**
	 * 营养管理发送消息
	 * sunxiao
	 */
	@Override
	public void nutritionManagementSendWechatMessage() {
		// TODO Auto-generated method stub
		Map<String, Object> planInfoMap = new HashMap<String, Object>();
		planInfoMap.put("planTemplateId",2);
		planInfoMap.put("status","ongoing");
		List<Map<String, Object>>  planInfoList=planInfoService.getPlanInfoByUserId(planInfoMap);
		
		String[] times = new String[]{"07","12","19","12","19","07","19"};
    	Calendar cal=Calendar.getInstance();
    	String time = times[cal.get(Calendar.DAY_OF_WEEK)-1];
    	SimpleDateFormat sdf = new SimpleDateFormat("HH");
    	if(time.equals(sdf.format(new Date()))){
    		for(Map<String, Object> map : planInfoList){
    			if("yes".equals((String)map.get("notice"))){
    				String openId = (String)map.get("open_id");
    				if(StringUtils.isNotNull(openId)){
    					String title = "宝大夫提醒您：";
    					String planType = "";
    					String url = "";
    					String token=baseDataService.findWechatToken();
    					if("07".equals(time)){
    						planType = "今天该给宝宝做些什么呢？看下营养师给的食谱吧。";
    						url = "http://s3.baodf.com/xiaoerke-healthPlan/ntr?value=251351#/nutritionFood";
    					}else if("12".equals(time)){
    						Article article = new Article();
    						Category category = new Category();
    						category.setId("4b516d7ff23d42bfad7cb69672a9c658");//营养管理目录
    						article.setCategory(category);
    						Page<Article> Page = articleService.findPage(new Page<Article>(1,30), article, true);
    						int Count = (int)Page.getCount();
    						int choose = 0;
    						Random r = new Random();
    						if(Count>=7){
    							choose = r.nextInt(Count/7)*7+cal.get(Calendar.DAY_OF_WEEK);
    						}else if(Count>0&&Count<7){
    							choose = r.nextInt(Count);
    						}
    						planType = (String) ((Map) Page.getList().get(choose)).get("title");
    						url = "http://s22.baodf.com/xiaoerke-knowledge/knowledge?value=251350#/knowledgeArticleContent/" +(String) ((Map) Page.getList().get(choose)).get("id")+",yygl";
    					}else if("19".equals(time)){
    						Map<String, Object> params = new HashMap<String, Object>();
    						params.put("createTime", new Date());
    						params.put("userId", map.get("user_id"));
    						List<NutritionEvaluate> list = nutritionManagementDao.getEvaluateListByInfo(params);
    						if(list.size()==0){
    							planType = "宝宝今天吃的怎么样呢？快来评估下吧。";
    							url = "http://s3.baodf.com/xiaoerke-healthPlan/ntr?value=251350#/nutritionAssess/noagain";
    						}else{
    							continue;
    						}
    					}
    					WechatMessageUtil.templateModel(title, "宝大夫", planType, "", "", "祝宝宝健康成长哦！", token, url, openId, nutritionManagementWechatMeesageTemplate);
    				}
    			}
    		}
    	}
	}
}
