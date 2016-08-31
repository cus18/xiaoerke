package com.cxqm.xiaoerke.modules.interaction.dao;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.interaction.entity.PatientRegisterPraise;
import com.cxqm.xiaoerke.modules.interaction.entity.PraiseVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    List<Map> getUserEvaluate(Map<String, Object> params);

    //根据doctorVo中的参数获取对医生的评价信息
    List<PraiseVo> findDoctorDetailPraiseInfo(PraiseVo praisevo);
    
    Integer updateCustomerEvaluation(Map<String, Object> params);
    
    Map<String,Object> selectCustomerEvaluation(@Param("id")String id);
    
    Map<String,Object> getCustomerStarInfoById(@Param("doctorId")String id);

    Map<String,Object> getCustomerStarSingById(@Param("doctorId")String id);

    Map<String,Object> getCustomerStarCountById(@Param("doctorId")String id);
    
    Map<String,Object> getDoctorHeadImageURIById(@Param("doctorId")String id);

    //根据条件查询评价信息sunxiao
    List<Map<String,Object>> getCustomerEvaluationListByInfo(Map map);

    /**
     * 医生详情页--获取电话咨询评价总数
     * @param dataMap
     * @return
     * @author chenxiaoqiong
     */
    int getTotalCount(HashMap<String, Object> dataMap);

    /**
     * 医生详情页--获取n条电话咨询评价
     * @param params
     * @return
     * @author chenxiaoqiong
     */
    List<HashMap<String,Object>> getDoctorEvaluateTop(HashMap<String, Object> params);

    /**
     * 医生详情页--获取所有电话咨询评价（带分页）
     * @param dataMap
     * @param page
     * @return
     * @author chenxiaoqiong
     */
    Page<PatientRegisterPraise> getDoctorEvaluate(HashMap<String, Object> dataMap, Page<PatientRegisterPraise> page);

    /**
     * 查询送心意数据
     * @param map
     * @return
     * @author sunxiao
     */
    List<Map<String,Object>> findReceiveTheMindList(Map map);

    /**
     * 查询差评数据
     * @param map
     * @return
     * @author sunxiao
     */
    List<Map<String,Object>> findDissatisfiedList(Map map);

    List<Map<String, Object>> findDoctorEvaluationById(Map<String, Object> map);

    List<Map<String, Object>> findDoctorAllEvaluationById(Map<String, Object> map);

}