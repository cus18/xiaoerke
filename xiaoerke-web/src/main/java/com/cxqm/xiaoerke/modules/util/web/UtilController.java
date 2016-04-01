/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.util.web;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.plan.entity.HealthPlanAddItemVo;
import com.cxqm.xiaoerke.modules.plan.service.PlanMessageService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 工具 Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "${xiaoerkePath}")
public class UtilController extends BaseController {

    @Autowired
    AccountService accountService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private PlanMessageService planMessageService;

    @Autowired
    private PatientRegisterService patientRegisterService;

    /**
     * 检测用户是否绑定登陆的接口
     * <p/>
     * response:
     * {
     * "status":"1"
     * }
     * //status为1表示用户已经绑定注册，0表示用户没有绑定注册
     */
    @RequestMapping(value = "/util/checkBind", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> checkBind(@RequestBody Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        //判断用户是否绑定了手机号，如果没有绑定，将让用户跳转到手机号的绑定页面
        User user = UserUtils.getUser();
        String userId = user.getId();
        response.put("accountFund", 0);
        if (!"null".equals(user)) {
            //账户余额
            Float account = accountService.accountFund(userId);
            response.put("accountFund", account / 100);
        }

        //订单生成时间
        if (null != params.get("patient_register_service_id")) {
            Date createDate = patientRegisterService.getOrderCreateDate((String) params.get("patient_register_service_id"));
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(createDate);
            rightNow.add(Calendar.MINUTE, +30);//日期减1年
            createDate = rightNow.getTime();
            response.put("createDate", createDate.getTime());
        }

        String BondSwitch = Global.getConfig("webapp.BondSwitch");
        Boolean Bl = new Boolean(BondSwitch);
        response.put("bondSwitch", Bl);
        if (userId == null) {
            response.put("status", "0");
            return response;
        } else {
            response.put("status", "1");
            return response;
        }
    }


    /**
     * 根据手机号，获取验证码，验证码将根据手机号，下推至用户手机
     * <p/>
     * params:{"userPhone":"13601025662"}
     * <p/>
     * response:
     * {
     * "code":"35678",
     * "status":"1"
     * }
     * //status为1表示获取验证码成功，为0表示获取验证码失败
     */
    @RequestMapping(value = "/util/user/getCode", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getUserCode(@RequestBody Map<String, Object> params) {
        String userPhone = (String) params.get("userPhone");
        return utilService.sendIdentifying(userPhone);
    }

    @RequestMapping(value = "/util/user/recordHealthPlanAddItem", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> recordHealthPlanAddItem(@RequestBody Map<String, Object> params,
                                                HttpServletRequest request,HttpSession session) throws Exception{
        String openId = WechatUtil.getOpenId(session, request);
        String addValue = (String) params.get("addValue");
        HashMap<String ,Object> response = new HashMap<String,Object>();
        User user = UserUtils.getUser();
        if(user.getLoginName()!=null||openId!=null){
            HealthPlanAddItemVo healthPlanAddItemVo = new HealthPlanAddItemVo();
            healthPlanAddItemVo.setTelephone(user.getPhone());
            if(user.getOpenid()!=null){
                healthPlanAddItemVo.setOpenid(user.getOpenid());
            }else{
                healthPlanAddItemVo.setOpenid(openId);
            }
            healthPlanAddItemVo.setAddValue(addValue);
            planMessageService.insertHealthPlanAddItem(healthPlanAddItemVo);
            response.put("result","success");
        }else{
            response.put("result","failure");
        }

        return response;
    }

    @RequestMapping(value = "/util/user/findHealthPlanAddItem", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    List<HealthPlanAddItemVo> findHealthPlanAddItem(HttpServletRequest request,HttpSession session) throws Exception {
        String openId = WechatUtil.getOpenId(session, request);
        User user = UserUtils.getUser();
        if(user.getLoginName()!=null||openId!=null){
            HealthPlanAddItemVo healthPlanAddItemVo = new HealthPlanAddItemVo();
            healthPlanAddItemVo.setTelephone(user.getPhone());
            if(user.getOpenid()!=null){
                healthPlanAddItemVo.setOpenid(user.getOpenid());
            }else{
                healthPlanAddItemVo.setOpenid(openId);
            }
            List<HealthPlanAddItemVo> healthPlanAddItemVos = planMessageService.findHealthPlanAddItem(healthPlanAddItemVo);
            return healthPlanAddItemVos;
        }else{
            return null;
        }
    }

    /**
     * 根据手机号，获取验证码，验证码将根据手机号，下推至用户手机
     * <p/>
     * params:{"userPhone":"13601025662"}
     * <p/>
     * response:
     * {
     * "code":"35678",
     * "status":"1"
     * }
     * //status为1表示获取验证码成功，为0表示获取验证码失败
     */
    @RequestMapping(value = "/util/doctor/getCode", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getDoctorCode(@RequestBody Map<String, Object> params) {
        String userPhone = String.valueOf(params.get("userPhone"));
        return utilService.sendIdentifying(userPhone);
    }


    /**
     * 用户登出操作
     */
    @RequestMapping(value = "/util/logOut", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String logOut() {
        UserUtils.getSubject().logout();
        return "success";
    }

    /**
     * 供前台调用 保存页面点击的日志文件
     */
    @SystemControllerLog(description = "")
    @RequestMapping(value = "/util/recordLogs", method = {RequestMethod.GET, RequestMethod.POST},
            produces = "text/plain;charset=UTF-8")
    public
    @ResponseBody
    String recordLogs(HttpServletRequest request) {
        try {
            String logContent = URLDecoder.decode(request.getParameter("logContent"), "UTF-8");
            return "success";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "false";
        }
    }
}
