package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionTemplateVo;

import java.util.List;
import java.util.Map;

/**
 * 运营推广service
 * @author sunxiao
 * 2016-11-10
 */
public interface OperationPromotionTemplateService {

    List<OperationPromotionTemplateVo> findOperationPromotionTemplateList(OperationPromotionTemplateVo vo);

    void operationPromotionTemplateOper(OperationPromotionTemplateVo vo);

    void deleteOperationPromotionTemplate(OperationPromotionTemplateVo vo);

}
