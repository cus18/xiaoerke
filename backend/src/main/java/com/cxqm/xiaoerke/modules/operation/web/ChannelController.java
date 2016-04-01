package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.service.ChannelService;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 渠道统计 Controller
 *
 * @author deliang
 * @version 2015-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/Channel")
public class ChannelController extends BaseController {

    @Autowired
    ChannelService channelService;

    /**
     * 获取渠道统计数据
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"ChannelMain", ""})
    public String ChannelMain(HttpServletRequest request,  Model model) throws Exception{
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if (StringUtils.isNull(startDate)) {//默认查看最近5天的
            Calendar ca = Calendar.getInstance();
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) - 2);
            Date start = ca.getTime();
            startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE)+2);
            Date end = ca.getTime();
            endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
        }
        HashMap<String, Object> iniMap = new HashMap<String, Object>();
        iniMap.put("startDate",startDate);
        iniMap.put("endDate",endDate);
        List<HashMap<String, Object>> listQdData = channelService.getTuiStatisticData(iniMap);
        model.addAttribute("channelVo", listQdData);

        RegisterServiceVo registerServiceVo = new RegisterServiceVo();
        registerServiceVo.setStartDate(startDate);
        registerServiceVo.setEndDate(endDate);
        model.addAttribute("registerServiceVo", registerServiceVo);
        return "operation/channel";
    }

}
