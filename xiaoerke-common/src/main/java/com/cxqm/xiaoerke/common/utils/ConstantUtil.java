package com.cxqm.xiaoerke.common.utils;

import com.cxqm.xiaoerke.common.config.Global;

public enum ConstantUtil {
	INSTANCE;
	public static  String SERVER_ADDRESS = Global.getConfig("SERVER_ADDRESS");
	public static String NO_PAY = "noPay";
	public static String NOT_INSTANT_CONSULTATION = "notInstantConsultation";
	public static String PAY_SUCCESS = "paySuccess";
	public static String USE_TIMES = "useTimes";
	public static String WITHIN_24HOURS = "within24Hours";
	public static String DISTRIBUTOR = "distributor";
	public static String CONSULTDOCTOR = "consultDoctor";

}
