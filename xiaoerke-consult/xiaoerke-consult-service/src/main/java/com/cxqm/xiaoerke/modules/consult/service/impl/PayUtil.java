package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.JsApiTicketUtil;
import com.cxqm.xiaoerke.common.utils.XMLUtil;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.account.exception.BusinessPaymentExceeption;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import jxl.Sheet;
import jxl.Workbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by wangbaowei on 2017/6/7.
 */
public class PayUtil {

    public static void main(String[] args){

        Map<String,String> openidList = getInfoByExc("/Users/wangbaowei/Desktop/工作/umbrella.xls");

        for (Map.Entry<String, String> entry : openidList.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo =  new SysPropertyVoWithBLOBsVo();
        sysPropertyVoWithBLOBsVo.setAppId("wx0baf90e904df0117");
        sysPropertyVoWithBLOBsVo.setPartner("1260344901");
        sysPropertyVoWithBLOBsVo.setPartnerKey("chenxingqiming00chenxingqiming00");
        sysPropertyVoWithBLOBsVo.setTransfers("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");


        String partner_trade_no = IdGen.uuid();//生成随机字符串
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("mch_appid", sysPropertyVoWithBLOBsVo.getAppId());//APPid
        parameters.put("mchid",sysPropertyVoWithBLOBsVo.getPartner());
        String nonce_str = IdGen.uuid(); //Sha1Util.getNonceStr();//商户订单号
        parameters.put("nonce_str", nonce_str);
        parameters.put("partner_trade_no", partner_trade_no);
        parameters.put("check_name", "NO_CHECK");
        parameters.put("amount","");//金额
        parameters.put("desc", "宝护伞”项目因人数过少，无法起到保障功能，决定将该项目关闭。\n\n给您带来不便敬请谅解~ \n\n如有疑问，请致电15810159447");
        parameters.put("spbill_create_ip", "101.200.192.34");
        parameters.put("openid", "");
        String sign = JsApiTicketUtil.createSign("UTF-8", parameters,sysPropertyVoWithBLOBsVo);
        parameters.put("sign", sign);
        String requestXML = JsApiTicketUtil.getRequestXml(parameters);
        try {
            String result = HttpRequestUtil.clientCustomSSLS(sysPropertyVoWithBLOBsVo.getTransfers(), requestXML,sysPropertyVoWithBLOBsVo);
            Map<String, String> returnMap = XMLUtil.doXMLParse(result);//解析微信返回的信息，以Map形式存储便于取值
            if (!"SUCCESS".equals(returnMap.get("result_code"))) {
                LogUtils.saveLog(Servlets.getRequest(), "00000040", "用户微信提现失败:" + result);//用户微信提现失败
                throw new BusinessPaymentExceeption();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String,String> getInfoByExc(String filePath){
        Workbook rwb;//@RequestBody
        InputStream stream;
        Map<String,String> hospitalInfo = new HashMap<String,String>();
        try {
            stream = new FileInputStream(filePath);
            //获取Excel文件对象
            rwb = Workbook.getWorkbook(stream);
            //获取文件的指定工作表 默认的第一个
            Sheet sheet = rwb.getSheet(0);
            int rowTotalNumber = sheet.getRows();
            //行数(表头的目录不需要，从1开始)
            for (int i = 1; i < rowTotalNumber; i++) {
                int j = 0;
                //获得单元格数据
                hospitalInfo.put(sheet.getCell(j, i).getContents(),sheet.getCell(j+1, i).getContents());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hospitalInfo;
    }
}
