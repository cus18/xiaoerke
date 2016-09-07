package com.cxqm.xiaoerke.modules.operation.web;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.entity.ChannelInfo;
import com.cxqm.xiaoerke.modules.operation.service.ChannelService;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
        String department = request.getParameter("department");
        if("所有".equals(department)){
            department = "all";
        }

        if (StringUtils.isNull(startDate)) {//默认查看最近5天的
            Calendar ca = Calendar.getInstance();
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) - 2);
            Date start = ca.getTime();
            startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE)+2);
            Date end = ca.getTime();
            endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
        }

        //获取所有部门
        StringBuffer sbf = new StringBuffer("所有,");
        String[] departs = sbf.append(Global.getConfig("backend.departments")).toString().split(",");
        model.addAttribute("departs",departs);
        HashMap<String, Object> iniMap = new HashMap<String, Object>();
        iniMap.put("startDate",startDate);
        iniMap.put("endDate",endDate);
        iniMap.put("department",department);
        List<HashMap<String, Object>> listQdData = channelService.getChannelCategoryStatistics(iniMap);
        model.addAttribute("channelCategoryVo", listQdData);

        RegisterServiceVo registerServiceVo = new RegisterServiceVo();
        registerServiceVo.setStartDate(startDate);
        registerServiceVo.setEndDate(endDate);
        registerServiceVo.setDepartment(department);
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
        String department = request.getParameter("department");
        String channel = request.getParameter("channel");
        if("所有".equals(department)){
            department = "all";
        }
        if("所有".equals(channel)){
            channel = "all";
        }

        if (StringUtils.isNull(startDate)) {//默认查看最近5天的
            Calendar ca = Calendar.getInstance();
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) - 2);
            Date start = ca.getTime();
            startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE)+2);
            Date end = ca.getTime();
            endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
        }

        //获取所有部门
        StringBuffer sbf = new StringBuffer("所有,");
        String[] departs = sbf.append(Global.getConfig("backend.departments")).toString().split(",");
        model.addAttribute("departs",departs);

        //获取所有渠道
        List<String> channels = channelService.getAllChannels();
        channels.add(0,"所有");
        model.addAttribute("channels",channels);

        HashMap<String, Object> iniMap = new HashMap<String, Object>();
        iniMap.put("startDate",startDate);
        iniMap.put("endDate",endDate);
        iniMap.put("department",department);
        iniMap.put("channel",channel);
        List<HashMap<String, Object>> listQdData = channelService.getChannelDetailStatistics(iniMap);
        model.addAttribute("channelDetailVo", listQdData);

        RegisterServiceVo registerServiceVo = new RegisterServiceVo();
        registerServiceVo.setStartDate(startDate);
        registerServiceVo.setEndDate(endDate);
        registerServiceVo.setDepartment(department);
        registerServiceVo.setChannel(channel);
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
        String operater = request.getParameter("operater");
        String department = request.getParameter("department");
        String marketer = request.getParameter("marker");
        String channel = request.getParameter("channel");

        JSONObject cancleSameObject = new JSONObject();
        Map<String,Object> searchMap = new HashMap<String, Object>();
        searchMap.put("marketer",marketer);
        //判断录入者录入的二维码是否有重复。如果有给予提示，如果没有则继续正常录入数据.
        boolean flag = channelService.isExistSameMarketer(searchMap);
        if(flag){
            cancleSameObject.put("result","该二维码【" + marketer + "】已经录入,请不要重复录入!\n点击确定按钮您可以继续录入其他二维码数据,谢谢!");
            cancleSameObject.put("status","1");
            return cancleSameObject.toString();
        }

        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setOperater(operater);
        channelInfo.setDepartment(department);
        channelInfo.setMarketer(marketer);
        channelInfo.setChannel(channel);

        JSONObject jsonObject = null;
        try {
            channelService.insertChannel(channelInfo);
            jsonObject = new JSONObject();
            jsonObject.put("result","保存成功!");
            jsonObject.put("status","0");
        } catch (Exception e) {
            jsonObject.put("result","保存失败!请重试");
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    /**
     * 用户统计（部门）
     *  sunxiao
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"userStatisticsDepartment", ""})
    public String userStatisticsDepartment(ChannelInfo info,HttpServletRequest request,  Model model) throws Exception{
        HashMap<String, Object> iniMap = new HashMap<String, Object>();
        iniMap.put("startDate",info.getStartDate());
        iniMap.put("endDate",info.getEndDate());
        iniMap.put("department","所有".equals(info.getDepartment())?"":info.getDepartment());
        List<HashMap<String, Object>> userDepartmentList = channelService.getUserStatisticsDepartment(iniMap);
        //获取所有部门
        StringBuffer sbf = new StringBuffer("所有,");
        String[] departs = sbf.append(Global.getConfig("backend.departments")).toString().split(",");
        model.addAttribute("departs", departs);
        model.addAttribute("userDepartmentList", userDepartmentList);
        //获取所有渠道
        List<String> channels = channelService.getAllChannels();
        channels.add(0, "所有");
        model.addAttribute("channels", channels);
        model.addAttribute("info",info);
        return "operation/userStatisticsDepartment";
    }

    /**
     * 用户统计（渠道）
     * sunxiao
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"userStatisticsChannel", ""})
    public String userStatisticsChannel(ChannelInfo info,HttpServletRequest request,  Model model) throws Exception{
        HashMap<String, Object> iniMap = new HashMap<String, Object>();
        iniMap.put("startDate",info.getStartDate());
        iniMap.put("endDate",info.getEndDate());
        iniMap.put("department","所有".equals(info.getDepartment())?"":info.getDepartment());
        iniMap.put("channel","所有".equals(info.getChannel())?"":info.getChannel());
        List<HashMap<String, Object>> userChannelList = channelService.getUserStatisticsChannel(iniMap);
        //获取所有部门
        StringBuffer sbf = new StringBuffer("所有,");
        String[] departs = sbf.append(Global.getConfig("backend.departments")).toString().split(",");
        model.addAttribute("departs", departs);
        model.addAttribute("userChannelList", userChannelList);
        //获取所有渠道
        List<String> channels = channelService.getAllChannels();
        channels.add(0, "所有");
        model.addAttribute("channels", channels);
        model.addAttribute("info",info);
        return "operation/userStatisticsChannel";
    }

    /**
     * 渠道咨询统计
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"ChannelConsultStatistics", ""})
    public String ChannelConsultStatistics(HttpServletRequest request,  Model model) throws Exception{
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String department = request.getParameter("department");
        String channel = request.getParameter("channel");
        if("所有".equals(department)){
            department = "all";
        }
        if("所有".equals(channel)){
            channel = "all";
        }

        if (StringUtils.isNull(startDate)) {//默认查看最近5天的
            Calendar ca = Calendar.getInstance();
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) - 2);
            Date start = ca.getTime();
            startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE)+2);
            Date end = ca.getTime();
            endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
        }

        //获取所有部门
        StringBuffer sbf = new StringBuffer("所有,");
        String[] departs = sbf.append(Global.getConfig("backend.departments")).toString().split(",");
        model.addAttribute("departs", departs);

        //获取所有渠道
        List<String> channels = channelService.getAllChannels();
        channels.add(0,"所有");
        model.addAttribute("channels",channels);

        HashMap<String, Object> iniMap = new HashMap<String, Object>();
        iniMap.put("startDate",startDate);
        iniMap.put("endDate",endDate);
        iniMap.put("department",department);
        iniMap.put("channel",channel);
        List<HashMap<String, Object>> listQdData = channelService.getAllConsultCountsByChannel(iniMap);
        model.addAttribute("channelConsultVo", listQdData);

        RegisterServiceVo registerServiceVo = new RegisterServiceVo();
        registerServiceVo.setStartDate(startDate);
        registerServiceVo.setEndDate(endDate);
        registerServiceVo.setDepartment(department);
        registerServiceVo.setChannel(channel);
        model.addAttribute("registerServiceVo", registerServiceVo);
        return "operation/channelConsult";
    }

    /**
     * 部门咨询统计
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"DepartmentConsultStatistics", ""})
    public String DepartmentConsultStatistics(HttpServletRequest request,  Model model) throws Exception{
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String department = request.getParameter("department");
        if("所有".equals(department)){
            department = "all";
        }

        if (StringUtils.isNull(startDate)) {//默认查看最近5天的
            Calendar ca = Calendar.getInstance();
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) - 2);
            Date start = ca.getTime();
            startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE)+2);
            Date end = ca.getTime();
            endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
        }

        //获取所有部门
        StringBuffer sbf = new StringBuffer("所有,");
        String[] departs = sbf.append(Global.getConfig("backend.departments")).toString().split(",");
        model.addAttribute("departs",departs);
        HashMap<String, Object> iniMap = new HashMap<String, Object>();
        iniMap.put("startDate",startDate);
        iniMap.put("endDate",endDate);
        iniMap.put("department",department);
        List<HashMap<String, Object>> listQdData = channelService.getAllConsultCountsByDepartment(iniMap);
        model.addAttribute("departmentConsultVo", listQdData);

        RegisterServiceVo registerServiceVo = new RegisterServiceVo();
        registerServiceVo.setStartDate(startDate);
        registerServiceVo.setEndDate(endDate);
        registerServiceVo.setDepartment(department);
        model.addAttribute("registerServiceVo", registerServiceVo);
        return "operation/departmentConsult";
    }

    /**
     * 删除渠道信息(根据id删除)
     * @param request
     * @return
     * @throws Exception
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"deleteChannelInfo"})
    public @ResponseBody String deleteChannelInfo(HttpServletRequest request) throws Exception{
        String channelId = request.getParameter("channelId");
        JSONObject jsonObject = null;
        try {
            channelService.deleteChannelInfo(channelId);
            jsonObject = new JSONObject();
            jsonObject.put("result","删除成功!");
        } catch (Exception e) {
            jsonObject.put("result","删除失败!请重试");
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    /**
     * 用户新关注与净留存统计
     *@author guozengguang
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"newUserAttentionAndRemainStatistics", ""})
    public String newUserAttentionAndRemainStatistics(HttpServletRequest request,  Model model) throws Exception{
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");


        if (StringUtils.isNull(startDate)) {//默认查看最近5天的
            Calendar ca = Calendar.getInstance();
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) - 2);
            Date start = ca.getTime();
            startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) + 2);
            Date end = ca.getTime();
            endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
        }


        HashMap<String, Object> iniMap = new HashMap<String, Object>();
        iniMap.put("startDate",startDate);
        iniMap.put("endDate",endDate);
        List<HashMap<String, Object>> listQdData = channelService.getNewUserAttentionAndRemainStatistics(iniMap);
        model.addAttribute("resultList", listQdData);

        RegisterServiceVo registerServiceVo = new RegisterServiceVo();
        registerServiceVo.setStartDate(startDate);
        registerServiceVo.setEndDate(endDate);
        model.addAttribute("registerServiceVo", registerServiceVo);
        return "operation/newUserAttentionAndRemain";
    }

    /**
     * 查询用户渠道
     * @author sunxiao
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"userChannelSearch"})
    public String userChannelSearch(WechatAttention vo ,HttpServletRequest request,  Model model) throws Exception{
        Page<WechatAttention> page = new Page<WechatAttention>();
        if(StringUtils.isNotNull(vo.getOpenid()) || StringUtils.isNotNull(vo.getNickname())){
            String todayAttention = request.getParameter("todayAttention");
            String todayConsult = request.getParameter("todayConsult");
            String temp = ((String)request.getParameter("pageNo"));
            Page<WechatAttention> pagess = null;
            if(temp==null){
                pagess = new Page<WechatAttention>();
            }else{
                Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
                Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
                pagess = new Page<WechatAttention>(pageNo,pageSize);
            }
            page = channelService.userChannelSearch(pagess,vo.getOpenid(),vo.getNickname(), todayAttention, todayConsult);
            model.addAttribute("todayAttention", todayAttention);
            model.addAttribute("todayConsult", todayConsult);
        }
        model.addAttribute("vo", vo);
        model.addAttribute("page", page);
        return "operation/userChannelSearch";
    }
}
