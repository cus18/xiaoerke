package com.cxqm.xiaoerke.modules.nonRealTimeConsult.web;

import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionPropertyService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.NonRealTimeConsultService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/09/05 0024.
 */
@Controller
@RequestMapping(value = "nonRealTimeConsultUser")
public class NonRealTimeConsultUserContorller {


    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultSessionPropertyService consultSessionPropertyService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private NonRealTimeConsultService nonRealTimeConsultUserService;

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService;


    @RequestMapping(value = "/getDepartmentList", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> departmentList(HttpSession session, HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("departmentList",nonRealTimeConsultUserService.departmentList());
        return resultMap;
    }

    @RequestMapping(value = "/getStarDoctorlist", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> starDoctorlist(HttpSession session, HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("startDoctorList",consultDoctorInfoService.findManagerDoctorInfoBySelective(null));
        return resultMap;
    }

    @RequestMapping(value = "/getStarDoctorInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> starDoctorInfo(HttpSession session, HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        return consultDoctorInfoService.getConsultDoctorInfo(null);
    }

    @RequestMapping(value = "/sendMsg", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void sendMsg(Integer sessionid,String toUser,String fromUser,String content,String type){
        nonRealTimeConsultUserService.savenConsultRecord(sessionid,toUser,fromUser,content,type);
    }
}
