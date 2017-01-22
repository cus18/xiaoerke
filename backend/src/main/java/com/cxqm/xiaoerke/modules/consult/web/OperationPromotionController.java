package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionTemplateVo;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionVo;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionService;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionTemplateService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Time;
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
        if(StringUtils.isNotNull(vo.getReplyText())){
            vo.setReplyText(StringEscapeUtils.unescapeHtml4(vo.getReplyText()));
            vo.setReplyText(vo.getReplyText().replace("<p>", "").replace("\r\n\t", "").replace("amp;", "").replace("</p>", "").replace("\"", "'"));
        }
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
     * 运营推广列表
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "operationPromotionTemplateList")
    public String operationPromotionTemplateList(OperationPromotionTemplateVo vo,HttpServletRequest request, Model model) {
        List<OperationPromotionTemplateVo> templateList = operationPromotionTemplateService.findOperationPromotionTemplateList(vo);
        model.addAttribute("vo", vo);
        model.addAttribute("templateList", templateList);
        return "modules/consult/operationPromotionTemplateList";
    }

    /**
     * 运营推广操作页面
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "operationPromotionTemplateOperForm")
    public String operationPromotionTemplateOperForm(OperationPromotionTemplateVo vo, Model model) {
        if(StringUtils.isNotNull(vo.getId()+"")){
            List<OperationPromotionTemplateVo> list = operationPromotionTemplateService.findOperationPromotionTemplateList(vo);//consultDoctorInfoService.findDepartmentList(vo);
            vo = list.get(0);
            if("consultPay".equals(vo.getType())){
                vo.setInfo3s(DateUtils.DateToStr(vo.getInfo3(),"date"));
                vo.setInfo4s(DateUtils.DateToStr(vo.getInfo4(), "date"));
                vo.setInfo5s(DateUtils.DateToStr(vo.getInfo5(), "time"));
                vo.setInfo6s(DateUtils.DateToStr(vo.getInfo6(), "time"));
            }
            vo.setImage("http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/pictureTransmission"+vo.getId());
        }
        model.addAttribute("vo", vo);
        return "modules/consult/operationPromotionTemplateOperForm";
    }

    /**
     * 添加修改运营推广信息
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "operationPromotionTemplateOper1")
    public String operationPromotionTemplateOper1(OperationPromotionTemplateVo vo,HttpServletRequest request) {
        vo.setInfo2(StringEscapeUtils.unescapeHtml4(vo.getInfo2()));
        vo.setInfo2(vo.getInfo2().replace("<p>", "").replace("\r\n\t", "").replace("amp;", "").replace("</p>", "").replace("\"", "'"));
        vo.setInfo3(DateUtils.StrToDate(vo.getInfo3s(),"date"));
        vo.setInfo4(DateUtils.StrToDate(vo.getInfo4s(), "date"));
        vo.setInfo5(DateUtils.StrToDate(vo.getInfo5s(),"time"));
        vo.setInfo6(DateUtils.StrToDate(vo.getInfo6s(), "time"));
        try {
            operationPromotionTemplateService.operationPromotionTemplateOper(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:" + adminPath + "/operationPromotion/operationPromotionTemplateList?type="+vo.getType();
    }

    /**
     * 添加修改运营推广信息
     * @param
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "operationPromotionTemplateOper")
    public String operationPromotionTemplateOper(OperationPromotionTemplateVo vo,HttpServletRequest request) {
        net.sf.json.JSONObject result = new net.sf.json.JSONObject();
        if(StringUtils.isNotNull(request.getParameter("info11")) || StringUtils.isNotNull(request.getParameter("info12"))){
            vo.setInfo1(request.getParameter("info11") + "," + request.getParameter("info12"));
        }
        if(StringUtils.isNotNull(request.getParameter("info21")) || StringUtils.isNotNull(request.getParameter("info22"))){
            vo.setInfo2(request.getParameter("info21") + "," + request.getParameter("info22"));
        }
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
     * 添加修改运营推广
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
        return "redirect:" + adminPath + "/operationPromotion/operationPromotionTemplateList?repage";
    }
}
