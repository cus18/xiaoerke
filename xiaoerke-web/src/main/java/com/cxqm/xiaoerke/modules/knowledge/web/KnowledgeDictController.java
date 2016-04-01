package com.cxqm.xiaoerke.modules.knowledge.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cxqm.xiaoerke.common.utils.StringUtils;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.cms.entity.BabyEmrVo;
import com.cxqm.xiaoerke.modules.cms.service.BabyEmrService;
import com.cxqm.xiaoerke.modules.cms.utils.BirthdayToAgeUtils;
import com.cxqm.xiaoerke.modules.sys.entity.Dict;
import com.cxqm.xiaoerke.modules.sys.service.DictService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;

/**
 * 知识库宝宝账号Controller
 * @author sunxiao
 * 2015-11-26
 */
@Controller
@RequestMapping(value = "")
public class KnowledgeDictController extends BaseController {

	@Autowired
	private DictService dictService;
	
	@Autowired
	private BabyEmrService babyEmrService;
	
	/**
	 * 郑玉巧每日提醒
	 * 2015年11月26日 下午8:50:55
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/knowledge/knowledgeDict/dailyRemind", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> dailyRemind(@RequestParam Date birthDay) throws Exception {
		HashMap<String, Object> response = new HashMap<String, Object>();
		long diff = new Date().getTime() - birthDay.getTime();
		long day = diff/(24*60*60*1000);
		Dict dict = new Dict();
		dict.setType("dailyRemind_type");
		dict.setId("dailyRemind_"+day+"day");
		List<Dict> list = dictService.findList(dict);
		if(list!=null){
			if(list.size()!=0){
				response.put("dailyRemind_tip", list.get(0).getValue());//3条提醒信息
				response.put("dailyRemind_todayTip", list.get(0).getLabel());//今日提醒
				response.put("dailyRemind_game", list.get(0).getDescription());//游戏
			}
		}
		LogUtils.saveLog(Servlets.getRequest(),"00000060","获取每日提醒，age:" + day+"天");
		return response;
	}
	
	/**
	 * 查询宝宝年龄对应的标准身高体重
	 * 2015年11月30日 下午4:53:43
	 * @return String
	 * @author sunxiao
	 */
	@RequestMapping(value = "/knowledge/knowledgeDict/standardFigure", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> standardFigure(HttpServletRequest request,HttpSession session) throws Exception {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		String openId = WechatUtil.getOpenId(session, request);
		
        LogUtils.saveLog(Servlets.getRequest(), "00000061","获取身高体重标准，openid:" + openId);
        List<BabyEmrVo> list = babyEmrService.getBabyEmrList(openId);
        if(list!=null){
        	if(list.size()>0){
        		String str = BirthdayToAgeUtils.BirthdayToAgeForStandardFigure(list.get(0).getBirthday(), (String)list.get(0).getGender());
        		
        		returnMap.put("standardFigure", str);
        		returnMap.put("age", BirthdayToAgeUtils.getAgeByBirthday(list.get(0).getBirthday()));
        		LogUtils.saveLog(Servlets.getRequest(), "00000061","获取身高体重标准，age:" + list.get(0).getBirthday());
        	}
        }
		return returnMap;
	}
	
}
