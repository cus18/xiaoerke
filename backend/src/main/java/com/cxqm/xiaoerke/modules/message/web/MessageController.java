package com.cxqm.xiaoerke.modules.message.web;

import com.cxqm.xiaoerke.common.bean.TemplateData;
import com.cxqm.xiaoerke.common.bean.WxTemplate;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.utils.DateUtil;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatInfoDao;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 消息Controller
 * @author sunxiao
 * @version 2016-3-21
 */
@Controller
@RequestMapping(value = "${adminPath}/message")
public class MessageController extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private WechatInfoDao wechatInfoDao;

    private String COLOR = "#000000";
    
	/**
	 * 发送微信消息表单
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("message:messageForm")
	@RequestMapping(value = "messageForm")
	public String messageForm(MessageVo message,HttpServletRequest request,HttpServletResponse response, Model model) {
		model.addAttribute("messagevo", message);
		return "modules/message/messageForm";
	}
	
	/**
	 * 发送微信消息
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("message:messageForm")
	@RequestMapping(value = "sendWechatMessage")
	public String sendWechatMessage(MessageVo message,HttpServletRequest request,HttpServletResponse response, Model model) {
		Map<String,Object> parameter = systemService.getWechatParameter();
        String token = (String)parameter.get("token");
		String remark = "祝您的宝宝健康成长";
		String templateId = "bRgqyNcnnpZT0A4B1uLCwKculSwuV81vmVXjiVpCETo";
        String errcode = this.templateModel(message.getFirst(), message.getKeyword1(), message.getKeyword2(), message.getKeyword3(), message.getKeyword4(), remark, token, message.getUrl(), message.getOpenId(), templateId);
        model.addAttribute("messagevo", message);
		model.addAttribute("message", "0".equals(errcode)?"消息发送成功！":"消息发送失败："+errcode);
		return "modules/message/messageForm";
	}
	
	private String templateModel(String first,String keyword1,String keyword2,String keyword3,String keyword4,String remark,String token,String url,String openid,String templateId){
        WxTemplate t = new WxTemplate();
        if(StringUtils.isNotNull(url))t.setUrl(url);
        t.setTouser(openid);
        t.setTopcolor(COLOR);
        t.setTemplate_id(templateId);
        Map<String,TemplateData> m = new HashMap<String,TemplateData>();

        TemplateData templateData = new TemplateData();
        templateData.setColor(COLOR);
        templateData.setValue(first);
        m.put("first", templateData);

        if(StringUtils.isNotNull(keyword1)){
            templateData = new TemplateData();
            templateData.setColor(COLOR);
            templateData.setValue(keyword1);
            m.put("keyword1", templateData);
        }

        if(StringUtils.isNotNull(keyword2)){
            templateData = new TemplateData();
            templateData.setColor(COLOR);
            templateData.setValue(keyword2);
            m.put("keyword2", templateData);
        }

        if(StringUtils.isNotNull(keyword3)){
            templateData = new TemplateData();
            templateData.setColor(COLOR);
            templateData.setValue(keyword3);
            m.put("keyword3", templateData);
        }

        if(StringUtils.isNotNull(keyword4)){
            templateData = new TemplateData();
            templateData.setColor(COLOR);
            templateData.setValue(keyword4);
            m.put("keyword4", templateData);
        }

        if(StringUtils.isNotNull(remark)){
            templateData = new TemplateData();
            templateData.setColor(COLOR);
            templateData.setValue(remark);
            m.put("remark", templateData);
        }
        t.setData(m);

        String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" +
                token + "", "POST", JSONObject.fromObject(t).toString());
        JSONObject jsonObject = JSONObject.fromObject(jsonobj);
        if(jsonobj!=null){
            if("0".equals(jsonObject.getString("errcode"))){
                System.out.println("发送模板消息成功！");
            }else{
                System.out.println(jsonObject.getString("errcode"));
            }
        }
		return jsonObject.getString("errcode");
    }

    /**
     * 客服未回复用户列表
     * sunxiao
     * @param
     * @param model
     * @return
     */
    @RequiresPermissions("message:messageForm")
    @RequestMapping(value = "noAnswerWechatRecordList")
    public String noAnswerWechatRecordList(MessageVo message,HttpServletRequest request,HttpServletResponse response, Model model) {
        Map<String, Object> param = new HashMap<String, Object>();
        if(StringUtils.isNotNull(message.getFromTime())){
            param.put("fromTime", DateUtils.DateToStr(new Date(), "date")+" "+message.getFromTime());
        }else{
            param.put("fromTime",new Date(new Date().getTime()-3*60*1000));
        }
        if(StringUtils.isNotNull(message.getToTime())){
            param.put("toTime",DateUtils.DateToStr(new Date(), "date")+" "+message.getToTime());
        }else{
            param.put("toTime",new Date());
        }
        List<Map<String,Object>> list =  wechatInfoDao.findNoAnswerUserList(param);
        /*List<String> list =  wechatInfoDao.getUsersOpenIdList(param);
        List<Map<String,Object>> lists = new ArrayList<Map<String, Object>>();
        for(String temp : list){
            param.put("openId",temp);
            List<Map<String,Object>> mapList = wechatInfoDao.getNoAnswerUserList(param);
            boolean flag = true;
            int j=0;
            for(int i=0;i<mapList.size();i++){
                if("2002".equals(mapList.get(i).get("opercode"))){
                    flag = false;
                    break;
                }
                j=i;
            }
            if(flag){
                lists.add(mapList.get(j));
            }
        }*/
        model.addAttribute("lists", list);
        model.addAttribute("vo", message);
        return "modules/message/noAnswerWechatRecordList";
    }
}
