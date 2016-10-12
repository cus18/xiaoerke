/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.utils;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.CacheUtils;
import com.cxqm.xiaoerke.common.utils.Exceptions;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.sys.dao.LogDao;
import com.cxqm.xiaoerke.modules.sys.dao.MenuDao;
import com.cxqm.xiaoerke.modules.sys.entity.Log;
import com.cxqm.xiaoerke.modules.sys.entity.Menu;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.sys.service.LogMongoDBServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2014-11-7
 */
public class LogUtils {
	
	public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";
	
	private static MongoDBService<MongoLog> mongoDBService = SpringContextHolder.getBean(LogMongoDBServiceImpl.class);
	private static LogDao logDao = SpringContextHolder.getBean(LogDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static String mongoEnabled = Global.getConfig("mongo.enabled");
	
	private static ExecutorService threadExecutor = Executors.newCachedThreadPool();

	/**
	 * 保存日志
	 * @param request
	 * @param title
	 */
	public static void saveLog(HttpServletRequest request, String title){
		saveLog(request, null, null, title);
	}


	/**
	 * 保存日志
	 * @param request
	 * @param handler
	 * @param ex
	 * @param title
	 */
	public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title){
		Log log = new Log();
		log.setTitle(title);
		log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
		log.setRemoteAddr(StringUtils.getRemoteAddr(request));
		log.setUserAgent(request.getHeader("user-agent"));
		log.setRequestUri(request.getRequestURI());
		log.setParams(request.getParameterMap());
		log.setMethod(request.getMethod());
		log.setCreateDate(new Date());
		//插入用户的openId
		log.setOpenId((String) request.getSession().getAttribute("openId"));
		// 异步保存日志
		Runnable thread = new SaveLogThread(log, handler, ex);
		threadExecutor.execute(thread);
	}

	/**
	 * 保存日志
	 */
	public static void saveLog(String title){
		Log log = new Log();
		log.setType(Log.TYPE_EXCEPTION);
		log.setTitle(title);
		// 异步保存日志
		Runnable thread = new SaveLogThread(log, null, null);
		threadExecutor.execute(thread);
	}
	/**
	 * 保存日志Log
	 */
	public static void saveLogVo(Log log){
		log.setType(Log.TYPE_ACCESS);
		// 异步保存日志
		Runnable thread = new SaveLogThread(log, null, null);
		threadExecutor.execute(thread);
	}
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request,String title,String parameters){
		Log log = new Log();
		log.setType(Log.TYPE_EXCEPTION);
		log.setTitle(title);
		log.setParameters(parameters);
		log.setRemoteAddr(StringUtils.getRemoteAddr(request));
		log.setUserAgent(request.getHeader("user-agent"));
		log.setRequestUri(request.getRequestURI());
		log.setParams(request.getParameterMap());
		log.setMethod(request.getMethod());
		log.setOpenId((String) request.getSession().getAttribute("openId"));
		// 异步保存日志
		Runnable thread = new SaveLogThread(log, null, null);
		threadExecutor.execute(thread);
	}

	/**
	 * 保存日志
	 */
	public static void saveLog(String title,String parameters){
		Log log = new Log();
		log.setType(Log.TYPE_EXCEPTION);
		log.setTitle(title);
		log.setParameters(parameters);
		log.setOpenId(parameters);
		// 异步保存日志
		Runnable thread = new SaveLogThread(log, null, null);
		threadExecutor.execute(thread);
	}


	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread extends Thread{
		private Log log;
		private Object handler;
		private Exception ex;
		
		public SaveLogThread(Log log, Object handler, Exception ex){
			super(SaveLogThread.class.getSimpleName());
			this.log = log;
			this.handler = handler;
			this.ex = ex;
		}
		
		@Override
		public void run() {
			// 获取日志标题
			if (StringUtils.isBlank(log.getTitle())){
				String permission = "";
				if (handler instanceof HandlerMethod){
					Method m = ((HandlerMethod)handler).getMethod();
					RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
					permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
				}
				log.setTitle(getMenuNamePath(log.getRequestUri(), permission));
			}
			// 如果有异常，设置异常信息
			log.setException(Exceptions.getStackTraceAsString(ex));
			// 如果无标题并无异常日志，则不保存信息
			if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())){
				return;
			}
			// 保存日志信息
			log.preInsert();
			//日志只存入mongodb，不再存入mysql
//            logDao.insert(log);
			if("true".equalsIgnoreCase(mongoEnabled)) {
				MongoLog mongoLog = LogMongoDBServiceImpl.buildMongoLog(log);
				mongoDBService.insert(mongoLog);
			}
		}
	}

	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	public static String getMenuNamePath(String requestUri, String permission){
		String href = StringUtils.substringAfter(requestUri, Global.getAdminPath());
		@SuppressWarnings("unchecked")
		Map<String, String> menuMap = (Map<String, String>)CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
		if (menuMap == null){
			menuMap = Maps.newHashMap();
			List<Menu> menuList = menuDao.findAllList(new Menu());
			for (Menu menu : menuList){
				// 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
				String namePath = "";
				if (menu.getParentIds() != null){
					List<String> namePathList = Lists.newArrayList();
					for (String id : StringUtils.split(menu.getParentIds(), ",")){
						if (Menu.getRootId().equals(id)){
							continue; // 过滤跟节点
						}
						for (Menu m : menuList){
							if (m.getId().equals(id)){
								namePathList.add(m.getName());
								break;
							}
						}
					}
					namePathList.add(menu.getName());
					namePath = StringUtils.join(namePathList, "-");
				}
				// 设置菜单名称路径
				if (StringUtils.isNotBlank(menu.getHref())){
					menuMap.put(menu.getHref(), namePath);
				}else if (StringUtils.isNotBlank(menu.getPermission())){
					for (String p : StringUtils.split(menu.getPermission())){
						menuMap.put(p, namePath);
					}
				}
				
			}
			CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
		}
		String menuNamePath = menuMap.get(href);
		if (menuNamePath == null){
			for (String p : StringUtils.split(permission)){
				menuNamePath = menuMap.get(p);
				if (StringUtils.isNotBlank(menuNamePath)){
					break;
				}
			}
			if (menuNamePath == null){
				return "";
			}
		}
		return menuNamePath;
	}
}
