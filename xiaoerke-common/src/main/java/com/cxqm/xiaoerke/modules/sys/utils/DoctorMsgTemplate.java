package com.cxqm.xiaoerke.modules.sys.utils;

import com.cxqm.xiaoerke.common.bean.WechatArticle;
import com.cxqm.xiaoerke.common.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by wangbaowei on 15/12/20.
 * 医生消息提醒
 */
public class DoctorMsgTemplate {



  public static void doctorAppointmentRemindAtNight2Sms(String doctorPhone,String doctorName,String num,String nameList){
    String content = "【接诊一览】尊敬的"+doctorName+"医生，明天已有"+num+"名宝宝预约您的门诊："+nameList+"。若您因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利！";
    SMSMessageUtil.sendMsg(doctorPhone,content);
  }

  public static void doctorAppointmentRemindAtNight2Wechat(String date,String num,String nameList,String token,String url,String openid){
      WechatMessageUtil.templateModel("明日接诊一览", date, num, nameList, "", "若您因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利。", token, url, openid, WechatMessageUtil.DOC_APPINTMENT_REMIND_ATNIGHT);
  }


  public static void doctorAppointmentRemindAtMoning2Sms(String doctorPhone,String doctorName,String num,String nameList){
      String content = "【接诊一览】尊敬的"+doctorName+"医生，今天已有"+num+"名宝宝预约您的门诊："+nameList+"。若您因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利！";
      SMSMessageUtil.sendMsg(doctorPhone, content);
  }

  public static void doctorAppointmentRemindAtMoning2Wechat(String date,String num,String nameList,String token,String url,String openid){
    WechatMessageUtil.templateModel("今日新增患者", date, num, nameList, "", "若您因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利。", token, url, openid, WechatMessageUtil.DOC_APPINTMENT_REMIND_ATNIGHT);
  }

  public static void doctorAppointmentRemindAt5minLater2Sms(String doctorName,String babyName,String doctorPhone,String date,String week,String beginTime,String hostpitalName,String userPhone){
      String content =  "【预约成功】尊敬的"+doctorName+"医生，"+babyName+"小朋友家长（"+userPhone+"）预约了您"+date+" ("+week+")"+beginTime+"在"+hostpitalName+"的门诊。若您因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利！";
      SMSMessageUtil.sendMsg(doctorPhone, content);
  }

  public static void doctorAppointmentRemindAt5minLater2Wechat(String doctorName,String content,String date,String token,String url,String openid){
      WechatMessageUtil.templateModel("预约成功通知", content, date, "", "", "若您因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利。", token, url, openid, WechatMessageUtil.DOC_APPINTMENT_SUCCESS);
  }

  public static void cancelDoctorAppointment2Sms(String doctorName,String babyName,String phone,String date,String week,String beginTime,String hostpitalName,String userPhone){
    String content = "【取消预约】尊敬的"+doctorName+"医生，"+babyName+"小朋友家长（"+userPhone+"）取消了您"+date+"("+week+")"+beginTime+" 在"+hostpitalName+"的门诊。";
    SMSMessageUtil.sendMsg(phone, content);
  }

  public static void cancelDoctorAppointment2Wechat(String content,String date,String token,String url,String openid){
      WechatMessageUtil.templateModel("取消预约通知", content, date, "", "", "若您因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利。", token, url, openid, WechatMessageUtil.DOC_APPINTMENT_CANCEL);
    }

   public static void dotorAppointmentBill2Sms(String phone,String doctorName,String income){
     String content = "【今日账单】尊敬的"+doctorName+"医生，您今日的接诊收入："+income+"元。如有任何疑问，请尽快与我们取得联系，谢谢您的支持！";
     SMSMessageUtil.sendMsg(phone, content);
   }

    public static void dotorAppointmentBill2Wechat(String date,String income,String token,String url,String openid){
//        2015年9月29日（星期二）
        WechatMessageUtil.templateModel("今日账单提醒", date, income + "元", "", "", "", token, url, openid, WechatMessageUtil.DOC_APPINTMENT_BILL);
    }

    public static void doctorAppointmentNestWeek2Sms(String phone,String doctorName,String date){
        String content = "（每周预报）尊敬的"+doctorName+"医生，您下周"+date+"有出诊时刻安排。可登录微信公众号宝大夫专家版查看详细安排。如有变化，请联系客服：400-623-7120。";
        SMSMessageUtil.sendMsg(phone, content);
    }

    public static void doctorAppointmentNestWeek2Wechat(String token,String url,String openid,String content,String date){
        WechatMessageUtil.templateModel("每周预报", content, date, "", "", "若您因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利。", token, url, openid, WechatMessageUtil.DOC_APPINTMENT_CANCEL);
    }


    /*
    电话咨询
     */
    public static void doctorPhoneonsultRemindAtNight2Sms(String doctorPhone,String doctorName,String num,String nameList){
        String content = "【电话咨询一览】尊敬的"+doctorName+"医生，明天已有"+num+"名宝宝预约您的电话咨询："+nameList+"。若您因特殊情况不能接听，请联系客服：400-623-7120。可登录微信公众号宝大夫专家版查看患者病情资料。宝大夫祝您工作顺利！";
        SMSMessageUtil.sendMsg(doctorPhone,content);
    }

    public static void doctorPhoneonsultRemindAtNight2Wechat(String date,String num,String nameList,String token,String url,String openId){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("电话咨询提醒");
        article.setDescription(date + "\n明日电话咨询一览\n接诊人数：" + num + "\n患者名单："+ nameList + "。"
                + "\n若您因特殊情况不能接听，请联系客服：400-623-7120。宝大夫祝您工作顺利！\n详情>");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }


    public static void doctorPhoneonsultRemindAtMoning2Sms(String doctorPhone,String doctorName,String num,String nameList){
        String content = "【电话咨询一览】尊敬的"+doctorName+"医生，今天已有"+num+"名宝宝预约您的电话咨询："+nameList+"。因特殊情况不能接听，请联系客服：400-623-7120。可登录微信公众号宝大夫专家版查看患者病情资料。宝大夫祝您工作顺利！";
        SMSMessageUtil.sendMsg(doctorPhone, content);
    }

    public static void doctorPhoneonsultRemindAtMoning2Wechat(String date,String num,String nameList,String token,String url, String openId){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("电话咨询提醒");
        article.setDescription(date + "\n今日新增电话咨询\n接诊人数：" + num + "\n患者名单："+ nameList + "。"
                + "\n若您因特殊情况不能接听，请联系客服：400-623-7120。宝大夫祝您工作顺利！\n详情>");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }


    public static void doctorPhoneonsultRemindAt5minLater2Sms(String doctorName,String babyName,String doctorPhone,String date,String week,String beginTime){
        String content =  "【电话咨询预约成功】尊敬的"+doctorName+"医生，"+babyName+"小朋友家长预约了您"+date+" ("+week+")"+beginTime+"的电话咨询。" +
                "若您因特殊情况不能接听，请联系客服：400-623-7120。宝大夫祝您工作顺利！";
        SMSMessageUtil.sendMsg(doctorPhone, content);
    }

    public static void doctorPhoneonsultRemindAt5minLater2Wechat(String babyName,String date,String week,String beginTime,String token,String url,String openId){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("预约成功电话咨询");
        article.setDescription(date + "\n预约成功通知\n详细信息：" + babyName +"小朋友家长预约了您"+date+" ("+week+")"+beginTime+"的电话咨询。" +
                "若您因特殊情况不能接听，请联系客服：400-623-7120。宝大夫祝您工作顺利！");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }

    }


    public static void cancelDoctorPhoneonsult2Sms(String doctorName,String babyName,String phone,String date,String week,String beginTime){
        String content = "【电话咨询取消预约】尊敬的"+doctorName+"医生，"+babyName+"小朋友家长取消了您"+date+"("+week+")"+beginTime+"的电话咨询。";
        SMSMessageUtil.sendMsg(phone, content);
    }

    public static void cancelDoctorPhoneonsult2Wechat(String babyName,String date,String week,String beginTime,String token,String url,String openId){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("取消电话咨询");
        article.setDescription(date + "\n取消预约通知\n详细信息：" + babyName +"小朋友家长取消了您"+date+" ("+week+")"+beginTime+"的电话咨询。" +
                "给您造成的不便，敬请谅解！宝大夫祝您工作顺利！");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }

    }


    public static void doctorPhoneonsultRemindAt5minBefore2Sms(String doctorName,String babyName,String phone,String userPhone){
        String content = "【接听提醒】尊敬的"+doctorName+"医生，"+babyName+"小朋友家长将在5min以后接通电话咨询，到时您会接到号码为"+userPhone+"的来电，请保持电话畅通。在这之前，您可以打开链接，查看患者详细的病情资料http://baod";
        SMSMessageUtil.sendMsg(phone, content);
    }

    public static void doctorPhoneonsultRemindAt5minBefore2Wechat(String babyName,String date,String userPhone, String token,String url,String openId){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("接听提醒");
        article.setDescription(date + "\n咨询接听通知\n详细信息：：" + babyName +"小朋友家长将在5min以后接通电话咨询，到时您会接到号码为"+userPhone+
                "的来电，请保持电话畅通。在这之前，您可以打开链接，查看患者详细的病情资料http://baod\n");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }


    public static void doctorPhoneonsultRemindFail2Sms(String doctorName,String babyName,String phone){
        String content = "【未接通】尊敬的"+doctorName+"医生，由于"+babyName+"小朋友家长预约电话咨询未接通，已将咨询费用退还给预约用户。有疑问请致电400-623-7120。";
        SMSMessageUtil.sendMsg(phone, content);
    }

    public static void doctorPhoneonsultRemindFail2Wechat(String babyName,String date, String token,String url,String openId){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("未接提醒");
        article.setDescription(date + "\n咨询未接通知\n详细信息：由于" + babyName +"小朋友家长预约电话咨询未接通，已将咨询费用退还给预约用户。有疑问请致电400-623-7120。");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }

}
