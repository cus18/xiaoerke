package com.cxqm.xiaoerke.modules.sys.utils;

import com.cxqm.xiaoerke.common.bean.WechatArticle;
import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import net.sf.json.JSONObject;

import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by wangbaowei on 15/12/7.
 */
public class PatientMsgTemplate {

    /**
     *预约成功 --短信
     * @param phone 用户电话
     *@param babyName 宝宝姓名
     *@param doctorName 医生姓名
     *@param visitDate 就诊日期
     *@param week 就诊周
     *@param beginTime 开始时间
     *@param endTime 结束时间
     *@param hospitalAddress 医院地址
     *@param hospitalName 医院名称
     *@param consultingAddress 诊室地址
     *@param url 短信链接地址
     * */
    public static void appointmentSuccess2Sms(String phone,String babyName,String doctorName,String visitDate,String week,String beginTime,String endTime,String hospitalAddress,String hospitalName,String consultingAddress,String url){
      String content = "（预约成功）"+babyName+"小朋友家长，您已成功预约"+doctorName+"医生，"+visitDate+"（"+week+"）"+beginTime+"~"+endTime+"，"+hospitalAddress+" "+hospitalName+" "+consultingAddress+"。客服电话：400-623-7120，详情请点击"+getShortUrl(url);
      SMSMessageUtil.sendMsg(phone,content);
    };

    /**
     * 黄牛提醒 --短信
     * 统计3个月内，同一个微信名预约4个不同的宝宝
     * */
    public static void DealerRemind(String phone){
      String content =  "温馨提示：宝大夫平台目前免费，为保障您的权益，请勿通过黄牛预约挂号。如需帮助，请关注微信公众号（宝大夫），或致电400-623-7120。";
      SMSMessageUtil.sendMsg(phone, content);
    };

    /**
     *预约成功 --微信
     *用户预约即推送,采用多客服接口
     *@param babyName 宝宝姓名
     *@param doctorName 医生姓名
     *@param visitDate 就诊日期
     *@param week 就诊周
     *@param beginTime 开始时间
     *@param endTime 结束时间
     *@param hospitalAddress 医院地址
     *@param hospitalName 医院名称
     *@param consultingAddress 诊室地址
     *@param url 短信链接地址
     *@param openId 用户微信唯一标示
     *@param token access_token
     * */
    public static void appointmentSuccess2Wechat(String babyName,String doctorName,String visitDate,String week,String beginTime,String endTime,String hospitalAddress,String hospitalName,String consultingAddress,String register_no,String openId,String token ,String url){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("预约成功通知");
        article.setDescription("医生：" + doctorName + "\n时间：" + visitDate + week + beginTime + "-" + endTime + "\n地点：" + hospitalAddress + hospitalName + "" + consultingAddress + "\n订单号:" + register_no + "\n点击即可查看'详细路线'和'见医生流程'\n\n如需取消预约,点击进入即可");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    };

    /**
     * 见医生须知 ---微信
     * 用户预约即推送,采用多客服接口
     * @param openId 用户微信的唯一标示
     * @param token 微信的access_token
     * */
    public static void seeDoctorNeedToKnow2Wechat(String openId,String token,String url,String doctorName,String visitDate,String week,String beginTime,String endTime,String hospitalAddress,String hospitalName,String consultingAddress,String register_no){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("见医生须知");
        article.setDescription("医生：" + doctorName + "\n时间：" + visitDate + week + beginTime + "-" + endTime + "\n地点：" + hospitalAddress + hospitalName + "" + consultingAddress + "\n订单号:" + register_no + "\n如需取消预约,点击进入即可");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }

    /**
     * 交通提醒 ---微信
     * 用户预约即推送,采用多客服接口
     * @param openId 用户微信的唯一标示
     * @param token 微信的access_token
     * @param contetn 交通信息
     * @param url 交通信息链接地址
     * */
    public static void trafficRemind2Wechat(String openId,String token,String contetn,String url){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("交通提醒");
        article.setDescription(contetn);
        article.setUrl(url);
        obj.add(article);
        if(null!=openId) {
            WechatMessageUtil.senMsgToWechat(token, openId, obj);
        }

    }

    /**
     * 评价提醒 ---微信
     * 采用微信模板
     * @param openid 用户微信的唯一标示
     * @param token 微信的access_token
     * @param url 模板链接地址
     * @param title 模板标题
     * @param keyword1 订单编号
     * @param keyword2 订单时间
     * @param remark 详情描述
     * */
    public static void evaluationRemind2Wechat(String openid,String token,String url,String title,String keyword1,String keyword2,String remark){
        WechatMessageUtil.evaluateRemind(title, keyword1, keyword2, remark, token, url, openid);
    }

    /**
     * 评价提醒 ---短信
     * 采用微信模板
     * @param phone 用户手机号
     * @param babyName 宝宝姓名
     * @param doctorName 医生姓名
     * */
    public static void evaluationRemind2Sms(String phone,String babyName,String doctorName){
        String content = babyName+"小朋友家长您好，感谢您使用宝大夫预约"+doctorName+"医生见面咨询，请登录宝大夫完成评价！";
        SMSMessageUtil.sendMsg(phone, content);
    }


    /**
     * 提现通知 ---微信
     * @param openid 用户微信的唯一标示
     * @param token 微信的access_token
     * */
    public void withdrawalsNotice2Wechat(String openid,String token){

    }

    /**
     * 提现成功 ---微信
     * @param openId 用户微信的唯一标示
     * @param token 微信的access_token
     * @param url 模板链接
     * */
    public static void withdrawalsSuccess2Wechat(String openId,String token,String url,String returnMoney,String returnDate){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("【提现成功通知 】");
        article.setDescription("您的提现资金已到账" + "\n提现金额：" + returnMoney +  "\n提现时间：" + returnDate +  "\n\n您的提现已经付款到您的微信钱包，请注意查收（到账时间以微信钱包查询结果为准）" );
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }

    /**
     * 取消通知 ---微信
     * @param openId 用户微信的唯一标示
     * @param token 微信的access_token
     * @param content 内容
     * @param url 链接地址
     *医生
     *时间
     *地点
     *地点
     * */
    public static void cancelNotice2Wechat(String openId,String token,String content,String url){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("取消预约");
        article.setDescription(content);
        article.setUrl(url);
        obj.add(article);
        if(null!=openId) {
            WechatMessageUtil.senMsgToWechat(token, openId, obj);
        }

    }


    /**
     * 取消通知 ---微信
     *@param phone 用户电话
     * @param doctorName 医生姓名
     * @param date 预约日期
     * @param week 周
     * @param beginTime 开始时间
     *医生
     *时间
     *地点
     *地点
     * */
    public static void cancelNotice2Sms(String phone,String doctorName,String date,String week,String beginTime){
      String content = "（取消预约）您已取消"+doctorName+"医生"+date+"（"+week+")"+beginTime+"的预约，如有疑问请致电400-623-7120。";
      SMSMessageUtil.sendMsg(phone, content);
    }


    /**
     * 诊后医嘱提醒 ---微信
     * @param openid 用户微信的唯一标示
     * @param token 微信的access_token
     * @param doctorName 医生姓名
     * @param content 医生叮嘱
     * @param url 模板链接
     * */
    public static void afterDiagnosisRemind2Wechat(String openid,String token,String doctorName,String content,String url){
        WechatMessageUtil.templateModel("您好，宝大夫医生给您发来一份医嘱提醒哦！", doctorName, content, "", "", "", token, url, openid, WechatMessageUtil.PATIENT_AFTERDIAGNOSIS);
    }

    /**
     * 分享提醒 ---微信
     * @param openid 用户微信的唯一标示
     * @param token 微信的access_token
     * */
    public static void shareRemind2Wechat(String openid,String token,String babyName,String doctorName,String url){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("【分享提醒】");
        article.setDescription(babyName+"小朋友家长您好,感谢你使用宝大夫预约" + doctorName + "医生见面咨询,点击后即可将此医生分享给其他好友哦,立即行动吧!");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openid))
        {
            WechatMessageUtil.senMsgToWechat(token,openid, obj);
        }
    }


    /**
     * --微信(电话咨询)
     */

    /**
     * 预约成功 --微信
     * @param doctorName 医生姓名
     * @param date 接听日期
     * @param week 接听周
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param phone 接听电话
     * @param register_no 订单号
     * @param openId
     * @param token
     * @param url
     */
    public static void consultPhoneSuccess2Wechat(String doctorName,String date,String week,String beginTime,String endTime,String phone,String register_no,String openId,String token ,String url){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("电话咨询预约成功通知");
        article.setDescription("医生：" + doctorName + "\n时间：" + date + week + beginTime + "-" + endTime + "\n接听电话：" + phone + "\n订单号:" + register_no + "\n\n\n查看全文");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }

    public static void consultPhoneSuccess2Msg(String babyName,String doctorName,String date,String week,String beginTime,String phone,String register_no){
        String content = "（电话咨询预约成功）"+babyName+"小朋友家长，您已成功预约了"+doctorName+"医生的电话咨询，预约时间是"+date+" "+week+" "+beginTime+"，届时您会接到号码为010-4006237120的来电请保持电话畅通。订单号："+register_no+"，有疑问请致电400-623-7120。";
        SMSMessageUtil.sendMsg(phone, content);
    }


    /**
     * 取消电话咨询 --微信
     * @param doctorName 医生姓名
     * @param date 接听日期
     * @param week 接听周
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param phone 接听电话
     * @param register_no 订单号
     * @param price 咨询费
     * @param openId
     * @param token
     * @param url
     */
    public static void consultPhoneCancel2Wechat(String doctorName,String date,String week,String beginTime,String endTime,String phone,String register_no,String price, String openId,String token ,String url){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("取消电话咨询");
        article.setDescription("医生：" + doctorName + "\n时间：" + date + week + beginTime + "-" + endTime + "\n接听电话：" + phone + "\n订单号:" + register_no + "（已取消）\n" + price +"元咨询费用已退还至您的宝大夫账户\n\n查看全文");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }

    /**
     * 退款成功
     * @param register_no 订单号
     * @param price 退款金额
     * @param openId
     * @param token
     * @param url
     */
    public static void consultPhoneRefund2Wechat(String register_no,String price, String openId,String token ,String url){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("退款成功通知");
        article.setDescription("由于电话未接通，已将咨询费用退还至您的宝大夫账户。\n订单号:" + register_no + "\n服务项目：电话咨询\n退款金额:" + price + "元\n\n如有疑问，请联系客服400-623-7120。\n\n查看全文");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }

    /**
     * 电话咨询提醒 --微信
     * @param doctorName 医生姓名
     * @param date 接听日期
     * @param week 接听周
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param phone 接听电话
     * @param register_no 订单号
     * @param openId
     * @param token
     * @param url
     */
    public static void consultPhoneWaring2Wechat(String doctorName,String date,String week,String beginTime,String endTime,String phone,String register_no,String openId,String token ,String url){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("电话咨询提醒");
        article.setDescription("医生：" + doctorName + "\n时间：" + date + week + beginTime + "-" + endTime + "\n接听电话：" + phone + "\n订单号:" + register_no + "\n您预约的常丽医生的电话咨询将在5分钟后进行通话，请保持手机畅通。\n\n查看全文");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }

    /**
     * 订单评价提醒
     * @param register_no 订单编号
     * @param date 订单时间
     * @param openId
     * @param token
     * @param url
     */
    public static void consultPhoneEvaluateWaring2Wechat(String register_no,String date, String openId,String token ,String url){
        ArrayList<Object> obj = new ArrayList<Object>();
        WechatArticle article = new WechatArticle();
        article.setTitle("订单评价提醒");
        article.setDescription("您的电话咨询订单可以进行评价了哦！\n订单号:" + register_no + "\n订单时间:" + date + "\n\n｛宝宝姓名｝小朋友家长您好，感谢您使用宝大夫预约｛医生姓名｝医生的电话咨询，点击后即可进行评价！\n\n查看全文");
        article.setUrl(url);
        obj.add(article);
        if(StringUtils.isNotNull(openId))
        {
            WechatMessageUtil.senMsgToWechat(token,openId, obj);
        }
    }

    /**
     * 将网页链接通过百度转化为短连接
     * @param url 网页地址
     * @return 转换后的短连接地址
     * */
    public static String getShortUrl(String url){
      String result = HttpRequestUtil.httpPost("url=" + url, "http://dwz.cn/create.php");
      JSONObject shortUrl=JSONObject.fromObject(result);
      return (String)shortUrl.get("tinyurl");
    }


}
