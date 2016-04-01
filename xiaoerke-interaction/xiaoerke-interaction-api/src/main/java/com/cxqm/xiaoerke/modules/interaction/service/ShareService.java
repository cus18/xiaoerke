package com.cxqm.xiaoerke.modules.interaction.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface ShareService {

	HashMap<String, Object> findShareDetailInfo(HashMap<String, Object> hashMap);

	HashMap<String, Object> getMyShareDetail(Map<String, Object> params);

	Map<String, Object> orderShareOperation(Map<String, Object> params,
			HttpSession session, HttpServletRequest request);

}
