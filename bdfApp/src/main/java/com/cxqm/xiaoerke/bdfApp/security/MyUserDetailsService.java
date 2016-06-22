package com.cxqm.xiaoerke.bdfApp.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;


/**
 * 用户细节管理
 * 
 * @version 1.0
 *
 */
@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private SystemService sysService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		if(username == null){
			throw new AuthenticationServiceException("用户名不能为空！");
		}
		User user = sysService.getUserByLoginName(username);
		if(user == null){
			throw new AuthenticationServiceException("用户名不存在！");
		}
		
		//设置权限
        List<String> permissions = sysService.findPermissionsByUserId(user.getId());
        Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
        for (int i = 0; i < permissions.size(); i++) {
               authSet.add(new SimpleGrantedAuthority(permissions.get(i)));
        }
        
		//匹配参数
		boolean enables = true;  
        boolean accountNonExpired = true;  
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        
		return new org.springframework.security.core.userdetails.User(
				username, user.getPassword(), enables,
				accountNonExpired, credentialsNonExpired, accountNonLocked,
				authSet);
	}

}
