package com.cxqm.xiaoerke.modules.operation.web;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.entity.ChannelInfo;
import com.cxqm.xiaoerke.modules.operation.service.ChannelService;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * 获取渠道增加数据
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"ChannelMain", ""})
    public String ChannelMain(HttpServletRequest request,  Model model) throws Exception{
        //获取渠道所有数据
        List<ChannelInfo> channelList = channelService.getChannelInfos();
        model.addAttribute("channelList",channelList);

        //获取所有部门
        String[] departs = Global.getConfig("backend.departments").split(",");
        model.addAttribute("departs",departs);
        return "operation/channel";
    }

    /**
     * 获取渠道分类统计数据
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"ChannelCategory", ""})
    public String ChannelCategory(HttpServletRequest request,  Model model) throws Exception{
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
        return "operation/channelCategory";
    }

    /**
     * 获取渠道细分数据
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"ChannelDetail", ""})
    public String ChannelDetail(HttpServletRequest request,  Model model) throws Exception{
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
        return "operation/channelDetail";
    }

    /**
     * 添加渠道
     *
     * @return
     */
    @RequiresPermissions("user")
    //@RequestMapping(value = "addChannel", produces = { "application/json;charset=UTF-8" })
    @RequestMapping(value = {"addChannel"})
    public @ResponseBody String addChannel(HttpServletRequest request) throws Exception{
        String create_by = request.getParameter("create_by");
        String department = request.getParameter("department");
        String marker = request.getParameter("marker");
        String channel = request.getParameter("channel");

        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setCreate_by(create_by);
        channelInfo.setDepartment(department);
        channelInfo.setMarker(marker);
        channelInfo.setChannel(channel);

        channelService.insertChannel(channelInfo);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("result","保存成功!");
        } catch (Exception e) {
            jsonObject.put("result","保存失败!请重试");
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

}
