package com.cxqm.xiaoerke.modules.interaction.service;

import java.util.HashMap;
import java.util.Map;

public interface ConcernService {

	void getMyConcernedDoctorList(Map<String, Object> params,
			HashMap<String, Object> response);

	void userConcernDoctor(Map<String, Object> params);

	void judgeIfUserConcernDoctor(Map<String, Object> params,
			HashMap<String, Object> response);

}
