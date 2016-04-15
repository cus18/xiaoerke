package com.cxqm.xiaoerke.modules.order.dao;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.OrderServiceVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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

    Map<String,Object> getPhoneConsultaServiceIndo(Integer phoneConsultaServiceId);

    //电话咨询订单列表
    List<HashMap<String, Object>> getPhoneConsultaList(@Param("userId") String userId,@Param("state") String state);

    /**
     * 分页查询电话咨询订单列表
     * sunxiao
     */
    Page<ConsultPhoneRegisterServiceVo> findConsultPhonePatientList(Page<ConsultPhoneRegisterServiceVo> page,ConsultPhoneRegisterServiceVo vo);

    /**
     * 根据条件查询电话咨询订单
     * sunxiao
     * @param map
     * @return
     */
    List<Map<String, Object>> getConsultPhoneRegisterListByInfo(Map<String, Object> map);
    List<ConsultPhoneRegisterServiceVo> getAllConsultPhoneRegisterListByInfo(ConsultPhoneRegisterServiceVo vo);

    /**
     *
     * 电话咨询 -- 通过state和userId分页查询
     * @param page
     * @param hashMap
     * @return
     * @author chenxiaoqiong
     */
    Page<OrderServiceVo> getPhoneConsultPageList(Page<OrderServiceVo> page, HashMap<String, Object> hashMap);

    /**
     *
     * 预约挂号和电话咨询 -- 通过state和userId分页查询
     * @param page
     * @param hashMap
     * @return
     * @author chenxiaoqiong
     */
    Page<OrderServiceVo> getOrderAllPageList(Page<OrderServiceVo> page, HashMap<String, Object> hashMap);

    List<HashMap<String, Object>> getOrderPhoneConsultListByTime(@Param("state") String state,@Param("date")Date date);

    HashMap<String, Object> getConsultConnectInfo(@Param("id") Integer id);

    /**
     * 更新订单状态
     * @param excuteMap
     * @author chenxiaoqiong
     */
    void changeConsultPhoneRegisterServiceState(HashMap<String, Object> excuteMap);

    /**
     * 每日清单
     * @param searchMap dortorId,date
     * @return
     * @author chenxiaoqiong
     */
    List<Map<String,Object>> getSettlementPhoneConsultInfoByDate(Map<String, Object> searchMap);
}