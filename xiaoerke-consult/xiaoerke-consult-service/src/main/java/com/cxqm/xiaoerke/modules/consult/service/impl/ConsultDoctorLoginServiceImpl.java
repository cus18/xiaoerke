package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorLoginService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaodeliang on 16/6/27.
 */

@Service
public class ConsultDoctorLoginServiceImpl implements ConsultDoctorLoginService {

    private static SystemService systemService = SpringContextHolder.getBean(SystemService.class);

    @Autowired
    private UtilService utilService;

    @Override
    public Map getDoctorLoginStatus(HttpSession session, HttpServletRequest request,HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String phone = CookieUtils.getCookie(request,response,"phone",false);
        result.put("status", "failure");
        User user = systemService.getUserByLoginName(phone);
        if(user!=null && StringUtils.isNotBlank(user.getId())){
            result.put("userPhone", user.getPhone());
            result.put("userId", user.getId());
            result.put("userName", user.getName());
            result.put("userType", user.getUserType());
            result.put("status","success");
        }
        String openId = WechatUtil.getOpenId(session, request);
        if (StringUtils.isNotNull(openId)) {
            result.put("openId", openId);
        } else {
            result.put("openId", "noOpenId");
        }
        return result;
    }

    @Override
    public Map doctorBinding(Map<String, Object> params,HttpSession session, HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String username = String.valueOf(params.get("username"));
        String openid = WechatUtil.getOpenId(session, request);
        String passeord = String.valueOf(params.get("password"));
        String status = utilService.bindUser4ConsultDoctor(username, passeord, openid);
        result.put("status", "failure");
        if (status.equals("1") && StringUtils.isNotNull(passeord) && StringUtils.isNotNull(username)) {
            CookieUtils.setCookie(response,"phone",passeord,86400);//两天重新登陆
            result.put("status","success");
        }
        return result;
    }



}
