package com.cxqm.xiaoerke.modules.healthRecords.service;

import java.util.Map;

/**
 * @author chenxiaoqiong
 * @version 2016/4/14
 */
public interface BabyIllnessInfoService {

    Map<String,Object> getIllnessInfoBySysPhoneConsultId(Integer sys_phoneConsult_id);
}
