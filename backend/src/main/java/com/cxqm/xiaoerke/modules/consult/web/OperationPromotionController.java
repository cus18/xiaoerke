package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionTemplateVo;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionVo;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionService;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionTemplateService;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 运营推广 Controller
 *
 * @author sunxiao
 * @version 2016-9-19
 */
@Controller
@RequestMapping(value = "${adminPath}/operationPromotion")
public class OperationPromotionController extends BaseController {

    @Autowired
    OperationPromotionService operationPromotionService;

    @Autowired
    OperationPromotionTemplateService operationPromotionTemplateService;

    /**
     * 关键字规则列表
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "operationPromotionKeywordList")
    public String operationPromotionKeywordList(OperationPromotionVo vo,HttpServletRequest request, Model model) {
        List<OperationPromotionVo> list = operationPromotionService.findKeywordRoleList(vo);
        model.addAttribute("vo", new OperationPromotionVo());
        model.addAttribute("list", list);
        return "modules/consult/operationPromotionKeywordList";
    }

    /**
     * 添加修改规则页面
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "saveUpdateRoleForm")
    public String saveUpdateRoleForm(OperationPromotionVo vo,HttpServletRequest request, Model model) {
        OperationPromotionVo returnVo = new OperationPromotionVo();
        if(StringUtils.isNotNull(vo.getRoleId())){
            List<OperationPromotionVo> list = operationPromotionService.findKeywordRoleList(vo);
            returnVo = list.get(0);
            returnVo.setKeyword(returnVo.getKeyword().replace(","," "));
        }
        model.addAttribute("vo", returnVo);
        return "modules/consult/saveUpdateRoleForm";
    }

    /**
     * 添加关键字规则
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "saveKeywordRole")
    public String saveKeywordRole(OperationPromotionVo vo,HttpServletRequest request, Model model) {
        String keywordQcode = request.getParameter("keywordQcode");
        vo.setMessageType(keywordQcode);
        operationPromotionService.saveKeywordRole(vo);
        model.addAttribute("vo", new OperationPromotionVo());
        try {
            HttpRequestUtil.httpPost("", "http://101.200.180.132:8080/keeper/patient/updateKeyWordRecovery");
            HttpRequestUtil.httpPost("", "http://101.201.197.251:8082/keeper/patient/updateKeyWordRecovery");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:" + adminPath + "/operationPromotion/operationPromotionKeywordList";
    }

    /**
     * 删除关键字规则
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "deleteKeywordRole")
    public String deleteKeywordRole(OperationPromotionVo vo,HttpServletRequest request, Model model) {
        operationPromotionService.deleteKeywordRole(vo);
        try {
            HttpRequestUtil.httpPost("", "http://101.200.180.132:8080/keeper/patient/updateKeyWordRecovery");
            HttpRequestUtil.httpPost("", "http://101.201.197.251:8082/keeper/patient/updateKeyWordRecovery");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:" + adminPath + "/operationPromotion/operationPromotionKeywordList";
    }

    /**
     * 咨询的医生科室列表
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "operationPromotionTemplateList")
    public String operationPromotionTemplateList(OperationPromotionTemplateVo vo,HttpServletRequest request, Model model) {
        List<OperationPromotionTemplateVo> departmentList = operationPromotionTemplateService.findOperationPromotionTemplateList(vo);
        model.addAttribute("vo", vo);
        model.addAttribute("departmentList", departmentList);
        return "modules/consult/departmentList";
    }

    /**
     * 咨询医生操作页面
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "departmentOperForm")
    public String departmentOperForm(OperationPromotionTemplateVo vo, Model model) {
        OperationPromotionTemplateVo cddvo = new OperationPromotionTemplateVo();
        if(StringUtils.isNotNull(vo.getId()+"")){
            List<OperationPromotionTemplateVo> list = operationPromotionTemplateService.findOperationPromotionTemplateList(vo);//consultDoctorInfoService.findDepartmentList(vo);
            cddvo = list.get(0);
            cddvo.setImage("http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/"+vo.getId());
        }
        model.addAttribute("vo", cddvo);
        return "modules/consult/departmentOperForm";
    }

    /**
     * 添加修改医生信息
     * @param
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "departmentOper")
    public String departmentOper(OperationPromotionTemplateVo vo) {
        net.sf.json.JSONObject result = new net.sf.json.JSONObject();
        try {
            operationPromotionTemplateService.operationPromotionTemplateOper(vo);
            result.put("result","suc");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result","fail");
        }
        return result.toString();
    }

    /**
     * 添加修改医生信息
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "deleteOperationPromotionTemplate")
    public String deleteOperationPromotionTemplate(OperationPromotionTemplateVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
        net.sf.json.JSONObject result = new net.sf.json.JSONObject();
        try {
            operationPromotionTemplateService.deleteOperationPromotionTemplate(vo);
            result.put("result", "suc");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "fail");
        }
        return "redirect:" + adminPath + "/consult/consultDoctorDepartmentList?repage";
    }
}
