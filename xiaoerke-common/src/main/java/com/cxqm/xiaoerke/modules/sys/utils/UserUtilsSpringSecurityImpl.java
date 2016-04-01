package com.cxqm.xiaoerke.modules.sys.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cxqm.xiaoerke.common.service.BaseService;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.modules.sys.dao.AreaDao;
import com.cxqm.xiaoerke.modules.sys.dao.MenuDao;
import com.cxqm.xiaoerke.modules.sys.dao.OfficeDao;
import com.cxqm.xiaoerke.modules.sys.dao.RoleDao;
import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.sys.entity.Area;
import com.cxqm.xiaoerke.modules.sys.entity.Menu;
import com.cxqm.xiaoerke.modules.sys.entity.Office;
import com.cxqm.xiaoerke.modules.sys.entity.Role;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;

/**
 * 用户工具类
 * @author ThinkGem
 * @version 2013-12-05
 */
public class UserUtilsSpringSecurityImpl {

	private static SystemService systemService = SpringContextHolder.getBean(SystemService.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	
	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";
	public static final String CACHE_USER = "user";

	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	
	
	public static User get(String id){
		User user = getUser(id);
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName){
		User user = userDao.getByLoginName(new User(null, loginName));
		return user;
	}	

    public static String getUserId(){
    	User u = getUser();
    	return u==null ? null:u.getId();
    }
    
    public static User getUser() {
        User user = (User) getCache(CACHE_USER);
        //long id = Thread.currentThread().getId();
        //String name = Thread.currentThread().getName();
        if (user == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                String loginName = null;
                if (principal instanceof org.springframework.security.core.userdetails.User) {
                    loginName = ((org.springframework.security.core.userdetails.User) principal).getUsername();
                } else if (principal instanceof String) {
                    loginName = principal.toString();
                }
                if (loginName != null) {
                    user = systemService.getUserByLoginName(loginName);
                    //id = Thread.currentThread().getId();
                    //name = Thread.currentThread().getName();
                    putCache(CACHE_USER, user);
                }
            }
        }
        if (user == null) {
            user = new User();

        }
        return user;
    }

    public static User getUser(String userId) {
        return systemService.getUser(userId);
    }

    public static String getUserName(String userId) {
        User user = systemService.getUser(userId);
        if (user == null || user.getName() == null) {
            return "";
        } else {
            return user.getName();
        }
    }

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        /*Object obj = getCacheMap().get(key);
        return obj == null ? defaultValue : obj;*/
    	RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    	if(attributes != null) {
	    	HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest(); 
	    	if(request != null) {
	    		HttpSession session = request.getSession();
	    		if(session != null)
	    			return session.getAttribute(key);
	    	}
    	}
    	
    	return defaultValue;
    }

    public static void putCache(String key, Object value) {
        //getCacheMap().put(key, value);
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
    	if(request != null) {
    		HttpSession session = request.getSession();
    		if(session != null)
    			session.setAttribute(key, value);
    	}
    }

    public static void removeCache(String key) {
        getCacheMap().remove(key);
    }

    public static Map<String, Object> getCacheMap() {
        SecurityUtils.Subject subject = SecurityUtils.getSubject();
        SecurityUtils.Principal principal = (SecurityUtils.Principal) subject.getPrincipal();

        return principal != null ? principal.getCacheMap() : new HashMap<String, Object>();
    }

    /**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_OFFICE_LIST);
		removeCache(CACHE_OFFICE_ALL_LIST);
		removeCache(CACHE_USER);
		//UserUtils.clearCache(getUser());
	}
	
    /**
	 * 获取当前用户角色列表
	 * @return
	 */
	public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (roleList == null){
			User user = getUser();
			if (user.isAdmin()){
				roleList = roleDao.findAllList(new Role());
			}else{
				Role role = new Role();
				role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
				roleList = roleDao.findList(role);
			}
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = menuDao.findAllList(new Menu());
			}else{
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuDao.findByUserId(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	
	/**
	 * 获取当前用户授权的区域
	 * @return
	 */
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
			areaList = areaDao.findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null){
			User user = getUser();
			if (user.isAdmin()){
				officeList = officeDao.findAllList(new Office());
			}else{
				Office office = new Office();
				office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
				officeList = officeDao.findList(office);
			}
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeAllList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_ALL_LIST);
		if (officeList == null){
			officeList = officeDao.findAllList(new Office());
		}
		return officeList;
	}
	
}
