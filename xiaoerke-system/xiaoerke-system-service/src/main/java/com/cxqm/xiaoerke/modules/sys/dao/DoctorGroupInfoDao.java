/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.CrudDao;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorGroupVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审批DAO接口
 *
 * @author deliang
 * @version 2014-05-16
 */
@MyBatisDao
public interface DoctorGroupInfoDao extends CrudDao<DoctorVo> {

    Page<DoctorGroupVo> findPageAllDoctorGroup(Page<HashMap<String, Object>> page,HashMap<String, Object> param);

    List<DoctorVo> findDoctorListInDoctorGroup(@Param("doctorGroupId") Integer doctorGroupId);

    DoctorGroupVo getDoctorGroupInfo(@Param("doctorGroupId") Integer doctorGroupId);

    Page<HashMap<String, Object>> findDoctorByDoctorGroup(HashMap<String, Object> doctorMap, Page<HashMap<String, Object>> page);

    DoctorGroupVo getDoctorGroupInfoByDoctor(@Param("doctorId") String doctorId);
    
    /**
     * 开通电话咨询修改修改doctorGroup表 
     * sunxiao
     * @param map
     */
    void updateSysDoctorGroup(Map<String, Object> map);
}
