package com.cxqm.xiaoerke.modules.interaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.modules.interaction.entity.PraiseVo;

public interface PatientRegisterPraiseService {

	List<HashMap<String, Object>> findAllPraiseByDoctorId(PraiseVo PraiseVo);

	String customerEvaluation(Map<String, Object> params, String openId);

	void getUserEvaluate(Map<String, Object> params,
			HashMap<String, Object> response);

	Map<String, Object> orderPraiseOperation(Map<String, Object> params,
			HttpSession session, HttpServletRequest request);
	
	//根据doctorVo中的参数获取对医生的评价信息
    List<PraiseVo> findDoctorDetailPraiseInfo(PraiseVo praisevo);
    
    void insertCancelReason(HashMap<String, Object> executeMap);
    
    Integer updateCustomerEvaluation(Map<String, Object> params);
    
    Map<String,Object> selectCustomerEvaluation(@Param("id")String id);
    
    Map<String,Object> getCustomerStarInfoById(@Param("doctorId")String id);
    
    Map<String,Object> getDoctorHeadImageURIById(@Param("doctorId")String id);

    /**
     * 医生详情页--获取电话咨询评价总数
     * @param params
     * @return
     * @author chenxiaoqiong
     */
    Integer getTotalCount(HashMap<String, Object> params);

    /**
     * 医生详情页--获取一条电话咨询评价
     * @param params
     * @return
     * @author chenxiaoqiong
     */
    HashMap<String,Object> getConsultEvaluateTop(HashMap<String, Object> params);

    /**
     * 医生详情页--获取所有电话咨询评价（带分页）
     * @param params
     * @return
     * @author chenxiaoqiong
     */
    HashMap<String,Object> getConsultEvaluate(HashMap<String,Object> params);
}
