package com.cxqm.xiaoerke.modules.operation.service;


import java.util.List;

import com.cxqm.xiaoerke.modules.wechat.entity.DoctorAttentionVo;


/**
 * 医生信息统计 Service
 *
 * @author deliang
 * @version 2015-11-05
 */

public interface DoctorOperationService {


    List<DoctorAttentionVo> findDoctorInfo(DoctorAttentionVo doctorAttentionVo);


}
