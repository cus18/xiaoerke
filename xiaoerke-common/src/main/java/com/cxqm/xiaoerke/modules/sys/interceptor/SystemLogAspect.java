package com.cxqm.xiaoerke.modules.sys.interceptor;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.sys.dao.LogDao;
import com.cxqm.xiaoerke.modules.sys.dao.MenuDao;
import com.cxqm.xiaoerke.modules.sys.entity.Log;
import com.cxqm.xiaoerke.modules.sys.entity.Menu;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.sys.service.LogMongoDBServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * pointer
 */
@Aspect
@Component
public class SystemLogAspect {


    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);


    @Pointcut("@annotation(com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog)")
    public void serviceAspect() {
    }


    @Pointcut("@annotation(com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog)")
    public void controllerAspect() {
    }


    public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";

    private static MongoDBService<MongoLog> mongoDBService = SpringContextHolder.getBean(LogMongoDBServiceImpl.class);
    private static LogDao logDao = SpringContextHolder.getBean(LogDao.class);
    private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
    private static String mongoEnabled = Global.getConfig("mongo.enabled");

    private static ExecutorService threadExecutor = Executors.newCachedThreadPool();


    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        try {
//            String  userId=
            String title = getControllerMethodDescription(joinPoint);
            saveLog(request, null, null, title);
        } catch (Exception e) {
            logger.error( e.getMessage());
        }
    }

    /**
     * @param joinPoint
     */
    @Before("serviceAspect()")
    public void doBeforeService(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        try {
            String title = getServiceMethodDescription(joinPoint);
            saveLog(request, null, null, title);
        } catch (Exception e) {

            logger.error( e.getMessage());
        }
    }

    /**
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        try {
            String title = getServiceMethodDescription(joinPoint);
            saveLog(request, null, null, title);
        } catch (Exception e1) {

            logger.error( e.getMessage());
        }
    }


    /**
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public static String getServiceMethodDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemServiceLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * @param request
     * @param handler
     * @param ex
     * @param title
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title) {
        String openId = (String) request.getSession().getAttribute("openId");
        Log log = new Log();
        log.setTitle(title);
        log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
        log.setRemoteAddr(StringUtils.getRemoteAddr(request));
        log.setUserAgent(request.getHeader("user-agent"));
        log.setRequestUri(request.getRequestURI());
        log.setParams(request.getParameterMap());
        log.setMethod(request.getMethod());
        log.setOpenId(openId);
        log.setUserId(UserUtils.getUser().getId());
        if(StringUtils.isNull(openId)){
            log.setOpenId(CookieUtils.getCookie(request,"openId"));
        }
        Runnable thread = new SaveLogThread(log, handler, ex);
        threadExecutor.execute(thread);
    }

    public static class SaveLogThread extends Thread {
        private Log log;
        private Object handler;
        private Exception ex;

        public SaveLogThread(Log log, Object handler, Exception ex) {
            super(SaveLogThread.class.getSimpleName());
            this.log = log;
            this.handler = handler;
            this.ex = ex;
        }

        @Override
        public void run() {
            if (StringUtils.isBlank(log.getTitle())) {
                String permission = "";
                if (handler instanceof HandlerMethod) {
                    Method m = ((HandlerMethod) handler).getMethod();
                    RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
                    permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
                }
                log.setTitle(getMenuNamePath(log.getRequestUri(), permission));
            }

            log.setException(Exceptions.getStackTraceAsString(ex));
            if(StringUtils.isNull(log.getTitle()) && StringUtils.isNotBlank(log.getParams()) &&log.getParams().split("=").length>0){
                log.setTitle(log.getParams().split("=")[1]);
            }

            log.preInsert();
            logDao.insert(log);

            if ("true".equalsIgnoreCase(mongoEnabled)) {
                MongoLog mongoLog = LogMongoDBServiceImpl.buildMongoLog(log);
                mongoDBService.insert(mongoLog);
            }
        }
    }

    public static String getMenuNamePath(String requestUri, String permission) {
        String href = StringUtils.substringAfter(requestUri, Global.getAdminPath());
        @SuppressWarnings("unchecked")
        Map<String, String> menuMap = (Map<String, String>) CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
        if (menuMap == null) {
            menuMap = Maps.newHashMap();
            List<Menu> menuList = menuDao.findAllList(new Menu());
            for (Menu menu : menuList) {

                String namePath = "";
                if (menu.getParentIds() != null) {
                    List<String> namePathList = Lists.newArrayList();
                    for (String id : StringUtils.split(menu.getParentIds(), ",")) {
                        if (Menu.getRootId().equals(id)) {
                            continue;
                        }
                        for (Menu m : menuList) {
                            if (m.getId().equals(id)) {
                                namePathList.add(m.getName());
                                break;
                            }
                        }
                    }
                    namePathList.add(menu.getName());
                    namePath = StringUtils.join(namePathList, "-");
                }

                if (StringUtils.isNotBlank(menu.getHref())) {
                    menuMap.put(menu.getHref(), namePath);
                } else if (StringUtils.isNotBlank(menu.getPermission())) {
                    for (String p : StringUtils.split(menu.getPermission())) {
                        menuMap.put(p, namePath);
                    }
                }

            }
            CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
        }
        String menuNamePath = menuMap.get(href);
        if (menuNamePath == null) {
            for (String p : StringUtils.split(permission)) {
                menuNamePath = menuMap.get(p);
                if (StringUtils.isNotBlank(menuNamePath)) {
                    break;
                }
            }
            if (menuNamePath == null) {
                return "";
            }
        }
        return menuNamePath;
    }

}  