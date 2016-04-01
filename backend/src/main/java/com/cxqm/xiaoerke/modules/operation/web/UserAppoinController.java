package com.cxqm.xiaoerke.modules.operation.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.service.UserAppoinService;

/**
 * 用户预约统计 Controller
 *
 * @author deliang
 * @version 2015-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/UserAppoin")
public class UserAppoinController extends BaseController {

    @Autowired
    private UserAppoinService userAppoinService;

    /**
     * 用户预约统计
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"userAppointInfo", ""})
    public String userAppointInfo(HttpServletRequest request, Model model) throws Exception {
        String begin_time = request.getParameter("begin_time");
        String end_time = request.getParameter("end_time");
        //获取所有有订单存在的医院
        HashMap<String, Object> newMap = new HashMap<String, Object>();
        newMap.put("begin_time", begin_time);
        newMap.put("end_time", end_time);
        newMap.put("searchFlag", "jiezhen");
        if (newMap != null && newMap.size() > 0) {
            HashMap<String, Object> resultMap = userAppoinService.getUserAppoinStatisticData(newMap);
            model.addAttribute("resultMap", resultMap);
        }
        return "operation/userAppointInfo";
    }

}
