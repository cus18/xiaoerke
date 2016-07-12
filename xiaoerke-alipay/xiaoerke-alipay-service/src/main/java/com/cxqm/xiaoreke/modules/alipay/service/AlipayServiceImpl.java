package com.cxqm.xiaoreke.modules.alipay.service;

import com.cxqm.xiaoreke.modules.alipay.config.AlipayConfig;
import com.cxqm.xiaoreke.modules.alipay.util.AlipayNotify;
import com.cxqm.xiaoreke.modules.alipay.util.AlipaySubmit;
import com.cxqm.xiaoreke.modules.alipay.util.httpClient.StringUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wangbaowei on 16/7/6.
 */
@Service
@Transactional(readOnly = false)
public class AlipayServiceImpl implements AlipayService {

    public String alipayment(String totleFee,String body,String describe,String showUrl)  {
        String result = "";
        //生成订单日期
        Date date = new Date();
        // 支付类型
        // 必填，不能修改
        String payment_type = "1";
        // 服务器异步通知页面路径
        // 需http://格式的完整路径，不能加?id=123这类自定义参数
        String notify_url = "http://baodaifu.51mypc.cn/hearthBeat/alipay/aliResult";
        // 页面跳转同步通知页面路径
        // 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
        String return_url = "http://baodaifu.51mypc.cn//hearthBeat/alipay/return_url";
        // 商户网站订单系统中唯一订单号，必填
        String out_trade_no = date.getTime() + "";
        // 必填
        describe = "充值测试";
        // 防钓鱼时间戳
        // 若要使用请调用类文件submit中的query_timestamp函数
        String anti_phishing_key = "";
        // 客户端的IP地址
        // 非局域网的外网IP地址，如：221.0.0.1
        String exter_invoke_ip = "";


//        String total_fee = ServletRequestUtils.getStringParameter(request, "amount","");
//        total_fee = "0.01";

//        String body = ServletRequestUtils.getStringParameter(request, "body","test");
        //商品展示地址
//        String show_url = ServletRequestUtils.getStringParameter(request, "show_url","http://www.elve.cn");
        //需以http://开头的完整路径，例如：http://www.xxx.com/myorder.html


        Map<String, String> sParaTemp = new HashMap<String, String>();
//        基本参数
        sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");//接口服务----手机
        sParaTemp.put("partner", AlipayConfig.partner);//支付宝PID
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);//统一编码
        sParaTemp.put("notify_url", notify_url);//异步通知页面
        sParaTemp.put("return_url", return_url);//页面跳转同步通知页面


        sParaTemp.put("out_trade_no", date.getTime()+payment_type);//商品订单编号
        sParaTemp.put("subject", describe);//商品名称
        sParaTemp.put("total_fee", totleFee);//价格
        sParaTemp.put("seller_id", AlipayConfig.partner);//价格
        sParaTemp.put("payment_type", payment_type);//支付类型
        sParaTemp.put("show_url", showUrl);
        sParaTemp.put("body", body);

//        sParaTemp.put("seller_email",AlipayConfig.partner);//卖家支付宝账号
//        sParaTemp.put("anti_phishing_key", anti_phishing_key);
//        sParaTemp.put("exter_invoke_ip", exter_invoke_ip);

        //建立请求
        try {
            String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"post","确认");

//            StringUtil.writeToWeb(sHtmlText, "html", response);
            return sHtmlText;
        } catch (Exception e) {
//            if(log.isErrorEnabled()){
//                log.error("ddddddddddddddddddddd");
//            }
            result = "{\"success\":false,\"msg\":\"跳转失败，请稍候再试！\"}";
//            StringUtil.writeToWeb(result, "html", response);
            return null;
        }
    }

    public String notification(Map requestParams,String out_trade_no,String tradeStatus){
        Map<String,String> params = new HashMap<String,String>();
//        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]: valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
//        String tradeNo = request.getParameter("out_trade_no");
//        String tradeStatus = request.getParameter("trade_status");
        //String notifyId = request.getParameter("notify_id");
        if(AlipayNotify.verify(params)){//验证成功
            if(tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                //要写的逻辑。自己按自己的要求写
//                log.error("ok.......");
//                System.out.println(">>>>>充值成功" + tradeNo);
            }
            return "web/pay/success";
        }else{//验证失败
            return "web/pay/fail";
        }

    }


    @Override
    public void returnAlipay(String detailData,String batchNum,String desc) {
        String notify_url = "http://baodaifu.51mypc.cn/hearthBeat/alipay/aliResult";
        String batch_no = DateFormatUtils.format(new Date(),"yyyyMMddHHmmss")+"32313";
//        String batch_num = "1";
        String detail_data = "2016062321001004690282522940^0.10^协商退款";
        detail_data = detailData+"^"+batchNum+"^"+desc;

        //        基本参数
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", "refund_fastpay_by_platform_pwd");//接口服务----手机
        sParaTemp.put("partner", AlipayConfig.partner);//支付宝PID
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);//统一编码
        sParaTemp.put("notify_url", "");//异步通知页面
        sParaTemp.put("seller_user_id", AlipayConfig.partner);//商品名称
        sParaTemp.put("refund_date", DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));//价格
        sParaTemp.put("batch_no", batch_no);//价格
        sParaTemp.put("batch_num", batchNum);//支付类型
        sParaTemp.put("detail_data", detail_data);
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
//        try {
//            StringUtil.writeToWeb(sHtmlText, "html", response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


}
