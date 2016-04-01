package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.entity.StatisticsTitle;
import com.cxqm.xiaoerke.modules.operation.service.BaseDataService;
import com.cxqm.xiaoerke.modules.operation.service.PlanStatisticsService;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  健康管理数据统计 Controller
 *
 * @author zhangbo
 * @version 2015-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/PlanStatistics")
public class PlanStatisticsController extends BaseController {
    @Autowired
    PlanStatisticsService planStatisticsService;


    /**
     * 获取渠道统计数据
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"getPlanStatistics", ""})
    public String getPlanStatistics(HttpServletRequest request, Model model) throws Exception {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        if (StringUtils.isNull(startDate)) {//默认查看最近5天的
            Calendar ca = Calendar.getInstance();
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) - 5);
            Date start = ca.getTime();
            startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE)+5);
            Date end = ca.getTime();
            endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date1 = sdf.parse(startDate);
        Date date2 = sdf.parse(endDate);
        long timeDiff = date2.getTime() - date1.getTime();
        int dayDiff = (int) (timeDiff / 1000 / 3600 / 24);  //初始化开始时间距今几天。
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("startDate", startDate);
        maps.put("endDate", null);
        List<String> openid=planStatisticsService.findPlanOpenids(maps);
        for (int i = 0; i < dayDiff; i++) {
            cal.setTime(date1);
            cal.add(Calendar.DATE, i);
            String date=sdf.format(cal.getTime());
            Map<String, Object> map = planStatisticsService.findPlanData(date);
            map.put("date", date);
            Calendar cal1 = Calendar.getInstance();
    		cal1.setTime(sdf.parse(date));
            cal1.add(Calendar.DATE, 1);
    		map.put("userStayNextDay", planStatisticsService.userStayForDays(sdf.format(cal1.getTime()), null, openid));
            cal1.add(Calendar.DATE, 1);
    		map.put("userStayThreeDay", planStatisticsService.userStayForDays(sdf.format(cal1.getTime()), null, openid));
    		cal1.add(Calendar.DATE, 4);
    		map.put("userStayWeek", planStatisticsService.userStayForDays(sdf.format(cal1.getTime()), null, openid));
        	list.add(map);
        }
        model.addAttribute("resultlist", list);
        RegisterServiceVo registerServiceVo = new RegisterServiceVo();
        registerServiceVo.setStartDate(startDate);
        registerServiceVo.setEndDate(endDate);
        model.addAttribute("registerServiceVo", registerServiceVo);
        return "operation/planStatistics";
    }

}
