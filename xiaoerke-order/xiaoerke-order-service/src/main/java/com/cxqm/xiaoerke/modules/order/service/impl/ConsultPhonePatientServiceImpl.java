package com.cxqm.xiaoerke.modules.order.service.impl;

import java.util.*;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.account.exception.BalanceNotEnoughException;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.healthRecords.entity.BabyIllnessInfoVo;
import com.cxqm.xiaoerke.modules.healthRecords.service.HealthRecordsService;
import com.cxqm.xiaoerke.modules.order.dao.ConsultPhoneTimingDialDao;
import com.cxqm.xiaoerke.modules.order.dao.PhoneConsultDoctorRelationDao;
import com.cxqm.xiaoerke.modules.order.dao.SysConsultPhoneServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneManuallyConnectVo;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import com.cxqm.xiaoerke.modules.order.exception.CreateOrderException;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.order.dao.ConsultPhoneRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private PhoneConsultDoctorRelationDao phoneConsultDoctorRelationDao;

    @Autowired
    private DoctorInfoService doctorInfoService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ConsultPhoneTimingDialDao consultPhoneTimingDialDao;


    /**
     * 查询电话咨询的订单
     * @param patientRegisterId
     * @return map
     * */

    @Override
    public Map<String,Object> getPatientRegisterInfo(Integer patientRegisterId){
        Map<String,Object> registerInfo = consultPhoneRegisterServiceDao.getPhoneConsultaServiceIndo(patientRegisterId);
        String position1 = (String)registerInfo.get("position1");
        String position = (String)registerInfo.get("position2");
        if(position1 != null && !"".equals(position1)){
            position = position1 + "、" + position;
        }
        registerInfo.put("position",position);
        registerInfo.put("expertise", doctorInfoService
                .getDoctorExpertiseById((String) registerInfo.get("doctorId"), null, null));
        // 根据医生ID和医院ID，获取医生的所处科室
        return registerInfo;
    }

    /**
     * 生成订单
     * */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public int PatientRegister(String openid, String babyId, String babyName, Date birthDay, String phoneNum, String illnessDesc, int sysConsultPhoneId) throws CreateOrderException{

//    先查询宝宝信息,有id就直接保存id,没有就先保存宝宝信息
        if(!StringUtils.isNotNull(babyId)){
            BabyBaseInfoVo babyVo = new BabyBaseInfoVo();
            babyVo.setBirthday(birthDay);
            babyVo.setName(babyName);
            babyVo.setState("0");
            babyVo.setOpenid(openid);
            babyVo.setUserid(UserUtils.getUser().getId());
            healthRecordsService.insertBabyInfo(babyVo);
            babyId = String.valueOf(babyVo.getId()) ; //!!!!!!!!!!!
        }

        BabyIllnessInfoVo illnessVo = new BabyIllnessInfoVo();
        illnessVo.setCreateTime(new Date());
        illnessVo.setDesc(illnessDesc);
        illnessVo.setStatus("0");
        illnessVo.setBabyinfoId(babyId);
        int illnessInfoId = healthRecordsService.insertBabyIllnessInfo(illnessVo);
        PatientVo PatientVo = utilService.CreateUser(UserUtils.getUser().getPhone(), "", "patient");
        SysConsultPhoneServiceVo sysConsultPhoneServiceVo = sysConsultPhoneServiceDao.selectByPrimaryKey(sysConsultPhoneId);
        ConsulPhonetDoctorRelationVo consulPhonetDoctorRelationVo = phoneConsultDoctorRelationDao.selectByDoctorId(sysConsultPhoneServiceVo.getSysDoctorId());
        ConsultPhoneRegisterServiceVo vo = new ConsultPhoneRegisterServiceVo();
        vo.setIllnessDescribeId(illnessVo.getId());
        vo.setCreateTime(new Date());
        String register_no = ChangzhuoMessageUtil.createRandom(true, 10);
        vo.setRegisterNo(register_no);
        vo.setState("0");
        vo.setSysPatientId(PatientVo.getId());
        vo.setSysPhoneconsultServiceId(sysConsultPhoneId);
        vo.setPhoneNum(phoneNum);
        vo.setCreat_by(UserUtils.getUser().getId());
        vo.setType("0");
        Integer serviceLength = consulPhonetDoctorRelationVo.getServerLength()*60*1000;
        vo.setSurplusTime(serviceLength);
        int result = consultPhoneRegisterServiceDao.insertSelective(vo);
        if(result== 0){
            throw new CreateOrderException();
        }
        SysConsultPhoneServiceVo sysVo = new SysConsultPhoneServiceVo();
        sysVo.setId(sysConsultPhoneId);
        sysVo.setState("1");
        sysVo.setUpdatedate(new Date());
        Integer sysState = sysConsultPhoneServiceDao.updateByPrimaryKeySelective(sysVo) ;
        if(sysState== 0){
            throw new CreateOrderException();
        }
        return vo.getId();
    }

    @Override
    public List<HashMap<String, Object>> getOrderList(String userId) {
        return consultPhoneRegisterServiceDao.getPhoneConsultaList(userId,null);
    }


    //    @Override
    public int cancelOrder(Integer phoneConsultaServiceId,String cancelReason) {
        int sysOrderState = 0;
        //取消订单
        ConsultPhoneRegisterServiceVo vo = consultPhoneRegisterServiceDao.selectByPrimaryKey(phoneConsultaServiceId);
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

    /**
     * 根据条件查询订单
     * sunxiao
     */
    @Override
    public List<Map<String, Object>> getConsultPhoneRegisterListByInfo(Map map) {
        // TODO Auto-generated method stub
        return consultPhoneRegisterServiceDao.getConsultPhoneRegisterListByInfo(map);
    }

    /**
     * 电话咨询取消预约退费
     * sunxiao
     * @param id
     */
    @Override
    public void refundConsultPhoneFee(String id,String cancelReason,Float price,String userId){
        int a = cancelOrder(Integer.valueOf(id),cancelReason);//取消预约
        if(a>0){//退费
            accountService.updateAccount(price*100, id, new HashMap<String, Object>(), false, userId,cancelReason);//补贴钱给用户
        }
    }

    /**
     * 获取手动接通页面信息
     * sunxiao
     * @param vo
     */
    @Override
    public Map manuallyConnectFormInfo(ConsultPhoneRegisterServiceVo vo) {
        Map map = new HashMap();
        map.put("id", vo.getId());
        List<Map<String, Object>> consultInfoList = getConsultPhoneRegisterListByInfo(map);
        map.remove("id");
        map.put("doctorId", vo.getDoctorId());
        map.put("fromDate","yes");
        List<Map<String, Object>> orderList = getConsultPhoneRegisterListByInfo(map);
        List list = new ArrayList();
        List orderTimeList = new ArrayList();
        Map orderTimeMap = new HashMap();
        for(Map<String, Object> temp : orderList){
            if(list.contains(temp.get("date"))){
                orderTimeMap.put(temp.get("date"),orderTimeMap.get(temp.get("date"))+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+temp.get("beginTime")+"-"+temp.get("endTime"));
            }else{
                orderTimeMap.put(temp.get("date"),temp.get("beginTime")+"-"+temp.get("endTime"));
            }
            list.add(temp.get("date"));
        }
        map = consultInfoList.get(0);
        map.put("orderTimeMap",orderTimeMap);
        return map;
    }

    /**
     * 手动接通电话
     * sunxiao
     * @param vo
     */
    @Override
    public void manuallyConnect(ConsultPhoneManuallyConnectVo vo){
        if("immediatelyDial".equals(vo.getDialType())){

        }else if("timingDial".equals(vo.getDialType())){

        }
    }
}
