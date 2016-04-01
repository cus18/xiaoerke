package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.service.AppointRegisterAccountService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 号源结算统计 Controller
 *
 * @author deliang
 * @version 2015-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/AppointRegisterAccount")
public class AppointRegisterAccountController extends BaseController {

    @Autowired
    AppointRegisterAccountService appoinRegisterAccountService;

    @RequiresPermissions("user")
    @RequestMapping(value = {"appointRegisterAccount", ""})
    public String appointRegisterAccount(HashMap<String, Object> iniMap, String search, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
        String begin_time = request.getParameter("begin_time");
        String end_time = request.getParameter("end_time");
        //获取所有有订单存在的医院
        HashMap<String, Object> newMap = new HashMap<String, Object>();
        newMap.put("begin_time", begin_time);
        newMap.put("end_time", end_time);
        newMap.put("searchFlag", "jiesuan");
        if(newMap!=null && newMap.size()>0){
            HashMap<String, Object> resultMap = appoinRegisterAccountService.getYYStatisticData(newMap);
            model.addAttribute("resultMap", resultMap);
        }
        return "operation/appointRegisterAccount";
    }
}
