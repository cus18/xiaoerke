package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.SendMindCouponVo;
import com.cxqm.xiaoerke.modules.consult.service.SendMindCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 送心意优惠卡 Controller
 *
 * @author sunxiao
 * @version 2016-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/sendMindCoupon")
public class SendMindCouponController extends BaseController {

    @Autowired
    SendMindCouponService sendMindCouponService;

    /**
     * 关键字规则列表
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "sendMindCouponList")
    public String sendMindCouponList(SendMindCouponVo vo , Model model) {
        List<SendMindCouponVo> list = sendMindCouponService.findSendMindCouponByInfo(vo);
        model.addAttribute("vo", new SendMindCouponVo());
        model.addAttribute("list", list);
        return "modules/consult/sendMindCouponList";
    }

    /**
     * 添加修改规则页面
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "saveUpdateSendMindCouponForm")
    public String saveUpdateSendMindCouponForm(SendMindCouponVo vo, Model model) {
        SendMindCouponVo returnVo = new SendMindCouponVo();
        if(StringUtils.isNotNull(vo.getId()+"")){
            List<SendMindCouponVo> list = sendMindCouponService.findSendMindCouponByInfo(vo);
            returnVo = list.get(0);
            returnVo.setImage("http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/sendMindCoupon"+vo.getId());
        }else{
            //returnVo.setPriority("0");
        }
        model.addAttribute("vo", returnVo);
        return "modules/consult/saveUpdateCouponForm";
    }

    /**
     * 添加关键字规则
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "saveSendMindCoupon")
    public String saveSendMindCoupon(SendMindCouponVo vo,HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
        String retString = sendMindCouponService.saveSendMindCoupon(vo);
        model.addAttribute("vo", new SendMindCouponVo());
        addMessage(redirectAttributes, retString);
        return "redirect:" + adminPath + "/sendMindCoupon/sendMindCouponList";
    }

    /**
     * 删除关键字规则
     * sunxiao
     * @param
     * @param
     */
    @RequestMapping(value = "deleteSendMindCoupon")
    public String deleteSendMindCoupon(SendMindCouponVo vo) {
        sendMindCouponService.deleteSendMindCoupon(vo);
        return "redirect:" + adminPath + "/sendMindCoupon/sendMindCouponList";
    }

}
