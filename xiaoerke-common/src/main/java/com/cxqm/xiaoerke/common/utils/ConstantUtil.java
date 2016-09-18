package com.cxqm.xiaoerke.common.utils;

import com.cxqm.xiaoerke.common.config.Global;

public enum ConstantUtil {

	INSTANCE;

	//小儿科用户端微信参数
	public static final String DOCTORCORPID = Global.getConfig("DOCTORCORPID");
	public static final String DOCTORSECTET = Global.getConfig("DOCTORSECTET");

	//小儿科医生端微信参数
	public static final String CORPID = Global.getConfig("CORPID");
	public static final String SECTET = Global.getConfig("SECTET");

	public static  String AMR_TOMP3_FUNC = Global.getConfig("AMR_TOMP3_FUNC");
	public static  String DOMAIN_VALUE = Global.getConfig("DOMAIN_VALUE");
	public static  String AMR_TOMP3_WINDOWSPATH = Global.getConfig("AMR_TOMP3_WINDOWSPATH");
	public static  String AMR_TOMP3_WINDOWSPATHTEMP = Global.getConfig("AMR_TOMP3_WINDOWSPATHTEMP");
	public static  String AMR_TOMP3_LINUXPATH = Global.getConfig("AMR_TOMP3_LINUXPATH");
	public static  String SERVER_ADDRESS = Global.getConfig("SERVER_ADDRESS");

	//初始化
	public static String APP_ID = Global.getConfig("APP_ID");
	public static String PARTNER = Global.getConfig("PARTNER");
	public static String PARTNER_KEY = Global.getConfig("PARTNER_KEY");
	public static String GATEURL = Global.getConfig("GATEURL");
	public static String TRANSFERS = Global.getConfig("TRANSFERS");
	public static String PAYRESULT = Global.getConfig("PAYRESULT");
	public static String SIGN_METHOD = Global.getConfig("SIGN_METHOD");

	public static String NOTIFY_APPOINT_URL = Global.getConfig("NOTIFY_APPOINT_URL");
	public static String NOTIFY_INSURANCE_URL = Global.getConfig("NOTIFY_INSURANCE_URL");
	public static String NOTIFY_CUSTOMER_URL = Global.getConfig("NOTIFY_CUSTOMER_URL");
	public static String NOTIFY_CONSULTPHONE_URL = Global.getConfig("NOTIFY_CONSULTPHONE_URL");
	public static String NOTIFY_UMBRELLA_URL = Global.getConfig("NOTIFY_UMBRELLA_URL");
	public static String NOTIFY_LOVEPLAN_URL = Global.getConfig("NOTIFY_LOVEPLAN_URL");
	public static String NOTIFY_DOCTORCONSULTPAY_URL = Global.getConfig("NOTIFY_DOCTORCONSULTPAY_URL");

	public static String DAY_QUARTER_NUMBER = Global.getConfig("DAY_QUARTER_NUMBER");
	public static String VISIT_INTERVAL = Global.getConfig("VISIT_INTERVAL");
	public static String MARKET_WEB_URL = Global.getConfig("MARKET_WEB_URL");
	public static String WISDOM_WEB_URL = Global.getConfig("WISDOM_WEB_URL");
	public static String ANGEL_WEB_URL = Global.getConfig("ANGEL_WEB_URL");
	public static String KEEPER_WEB_URL = Global.getConfig("KEEPER_WEB_URL");
	public static String DOCTOR_WEB_URL = Global.getConfig("DOCTOR_WEB_URL");
	public static String TITAN_WEB_URL = Global.getConfig("TITAN_WEB_URL");
	//咨询支付
	public static String NO_PAY = "noPay";
	public static String NOT_INSTANT_CONSULTATION = "notInstantConsultation";
	public static String PAY_SUCCESS = "paySuccess";
	public static String USE_TIMES = "useTimes";
	public static String WITHIN_24HOURS = "within24Hours";

	public static String DISTRIBUTOR = "distributor";
	public static String CONSULTDOCTOR = "consultDoctor";


	public static String BABYCOIN = Global.getConfig("BABY_COIN");//扫码送宝宝币初始值
	public static String CONSUL_AMOUNT = Global.getConfig("CONSUL_AMOUNT");//购买一次咨询所需付款金额
	public static String ONCE_CONSULT_NEED_BABY_COIN = Global.getConfig("ONCE_CONSULT_NEED_BABY_COIN");//购买一次咨询所需宝宝币数


	public static String MAX_INSTANT_CONSULT_NUM = Global.getConfig("MAX_INSTANT_CONSULT_NUM");

	public static String MAX_INSTANT_CONSULT_START_TIME = Global.getConfig("MAX_INSTANT_CONSULT_START_TIME");
	public static String MAX_INSTANT_CONSULT_END_TIME = Global.getConfig("MAX_INSTANT_CONSULT_END_TIME");


}
