package com.cxqm.xiaoerke.authentication.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;

public class UserAuthenticationProvider implements AuthenticationProvider, Serializable {

	private static final long serialVersionUID = -8711338767114063091L;

	@Autowired
	transient SystemService systemService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		User user = systemService.getUserByLoginName(String.valueOf(authentication.getPrincipal()));

		if (user != null) {
			boolean success = true;
			try {
				success = systemService.validatePassword(authentication.getCredentials().toString(), user.getPassword());
			} catch (Exception e) {
				throw new AuthenticationServiceException("用户名和密码不匹配!");
			}

			if (!success) {
				throw new AuthenticationServiceException("用户名和密码不匹配!");
			}

			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			List<String> permissions = systemService.findPermissionsByUserId(user.getId());
			for (int i = 0; i < permissions.size(); i++) {
				grantedAuthorities.add(new SimpleGrantedAuthority(permissions.get(i)));
			}

			/***
			 * 生成用户详细信息
			 */
			Map<String, Object> userDetails = new HashMap<String, Object>();
			userDetails.put("userId", user.getId());
			userDetails.put("nickName", user.getName());
			userDetails.put("loginName", user.getLoginName());
			
			MyUserAuthenticationToken auth = new MyUserAuthenticationToken(
					authentication.getPrincipal(),
					authentication.getCredentials(), 
					grantedAuthorities,
					userDetails);
			return auth;
		} else {
			throw new AuthenticationServiceException("用户名和密码不匹配!");
		}
	}

	public boolean supports(Class<?> arg0) {
		return true;
	}

	public class MyUserAuthenticationToken extends AbstractAuthenticationToken {

		private static final long serialVersionUID = -1092219614309982278L;
		private final Object principal;
		private Object credentials;

		public MyUserAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Object details) {
			super(authorities);
			this.principal = principal;
			this.credentials = credentials;
			super.setDetails(details);
			super.setAuthenticated(true);
		}

		public Object getCredentials() {
			return this.credentials;
		}

		public Object getPrincipal() {
			return this.principal;
		}
	}
}