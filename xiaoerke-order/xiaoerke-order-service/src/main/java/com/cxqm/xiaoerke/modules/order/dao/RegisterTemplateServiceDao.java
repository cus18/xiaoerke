/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.order.dao;

import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.RegisterTemplateServiceVo;

/**
 * 审批DAO接口
 *
 * @author sunxiao
 * @version 2015-09-01
 */
@MyBatisDao
public interface RegisterTemplateServiceDao {

    void updateRegisterTemplateByInfo(Map<String, Object> executeMap);
    
    void deleteRegisterTemplateByinfo(Map<String, Object> executeMap);
    
    List<RegisterTemplateServiceVo> getRegisterTemplateList(Map<String, Object> executeMap);
    
    void saveRegisterTemplate(Map<String, Object> executeMap);
    
    List<RegisterTemplateServiceVo> getRegisterTemplate(RegisterTemplateServiceVo registerTemplate);
    
}
