package com.cxqm.xiaoerke.modules.order.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.consult.sdk.CCPRestSDK;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.healthRecords.entity.BabyIllnessInfoVo;
import com.cxqm.xiaoerke.modules.healthRecords.service.HealthRecordsService;
import com.cxqm.xiaoerke.modules.order.dao.*;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneManuallyConnectVo;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import com.cxqm.xiaoerke.modules.order.exception.CancelOrderException;
import com.cxqm.xiaoerke.modules.order.exception.CreateOrderException;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.cxqm.xiaoerke.modules.sys.utils.DoctorMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private SystemService systemService;

    @Autowired
    private ConsultPhoneTimingDialDao consultPhoneTimingDialDao;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;
            
    @Autowired
    private ConsultPhoneManuallyConnectRecordDao consultPhoneManuallyConnectRecordDao;

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService;


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
                .getDoctorExpertiseById((String) registerInfo.get("doctorId"),(String) registerInfo.get("hospitalId") , null));
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
    public String createConsultOrder(String babyName, String phoneNum, String illnessDesc, String doctorId) throws CreateOrderException {
        HashMap<String, Object> doctorInfo = consultDoctorInfoService.getConsultDoctorHomepageInfo(doctorId);
        ConsultPhoneRegisterServiceVo vo = new ConsultPhoneRegisterServiceVo();
        User user = systemService.getUserById(doctorId);
        vo.setBabyName(babyName);
        vo.setPhoneNum(phoneNum);
        vo.setCreateTime(new Date());
        vo.setSysPatientId(user.getLoginName());
        consultPhoneRegisterServiceDao.insertSelective(vo);


//        HashMap<String, Object> result = CCPRestSDK.callback(phoneNum,user.getLoginName(),
//                "01057115120", "01057115120", null,
//                "true", null, 123+"",
//                "15", null, "0",
//                "1", "10", null);
//        String statusCode = (String) result.get("statusCode");
        return vo.getId().toString();
    }

    @Override
    public List<HashMap<String, Object>> getOrderList(String userId) {
        return consultPhoneRegisterServiceDao.getPhoneConsultaList(userId,null);
    }


    @Override
    @Transactional(rollbackFor=Exception.class)
    public Float cancelOrder(Integer phoneConsultaServiceId,String cancelReason,String cancelState) throws CancelOrderException {
        int sysOrderState = 0;
//        Float price = 0f;
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

        //取消订单
        Map<String,Object> map = consultPhoneRegisterServiceDao.getPhoneConsultaServiceIndo(phoneConsultaServiceId);

        ConsultPhoneRegisterServiceVo consultPhoneRegisterServiceVo= new ConsultPhoneRegisterServiceVo();
        consultPhoneRegisterServiceVo.setState("4");
        consultPhoneRegisterServiceVo.setUpdateTime(new Date());
        consultPhoneRegisterServiceVo.setId(phoneConsultaServiceId);
        consultPhoneRegisterServiceVo.setDeleteBy(UserUtils.getUser().getName());
        int state = consultPhoneRegisterServiceDao.updateByPrimaryKeySelective(consultPhoneRegisterServiceVo);
        //取消号源
        if(state>0){
            sysOrderState = sysConsultPhoneServiceDao.cancelOrder((Integer)map.get("sys_phoneConsult_service_id"),cancelState);
        }
        //退钱
        if(sysOrderState>0){
            String doctorName = (String) map.get("doctorName");
            String doctorPhone = (String) map.get("doctorPhone");
            String babyName = (String) map.get("babyName");
            String date = (String) map.get("date");
            String beginTime = (String) map.get("beginTime");
            String week = DateUtils.getWeekOfDate(DateUtils.StrToDate((String) map.get("date"), "yyyy/MM/dd"));

            //发消息
            Map<String,Object> parameter = systemService.getWechatParameter();
            String token = (String)parameter.get("token");
            String url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() +"/titan/phoneConsult#/orderDetail"+(String) map.get("doctorId")+","+(Integer) map.get("orderId")+",phone";
            PatientMsgTemplate.consultPhoneCancel2Wechat(doctorName,date, week, beginTime,(String) map.get("endTime"), (String) map.get("phone"),(String) map.get("orderNo"),(Float) map.get("price")+"",(String) map.get("openid"),token,url);
            PatientMsgTemplate.consultPhoneRefund2Msg(doctorName, (Float) map.get("price")+"", (String) map.get("phone"),(String) map.get("date"), week, (String) map.get("beginTime"),(String) map.get("orderNo"));

            if("1".equals((String)map.get("status")) && "3".equals((String)map.get("types"))) {
                //医生-短信
                DoctorMsgTemplate.cancelDoctorPhoneConsult2Sms(doctorName, babyName, doctorPhone, date, week, beginTime);
                //医生-微信
                Map<String, Object> tokenMap = systemService.getDoctorWechatParameter();
                String doctorToken = (String) tokenMap.get("token");
                String openId = doctorInfoService.findOpenIdByDoctorId((String) map.get("doctorId"));
                if (StringUtils.isNotNull(openId)) {
                    DoctorMsgTemplate.cancelDoctorPhoneConsult2Wechat(babyName, doctorName, date, week, beginTime, doctorToken, "", openId);
                }
            }
        }
        if(sysOrderState== 0){
                throw  new CancelOrderException();
        }

        return (Float)map.get("price");
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
        for(ConsultPhoneRegisterServiceVo tvo : pages.getList()){
            tvo.setSurplusDate(new Date(tvo.getSurplusTime()));
            Map map = new HashMap();
            map.put("orderId",tvo.getId());
            map.put("state",1);
            List<ConsultPhoneManuallyConnectVo> list = consultPhoneTimingDialDao.getConsultPhoneTimingDialByInfo(map);
            if(list.size()!=0){
                tvo.setState("daichonglian");
            }
        }
        return pages;
    }

    /**
     * 分页查询电话咨询订单列表
     * sunxiao
     */
    @Override
    public int getNewOrderCount(String state){
        // TODO Auto-generated method stub
        int count = consultPhoneRegisterServiceDao.getNewOrderCount(state);
        return count;
    }

    public List<ConsultPhoneRegisterServiceVo> getAllConsultPhoneRegisterListByInfo(ConsultPhoneRegisterServiceVo vo){
        List<ConsultPhoneRegisterServiceVo> list = consultPhoneRegisterServiceDao.getAllConsultPhoneRegisterListByInfo(vo);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        for(ConsultPhoneRegisterServiceVo temp : list){
            temp.setConsultPhoneTimeFromStr(DateUtils.DateToStr(temp.getDate(), "date") + " " + DateUtils.DateToStr(temp.getBeginTime(), "time"));
            temp.setType(temp.getPrice() + "元/" + temp.getType() + "min");
            temp.setConsultPhoneTimeToStr(sdf.format(new Date(temp.getSurplusTime())));
            temp.setOrderTimeFromStr(DateUtils.DateToStr(temp.getCreateTime(),"datetime"));
            if("0".equals(temp.getState())){
                temp.setState("待支付");
                temp.setPayState("待支付");
            }else if("1".equals(temp.getState())){
                temp.setState("待接通");
                temp.setPayState("已付款");
            }else if("2".equals(temp.getState())){
                temp.setState("待评价");
                temp.setPayState("已付款");
            }else if("3".equals(temp.getState())){
                temp.setState("待分享");
                temp.setPayState("已付款");
            }else if("4".equals(temp.getState())){
                temp.setState("已取消");
                temp.setPayState("已退款");
            }
            Map map = new HashMap();
            map.put("orderId", temp.getId());
            map.put("state", 1);
            List<ConsultPhoneManuallyConnectVo> clist = consultPhoneTimingDialDao.getConsultPhoneTimingDialByInfo(map);
            if(clist.size()!=0){
                temp.setState("等待重连");
                temp.setPayState("已付款");
            }
            if(temp.getUpdateTime()!=null){
                temp.setOrderTimeToStr(DateUtils.DateToStr(temp.getUpdateTime(), "datetime"));
            }
        }
        return list;
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
        Map orderTimeMap = new HashMap();
        for(Map<String, Object> temp : orderList){
            orderTimeMap.put(temp.get("date"), orderTimeMap.containsKey(temp.get("date")) ? orderTimeMap.get(temp.get("date")) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + temp.get("beginTime").toString().substring(0, 5) + "--" + temp.get("endTime").toString().substring(0, 5) : temp.get("beginTime").toString().substring(0, 5)+"--"+temp.get("endTime").toString().substring(0,5));
        }
        Map paramMap = new HashMap();
        paramMap.put("orderId",vo.getId());
        List<ConsultPhoneManuallyConnectVo> recordList = consultPhoneManuallyConnectRecordDao.getManuallyConnectRecordListByInfo(paramMap);
        for(ConsultPhoneManuallyConnectVo cvo : recordList){
            cvo.setSurplusDate(new Date(cvo.getSurplusTime()));
        }
        map = consultInfoList.get(0);
        map.put("surplusTime",new Date((Long)map.get("surplusTime")));
        map.put("orderTimeMap",orderTimeMap);
        map.put("recordList",recordList);
        return map;
    }

    /**
     * 手动接通电话
     * sunxiao
     * @param vo
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public JSONObject manuallyConnect(ConsultPhoneManuallyConnectVo vo) throws Exception{
        JSONObject result = new JSONObject();
        boolean tip = true;
        if("immediatelyDial".equals(vo.getDialType())){
            dialConnection(vo);
            vo.setDialDate(new Date());
        }else if("timingDial".equals(vo.getDialType())){
            Map param = new HashMap();
            param.put("orderId",vo.getOrderId());
            param.put("state","1");
            List<ConsultPhoneManuallyConnectVo> list = consultPhoneTimingDialDao.getConsultPhoneTimingDialByInfo(param);
            if(list.size()==0){
                vo.setState("1");//待拨打
                consultPhoneTimingDialDao.saveConsultPhoneTimingDialInfo(vo);
            }else{
                result.put("result","已设置定时拨打！");
                tip = false;
            }
        }
        if(tip){
            vo.setOperBy(UserUtils.getUser().getName());
            consultPhoneManuallyConnectRecordDao.saveManuallyConnectRecordInfo(vo);
        }
        return result;
    }

    @Override
    public List<ConsultPhoneManuallyConnectVo> getConsultPhoneTimingDialByInfo(Map<String, Object> map) {
        List<ConsultPhoneManuallyConnectVo> list = consultPhoneTimingDialDao.getConsultPhoneTimingDialByInfo(map);
        return list;
    }

    @Override
    public int updateConsultPhoneTimingDialInfo(ConsultPhoneManuallyConnectVo vo) {
        int count = consultPhoneTimingDialDao.updateConsultPhoneTimingDialInfo(vo);
        return count;
    }

    /**
     * 电话咨询拨打用户和医生电话
     * @return
     */
    private String dialConnection(ConsultPhoneManuallyConnectVo vo){
        long conversationLength = vo.getSurplusTime()/1000;

//        CCPRestSDK sdk = new CCPRestSDK();
//        sdk.init("sandboxapp.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
//        sdk.setSubAccount("2fa43378da0a11e59288ac853d9f54f2", "0ad73d75ac5bcb7e68fb191830b06d6b");
//        sdk.setAppId("aaf98f8952f7367a0153084e29992035");

        HashMap<String, Object> result = CCPRestSDK.callback(vo.getDoctorPhone(), vo.getUserPhone(),
                "01057115120", "01057115120", null,
                "true", null, vo.getOrderId() + "",
                conversationLength + "", null, "0",
                "1", "60", null);
        String statusCode = (String) result.get("statusCode");
        return statusCode;
    }

    @Override
    public ConsultPhoneRegisterServiceVo selectByPrimaryKey(Integer phoneConsultaServiceId){
       return  consultPhoneRegisterServiceDao.selectByPrimaryKey(phoneConsultaServiceId);
    }

    @Override
    public int CancelAppointNoPay() {
        consultPhoneRegisterServiceDao.cancelAppointNoPayRegiste();
      return  consultPhoneRegisterServiceDao.cancelAppointNoPayOrder();

    }
}
