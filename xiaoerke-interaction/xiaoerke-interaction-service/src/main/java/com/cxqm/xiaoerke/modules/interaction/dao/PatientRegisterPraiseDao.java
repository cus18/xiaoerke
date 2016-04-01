package com.cxqm.xiaoerke.modules.interaction.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.interaction.entity.PatientRegisterPraise;
import com.cxqm.xiaoerke.modules.interaction.entity.PraiseVo;

@MyBatisDao
public interface PatientRegisterPraiseDao {
    int deleteByPrimaryKey(String id);

    int insert(PatientRegisterPraise record);

    int insertSelective(PatientRegisterPraise record);

    PatientRegisterPraise selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PatientRegisterPraise record);

    int updateByPrimaryKey(PatientRegisterPraise record);

    List<HashMap<String,Object>> findAllPraiseByDoctorId(PraiseVo PraiseVo);

    void saveCustomerEvaluation(Map<String, Object> params);

    void saveQuestionnaireSurvey(List<HashMap<String, Object>> list);

    void PatientRegisterServiceIsPraise(HashMap<String, Object> executeMap);

    void insertCancelReason(HashMap<String, Object> executeMap);

    List<Map> getUserEvaluate(String doctorId);

    Page<HashMap<String,Object>> getConsultEvaluate(HashMap<String, Object> dataMap, Page<HashMap<String, Object>> page);

    //根据doctorVo中的参数获取对医生的评价信息
    List<PraiseVo> findDoctorDetailPraiseInfo(PraiseVo praisevo);
    
    Integer updateCustomerEvaluation(Map<String, Object> params);
    
    Map<String,Object> selectCustomerEvaluation(@Param("id")String id);
    
    Map<String,Object> getCustomerStarInfoById(@Param("doctorId")String id);
    
    Map<String,Object> getDoctorHeadImageURIById(@Param("doctorId")String id);
}