package com.cxqm.xiaoerke.modules.plan.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.plan.dao.PlanInfoDao;
import com.cxqm.xiaoerke.modules.plan.dao.PlanInfoTaskConfirmDao;
import com.cxqm.xiaoerke.modules.plan.dao.PlanInfoTaskDao;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfo;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfoTask;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfoTaskConfirm;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoTaskConfirmService;

@Service
@Transactional(readOnly = false)
public class PlanInfoTaskConfirmServiceImpl implements
		PlanInfoTaskConfirmService {

	 @Autowired
	 private PlanInfoTaskConfirmDao planInfoTaskConfirmDao;
	 
	 @Autowired
	 private PlanInfoTaskDao planInfoTaskDao;
	 
	 @Autowired
	 private PlanInfoDao planInfoDao;
	
	@Override
	public boolean punchTicket(long planInfoTaskId) {
		// TODO Auto-generated method stub
		try {
			PlanInfoTask planInfoTask= planInfoTaskDao.selectByPrimaryKey(planInfoTaskId);
			PlanInfo planInfo=planInfoDao.selectByPrimaryKey(planInfoTask.getPlanInfoId());
			PlanInfoTaskConfirm planInfoTaskConfirm=new PlanInfoTaskConfirm();
			planInfoTaskConfirm.setCreateBy(planInfo.getUserId());
			planInfoTaskConfirm.setUserId(planInfo.getUserId());
			planInfoTaskConfirm.setOpenId(planInfo.getOpenId());
			planInfoTaskConfirm.setTaskDate(planInfoTask.getCreateTime());
			planInfoTaskConfirm.setPlanInfoTaskId(planInfoTaskId);
			planInfoTaskConfirm.setCreateTime(new Date());
			planInfoTaskConfirmDao.insertSelective(planInfoTaskConfirm);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 打卡详情
	 * sunxiao
	 * @param planInfoTaskConfirm
	 * @return
	 */
	@Override
	public HashMap<String, Object> confirmDetail(
			PlanInfoTask planInfoTask) {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		// TODO Auto-generated method stub
		List<PlanInfoTask> list = planInfoTaskDao.selectByInfo(planInfoTask);
		List<PlanInfoTaskConfirm> confirmList = new ArrayList<PlanInfoTaskConfirm>();
		List<String> dateList = new ArrayList<String>();
		List<String> dateTempList = new ArrayList<String>();
		Map<Object,Integer> countMap = new HashMap<Object,Integer>();
		List<String> totalTempList = new ArrayList<String>();
		int count = 0;
		Calendar calendar = new GregorianCalendar();
		for(PlanInfoTask p : list){
			PlanInfoTaskConfirm planInfoTaskConfirm = new PlanInfoTaskConfirm();
			planInfoTaskConfirm.setPlanInfoTaskId(p.getId());
			confirmList = planInfoTaskConfirmDao.getConfirmByInfo(planInfoTaskConfirm);
			Date date = new Date();
			for(PlanInfoTaskConfirm pc : confirmList){
				if("food".equals(planInfoTask.getType())||"sport".equals(planInfoTask.getType())){
					dateList.add(DateUtils.DateToStr(pc.getCreateTime(),"date"));
				}else{
					if(DateUtils.DateToStr(pc.getCreateTime(),"date").equals(DateUtils.DateToStr(new Date(),"date"))){
						count++;
					}else{
						calendar.add(calendar.DATE,-1);
						date=calendar.getTime();
						if(DateUtils.DateToStr(pc.getCreateTime(),"date").equals(DateUtils.DateToStr(date,"date"))){
							count++;
						}else{
							break;
						}
					}
				}
				if(!dateTempList.contains(DateUtils.DateToStr(pc.getCreateTime(),"date"))){
					dateTempList.add(DateUtils.DateToStr(pc.getCreateTime(),"date"));
				}
			}
			int time = Integer.parseInt((DateUtils.DateToStr(p.getTimeHappen(),"time").split(":")[0]));
			if("food".equals(planInfoTask.getType())){
				if(dateTempList.contains((DateUtils.DateToStr(new Date(),"date")))){
					if(time>=0&&time<=10){
						returnMap.put("am", "yes");
					}else if(time>10&&time<=14){
						returnMap.put("mm", "yes");
					}else if(time>14&&time<=23){
						returnMap.put("pm", "yes");
					}
				}else{
					if(time>=0&&time<=10){
						returnMap.put("am", "no");
					}else if(time>10&&time<=14){
						returnMap.put("mm", "no");
					}else if(time>14&&time<=23){
						returnMap.put("pm", "no");
					}
				}
				if(time>=0&&time<=10){
					returnMap.put("amid", p.getId());
					returnMap.put("amtime", DateUtils.DateToStr(p.getTimeHappen(),"time"));
				}else if(time>10&&time<=14){
					returnMap.put("mmid", p.getId());
					returnMap.put("mmtime", DateUtils.DateToStr(p.getTimeHappen(),"time"));
				}else if(time>14&&time<=23){
					returnMap.put("pmid", p.getId());
					returnMap.put("pmtime", DateUtils.DateToStr(p.getTimeHappen(),"time"));
				}
			}else if("sport".equals(planInfoTask.getType())){
				if(dateTempList.contains((DateUtils.DateToStr(new Date(),"date")))){
					if(time>=0&&time<=12){
						returnMap.put("am", "yes");
					}else if(time>12&&time<=23){
						returnMap.put("pm", "yes");
					}
				}else{
					if(time>=0&&time<=12){
						returnMap.put("am", "no");
					}else if(time>12&&time<=23){
						returnMap.put("pm", "no");
					}
				}
				if(time>=0&&time<=12){
					returnMap.put("amid", p.getId());
					returnMap.put("amtime", DateUtils.DateToStr(p.getTimeHappen(),"time"));
				}else if(time>12&&time<=23){
					returnMap.put("pmid", p.getId());
					returnMap.put("pmtime", DateUtils.DateToStr(p.getTimeHappen(),"time"));
				}
			}else{
				if(dateTempList.contains((DateUtils.DateToStr(new Date(),"date")))){
					returnMap.put("all", "yes");
				}else{
					returnMap.put("all", "no");
				}
				returnMap.put("allid", p.getId());
				returnMap.put("alltime", DateUtils.DateToStr(p.getTimeHappen(),"time"));
			}
		}
		
		if("food".equals(planInfoTask.getType())||"sport".equals(planInfoTask.getType())){
			for(Object i : dateList){
				countMap.put(i, countMap.get(i)==null?1:countMap.get(i)+1);
			}
			for(String s : dateTempList){
				if((countMap.get(s)==3&&"food".equals(planInfoTask.getType()))||(countMap.get(s)==2&&"sport".equals(planInfoTask.getType()))){
					totalTempList.add(s);
				}
			}
			int c = 0;
			for(String s: totalTempList){
				if(totalTempList.contains(DateUtils.DateToStr(new Date(),"date"))){
					c++;
				}else{
					calendar.setTime(new Date());
					calendar.add(calendar.DATE,-1);
					if(totalTempList.contains(DateUtils.DateToStr(calendar.getTime(),"date"))){
						c++;
					}else{
						break;
					}
				}
			}
			returnMap.put("continue", c);
			returnMap.put("total", totalTempList.size());
		}else{
			returnMap.put("continue", count);
			returnMap.put("total", confirmList.size());
		}
		return returnMap;
	}

	/**
	 * 获取打卡情况
	 * sunxiao
	 * @return
	 */
	@Override
	public String getConfirmSituation(PlanInfo planInfo) {
		// TODO Auto-generated method stub
		String result="";
		List<Map<String,Object>> planList= planInfoTaskDao.getPlanInfoTaskListByPlanInfoId(planInfo.getId()+"");
		PlanInfo planInfoTemp = planInfoDao.selectByPrimaryKey(planInfo.getId());
		int count=0;
		for(Map<String,Object> map : planList){
			PlanInfoTaskConfirm planInfoTaskConfirm = new PlanInfoTaskConfirm();
			planInfoTaskConfirm.setPlanInfoTaskId((Long)map.get("id"));
			List<PlanInfoTaskConfirm> list = planInfoTaskConfirmDao.getConfirmByInfo(planInfoTaskConfirm);
			count+=list.size();
		}
		long temp = (planInfoTemp.getEndTime().getTime()-planInfoTemp.getStartTime().getTime())/(24*1000*60*60);
		
		if(0==count){
			result = "none";//没有打卡
		}else if(count<temp*planList.size()){
			result = "part";//部分打卡
		}else if(count==temp*planList.size()){
			result = "all";//全部打卡
		}
		return result;
	}

	@Override
	public PlanInfoTaskConfirm getPlanInfoTaskConfirmByPlanInfoTaskId(
			String planInfoTaskId) {
		// TODO Auto-generated method stub
		return planInfoTaskConfirmDao.getPlanInfoTaskConfirmByPlanInfoTaskId(planInfoTaskId);
	}

	/**
	 * 任务列表获取打卡情况
	 * sunxiao
	 * @param planInfoId
	 * @return
	 */
	@Override
	public HashMap<String, Object> taskListConfirm(String planInfoId) {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		String defecate = "打卡";
		String massage = "打卡";
		int foodCount = 0;
		int sportCount = 0;
		int defecateDays = 0;
		int massageDays = 0;
		int foodDays = 0;
		int sportDays = 0;
		String defecateId = "";
		String massageId = "";
		StringBuffer foodIds = new StringBuffer("");
		StringBuffer sportIds = new StringBuffer("");
		Map<Object,Integer> foodCountMap = new HashMap<Object,Integer>();
		List<String> foodList = new ArrayList<String>();
		List<String> foodTempList = new ArrayList<String>();
		Map<Object,Integer> sportCountMap = new HashMap<Object,Integer>();
		List<String> sportList = new ArrayList<String>();
		List<String> sportTempList = new ArrayList<String>();
		PlanInfo planInfo = planInfoDao.selectByPrimaryKey(Long.valueOf(planInfoId));
		List<Map<String,Object>> planInfoTaskList = planInfoTaskDao.getPlanInfoTaskListByPlanInfoId(planInfoId);
		for(Map<String, Object> map : planInfoTaskList){
			PlanInfoTaskConfirm planInfoTaskConfirm = new PlanInfoTaskConfirm();
			planInfoTaskConfirm.setPlanInfoTaskId((Long)map.get("id"));
			List<PlanInfoTaskConfirm> list = planInfoTaskConfirmDao.getConfirmByInfo(planInfoTaskConfirm);
			if(list!=null){
				if(list.size()!=0){
					for(PlanInfoTaskConfirm p : list){
						if("defecate".equals(map.get("type"))){
							if(DateUtils.DateToStr(p.getCreateTime(),"date").equals(DateUtils.DateToStr(new Date(),"date"))){
								defecate = "完成";
								defecateId = p.getPlanInfoTaskId()+"";
							}
						}else if("massage".equals(map.get("type"))){
							if(DateUtils.DateToStr(p.getCreateTime(),"date").equals(DateUtils.DateToStr(new Date(),"date"))){
								massage = "完成";
								massageId = p.getPlanInfoTaskId()+"";
							}
						}else if("food".equals(map.get("type"))){
							if(DateUtils.DateToStr(p.getCreateTime(),"date").equals(DateUtils.DateToStr(new Date(),"date"))){
								foodCount++;
								foodIds.append(p.getPlanInfoTaskId()+",");
							}
							foodList.add(DateUtils.DateToStr(p.getCreateTime(),"date"));
							if(!foodTempList.contains(DateUtils.DateToStr(p.getCreateTime(),"date"))){
								foodTempList.add(DateUtils.DateToStr(p.getCreateTime(),"date"));
							}
						}else if("sport".equals(map.get("type"))){
							if(DateUtils.DateToStr(p.getCreateTime(),"date").equals(DateUtils.DateToStr(new Date(),"date"))){
								sportCount++;
								sportIds.append(p.getPlanInfoTaskId()+",");
							}
							sportList.add(DateUtils.DateToStr(p.getCreateTime(),"date"));
							if(!sportTempList.contains(DateUtils.DateToStr(p.getCreateTime(),"date"))){
								sportTempList.add(DateUtils.DateToStr(p.getCreateTime(),"date"));
							}
						}
					}
					if("defecate".equals(map.get("type"))){
						defecateDays=list.size();
					}else if("massage".equals(map.get("type"))){
						massageDays=list.size();
					}
				}
			}
		}
		for(Object i : foodList){
			foodCountMap.put(i, foodCountMap.get(i)==null?1:foodCountMap.get(i)+1);
		}
		for(String s : foodTempList){
			if(foodCountMap.get(s)==3){
				foodDays++;
			}
		}
		for(Object i : sportList){
			sportCountMap.put(i, sportCountMap.get(i)==null?1:sportCountMap.get(i)+1);
		}
		for(String s : sportTempList){
			if(sportCountMap.get(s)==2){
				sportDays++;
			}
		}
		returnMap.put("defecate", defecate);
		returnMap.put("massage", massage);
		returnMap.put("food", foodCount);
		returnMap.put("sport", sportCount);
		returnMap.put("defecateId", defecateId);
		returnMap.put("massageId", massageId);
		returnMap.put("foodIds", foodIds);
		returnMap.put("sportIds", sportIds);
		returnMap.put("defecateDays", defecateDays);
		returnMap.put("massageDays", massageDays);
		returnMap.put("foodDays", foodDays);
		returnMap.put("sportDays", sportDays);
		returnMap.put("motherMust", planInfo.getNotice());
		return returnMap;
	}
	
}
 
