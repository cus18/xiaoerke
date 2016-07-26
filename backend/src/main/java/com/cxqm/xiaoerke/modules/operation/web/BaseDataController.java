package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.service.BaseDataService;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * 基础数据统计 Controller
 *
 * @author zhangbo
 * @version 2015-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/BaseData")
public class BaseDataController extends BaseController {
    @Autowired
    BaseDataService baseDataService;

    @Autowired
    BabyUmbrellaInfoService babyUmbrellaInfoService;

    /**
     * 获取渠道统计数据
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"getBaseDataStatistics", ""})
    public String getBaseDataStatistics(HttpServletRequest request, Model model) throws Exception {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //用户行为统计分析
        if (StringUtils.isNull(startDate)) {//默认查看最近5天的
            Calendar ca = Calendar.getInstance();
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) - 5);
            Date start = ca.getTime();
            startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE)+5);
            Date end = ca.getTime();
            endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
        }
        List<HashMap<String, Object>> list = baseDataService.findStatisticsTitleList(startDate, endDate);
        model.addAttribute("resultlist", list);
        RegisterServiceVo registerServiceVo = new RegisterServiceVo();
        registerServiceVo.setStartDate(startDate);
        registerServiceVo.setEndDate(endDate);
        model.addAttribute("registerServiceVo", registerServiceVo);
        return "operation/baseData";
    }

    /**
     * 保护伞用户统计
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"getUmbrellaDataStatistics", ""})
    public String getUmbrellaDataStatistics(HttpServletRequest request, Model model) throws Exception {
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

        List<HashMap<String, Object>> list = babyUmbrellaInfoService.findStatisticsList(startDate, endDate);
        model.addAttribute("resultList", list);

//        model.addAttribute("addFamily", ;
//        model.addAttribute("totalFamily", 1);
//        model.addAttribute("addUser", 3);
//        model.addAttribute("totalUser", 4);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "operation/umbrellaData";
    }

    /**
     * 统计：用户基础数据
     *
     * @throws ParseException sys/BaseData  /sys/BaseData/addBaseDataStatistics
     */
    @RequestMapping(value = "addBaseDataStatistics", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> addBaseDataStatistics() throws ParseException {

        HashMap<String, Object> r = new HashMap<String, Object>();
        //用户行为统计分析
        baseDataService.insertStatisticsTitle();
        r.put("Taylor", "OK");
        return r;
    }
}
