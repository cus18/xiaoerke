package com.cxqm.xiaoerke.modules.marketing.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.marketing.entity.MarketingActivities;
import com.cxqm.xiaoerke.modules.marketing.service.MarketingActivitiesService;

@Controller
@RequestMapping(value = "marketing")
public class MarketingActivitiesController {

	@Autowired
	MarketingActivitiesService  marketingActivitiesService;
	
	@RequestMapping(value = "/getMarketingActivitiesByOpenid", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getMarketingActivitiesByOpenid(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(params.get("score")!=null||!params.get("score").equals("")){
			String score=params.get("score").toString();
		List<Map<String, Object>> list = marketingActivitiesService.getMarketingActivitiesByOpenid(null, score);
		List<Map<String, Object>> listM = marketingActivitiesService.getMarketingActivitiesByOpenid(null, null);
		double part=list.size();
		double all=listM.size();
		double no = part / all * 100;
		map.put("accounting", (int)Math.floor(no) + "%");
		return map;
	}
		String openid = WechatUtil.getOpenId(session, request);
		List<Map<String, Object>> listM = marketingActivitiesService.getMarketingActivitiesByOpenid(openid, null);
		map.put("marketingList", listM);
		return map;
	}
	
	@RequestMapping(value = "/saveMarketingActivities", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  saveMarketingActivities(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session){
		Map<String ,Object> map=new HashMap<String, Object>();
		MarketingActivities marketingActivities=new MarketingActivities();
		String openid=WechatUtil.getOpenId(session, request);
		marketingActivities.setOpenid(openid);
		marketingActivities.setResult(params.get("result").toString());
		marketingActivities.setScore(params.get("score").toString());
		marketingActivities.setCreateDate(new Date());
		map.put("id", marketingActivitiesService.saveMarketingActivities(marketingActivities));
		return map;
	}
	
	@RequestMapping(value = "/updateMarketingActivities", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  updateMarketingActivities(@RequestBody Map<String, Object> params){
		Map<String ,Object> map=new HashMap<String, Object>();
		MarketingActivities marketingActivities=new MarketingActivities();
		marketingActivities.setId(Integer.parseInt(params.get("id").toString()));
		marketingActivities.setIfShare(params.get("ifShare").toString());
		map.put("updateResult", marketingActivitiesService.updateMarketingActivities(marketingActivities));
		return map;
	}
}
