package com.cxqm.xiaoerke.modules.nonRealTimeConsult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorDepartmentVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultRecordVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultSessionVo;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public interface NonRealTimeConsultService {

    List<NonRealTimeConsultSessionVo> selectByNonRealTimeConsultSessionVo(NonRealTimeConsultSessionVo realTimeConsultSessionVo);

    List<NonRealTimeConsultRecordVo> selectSessionRecordByVo(NonRealTimeConsultRecordVo nonRealTimeConsultRecordVo);

    /**
     * 查找所有科室
     *
     * */

    List<ConsultDoctorDepartmentVo> departmentList();

    /**
     * 查找所有明星医生
     * */
    List<ConsultDoctorInfoVo> starDoctorList();

    /**
     * 查找所有明星医生主页资料
     * */
    HashMap<String,Object> starDoctorInfo(String userId);


    /**
     * 获取用户宝宝信息
     * */
    BabyBaseInfoVo babyBaseInfo(String openid);

    /**
     * 获取用户宝宝信息
     * */
    void saveBabyBaseInfo(BabyBaseInfoVo babyBaseInfoVo);

    /**
     * 创建会话。保存宝宝问诊信息
     * */
    HashMap<String, Object> createSession(String csUserId,String openid,String content);

    /**
     * 定时器- 每天一次
     * 每天更新医生的咨询量 以及好评度
     * */
    void updateConsultDoctorInfo();

    /**
     * 患者医生间的咨询消息
     * */
    void savenConsultRecord(Integer sessionid, String userId, String fromType, String content, String msgtype);

    /**
     * 上传文件
     * */
    HashMap<String, Object> uploadMediaFile( MultipartFile file);

    /**
     * 根据部门去查询医生
     * */
    List<ConsultDoctorInfoVo> getDoctorListByDepartment(String departmentName);

    /**
     * 定时器 -每小时一次
     * 将36小时后的会话关闭
     * */
    void sessinTimeOut();

    //记录评价信息
    void saveCustomerEvaluation(String openid, String doctorid, String sessionid);

    String getNonRealtimeCustomerId(Integer sessionid);

    void sendRemindDoctor(String doctorId ,String userName);

    void sendRemindUser(NonRealTimeConsultSessionVo nonRealTimeConsultSessionVo);
}
