package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.service.UserConsultService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;

/**
 * 用户咨询统计 Controller
 *
 * @author deliang
 * @version 2015-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/UserConsultInfo")
public class UserConsultController extends BaseController {

    @Autowired
    private UserConsultService userConsultService;

    /**
     * 用户咨询统计信息
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"UserConsultInfo", ""})
    public String UserConsultInfo(HttpServletRequest request, Model model) {
        String begin_time = request.getParameter("begin_time");
        String end_time = request.getParameter("end_time");
        HashMap<String, Object> newMap = new HashMap<String, Object>();
        newMap.put("begin_time", begin_time);
        newMap.put("end_time", end_time);
        if(StringUtils.isNotNull(begin_time)){
            HashMap<String, Object> resultMap = userConsultService.getConsultStatisticData(newMap);
            model.addAttribute("resultMap", resultMap);
        }
        return "operation/UserConsult";
    }
}
