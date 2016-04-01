/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorIllnessRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;

/**
 * 医生和疾病关联表的DAO接口
 *
 * @author thinkgem
 * @version 2014-05-16
 */
@MyBatisDao
public interface DoctorIllnessRelationDao {
	
	  /**
     * 删除所有医生与该疾病的关联关系
     */
    void deleteDoctorAndIllnessRelationByIllnessId(IllnessVo illnessVo);

    //获取医生的从业经历
    HashMap<String, Object> getDoctorCardExpertise(HashMap<String, Object> Map);


    //批量插入医生与疾病关联信息表数据
    void insertIllnessRelationExecute(List<HashMap<String,Object>> arrayList);

    //==============================以下部分属于运维系统==============================================

    //将数据插入到doctor_illness_relation中（单条）
    void insertDoctorIllnessRelationData(DoctorIllnessRelationVo doctorIllnessRelationVo);

    //校验，根据医生的主键和疾病主键查询此该医生是否已经关联了该疾病
    DoctorIllnessRelationVo findDoctorIllnessRelationInfo(DoctorIllnessRelationVo doctorIllnessRelationVo);

    //根据doctorId删除医生与疾病的所有关联信息
    void  deleteDoctorAndIllnessRelation(DoctorIllnessRelationVo doctorIllnessRelationVo);

}
