package com.cxqm.xiaoerke.modules.operation.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.service.UserActionDetailService;

/**
 * 用户行为详细记录 Controller
 *
 * @author deliang
 * @version 2015-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/UserActionDetail")
public class UserActionDetailController extends BaseController {

    @Autowired
    UserActionDetailService userActionDetailService;

    /**
     * 根据用户的openid查询用户行为的详细记录
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"UserActionDetail", ""})
    public String UserActionDetail(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
        HashMap<String,Object> searchMap = new HashMap<String, Object>();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String  begin_time = request.getParameter("begin_time");
        String  end_time = request.getParameter("end_time");
        String  openid = request.getParameter("openid");
        searchMap.put("begin_time",begin_time);
        searchMap.put("end_time",end_time);
        searchMap.put("openid",openid);
        if(request.getParameter("begin_time") != null && request.getParameter("openid")!=null){
            resultMap = userActionDetailService.getUserFullToDoList(searchMap);
        }
        model.addAttribute("resultMap", resultMap);
        return "operation/userActionDetail";
    }
}
