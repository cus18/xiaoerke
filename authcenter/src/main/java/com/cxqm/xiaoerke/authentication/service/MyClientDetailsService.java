package com.cxqm.xiaoerke.authentication.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class MyClientDetailsService implements ClientDetailsService {


	private List<Map<String, String>> list;

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws OAuth2Exception {

		for (Map<String,String> map : list) {
			String id = map.get("id");
			String secretKey = map.get("secretKey");
			if (clientId.equals(id)) {
				List<String> authorizedGrantTypes = new ArrayList<String>();
				authorizedGrantTypes.add("password");
				authorizedGrantTypes.add("refresh_token");
				authorizedGrantTypes.add("client_credentials");
				authorizedGrantTypes.add("authorization_code");

				BaseClientDetails clientDetails = new BaseClientDetails();
				clientDetails.setClientId(id);
				clientDetails.setClientSecret(secretKey);
				clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);

				return clientDetails;
			}
		}
		throw new NoSuchClientException( "No client recognized with id: " + clientId);
	}


	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}
}