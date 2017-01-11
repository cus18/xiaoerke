package com.cxqm.xiaoerke.modules.interaction.service;

import com.cxqm.xiaoerke.modules.interaction.entity.PraiseVo;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    Map<String,Object> select2016EvaluationByOpenId(@Param("openId") String openId);

    Map<String,Object> select2016EvaluationByOpenId_2(@Param("openId") String openId);

    Map<String,Object> getCustomerStarInfoById(@Param("doctorId")String id);

    Map<String,Object> getCustomerStarSingById(@Param("doctorId")String id);

    Map<String,Object> getCustomerStarCountById(@Param("doctorId")String id);
    
    Map<String,Object> getDoctorHeadImageURIById(@Param("doctorId")String id);

    //根据条件查询评价信息sunxiao
    List<Map<String,Object>> getCustomerEvaluationListByInfo(Map map);
    /**
     * 医生详情页--获取电话咨询评价总数
     * @param params
     * @return
     * @author chenxiaoqiong
     */
    Integer getTotalCount(HashMap<String, Object> params);

    /**
     * 获取n条电话咨询评价
     * @param params
     * @return
     * @author chenxiaoqiong
     */
    HashMap<String,Object> getDoctorEvaluateTop(HashMap<String, Object> params);

    /**
     * 医生详情页--获取所有电话咨询评价（带分页）
     * @param params
     * @return
     * @author chenxiaoqiong
     */
    HashMap<String,Object> getDoctorEvaluate(HashMap<String, Object> params);

    void saveCustomerEvaluation(Map<String, Object> params);

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

    public void sendRemindMsgToUser(String userId,String sessionId);

    List<Map<String,Object>> findDoctorEvaluationById(Map<String,Object> map);

    List<Map<String,Object>> findDoctorAllEvaluationByInfo(Map<String,Object> map);

    String getNonRealtimeCustomerId(Integer sessionid);
}
