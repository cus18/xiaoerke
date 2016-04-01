package com.cxqm.xiaoerke.modules.bankend.service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorIllnessRelationVo;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;

import java.util.HashMap;
import java.util.List;

/**
 * 医院相关
 * @author 得良
 * @version 2015-07-29
 */
@Transactional(readOnly = false)
public interface IllnessService {
	/**
	 * 插入医院信息数据
	 * @param hospitalVo
	 */
	void insertIllnessData(IllnessVo hospitalVo);

	boolean judgeIllnessExist(IllnessVo illnessVo);

	//查询系统内部所有二级疾病
	Page<HashMap<String, Object>> findPageAllLevel_2(Page<HashMap<String, Object>> page, HashMap<String, Object> hashMap);

	Page<IllnessVo> findIllnessVoList(Page<IllnessVo> page, IllnessVo illnessVo);

	List<IllnessVo> findIllnessLevel_2List(IllnessVo illnessVo);

	List<IllnessVo> findSysIllnessBySysDoctorId(DoctorIllnessRelationVo doctorIllnessRelationVO);

	Page<IllnessVo> findAllIllness(Page<IllnessVo> illnessVoPage, HashMap<String, Object> searchMap);

	//根据疾病表主键更新疾病信息
	void updateIllness(IllnessVo illnessVo);

	//根据疾病id删除疾病
	void deleteIllnessById(IllnessVo illnessVo);

}
