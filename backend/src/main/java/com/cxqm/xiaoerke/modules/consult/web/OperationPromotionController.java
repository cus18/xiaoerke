package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionVo;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionService;
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

    /**
     * 关键字规则列表
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "operationPromotionKeywordList")
    public String operationPromotionKeywordList(OperationPromotionVo vo,HttpServletRequest request, Model model) {
        operationPromotionService.findKeywordRoleList(vo);
        model.addAttribute("vo", new OperationPromotionVo());
        return "modules/consult/operationPromotionKeywordList";
    }

    /**
     * 添加关键字规则
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "saveKeywordRole")
    public String saveKeywordRole(OperationPromotionVo vo,HttpServletRequest request, Model model) {
        operationPromotionService.saveKeywordRole(vo);
        model.addAttribute("vo", new OperationPromotionVo());
        return "modules/consult/operationPromotionKeywordList";
    }
}
