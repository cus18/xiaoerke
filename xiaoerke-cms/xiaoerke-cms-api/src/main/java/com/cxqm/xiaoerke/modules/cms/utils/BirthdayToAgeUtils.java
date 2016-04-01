package com.cxqm.xiaoerke.modules.cms.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;

/**
 * 内容管理工具类
 * @author sunxiao
 * @version 2015-12-01
 */
public class BirthdayToAgeUtils {
	
    private static ServletContext context = SpringContextHolder.getBean(ServletContext.class);

	private static final String CMS_CACHE = "cmsCache";
	
	/**
	 * 根据宝宝的生日计算所属的年龄段
	 * 2015年12月1日 下午4:25:23
	 * @return String
	 * @author sunxiao
	 */
	public static String BirthdayToAge(Date birthday){
		String returnStr = new String();
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd,hh:mm:ss");
		Date current = new Date();
		now.setTime(new Date());
		now.add(Calendar.DATE,-28);
		Date d28 = now.getTime();
		System.out.println(sdf.format(d28));
		
		now.add(Calendar.MONTH, -2);
		Date m3 = now.getTime();
		System.out.println(sdf.format(m3));
		
		now.add(Calendar.MONTH, -3);
		Date m6 = now.getTime();
		System.out.println(sdf.format(m6));
		
		now.add(Calendar.MONTH, -3);
		Date m9 = now.getTime();
		System.out.println(sdf.format(m9));
		
		now.add(Calendar.MONTH, -3);
		Date m12 = now.getTime();
		System.out.println(sdf.format(m12));
		
		now.add(Calendar.MONTH, -6);
		Date m18 = now.getTime();
		System.out.println(sdf.format(m18));
		
		now.add(Calendar.MONTH, -6);
		Date m24 = now.getTime();
		System.out.println(sdf.format(m24));
		
		now.add(Calendar.MONTH, -12);
		Date m36 = now.getTime();
		System.out.println(sdf.format(m36));
		
		if(birthday.getTime()>d28.getTime()&&birthday.getTime()<current.getTime()){
			returnStr = "0-28天"; 
		}else if(birthday.getTime()>m3.getTime()&&birthday.getTime()<d28.getTime()){
			returnStr = "1-3月"; 
		}else if(birthday.getTime()>m6.getTime()&&birthday.getTime()<m3.getTime()){
			returnStr = "4-6月"; 
		}else if(birthday.getTime()>m9.getTime()&&birthday.getTime()<m6.getTime()){
			returnStr = "7-9月"; 
		}else if(birthday.getTime()>m12.getTime()&&birthday.getTime()<m9.getTime()){
			returnStr = "10-12月"; 
		}else if(birthday.getTime()>m18.getTime()&&birthday.getTime()<m12.getTime()){
			returnStr = "13-18月"; 
		}else if(birthday.getTime()>m24.getTime()&&birthday.getTime()<m18.getTime()){
			returnStr = "19-24月"; 
		}else if(birthday.getTime()>m36.getTime()&&birthday.getTime()<m24.getTime()){
			returnStr = "25-36月"; 
		}else{
			returnStr = "25-36月"; 
		}
		
		return returnStr;
	}
	
	/**
	 * 根据宝宝的生日计算对应的标准身高体重
	 * 2015年12月1日 下午4:26:24
	 * @return String
	 * @author sunxiao
	 */
	public static String BirthdayToAgeForStandardFigure(Date birthday,String gender){
		String returnStr = "";
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date current = new Date();
		now.setTime(new Date());
		now.add(Calendar.DATE,-3);
		Date d3 = now.getTime();
		System.out.println(sdf.format(d3));
		
		now.add(Calendar.DATE, -27);
		Date d30 = now.getTime();
		System.out.println(sdf.format(d30));
		
		now.add(Calendar.MONTH, -2);
		Date m3 = now.getTime();
		System.out.println(sdf.format(m3));
		
		now.add(Calendar.MONTH, -1);
		Date m4 = now.getTime();
		System.out.println(sdf.format(m4));
		
		now.add(Calendar.MONTH, -1);
		Date m5 = now.getTime();
		System.out.println(sdf.format(m5));
		
		now.add(Calendar.MONTH, -1);
		Date m6 = now.getTime();
		System.out.println(sdf.format(m6));
		
		now.add(Calendar.MONTH, -2);
		Date m8 = now.getTime();
		System.out.println(sdf.format(m8));
		
		now.add(Calendar.MONTH, -2);
		Date m10 = now.getTime();
		System.out.println(sdf.format(m10));
		
		now.add(Calendar.MONTH, -2);
		Date m12 = now.getTime();
		System.out.println(sdf.format(m12));
		
		now.add(Calendar.MONTH, -3);
		Date m15 = now.getTime();
		System.out.println(sdf.format(m15));
		
		now.add(Calendar.MONTH, -3);
		Date m18 = now.getTime();
		System.out.println(sdf.format(m18));
		
		now.add(Calendar.MONTH, -3);
		Date m21 = now.getTime();
		System.out.println(sdf.format(m21));
		
		now.add(Calendar.MONTH, -3);
		Date m24 = now.getTime();
		System.out.println(sdf.format(m24));
		
		now.add(Calendar.MONTH, -6);
		Date m30 = now.getTime();
		System.out.println(sdf.format(m30));
		
		now.add(Calendar.MONTH, -6);
		Date m36 = now.getTime();
		System.out.println(sdf.format(m36));
		
		if("1".equals(gender)){
			if(birthday.getTime()>d3.getTime()&&birthday.getTime()<current.getTime()){
				returnStr = "47-53.7cm;2.60-4.10kg";
			}else if(birthday.getTime()>d30.getTime()&&birthday.getTime()<d3.getTime()){
				returnStr = "52.1-61.5cm;3.90-6.40kg";
			}else if(birthday.getTime()>m3.getTime()&&birthday.getTime()<d30.getTime()){
				returnStr = "56.0-65.2cm;5.00-7.80kg";
			}else if(birthday.getTime()>m4.getTime()&&birthday.getTime()<m3.getTime()){
				returnStr = "59.3-67.4cm;5.80-8.80kg";
			}else if(birthday.getTime()>m5.getTime()&&birthday.getTime()<m4.getTime()){
				returnStr = "61.3-69.4cm;6.30-9.50kg";
			}else if(birthday.getTime()>m6.getTime()&&birthday.getTime()<m5.getTime()){
				returnStr = "63.5-71.4cm;6.80-10.20kg";
			}else if(birthday.getTime()>m8.getTime()&&birthday.getTime()<m6.getTime()){
				returnStr = "65.2-74.5cm;7.00-10.91kg";
			}else if(birthday.getTime()>m10.getTime()&&birthday.getTime()<m8.getTime()){
				returnStr = "67.9-77.6cm;7.58-11.52kg";
			}else if(birthday.getTime()>m12.getTime()&&birthday.getTime()<m10.getTime()){
				returnStr = "70.7-80.3cm;8.08-12.2kg";
			}else if(birthday.getTime()>m15.getTime()&&birthday.getTime()<m12.getTime()){
				returnStr = "72.9-83.6cm;8.55-12.86kg";
			}else if(birthday.getTime()>m18.getTime()&&birthday.getTime()<m15.getTime()){
				returnStr = "75.4-87.2cm;8.90-13.51kg";
			}else if(birthday.getTime()>m21.getTime()&&birthday.getTime()<m18.getTime()){
				returnStr = "78.3-90.0cm;9.37-14.33kg" ;
			}else if(birthday.getTime()>m24.getTime()&&birthday.getTime()<m21.getTime()){
				returnStr = "80.9-94.9cm;9.70-14.80kg";
			}else if(birthday.getTime()>m30.getTime()&&birthday.getTime()<m24.getTime()){
				returnStr = "84.0-98.1cm;10.60-16.15kg";
			}else if(birthday.getTime()>m36.getTime()&&birthday.getTime()<m30.getTime()){
				returnStr = "88.2-102.8cm;11.50-17.60kg";
			}
		}else{
			if(birthday.getTime()>d3.getTime()&&birthday.getTime()<current.getTime()){
				returnStr = "46.5-52.8cm;2.60-4.00kg";
			}else if(birthday.getTime()>d30.getTime()&&birthday.getTime()<d3.getTime()){
				returnStr = "51.3-59.8cm;3.70-5.90kg";
			}else if(birthday.getTime()>m3.getTime()&&birthday.getTime()<d30.getTime()){
				returnStr = "54.7-63.5cm;4.60-7.20kg";
			}else if(birthday.getTime()>m4.getTime()&&birthday.getTime()<m3.getTime()){
				returnStr = "58.0-66.0cm;5.30-8.10kg";
			}else if(birthday.getTime()>m5.getTime()&&birthday.getTime()<m4.getTime()){
				returnStr = "60.0-68.0cm;5.80-8.80kg";
			}else if(birthday.getTime()>m6.getTime()&&birthday.getTime()<m5.getTime()){
				returnStr = "62.0-70.0cm;6.30-9.50kg";
			}else if(birthday.getTime()>m8.getTime()&&birthday.getTime()<m6.getTime()){
				returnStr = "63.6-72.6cm;6.54-10.1kg";
			}else if(birthday.getTime()>m10.getTime()&&birthday.getTime()<m8.getTime()){
				returnStr = "66.3-76.0cm;7.06-11.0kg";
			}else if(birthday.getTime()>m12.getTime()&&birthday.getTime()<m10.getTime()){
				returnStr = "68.7-79.3cm;7.56-11.32kg";
			}else if(birthday.getTime()>m15.getTime()&&birthday.getTime()<m12.getTime()){
				returnStr = "71.9-82.0cm;8.10-11.95kg";
			}else if(birthday.getTime()>m18.getTime()&&birthday.getTime()<m15.getTime()){
				returnStr = "74.8-86.0cm;8.48-12.73kg";
			}else if(birthday.getTime()>m21.getTime()&&birthday.getTime()<m18.getTime()){
				returnStr = "77.3-89.2cm;9.00-13.45kg";
			}else if(birthday.getTime()>m24.getTime()&&birthday.getTime()<m21.getTime()){
				returnStr = "79.6-93.6cm;9.20-14.10kg";
			}else if(birthday.getTime()>m30.getTime()&&birthday.getTime()<m24.getTime()){
				returnStr = "83.0-97.8cm;10.20-15.95kg";
			}else if(birthday.getTime()>m36.getTime()&&birthday.getTime()<m30.getTime()){
				returnStr = "87.0-101.6cm;10.93-17.20kg";
			}
		}
		return returnStr;
	}
	
	public static String getAgeByBirthday(Date birthday){
		long day=getDays(DateUtils.DateToStr(birthday,"date"), DateUtils.DateToStr(new Date(),"date"));
        long year=day/365;
        long month=(day-365L*year)/30;
        long week=(day-365L*year)%30/7;
        return year+"岁"+month+"月"+week+"周";
	}
	
	/**
     * 两个时间之间的天数
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return Math.abs(day);
    }
}