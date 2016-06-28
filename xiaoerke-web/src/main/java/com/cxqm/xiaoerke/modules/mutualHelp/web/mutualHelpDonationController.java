package com.cxqm.xiaoerke.modules.mutualHelp.web;

import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.modules.mutualHelp.entity.MutualHelpDonation;
import com.cxqm.xiaoerke.modules.mutualHelp.service.MutualHelpDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 蛋蛋捐款 controller
 * Created by Administrator on 2016/6/27 0027.
 */
@Controller
@RequestMapping(value = "mutualHelp/donation")
public class MutualHelpDonationController {
    @Autowired
    MutualHelpDonationService service;

    /**
     * 照片墙：我的捐款信息+所有捐款和留言信息
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/photoWall", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getDetail(HttpServletRequest request, Map<String, Object> paramMap){
        String openId = (String) paramMap.get("openId");
        if(openId ==null){
            openId = CookieUtils.getCookie(request,"openId");
        }

        HashMap<String,Object> searchMap = new HashMap<String, Object>();
        searchMap.put("openId", openId);
        searchMap.put("userId", (Integer) paramMap.get("userId"));
        searchMap.put("donationType", (Integer) paramMap.get("donationType"));
        return service.getDonatonDetail(searchMap);
    }

    /**
     * 捐款总人次
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/count", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getManTime(Map<String, Object> paramMap){
        Map<String,Object> response = new HashMap<String, Object>();
        Integer type = (Integer) paramMap.get("donationType");
        response.put("count",service.getCount(type));

        return response;
    }

    /**
     * 捐款总金额
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/sumMoney", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getSumMoney(Map<String, Object> paramMap){
        Map<String,Object> response = new HashMap<String, Object>();
        Integer type = (Integer) paramMap.get("donationType");
        response.put("count",service.getCount(type));

        return response;
    }


    /**
     * 获取最新一条留言
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/lastNote", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getLastNote(Map<String, Object> paramMap){
        HashMap<String,Object> searchMap = new HashMap<String, Object>();
        searchMap.put("donationType", (Integer) paramMap.get("donationType"));

        return service.getLastNote(searchMap);
    }

    /**
     * 捐款或留言
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/addNoteAndDonation", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> addNoteAndDonation(HttpServletRequest request, Map<String, Object> paramMap){
        String openId = (String) paramMap.get("openId");
        if(openId ==null){
            openId = CookieUtils.getCookie(request,"openId");
        }

        MutualHelpDonation mutualHelpDonation = new MutualHelpDonation();
        mutualHelpDonation.setOpenId(openId);
        mutualHelpDonation.setUserId((String) paramMap.get("userId"));
        mutualHelpDonation.setMoney((Double) paramMap.get("money"));
        mutualHelpDonation.setLeaveNote((String) paramMap.get("leaveNote"));
        mutualHelpDonation.setDonationType((Integer) paramMap.get("donationType"));

        int n = service.saveNoteAndDonation(mutualHelpDonation);
        Map<String,Object> response = new HashMap<String, Object>();
        if(n>0){
            response.put("insert","success");
        }else{
            response.put("insert","failed");
        }
        return response;
    }
}
