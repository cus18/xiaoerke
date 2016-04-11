/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.order.dao;

import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterTemplateVo;
import com.cxqm.xiaoerke.modules.order.entity.RegisterTemplateServiceVo;

/**
 * 电话咨询号源模板dao
 *
 * @author sunxiao
 * @version 2016-04-01
 */
@MyBatisDao
public interface ConsultPhoneRegisterTemplateDao {

    void updateRegisterTemplateByInfo(Map<String, Object> executeMap);
    
    void deleteRegisterTemplateByinfo(Map<String, Object> executeMap);
    
    List<ConsultPhoneRegisterTemplateVo> getRegisterTemplateList(Map<String, Object> executeMap);
    
    void saveRegisterTemplate(Map<String, Object> executeMap);
    
    List<ConsultPhoneRegisterTemplateVo> getRegisterTemplate(ConsultPhoneRegisterTemplateVo registerTemplate);
    
}
