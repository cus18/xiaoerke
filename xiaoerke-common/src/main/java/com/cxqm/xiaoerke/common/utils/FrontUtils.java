package com.cxqm.xiaoerke.common.utils;

import com.cxqm.xiaoerke.common.persistence.Page;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 医生和用户前端操作工具类 组装Page，常量等信息.
 * 
 * @author admin
 * @version 1.0.2015/03/02
 */
public class FrontUtils {

	public static final String DATE_FORMAT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

	private FrontUtils() {

	}

	public static <T> long generatorTotalPage(Page<T> resultPage) {
		long count = resultPage.getCount();
		int pageSize = resultPage.getPageSize();
		long tmp = count % pageSize == 0 ? count / pageSize : count / pageSize
				+ 1;
		return tmp;
	}

	public static <T> Page<T> generatorPage(String currentPage, String pageSize) {
		int pageNo = StringUtils.isNotBlank(currentPage) ? Integer
				.valueOf(currentPage) : 1;
		int _pageSize = StringUtils.isNotBlank(pageSize) ? Integer
				.valueOf(pageSize) : 10;
		Page<T> page = new Page<T>(pageNo, _pageSize);
		return page;
	}

	public static <T> T readJson(String jsonStr, Class<?> collectionClass,
			Class<?>... elementClasses) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(
				collectionClass, elementClasses);
		return mapper.readValue(jsonStr, javaType);
	}

	/**
	 * 获取时间中的日期
	 * 
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static String time2Date(Date time) throws ParseException {

		String date = DateFormatUtils.format(time, DATE_FORMAT_PATTERN);

		return date;
	}

	public static Map<String, String> calculateResult(
			Map<String, List<String>> map) {
		 DecimalFormat df = new DecimalFormat("#.00");
		Map<String, String> resultMap = new HashMap<String, String>();
		Set<String> keySet = map.keySet();
		for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			List<String> list = map.get(key);
			double value = 0.00d;
			for (int i = 0; i < list.size(); i++) {
				value += Double.valueOf(list.get(i));
			}
			resultMap.put(key, df.format(value / list.size()) + "");
		}
		return resultMap;
	}

}
