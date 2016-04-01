/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.webapp.security;

import org.springframework.stereotype.Service;

import com.cxqm.xiaoerke.modules.sys.security.SystemAuthorizingRealm;

/**
 * 系统安全认证实现类
 * @author ThinkGem
 * @version 2014-7-5
 */
@Service
//@DependsOn({"userDao","roleDao","menuDao"})
public class MySystemAuthorizingRealm extends SystemAuthorizingRealm {

}
