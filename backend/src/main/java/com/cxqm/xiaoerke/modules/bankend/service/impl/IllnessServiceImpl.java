package com.cxqm.xiaoerke.modules.bankend.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.bankend.service.IllnessService;


import com.cxqm.xiaoerke.modules.sys.dao.DoctorIllnessRelationDao;
import com.cxqm.xiaoerke.modules.sys.dao.IllnessDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorIllnessRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class IllnessServiceImpl implements IllnessService {

	@Autowired
	private IllnessDao illnessDao;

	@Autowired
	private DoctorIllnessRelationDao doctorIllnessRelationDao;

	@Override
	public void insertIllnessData(IllnessVo illnessVo) {
		illnessDao.insertIllness(illnessVo);
	}

	/**
	 * 判断该疾病在疾病库中是否已经存在
	 * @param illnessVo
	 * @return
	 */
	@Override
	public boolean judgeIllnessExist(IllnessVo illnessVo){
		DoctorIllnessRelationVo doctorIllnessRelationVO = new DoctorIllnessRelationVo();
		doctorIllnessRelationVO.setLevel_1(illnessVo.getLevel_1());
		doctorIllnessRelationVO.setLevel_2(illnessVo.getLevel_2());
		illnessVo=illnessDao.findSysIllnessInfo(doctorIllnessRelationVO);
		if(illnessVo !=null ){
			return true;
		}
		return false;
	}


	//查询系统内部所有二级疾病
	@Override
	public Page<HashMap<String, Object>> findPageAllLevel_2(Page<HashMap<String, Object>> page,HashMap<String,Object> hashMap) {

		return illnessDao.findPageAllLevel_2(page,hashMap);

	}

	/**
	 * @author zdl
	 * 获取一级疾病信息
	 * @param page
	 * @param
	 * @return
	 */
	@Override
	public Page<IllnessVo> findIllnessVoList(Page<IllnessVo> page, IllnessVo illnessVo) {
		// 设置分页参数
		illnessVo.setPage(page);
		//如果illnessVo中Level_1为空，则查询所有一级疾病
		if(!StringUtils.isNotNull(illnessVo.getLevel_1())){
			// 执行分页查询
			return illnessDao.findFirstIllnessVo(page,illnessVo);
		}
		return null;
	}


	/**
	 * 根据level_1查询对应的level_2信息
	 * @param illnessVo
	 * @return List<illnessVo>
	 */
	@Override
	public List<IllnessVo> findIllnessLevel_2List(IllnessVo illnessVo){
		return illnessDao.findSecondIllnessByFirst(illnessVo.getLevel_1());
	}

	/**
	 * 根据医生id查询当前医生所关联的疾病信息
	 * @param doctorIllnessRelationVO
	 * @return
	 */
	@Override
	public List<IllnessVo> findSysIllnessBySysDoctorId(DoctorIllnessRelationVo doctorIllnessRelationVO){
		return illnessDao.findSysIllnessBySysDoctorId(doctorIllnessRelationVO);
	}

//	illnessVo=illnessDao.findSysIllnessInfo(doctorIllnessRelationVO);
//
//	/**
//	 * 判断该疾病在疾病库中是否已经存在
//	 * @param illnessVo
//	 * @return
//	 */
//	public boolean judgeIllnessExist(illnessVo illnessVo){
//		doctorIllnessRelationVO doctorIllnessRelationVO = new doctorIllnessRelationVO();
//		doctorIllnessRelationVO.setLevel_1(illnessVo.getLevel_1());
//		doctorIllnessRelationVO.setLevel_2(illnessVo.getLevel_2());
//		illnessVo=illnessDao.findSysIllnessInfo(doctorIllnessRelationVO);
//		if(illnessVo !=null ){
//			return true;
//		}
//		return false;
//	}

	/**
	 * 获取疾病库列表
	 * @param illnessVoPage
	 * @return
	 */
	@Override
	public Page<IllnessVo> findAllIllness(Page<IllnessVo> illnessVoPage,HashMap<String, Object> searchMap){
		//获取疾病库列表
		Page<IllnessVo> resultPage = illnessDao.findAllIllnessList(illnessVoPage,searchMap);
		return resultPage;
	}

	//根据疾病表主键更新疾病信息
	@Override
	public void updateIllness(IllnessVo illnessVo){
		illnessDao.updateIllness(illnessVo);
	}

	//根据疾病id删除疾病
	@Override
	public void deleteIllnessById(IllnessVo illnessVo){
		illnessDao.deleteIllnessById(illnessVo);
		//删除所有医生与该疾病的关联关系
		doctorIllnessRelationDao.deleteDoctorAndIllnessRelationByIllnessId(illnessVo);
	}

}
