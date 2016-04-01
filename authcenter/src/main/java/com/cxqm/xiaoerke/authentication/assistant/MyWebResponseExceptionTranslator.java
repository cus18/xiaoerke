package com.cxqm.xiaoerke.authentication.assistant;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

public class MyWebResponseExceptionTranslator extends
		DefaultWebResponseExceptionTranslator {
	
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
		ResponseEntity<OAuth2Exception>  response = super.translate(e);
		addExtInfomation(response.getBody());
		return response;
	}

	private void addExtInfomation(OAuth2Exception e){
		if(e instanceof InvalidTokenException){
			if(e.getMessage().equals("Token has expired")){
				e.addAdditionalInformation("resultCode", "0900");
				e.addAdditionalInformation("resultMsg", "token已过期");
			}else{
				e.addAdditionalInformation("resultCode", "0904");
				e.addAdditionalInformation("resultMsg", "token不存在");
			}
		}else if(e instanceof BadClientCredentialsException){
			e.addAdditionalInformation("resultCode", "0901");
			e.addAdditionalInformation("resultMsg", "客户端验证错误");
		}else{
			e.addAdditionalInformation("resultCode", "0999");
			e.addAdditionalInformation("resultMsg", "认证错误");
		}
	}
}
