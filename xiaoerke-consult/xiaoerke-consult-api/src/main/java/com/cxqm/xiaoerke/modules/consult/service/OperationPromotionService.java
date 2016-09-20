package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorTimeGiftVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 咨询医生信息service
 * @author sunxiao
 * 2016-09-19
 */
public interface OperationPromotionService {

    List<OperationPromotionVo> findKeywordRoleList(OperationPromotionVo vo);

    void saveKeywordRole(OperationPromotionVo vo);

    Map getAllRoleListByKeyword();
}
