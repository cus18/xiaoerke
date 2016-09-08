/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取两个日期之间差值（单位，毫秒）
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceMillisecondOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime);
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}

	/**
	 * 获取过去时间
	 * @param date
	 * @return
	 */
	public static long pastMillisSecond(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t;
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }


	/**
	 * 毫秒转换成日期
	 * @param timeMillis
	 * @return
	 */
	public static String timeMillisToDate(long timeMillis){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeMillis);
		return formatter.format(calendar.getTime());
	}



		/**
         * 获取两个日期之间的天数
         *
         * @param before
         * @param after
         * @return
         */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 获取两个日期之间的分钟数
	 *
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getMinuteOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 );
	}
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
	}
	/**
	 * 获取周几
	 */
	public static String getDayWeek(int week) {
		if (week == 1) {
			return "周一";
		} else if (week == 2) {
			return "周二";
		} else if (week == 3) {
			return "周三";
		} else if (week == 4) {
			return "周四";
		} else if (week == 5) {
			return "周五";
		} else if (week == 6) {
			return "周六";
		} else if (week == 7) {
			return "周日";
		} else {
			return "";
		}
	}

	/**
	 * 判断当前日期是星期几
	 *
	 * @param pTime 修要判断的时间
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 日期转换成字符串
	 *@author zdl
	 * @param date
	 * @return str
	 */
	public static String DateToStr(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}


	/**
	 * @author zdl
	 * 格式化日期
	 * @param params
	 * @return
	 */
	public static Date formatDate(@RequestBody Map<String, Object> params) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		try {
			date = sdf.parse((String) params.get("date"));
		} catch (ParseException e) {
			e.printStackTrace();
		}


		return date;
	}


		/**
         * @author zdl
         * String to date
         * @param StringDate
         * @return
         */
	public static Date formatDateString(String StringDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		try {
			date = sdf.parse(StringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * @author zdl
	 * Date to Date
	 */
	public static Date formatDateToDate(Date date, Object... pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd hh:mm:ss");
		}
		try {
			date = sdf.parse(formatDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
	public static Date StrToDate(String str,String flag) {
		SimpleDateFormat format = null;
		if("time".equals(flag)){
			format = new SimpleDateFormat("HH:mm");
		}else if("date".equals(flag)) {
			format = new SimpleDateFormat("yyyy-MM-dd");
		}else if("datetime".equals(flag)){
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else if("xiangang".equals(flag)){
			format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		}else if("datetimesec".equals(flag)){
			format = new SimpleDateFormat("yyyyMMddHHmmss");
		}else{
			format = new SimpleDateFormat(flag);
		}
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
    
    /**
     * 日期转换成字符串
     *
     * @param str
     * @return date
     */
    public static String DateToStr(Date date,String flag) {
    	SimpleDateFormat format = null;
    	if("time".equals(flag)){
    		format = new SimpleDateFormat("HH:mm");
    	}else if("date".equals(flag)) {
    		format = new SimpleDateFormat("yyyy-MM-dd");
    	}else if("datetime".equals(flag)){
    		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	}else if("monthDate".equals(flag)){
			format = new SimpleDateFormat("MM/dd");
		}
        String dateStr = null;
    	dateStr = format.format(date);
        return dateStr;
    }

	public static String getWeekOfDate(Date dt) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static String formatDateToStr(Date date,String formatStr) {
		SimpleDateFormat format = null;
		format = new SimpleDateFormat(formatStr);
		String dateStr = null;
		dateStr = format.format(date);
		return dateStr;
	}
    

}
