package com.cxqm.xiaoerke.modules.interaction.service;

import java.util.HashMap;
import java.util.Map;

public interface ConcernService {

	void getMyConcernedDoctorList(Map<String, Object> params,
			HashMap<String, Object> response);

	void userConcernDoctor(Map<String, Object> params);

	void judgeIfUserConcernDoctor(Map<String, Object> params,
			HashMap<String, Object> response);

    /**
     * 获取粉丝列表（关注我的用户）
     * @param params
     * @param response
     * @author chenxiaoqiong
     */
    void getMyFansList(Map<String, Object> params, HashMap<String, Object> response);
}
