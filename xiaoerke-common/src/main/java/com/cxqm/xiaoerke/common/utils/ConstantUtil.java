package com.cxqm.xiaoerke.common.utils;

public class ConstantUtil {

	//初始化
	public static String APP_ID = "wx0baf90e904df0117";//微信开发平台应用id
	public static String APP_SECRET = "b3dac0be3e1739af01fee0052ea7a68f";//应用对应的凭证
	//应用对应的密钥
	public static String APP_KEY = "L8LrMqqeGRxST5reouB0K66CaYAWpqhAVsq7ggKkxHCOastWksvuX1uvmvQclxaHoYd3ElNBrNO2DHnnzgfVG9Qs473M3DTOZug5er46FhuGofumV8H2FVR9qkjSlC5K";
	public static String PARTNER = "1260344901";//财付通商户号
	public static String PARTNER_KEY = "chenxingqiming00chenxingqiming00";//商户号对应的密钥
	public static String TOKENURL = "https://api.weixin.qq.com/cgi-bin/token";//获取access_token对应的url
	public static String GRANT_TYPE = "client_credential";//常量固定值
	public static String EXPIRE_ERRCODE = "42001";//access_token失效后请求返回的errcode
	public static String FAIL_ERRCODE = "40001";//重复获取导致上一次获取的access_token失效,返回错误码
	public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//获取预支付id的接口url
	public static String TRANSFERS = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";//获取预支付id的接口url
	public static String PAYRESULT = "https://api.mch.weixin.qq.com/pay/orderquery";//获取支付结果
	public static String ACCESS_TOKEN = "access_token";//access_token常量值
	public static String ERRORCODE = "errcode";//用来判断access_token是否失效的值
	public static String SIGN_METHOD = "MD5";//签名算法常量值
	//package常量值
	public static String packageValue = "bank_type=WX&body=%B2%E2%CA%D4&fee_type=1&input_charset=GBK&notify_url=http%3A%2F%2F127.0.0.1%3A8180%2Ftenpay_api_b2c%2FpayNotifyUrl.jsp&out_trade_no=2051571832&partner=1900000109&sign=10DA99BCB3F63EF23E4981B331B0A3EF&spbill_create_ip=127.0.0.1&time_expire=20131222091010&total_fee=1";
	public static String traceid = "wbw565871712";//测试用户id

	public static String NOTIFY_URL = "http://s11.baodf.com/xiaoerke-appoint/ap/user/getPayNotifyInfo";
	public static String NOTIFY_INSURANCE_URL = "http://s3.baodf.com/xiaoerke-insurance-webapp/" +
			"ap/user/getInsurancePayNotifyInfo";
	public static String NOTIFY_CUSTOMER_URL = "http://s3.baodf.com/xiaoerke-insurance-webapp/" +
			"ap/user/getInsurancePayNotifyInfo";

	public static int DAY_QUARTER_NUMBER = 96;
	
	public static int VISIT_INTERVAL = 15;
	//初始化
//	public static String APP_ID = "wxa19496b1076e7352";//微信开发平台应用id
//	public static String APP_SECRET = "f645d4bcf81c905b3ad628cda79bd7ee";//应用对应的凭证
//	//应用对应的密钥
//	public static String APP_KEY = "L8LrMqqeGRxST5reouB0K66CaYAWpqhAVsq7ggKkxHCOastWksvuX1uvmvQclxaHoYd3ElNBrNO2DHnnzgfVG9Qs473M3DTOZug5er46FhuGofumV8H2FVR9qkjSlC5K";
//	public static String PARTNER = "1252031301";//财付通商户号
//	public static String PARTNER_KEY = "chenxingqiming00chenxingqiming00";//商户号对应的密钥
//	public static String TOKENURL = "https://api.weixin.qq.com/cgi-bin/token";//获取access_token对应的url
//	public static String GRANT_TYPE = "client_credential";//常量固定值
//	public static String EXPIRE_ERRCODE = "42001";//access_token失效后请求返回的errcode
//	public static String FAIL_ERRCODE = "40001";//重复获取导致上aa一次获取的access_token失效,返回错误码
//	public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//获取预支付id的接口url
//	public static String TRANSFERS = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";//获取预支付id的接口url
//	public static String PAYRESULT = "https://api.mch.weixin.qq.com/pay/orderquery";//获取支付结果
//	public static String ACCESS_TOKEN = "access_token";//access_token常量值
//	public static String ERRORCODE = "errcode";//用来判断access_token是否失效的值
//	public static String SIGN_METHOD = "MD5";//签名算法常量值
//	//package常量值
//	public static String packageValue = "bank_type=WX&body=%B2%E2%CA%D4&fee_type=1&input_charset=GBK&notify_url=http%3A%2F%2F127.0.0.1%3A8180%2Ftenpay_api_b2c%2FpayNotifyUrl.jsp&out_trade_no=2051571832&partner=1900000109&sign=10DA99BCB3F63EF23E4981B331B0A3EF&spbill_create_ip=127.0.0.1&time_expire=20131222091010&total_fee=1";
//	public static String traceid = "wbw565871712";//测试用户id
//
//	public static String NOTIFY_URL = "http://xiaoxiaoerke.cn/xiaoerke-appoint/ap/user/getPayNotifyInfo";

	public static String BAODF_URL = "http://baodf.com";
	public static String WEB_URL = "http://xiaoxiaoerke.cn";
	public static String S1_WEB_URL = "http://s11.baodf.com/";
	public static String S2_WEB_URL = "http://s22.baodf.com/";
	public static String S3_WEB_URL = "http://s3.baodf.com/";
//	public static String WEB_URL = "http://www.baodf.com";
}
