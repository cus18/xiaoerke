package com.cxqm.xiaoerke.modules.sys.utils;

import com.cxqm.xiaoerke.common.bean.TemplateData;
import com.cxqm.xiaoerke.common.bean.WxTemplate;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.common.utils.JsonUtil;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信消息目前主要包含是模板消息以及微信多客服消息
 * Created by wangbaowei on 15/11/30
 */
public class WechatMessageUtil {

    //接诊提醒消息模板id
    protected static final String ADMISSION_REMIND = "Y35f0JnXhPs9e22dGEaw6NF8qlC2hnGARoqHFJdaN5g";
    //评价订单提醒模板id
    protected static final String ORDER_EVALUATION = "NBPM7A-baR6cij7xrggjNe2GD3sf2MPmjZX2b0AC-f0";
    //付费用户管理通知模板id
    protected static final String MANAGER_REMIND = "jSCx1TKBj2Hx8E3HGha4U4BqKufy7E-oIWjklQsx2ko";

    protected static final String PATIENT_AFTERDIAGNOSIS = "QB9WyNC-arwZgPkl9JFYNFKp2NbtjV-UlS0MLHD2m6I";

    //医生端今日账单提醒
    protected static final String DOC_APPINTMENT_BILL= "IvN_yVJ4ysfEGSsv3r4EI-kE0IOM40ek5d7j8rGtRE8";

    //医生端明天账单提醒
    protected static final String DOC_APPINTMENT_REMIND_ATNIGHT= "IvN_yVJ4ysfEGSsv3r4EI-kE0IOM40ek5d7j8rGtRE8";

    //医生端预约成功提醒提醒
    protected static final String DOC_APPINTMENT_SUCCESS= "OPENTM207664688";

    //电话咨询未接通提醒
    public static final String CONSULT_PHONE_UNCONNNECT= "mLwO1rHV4xTP3ppNyfMiYNfNBqDShjO5bzZ_eu6aRVg";

    protected  static final String CONSULT_REUTNPAY_SUCCESS = "xG2qnez1yCRX8wwXWUzkROqaaioyJR573q1ZYSY_BdY";

    protected static final String DOC_APPINTMENT_CANCEL= "OPENTM203353498";
    //模板颜色
    private static final String COLOR = "#0000CC";

    /**
     * @param content 消息内容
     * @param token 微信accee_toekn值
     * 向服务管理员发送付费预约用户的微信模板信息
     * */
    public static void sendMsgToManagerForAccount(String content,String token){
        String managerPhone = Global.getConfig("webapp.ManagerOpenid");
        String [] st = managerPhone.split(",");
        for(String str:st){
            templateModel("预约成功","宝大夫",content,"","","请及时查看",token,"",str,"jSCx1TKBj2Hx8E3HGha4U4BqKufy7E-oIWjklQsx2ko");
        }
    }

    /**
     * 公共模板模型
     * @param first 模板标题
     * @param keyword1 keyword1内容
     * @param keyword2 keyword2内容
     * @param keyword3 keyword3内容
     * @param keyword4 keyword4内容
     * @param remark remark详细说明
     * @param token 维系in公众平台token
     * @param url 模板链接地址
     * @param openid 接收消息用户
     * @param templateId 模板id
     *
     * */
    public static void templateModel(String first,String keyword1,String keyword2,String keyword3,String keyword4,String remark,String token,String url,String openid,String templateId){
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
                LogUtils.saveLog("wechatMsgError",jsonobj);
            }
        }
    }

    /**
	 * 接诊提醒
	 * @param first 说明
	 * @param keyword1 接诊时间
	 * @param keyword2 接诊人数
	 * @param keyword3 患者名单
	 * @param remark 注意提示
	 * @param token 微信token
	 * @param url 模板链接地址
	 * */
	public static void receptionReminder(String first,String keyword1,String keyword2,String keyword3,String remark,String token,String url,String openid){
		WxTemplate t = new WxTemplate();
		t.setUrl(url);
		t.setTouser(openid);
		t.setTopcolor("#000000");
		t.setTemplate_id("1joU-7Q4eiqoGsRmZ0zVRvgmlzqTs4jzW50KUfEgXIg");
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();

		TemplateData templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(first);
		m.put("first", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword1);
		m.put("keyword1", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword2);
		m.put("keyword2", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword3);
		m.put("keyword3", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(remark);
		m.put("remark", templateData);

		t.setData(m);
		String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+
				token+"","POST", JSONObject.fromObject(t).toString());
		JSONObject jsonObject = JSONObject.fromObject(jsonobj);
		if(jsonobj!=null){
			if("0".equals(jsonObject.getString("errcode"))){
				System.out.println("发送模板消息成功！");
			}else{
				System.out.println(jsonObject.getString("errcode"));
			}
		}
	}

    /**
     * 评价提醒
     * */
    public static void evaluateRemind(String first,String keyword1,String keyword2,String remark,String token,String url,String openid){
		WxTemplate t = new WxTemplate();
		t.setUrl(url);
		t.setTouser(openid);
		t.setTopcolor("#000000");
		t.setTemplate_id("NBPM7A-baR6cij7xrggjNe2GD3sf2MPmjZX2b0AC-f0");
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();

		TemplateData templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(first);
		m.put("first", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword1);
		m.put("keyword1", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword2);
		m.put("keyword2", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(remark);
		m.put("remark", templateData);

		t.setData(m);
		String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+
				token+"","POST", JSONObject.fromObject(t).toString());
		JSONObject jsonObject = JSONObject.fromObject(jsonobj);
		if(jsonobj!=null){
			if("0".equals(jsonObject.getString("errcode"))){
				System.out.println("发送模板消息成功！");
			}else{
				System.out.println(jsonObject.getString("errcode"));
			}
		}

	}

    /**
     * 微信多客服消息发送接口
     * @param token 微信access_token
     * @param openId 用户的openId
     * @param obj 消息体
     * */
    public static String sendMsgToWechat(String token,String openId,ArrayList<Object> obj){
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+ token;
        try {
            String str = JsonUtil.getJsonStrFromList(obj);
            String json = "{\"touser\":\""+openId+"\",\"msgtype\":\"news\",\"news\":" +
                    "{\"articles\":" +str +"}"+"}";
            System.out.println(json);
            String result = HttpRequestUtil.httpPost(json, url);
            System.out.print(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

}
