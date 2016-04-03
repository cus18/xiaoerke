package com.cxqm.xiaoerke.modules.order.dao;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.OrderServiceVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MyBatisDao
public interface ConsultPhoneRegisterServiceDao {

    int deleteByPrimaryKey(Integer id);

    int insert(ConsultPhoneRegisterServiceVo record);

    int insertSelective(ConsultPhoneRegisterServiceVo record);

    ConsultPhoneRegisterServiceVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsultPhoneRegisterServiceVo record);

    int updateByPrimaryKey(ConsultPhoneRegisterServiceVo record);

    Map<String,Object> getPhoneConsultaServiceIndo(Integer phoneConsultServiceId);

    //电话咨询订单列表
    List<HashMap<String, Object>> getPhoneConsultaList(@Param("userId") String userId,@Param("state") String state);

    /**
     * 分页查询电话咨询订单列表
     * sunxiao
     */
    Page<ConsultPhoneRegisterServiceVo> findConsultPhonePatientList(Page<ConsultPhoneRegisterServiceVo> page,ConsultPhoneRegisterServiceVo vo);
}