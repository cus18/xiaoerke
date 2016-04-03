package com.cxqm.xiaoerke.modules.order.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.healthRecords.entity.BabyIllnessInfoVo;
import com.cxqm.xiaoerke.modules.healthRecords.service.HealthRecordsService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.order.dao.ConsultPhoneRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.SysConsultPhoneServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;

/**
 * Created by wangbaowei on 16/3/18.
 * 电话咨询订单相关的接口
 *
 */

@Service
public class ConsultPhonePatientServiceImpl implements ConsultPhonePatientService {

    @Autowired
    private ConsultPhoneRegisterServiceDao consultPhoneRegisterServiceDao;

    @Autowired
    private SysConsultPhoneServiceDao sysConsultPhoneServiceDao;

    @Autowired
    private UtilService utilService;

    @Autowired
    private HealthRecordsService healthRecordsService;


    /**
     * 查询电话咨询的订单
     * @param patientRegisterId
     * @return map
     * */

    @Override
    public Map<String,Object> getPatientRegisterInfo(Integer patientRegisterId){
      return  consultPhoneRegisterServiceDao.getPhoneConsultaServiceIndo(patientRegisterId);
    }

    /**
     * 生成订单
     * */
    @Override
    public int PatientRegister(String openid, String babyId, String babyName, Date birthDay, String phoneNum, String illnessDesc, int sysConsultPhoneId){

//    先查询宝宝信息,有id就直接保存id,没有就先保存宝宝信息
      if(!StringUtils.isNotNull(babyId)){
        BabyBaseInfoVo babyVo = new BabyBaseInfoVo();
        babyVo.setBirthday(birthDay);
        babyVo.setName(babyName);
        babyVo.setState("0");
        babyVo.setOpenid(openid);
        babyVo.setUserid(UserUtils.getUser().getId());
        babyId = String.valueOf(healthRecordsService.insertBabyInfo(babyVo)) ; //!!!!!!!!!!!
      }

      BabyIllnessInfoVo illnessVo = new BabyIllnessInfoVo();
      illnessVo.setCreateTime(new Date());
      illnessVo.setDesc(illnessDesc);
      illnessVo.setStatus("0");
      illnessVo.setBabyinfoId(babyId);
      int illnessInfoId = healthRecordsService.insertBabyIllnessInfo(illnessVo);

      PatientVo PatientVo = utilService.CreateUser(UserUtils.getUser().getPhone(),"", "patient");
      ConsultPhoneRegisterServiceVo vo = new ConsultPhoneRegisterServiceVo();
      vo.setIllnessDescribeId(illnessInfoId);
      vo.setCreateTime(new Date());
      String register_no = ChangzhuoMessageUtil.createRandom(true, 10);
      vo.setRegisterNo(register_no);
      vo.setState("0");
      vo.setSysPatientId(PatientVo.getId());
      vo.setSysPhoneconsultServiceId(sysConsultPhoneId);
      vo.setPhoneNum(phoneNum);
      return consultPhoneRegisterServiceDao.insertSelective(vo);
    }

    @Override
    public List<HashMap<String, Object>> getOrderList(String userId) {
      return consultPhoneRegisterServiceDao.getPhoneConsultaList(userId,null);
    }


//    @Override
    public int cancelOrder(Integer phoneConsultServiceId,String cancelReason) {
        int sysOrderState = 0;
     //取消订单
        ConsultPhoneRegisterServiceVo vo = consultPhoneRegisterServiceDao.selectByPrimaryKey(phoneConsultServiceId);
        vo.setState("6");
        vo.setUpdateTime(new Date());
        int state = consultPhoneRegisterServiceDao.updateByPrimaryKeySelective(vo);
        //取消号源
        if(state>0){
          sysOrderState = sysConsultPhoneServiceDao.cancelOrder(vo.getSysPhoneconsultServiceId(),"0");
        }
        //发消息


        return sysOrderState;
    }

    /**
     * 更新订单状态
     * */
    @Override
    public int updateOrderInfoBySelect(ConsultPhoneRegisterServiceVo vo) {
        return consultPhoneRegisterServiceDao.updateByPrimaryKeySelective(vo);
    }

    /**
     * 分页查询电话咨询订单列表
     * sunxiao
     */
	@Override
	public Page<ConsultPhoneRegisterServiceVo> findConsultPhonePatientList(
			Page<ConsultPhoneRegisterServiceVo> page,
			ConsultPhoneRegisterServiceVo vo) {
		// TODO Auto-generated method stub
		Page<ConsultPhoneRegisterServiceVo> pages = consultPhoneRegisterServiceDao.findConsultPhonePatientList(page,vo);
		return pages;
	}



}
