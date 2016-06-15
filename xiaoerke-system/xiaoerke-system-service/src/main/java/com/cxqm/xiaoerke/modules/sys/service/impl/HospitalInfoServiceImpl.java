package com.cxqm.xiaoerke.modules.sys.service.impl;


import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorDao;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorHospitalRelationDao;
import com.cxqm.xiaoerke.modules.sys.dao.HospitalDao;
import com.cxqm.xiaoerke.modules.sys.dao.SysHospitalContactDao;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysHospitalContactVo;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = false)
public class HospitalInfoServiceImpl implements HospitalInfoService {

    @Autowired
    private HospitalDao hospitalDao;

	@Autowired
	private SysHospitalContactDao sysHospitalContactDao;

	@Autowired
	private DoctorDao doctorDao;

	@Autowired
	private DoctorHospitalRelationDao doctorHospitalRelationDao;

	 //查询系统内部所有医院
	@Override
    public Page<HashMap<String, Object>> findPageAllHospital(Page<HashMap<String, Object>> page) {
		return hospitalDao.findPageAllHospital(page);
    }

    @Override
    public Map<String,Object> listSecondIllnessHospital(Map<String,Object> params)
	{
		HashMap<String, Object> response = new HashMap<String, Object>();

		String illnessSecondId = (String) params.get("illnessSecondId");
		String currentPage = ((String) params.get("pageNo"));
		String pageSize = ((String) params.get("pageSize"));
		String orderBy = (String) params.get("orderBy");
		String consultPhone = (String) params.get("consultPhone");

		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage,
				pageSize);
		HashMap<String, Object> secondIllnessInfo = new HashMap<String, Object>();
		secondIllnessInfo.put("orderBy", orderBy);
		secondIllnessInfo.put("illnessSecondId", illnessSecondId);
		secondIllnessInfo.put("consultPhone", consultPhone);
		Page<HashMap<String, Object>> resultPage = hospitalDao.findPageHospitalByIllnessSecond(secondIllnessInfo, page);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000045" + illnessSecondId);//获取二级分类疾病下的所有关联医院

		response.put("pageNo", resultPage.getPageNo());
		response.put("pageSize", resultPage.getPageSize());
		long tmp = FrontUtils.generatorTotalPage(resultPage);
		response.put("pageTotal", tmp + "");
		List<HashMap<String, Object>> list = resultPage.getList();
		List<HashMap<String, Object>> hospitalVoList = new ArrayList<HashMap<String, Object>>();
		if (list != null && !list.isEmpty()) {
			for (Map hospitalVoMap : list) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("hospitalId", hospitalVoMap.get("id"));
				dataMap.put("hospitalName", hospitalVoMap.get("name"));
				dataMap.put("hospitalLocation", hospitalVoMap.get("position"));
				hospitalVoList.add(dataMap);
			}
		}
		response.put("hospitalData", hospitalVoList);
		return response;
	}
    
	//获取一个医生所在医院的所在部门的全称
	@Override
	public String getDepartmentFullName(String doctorId, String hospitalId) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("doctorId", doctorId);
		hashMap.put("hospitalId", hospitalId);
		Map relationInfo = doctorHospitalRelationDao.getDepartmentName(hashMap);
		if (relationInfo != null && !(relationInfo.isEmpty())) {
			boolean a = StringUtils.isNotNull((String)relationInfo.get("department_level2") );
			if(a){
				return  relationInfo.get("department_level1") + "  " + relationInfo.get("department_level2");
			}else{
				return (String)relationInfo.get("department_level1");
			}
		} else {
			return "";
		}
	}

	@Override
	public HashMap<String,Object> getHospitalDetailInfo(String hospitalId) {
		HospitalVo hVo = new HospitalVo();
		hVo.setId(hospitalId);
		hVo = hospitalDao.getHospital(hVo);
		HashMap<String,Object> result = new HashMap<String,Object>();
		result.put("hospitalName",hVo.getName());
		result.put("hospitalDetails",hVo.getDetails()); //医院简介

		Map<String,Object> param = new HashMap<String,Object>();
		param.put("hospitalId",hospitalId);
		List<SysHospitalContactVo> contactVoList = sysHospitalContactDao.getHospitalContactByInfo(param);
		if(contactVoList.size()!=0){
			SysHospitalContactVo contactVo = contactVoList.get(0);
			result.put("contactName",contactVo.getContactName()); //合作机构联系人姓名
			result.put("contactPhone",contactVo.getContactPhone()); //合作机构联系人电话
			result.put("costReduction",contactVo.getCostReduction()); //费用减免
			result.put("greenChannel",contactVo.getGreenChannel()); //绿色通道
			result.put("limitStandard",contactVo.getLimitStandard()); //限价标准
			result.put("limitRange",contactVo.getLimitRange()); //限价范围
			result.put("limitDisease",contactVo.getLimitDisease()); //限价疾病
			result.put("chineseMedicine",contactVo.getChineseMedicine()); //中药
			result.put("westernMedicine",contactVo.getWesternMedicine()); //西药
			result.put("inspectionItems",contactVo.getInspectionItems()); //检查项目
			result.put("medicineFee",contactVo.getMedicineFee()); //药费
			result.put("inspectionFee",contactVo.getInspectionFee()); //检查费
			result.put("clinicItems",contactVo.getClinicItems());//诊疗项目
		}

		return result;
	}

	@Override
	@SystemServiceLog(description = "00000043")//获取医院所有医院
	public Map<String,Object> listAllHospital(Map<String,Object> params)
	{
		HashMap<String, Object> response = new HashMap<String, Object>();
		String currentPage = (String) params.get("pageNo");
		String pageSize = (String) params.get("pageSize");
		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage,
				pageSize);

		Page<HashMap<String, Object>> resultPage = hospitalDao.findPageAllHospital(page);

		long tmp = FrontUtils.generatorTotalPage(resultPage);
		response.put("pageTotal", tmp + "");
		response.put("pageNo", resultPage.getPageNo());
		response.put("pageSize", resultPage.getPageSize());

		List<HashMap<String, Object>> list = resultPage.getList();
		List<HashMap<String, Object>> hospitalVoList = new ArrayList<HashMap<String, Object>>();

		if (list != null && !list.isEmpty()) {
			for (Map hospitalMap : list) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("hospitalId", hospitalMap.get("id"));
				dataMap.put("hospitalName", hospitalMap.get("name"));
				dataMap.put("hospitalLocation", hospitalMap.get("position"));
				if(hospitalMap.containsKey("hospital_type"))
				{
					dataMap.put("hospitalType", hospitalMap.get("hospital_type"));
				}
				hospitalVoList.add(dataMap);
			}
		}
		response.put("hospitalData", hospitalVoList);

		return response;
	}

	@SystemServiceLog(description = "00000044")//获取医院所有科室
	public Map<String,Object> listHospitalDepartment(Map<String,Object> params)
	{
		HashMap<String, Object> response = new HashMap<String, Object>();

		String hospitalId = (String) params.get("hospitalId");
		String currentPage = ((String) params.get("pageNo"));
		String pageSize = ((String) params.get("pageSize"));
		String consultPhone = ((String) params.get("consultPhone"));


		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage,
				pageSize);
		HashMap<String, Object> hospitalInfo = new HashMap<String, Object>();
		hospitalInfo.put("hospitalId", hospitalId);
		hospitalInfo.put("consultPhone", consultPhone);

		Page<HashMap<String, Object>> resultPage = hospitalDao.findPageDepartmentByHospital(hospitalInfo, page);

		response.put("pageNo", resultPage.getPageNo());
		response.put("pageSize", resultPage.getPageSize());
		long tmp = FrontUtils.generatorTotalPage(resultPage);
		response.put("pageTotal", tmp + "");
		List<HashMap<String, Object>> list = resultPage.getList();
		List<HashMap<String, Object>> departmentDataList = new ArrayList<HashMap<String, Object>>();
		if (list != null && !list.isEmpty()) {
			for (Map departmentDataMap : list) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("hospitalId", departmentDataMap.get("hospitalId"));
				dataMap.put("hospitalName", departmentDataMap.get("hospitalName"));
				dataMap.put("departmentLevel1Name", departmentDataMap.get("departmentLevel1Name"));
				departmentDataList.add(dataMap);
			}
		}
		response.put("departmentData", departmentDataList);

		return response;
	}

	@Override
	public List<HospitalVo> findHospitalsByDoctorIds(String[] doctorIds) {
		return hospitalDao.findHospitalsByDoctorIds(doctorIds);
	}

	@Override
	public Page<HashMap<String, Object>> findPageHospitalByTime(
			HashMap<String, Object> hospitalInfo,
			Page<HashMap<String, Object>> page) {
		return hospitalDao.findPageHospitalByTime(hospitalInfo, page);
	}

	@Override
	public List<HashMap<String, Object>> findConsultHospitalByTime(Date date) {
		return hospitalDao.findConsultHospitalByTime(date);
	}

	@Override
	public HashMap<String, Object> findHospitalIdByHospitalName(
			HashMap<String, Object> hashMap) {
		return hospitalDao.findHospitalIdByHospitalName(hashMap);
	}

	@Override
	public List<HospitalVo> findAllOrderHospital(HashMap<String,Object> newMap){
		return hospitalDao.findAllOrderHospital(newMap);
	};

	@Override
	public int insertSysHospitalContactVo(
			SysHospitalContactVo sysHospitalContactVo) {
		return sysHospitalContactDao.insert(sysHospitalContactVo);
	}

	@Override
	public Page<HashMap<String, Object>> getHospitalListByConsulta(Page<HashMap<String, Object>> page) {
		return hospitalDao.findAllHospitalByConsulta(page);
	}

	//获取合作医院信息sunxiao
	@Override
	public List<SysHospitalContactVo> getHospitalContact(Map map){
		List<SysHospitalContactVo> list = sysHospitalContactDao.getHospitalContactByInfo(map);
		return  list;
	}

	@Override
	public Map<String, Object> listDepartmentHospital(Map<String, Object> params) {

		HashMap<String, Object> response = new HashMap<String, Object>();

		String departmentLevel1Name = (String) params.get("departmentLevel1Name");
		String currentPage = ((String) params.get("pageNo"));
		String pageSize = ((String) params.get("pageSize"));
		String consultPhone = ((String) params.get("consultPhone"));


		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage,
				pageSize);
		HashMap<String, Object> hospitalInfo = new HashMap<String, Object>();
		hospitalInfo.put("departmentLevel1Name", departmentLevel1Name);
		hospitalInfo.put("consultPhone", consultPhone);

		Page<HashMap<String, Object>> resultPage = hospitalDao.listDepartmentHospital(hospitalInfo, page);

		response.put("pageNo", resultPage.getPageNo());
		response.put("pageSize", resultPage.getPageSize());
		long tmp = FrontUtils.generatorTotalPage(resultPage);
		response.put("pageTotal", tmp + "");
		List<HashMap<String, Object>> list = resultPage.getList();
		List<HashMap<String, Object>> departmentDataList = new ArrayList<HashMap<String, Object>>();
		if (list != null && !list.isEmpty()) {
			for (Map departmentDataMap : list) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("hospitalId", departmentDataMap.get("sys_hospital_id"));
				dataMap.put("hospitalName", departmentDataMap.get("hospitalName"));
				departmentDataList.add(dataMap);
			}
		}
		response.put("departmentData", departmentDataList);
		return response;
	}
}
