package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.MessageContentConfVo;
import com.cxqm.xiaoerke.modules.consult.service.MessageContentConfService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文案配置 Controller
 *
 * @author sunxiao
 * @version 2016-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/messageContentConf")
public class MessageContentConfController extends BaseController {

    @Autowired
    MessageContentConfService messageContentConfService;

    /**
     * 关键字规则列表
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "messageContentConfList")
    public String messageContentConfList(MessageContentConfVo vo , Model model) {
        List<MessageContentConfVo> list = messageContentConfService.findMessageContentConfByInfo(vo);
        model.addAttribute("vo", new MessageContentConfVo());
        model.addAttribute("list", list);
        return "modules/consult/messageContentConfList";
    }

    /**
     * 添加修改规则页面
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "saveUpdateMessageContentConfForm")
    public String saveUpdateMessageContentConfForm(MessageContentConfVo vo, Model model) {
        MessageContentConfVo returnVo = new MessageContentConfVo();
        if(StringUtils.isNotNull(vo.getId()+"")){
            List<MessageContentConfVo> list = messageContentConfService.findMessageContentConfByInfo(vo);
            returnVo = list.get(0);
            returnVo.setContent("<p>\r\n\t"+returnVo.getContent().replace("\r\n","</p>\r\n<p>\r\n\t")+"</p>");
        }
        model.addAttribute("vo", returnVo);
        return "modules/consult/saveUpdateConfForm";
    }

    /**
     * 添加关键字规则
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "saveMessageContentConf")
    public String saveMessageContentConf(MessageContentConfVo vo,HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
        String[] weekList = request.getParameterValues("weekList");
        String weeks = "";
        if(weekList == null){
            weeks = "0,";
            vo.setStartTime("00:00");
            vo.setEndTime("23:59");
        }else{
            for(String temp:weekList){
                weeks = weeks+temp+",";
            }
        }
        vo.setWeek(weeks.substring(0, weeks.length() - 1));
        String retString = "内容不能为空！";
        if(StringUtils.isNotNull(vo.getContent())){
            vo.setContent(StringEscapeUtils.unescapeHtml4(vo.getContent()));
            vo.setContent(vo.getContent().replace("<p>","").replace("\r\n\t","").replace("amp;","").replace("</p>","").replace("\"","'"));
            retString = messageContentConfService.saveMessageContentConf(vo);
        }
        model.addAttribute("vo", new MessageContentConfVo());
        addMessage(redirectAttributes, retString);
        return "redirect:" + adminPath + "/messageContentConf/messageContentConfList";
    }

    /**
     * 删除关键字规则
     * sunxiao
     * @param
     * @param model
     */
    @RequestMapping(value = "deleteMessageContentConf")
    public String deleteMessageContentConf(MessageContentConfVo vo,HttpServletRequest request, Model model) {
        messageContentConfService.deleteMessageContentConf(vo);
        return "redirect:" + adminPath + "/messageContentConf/messageContentConfList";
    }

}
