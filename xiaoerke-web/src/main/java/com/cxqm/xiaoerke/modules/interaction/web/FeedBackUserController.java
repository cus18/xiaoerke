/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.interaction.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.interaction.entity.UserFeedbackVo;
import com.cxqm.xiaoerke.modules.interaction.service.FeedbackService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "feedback")
public class FeedBackUserController extends BaseController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 意见反馈
     */
    @RequestMapping(value = "/user/sendAdvice", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> sendAdvice(@RequestBody Map<String, Object> params,HttpSession session,HttpServletRequest request) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        String openId = WechatUtil.getOpenId(session, request);
        params.put("user",openId);
        params.put("project", "web-app");
        feedbackService.sendAdvice(params);
        return null;
    }

    /**
     * 意见反馈
     */
    @RequestMapping(value = "/user/saveFeedBack", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> saveFeedBack(@RequestBody Map<String, Object> params,HttpSession session,HttpServletRequest request) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        String openId = WechatUtil.getOpenId(session, request);
        params.put("user",openId);
        UserFeedbackVo feedbackVo =  new UserFeedbackVo();
        feedbackVo.setOpenid(openId);
        feedbackVo.setContent((String) params.get("contact"));
        feedbackVo.setCreateTime(new Date());
        feedbackVo.setType((String) params.get("advice"));
        feedbackVo.setSolve("未解决");
        feedbackService.saveFeedBack(feedbackVo);
        return null;
    }

    /**
     * 微信支付反馈
     */
    @RequestMapping(value = "/user/wxpayAdvice", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String wxPayAdvice(@RequestBody Map<String, Object> params,HttpSession session,HttpServletRequest request) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        String openId = WechatUtil.getOpenId(session,request);
        params.put("user",openId);
        params.put("project", "wechatPay");
        params.put("user", UserUtils.getUser().getId());
        String payAdvice= "";

        List<HashMap<String,Object>> payList = (List)params.get("payList");
        int i = 1;
        for(HashMap<String,Object> map:payList){
            Boolean flag = (Boolean)map.get("checked");
            if(flag){
                payAdvice += i+"、"+ map.get("text") + map.get("reason")+";";
                i++;
            }
        }
        params.put("advice",payAdvice);
        feedbackService.sendAdvice(params);
        return "true";
    }

    /**
     * 长久没用宝大夫原因调研
     */
    @RequestMapping(value = "/user/consultVisit", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String consultVisit(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        params.put("user",params.get("openId"));
        params.put("project", "consultVisit");
        feedbackService.sendAdvice(params);
        return "true";
    }

}
