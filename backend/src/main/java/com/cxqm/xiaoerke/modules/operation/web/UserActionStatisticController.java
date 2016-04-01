package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.service.AppointRegisterAccountService;
import com.cxqm.xiaoerke.modules.operation.service.UserActionStatisticService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

/**
 * 用户行为统计分析 Controller
 *
 * @author deliang
 * @version 2015-11-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/userActionStatistic")
public class UserActionStatisticController extends BaseController {

    @Autowired
    UserActionStatisticService userActionStatisticService;

    @RequiresPermissions("user")
    @RequestMapping(value = {"userActionStatistic", ""})
    public String userActionStatistic(HttpServletRequest request, Model model) throws Exception {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //获取所有有订单存在的医院
        HashMap<String, Object> newMap = new HashMap<String, Object>();
        newMap.put("startDate", startDate);
        newMap.put("endDate", endDate);
        System.out.println(startDate);
        if (StringUtils.isNotNull(startDate)) {
            HashMap<String, Object> resultMap = userActionStatisticService.userOperationData(newMap);
            model.addAttribute("resultMap", resultMap);
        }else{
            HashMap<String, Object> resultMap = new HashMap<String,Object>();
            resultMap.put("startDate", DateUtils.DateToStr(new Date(), "date"));
            resultMap.put("endDate",DateUtils.DateToStr(new Date(),"date"));
            model.addAttribute("resultMap", resultMap);
        }
        return "operation/userActionStatistic";
    }
}
