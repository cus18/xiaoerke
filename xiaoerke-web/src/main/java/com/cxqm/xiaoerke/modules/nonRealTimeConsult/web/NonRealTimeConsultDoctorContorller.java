package com.cxqm.xiaoerke.modules.nonRealTimeConsult.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionPropertyService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/09/05 0024.
 */
@Controller
@RequestMapping(value = "nonRealTimeConsultDoctor")
public class NonRealTimeConsultDoctorContorller {


    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultSessionPropertyService consultSessionPropertyService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService;


    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void test(HttpSession session, HttpServletRequest request) {
//        babyCoinInit(session, request);
//        getBabyCoinInfo(session, request);
    }

    /**
     * 获取医生的登陆状态
     * response:
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/GetDoctorLoginStatus", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map GetDoctorLoginStatus(HttpSession session, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        //根据openid查询当前医生
        Map param = new HashMap();
        param.put("openId", WechatUtil.getOpenId(session, request));
        List<ConsultDoctorInfoVo> consultDoctorInfoVos = consultDoctorInfoService.getConsultDoctorByInfo(param);
        if (consultDoctorInfoVos != null && consultDoctorInfoVos.size() > 0 && StringUtils.isNotBlank(consultDoctorInfoVos.get(0).getUserId())) {
            response.put("status", "success");
        } else {
            response.put("status", "failure");
        }
        return param;
    }


}
