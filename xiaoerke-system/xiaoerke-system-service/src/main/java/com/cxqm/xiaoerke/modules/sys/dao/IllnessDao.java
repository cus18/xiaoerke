/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.dao;

import java.util.HashMap;
import java.util.List;

import com.cxqm.xiaoerke.modules.sys.entity.DoctorIllnessRelationVo;
import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;

/**
 * 审批DAO接口
 *
 * @author thinkgem
 * @version 2014-05-16
 */
@MyBatisDao
public interface IllnessDao {

    //分页显示所有的疾病一级类别列表
    Page<IllnessVo> findFirstIllness(HashMap<String, Object> illnessInfo, Page<HashMap<String, Object>> page);

    //根据一类疾病查找所下属的二类疾病
    List<IllnessVo> findSecondIllnessByFirst(@Param(value = "illnessName") String illnessName);

    //批量插入疾病信息
    void insertIllnessExecute(List<HashMap<String,Object>> arrayList);

    //根据手机号查询doctorId
    HashMap<String, Object> findIllnessIdByLevel2Execute(HashMap<String, Object> hashMap);

    List<IllnessVo> findSecondIllnessByName(@Param(value = "illnessName") String illnessName);

    //==============================以下部分属于运维系统==============================================

    //单条插入疾病信息
    void insertIllness(IllnessVo illnessVo);

    //根据一类疾病和二类疾病查询疾病信息表主键
    IllnessVo findSysIllnessInfo(DoctorIllnessRelationVo doctorIllnessRelationVo);

    //查询系统内部所有二级疾病
    Page<HashMap<String, Object>> findPageAllLevel_2(Page<HashMap<String, Object>> page,HashMap<String,Object> hashMap);

    //分页显示所有的疾病一级类别列表
    Page<IllnessVo> findFirstIllnessVo(Page<IllnessVo> page,IllnessVo illnessVo);

    /**
     * 根据医生id查询当前医生所关联的疾病信息 zdl
     * @param doctorIllnessRelationVo
     * @return
     */
    List<IllnessVo> findSysIllnessBySysDoctorId(DoctorIllnessRelationVo doctorIllnessRelationVo);

    /**
     * 获取疾病库列表 zdl
     */
    Page<IllnessVo> findAllIllnessList(Page<IllnessVo> illnessVoPage,HashMap<String, Object> searchMap);

    /**
     * 根据疾病表主键更新疾病信息
     */
    void updateIllness(IllnessVo illnessVo);

    /**
     * 根据疾病表id删除疾病信息
     */
    void deleteIllnessById(IllnessVo illnessVo);
  
    
    List<IllnessVo> getFirstIllnessList();
    
  //获取医生所有的一类疾病
    List<IllnessVo> findSysIllness_1BySysDoctorId(HashMap<String,Object> hashMap);
    
}
