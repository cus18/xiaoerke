package com.cxqm.xiaoerke.modules.bankend.service;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorIllnessRelationVo;

import org.springframework.transaction.annotation.Transactional;

/**
 * 医院相关
 * @author 得良
 * @version 2015-07-29
 */
@Transactional(readOnly = false)
public interface DoctorIllnessRelationService {

	//根据一类疾病和二类疾病查询疾病信息表信息
	IllnessVo findSysIllnessInfo(DoctorIllnessRelationVo doctorIllnessRelationVo);

	//往doctor_illness_relation中插入数据（单条）
	void insertDoctorIllnessRelationData(DoctorIllnessRelationVo doctorIllnessRelationVo);

	//校验，根据医生的主键和疾病主键查询此该医生是否已经关联了该疾病
	DoctorIllnessRelationVo findDoctorIllnessRelationInfo(DoctorIllnessRelationVo doctorIllnessRelationVo);

	//根据doctorId删除医生与疾病的所有关联信息（先删除，后保存）
	void  deleteDoctorAndIllnessRelation(DoctorIllnessRelationVo doctorIllnessRelationVo);
}
