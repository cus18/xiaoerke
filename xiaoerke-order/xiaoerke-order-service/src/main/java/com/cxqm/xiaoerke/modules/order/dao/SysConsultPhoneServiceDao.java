package com.cxqm.xiaoerke.modules.order.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;

@MyBatisDao
public interface SysConsultPhoneServiceDao {

    int deleteByPrimaryKey(Integer id);

    int insert(SysConsultPhoneServiceVo record);

    int insertSelective(SysConsultPhoneServiceVo record);

    SysConsultPhoneServiceVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysConsultPhoneServiceVo record);

    int updateByPrimaryKey(SysConsultPhoneServiceVo record);

    List<SysConsultPhoneServiceVo> selectConsultList(@Param("doctorId") String doctorId, @Param("state") String state);

    int cancelOrder(@Param("id")Integer phoneConsultaServiceId,@Param("state") String state);

    Integer finCountOfService();

    //根据医生获取可预约日期
    List<HashMap<String, Object>> getConsultDateList(HashMap<String,Object> hashMap);

    //根据医生和日期获取可预约时间
    List<HashMap<String,Object>> findConsultPhoneTimeListByDoctorAndDate(HashMap<String, Object> hashMap);

    Map<String,Object> getRegisterInfo(@Param("sysConsultPhoneServiceId") Integer sysConsultPhoneServiceId);
    
    /**
	 * 根据条件查询电话咨询信息
	 * sunxiao
	 */
    List<SysConsultPhoneServiceVo> findSysConsultPhoneByInfo(Map<String, Object> map);
}