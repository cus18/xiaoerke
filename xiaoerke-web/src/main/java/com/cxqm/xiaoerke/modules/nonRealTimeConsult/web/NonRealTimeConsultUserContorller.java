package com.cxqm.xiaoerke.modules.nonRealTimeConsult.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionPropertyService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.NonRealTimeConsultService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
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
    public Map<String,Object> starDoctorInfo(HttpSession session, HttpServletRequest request,@RequestBody Map<String, Object> params) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        return consultDoctorInfoService.getConsultDoctorInfo(null);
    }

    @RequestMapping(value = "/savenConsultRecord", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void savenConsultRecord(@RequestParam(required = true) Integer sessionid, String userId, String fromType, String content, String msgtype){
        nonRealTimeConsultUserService.savenConsultRecord(sessionid,userId,fromType,content,msgtype);
    }

    @RequestMapping(value = "/getBabyBaseInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> getBabyBaseInfo(HttpSession session, HttpServletRequest request){
        Map<String,Object> reusltMap = new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session,request);
        openid = "o3_NPwmURoiCGRcQ8mXq7odkJqsU";
        BabyBaseInfoVo babyBaseInfoVo = nonRealTimeConsultUserService.babyBaseInfo(openid);
        reusltMap.put("babySex",babyBaseInfoVo.getSex());
        reusltMap.put("babyBirthDay", DateUtils.DateToStr(babyBaseInfoVo.getBirthday(),"date"));
        return reusltMap;
    }

    /**
     * 聊天咨询文件上传
     * @param file
     * @return {"status","success"}
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="/uploadMediaFile",method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String,Object> UploadFile(@RequestParam("file") MultipartFile file) throws UnsupportedEncodingException {
        HashMap<String, Object> response = nonRealTimeConsultUserService.uploadMediaFile(file);
        return response;
    }

}
