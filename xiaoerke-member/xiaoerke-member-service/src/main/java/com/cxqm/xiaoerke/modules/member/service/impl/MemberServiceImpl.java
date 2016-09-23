package com.cxqm.xiaoerke.modules.member.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.service.ServiceException;
import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.account.entity.AccountInfo;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.account.service.PayRecordService;
import com.cxqm.xiaoerke.modules.member.dao.MemberSendMessageDao;
import com.cxqm.xiaoerke.modules.member.dao.MemberServiceRelationDao;
import com.cxqm.xiaoerke.modules.member.dao.MemberservicerelItemservicerelRelationDao;
import com.cxqm.xiaoerke.modules.member.entity.MemberSendMessageVo;
import com.cxqm.xiaoerke.modules.member.entity.MemberServiceRelationVo;
import com.cxqm.xiaoerke.modules.member.entity.MemberservicerelItemservicerelRelationVo;
import com.cxqm.xiaoerke.modules.member.service.MemberService;
import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 会员服务 实现
 *
 * @author deliang
 * @version 2015-12-09
 */
@Service
@Transactional(readOnly = false)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberServiceRelationDao memberServiceRelationDao;

    @Autowired
    private MemberservicerelItemservicerelRelationDao memberservicerelItemservicerelRelationDao;

    @Autowired
    private MemberSendMessageDao memberSendMessageDao;

    @Autowired
    private SystemService systemService;

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private UserDao userDao;

    @Autowired
	private UserInfoService userInfoService;


    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Autowired
    AccountService accountService;

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    /**
     * 生成会员（来源：收费）
     *
     * @param memberType 会员类型 day、week、month、quarter、year
     * @param openId
     * @param userId
     * @param source
     * @param remark
     * @return status : 成功 true  失败  false
     */
    @Override
    public Boolean produceChargeMember(String memberType, String openId, String userId, String source, String remark) {
        Integer memberServiceId = produceChargeMemberByMemberType(memberType, openId, userId, source, remark);
        if (memberServiceId != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成会员（来源：关注、扫普通码）
     * 条件：预约机会在有效期内，不送会员 ， 有订单用户，不送会员  ,是黄牛 不送
     *
     * @param memberType 会员类型 day、week、month、quarter、year
     * @return status : 成功 true  失败  false
     */
    @Override
    public Boolean produceExtendMember(String memberType, String openId, String userId, String source, String remark) {
        List<MemberservicerelItemservicerelRelationVo> memberList = this.findMemberPropertyAppAvailable();
        if (memberList != null && memberList.size() > 0)//有会员记录
            return false;
        this.produceChargeMemberByMemberType(memberType, openId, userId, source, remark);
        return true;
    }

    /**
     * 获取会员属性
     *
     * @return list
     */
    @Override
    public List<MemberservicerelItemservicerelRelationVo> findMemberProperty() {
        MemberservicerelItemservicerelRelationVo vo = new MemberservicerelItemservicerelRelationVo();
        vo.setSysUserId(UserUtils.getUser().getId());
        List<MemberservicerelItemservicerelRelationVo> listVo = memberservicerelItemservicerelRelationDao.selectByUserId(vo);
        if (listVo != null && listVo.size() > 0) {
            return listVo;
        }
        return null;
    }

    /**
     * 根据会员服务id获取服务属性
     *
     * @return list
     */
    @Override
    public MemberservicerelItemservicerelRelationVo findMemberProperty(String memberServiceId) {
        MemberservicerelItemservicerelRelationVo Vo = memberservicerelItemservicerelRelationDao.selectByPrimaryKey(Integer.parseInt(memberServiceId));
        if (Vo != null) {
            return Vo;
        }
        return null;
    }

    /**
     * 获取会员属性（有效会员）
     *
     * @return list
     */
    @Override
    public List<MemberservicerelItemservicerelRelationVo> findMemberPropertyAppAvailable() {
        List<MemberservicerelItemservicerelRelationVo> listVo;
        MemberservicerelItemservicerelRelationVo vo = new MemberservicerelItemservicerelRelationVo();
        vo.setSysUserId(UserUtils.getUser().getId());//"ac6287c2126c4db59c07f1e2fb20a8ab"
        listVo = memberservicerelItemservicerelRelationDao.selectAvailableListByUserId(vo);
        if (listVo != null && listVo.size() > 0) {
            return listVo;
        }
        return null;
    }

    /**
     * 根据userId获取会员属性（有效会员）
     *
     * @return list
     */
    @Override
    public List<MemberservicerelItemservicerelRelationVo> findMemberPropertyAppAvailable(String userId) {
        List<MemberservicerelItemservicerelRelationVo> listVo;
        MemberservicerelItemservicerelRelationVo vo = new MemberservicerelItemservicerelRelationVo();
        vo.setSysUserId(userId);
        listVo = memberservicerelItemservicerelRelationDao.selectAvailableListByUserId(vo);
        if (listVo != null && listVo.size() > 0) {
            return listVo;
        }
        return null;
    }

    @Override
    public void sendExtendOldMemberWechatMessage(String openid) {
        Runnable thread = new sendExtendOldMemberWechatMessageThread(openid);
        threadExecutor.execute(thread);
    }

    public class sendExtendOldMemberWechatMessageThread extends Thread {
        private String openid;

        public sendExtendOldMemberWechatMessageThread(String openid) {
            super(sendExtendOldMemberWechatMessageThread.class.getSimpleName());
            this.openid = openid;
        }

        @Override
        public void run() {
            //先根据用户的openid判断，是否需要给用户推送消息
            MemberSendMessageVo memberSendMessageVo = new MemberSendMessageVo();
            memberSendMessageVo.setOpenid(openid);
            memberSendMessageVo = memberSendMessageDao.selectByOpenId(memberSendMessageVo);

            //新生成member_Send_Message记录
            if (memberSendMessageVo == null) {
                sendMemberURLMessage(openid);
                insertMemberSendMessage(openid, "1");
            } else {
                if (memberSendMessageVo.getStatus().equals("0")) {
                    sendMemberURLMessage(openid);
                    memberSendMessageVo.setStatus("1");//status 为1 表示消息已推送
                    memberSendMessageDao.updateByOpenid(memberSendMessageVo);
                }
            }
        }
    }

    /**
     * 扫码推送消息
     *
     * @param openid
     */
    @Override
    public void sendExtendScanMemberWechatMessage(String openid) {
        //先根据用户的openid判断，是否需要给用户推送消息
        MemberSendMessageVo memberSendMessageVo = new MemberSendMessageVo();
        memberSendMessageVo.setOpenid(openid);
        memberSendMessageVo = memberSendMessageDao.selectByOpenId(memberSendMessageVo);
        if (memberSendMessageVo == null) {//系统中没有记录 新生成
            insertMemberSendMessage(openid, "1");
            sendMemberURLMessage(openid);
        } else {//系统中有记录
            if (memberSendMessageVo.getStatus().equals("0")) {
                sendMemberURLMessage(openid);
            }
        }
    }

    /**
     * 生成会员推送消息记录
     *
     * @param openid
     * @param status 1 已推送 0 待推送
     * @return true 成功 false 失败
     */
    @Override
    public Boolean produceMemberWechatMessageRecord(String openid, String status) {
        MemberSendMessageVo memberSendMessageVo = new MemberSendMessageVo();
        memberSendMessageVo.setOpenid(openid);
        memberSendMessageVo = memberSendMessageDao.selectByOpenId(memberSendMessageVo);
        if (memberSendMessageVo == null) {
            insertMemberSendMessage(openid, status);
            return true;
        }
        return false;
    }

    /**
     * 更新会员可用次数
     *
     * @param id    主键
     * @param times 可正可负  如-1，表示会员次数-1  如1，表示会员次数+1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMemberLeftTimes(String id, String times) throws ServiceException {
        Integer updateTimes = Integer.parseInt(times);
        MemberservicerelItemservicerelRelationVo vo = new MemberservicerelItemservicerelRelationVo();
        vo.setId(Integer.parseInt(id));
        if (updateTimes < 0) {
            vo.setLeftTimes(-updateTimes);
            memberservicerelItemservicerelRelationDao.updateLeftTimesSubtractByPrimaryKey(vo);
        } else {
            vo.setLeftTimes(updateTimes);
            memberservicerelItemservicerelRelationDao.updateLeftTimesAddByPrimaryKey(vo);
        }
    }

    /**
     * 获取所有的会员服务信息
     *
     * @return
     */
    @Override
    public Page<MemberservicerelItemservicerelRelationVo> findMemberServiceList(Page<MemberservicerelItemservicerelRelationVo> page,MemberservicerelItemservicerelRelationVo mvo){
        Page<MemberservicerelItemservicerelRelationVo> pageList = memberservicerelItemservicerelRelationDao.findMemberServiceList(page,mvo);
        if(pageList.getList()!=null && pageList.getList().size()>0){
        	for(MemberservicerelItemservicerelRelationVo vo : pageList.getList()){
        		vo.setActivateDateStr(DateUtils.DateToStr(vo.getActivateDate(),"datetime"));
                vo.setServiceValidityPeriod(1+(vo.getEndDate().getTime()-new Date().getTime())/(1000*24*60*60));
                if(vo.getPeriod()==7){
                	vo.setType("周会员");
                }else if(vo.getPeriod()==31){
                	vo.setType("月会员");
                }else if(vo.getPeriod()==93){
                	vo.setType("季会员");
                }else if(vo.getPeriod()==366){
                	vo.setType("年会员");
                }
                if ("appoint".equals(vo.getSource())) {
                    vo.setIsPay("是");
                } else {
                    vo.setIsPay("否");
                }
                if("youxiao".equals(vo.getStatus())){
                	vo.setStatus("有效");
                }else if("shixiao".equals(vo.getStatus())){
                	vo.setStatus("失效");
                }else if("yituikuan".equals(vo.getStatus())){
                	vo.setStatus("已退款");
                }
        	}
            return pageList;
        }
        return new Page<MemberservicerelItemservicerelRelationVo>();
    }
    
    /**
     * 获取所有的会员服务信息
     * @return
     */
    @Override
    public List<MemberservicerelItemservicerelRelationVo> getAllMemberServiceList(MemberservicerelItemservicerelRelationVo mvo,String flag){
        List<MemberservicerelItemservicerelRelationVo> list = memberservicerelItemservicerelRelationDao.getAllMemberServiceList(mvo);
        List<MemberservicerelItemservicerelRelationVo> tempList = new ArrayList<MemberservicerelItemservicerelRelationVo>();
        if(list!=null && list.size()>0){
        	for(MemberservicerelItemservicerelRelationVo vo : list){
        		vo.setActivateDateStr(DateUtils.DateToStr(vo.getActivateDate(),"datetime"));
                vo.setServiceValidityPeriod(1+(vo.getEndDate().getTime()-new Date().getTime())/(1000*24*60*60));
                if(vo.getPeriod()==7){
                	vo.setType("周会员");
                }else if(vo.getPeriod()==31){
                	vo.setType("月会员");
                }else if(vo.getPeriod()==93){
                	vo.setType("季会员");
                }else if(vo.getPeriod()==366){
                	vo.setType("年会员");
                }
                if("appoint".equals(vo.getSource())){
                	vo.setIsPay("是");
                }else{
                	vo.setIsPay("否");
                }
                if("youxiao".equals(vo.getStatus())){
                	vo.setStatus("有效");
                }else if("shixiao".equals(vo.getStatus())){
                	vo.setStatus("失效");
                }else if("yituikuan".equals(vo.getStatus())){
                    if(flag.equals("exportData")){
                        MemberservicerelItemservicerelRelationVo tempVo = new MemberservicerelItemservicerelRelationVo();
                        tempVo.setId(vo.getId());
                        tempVo.setSource(vo.getSource());
                        tempVo.setNickName(vo.getNickName());
                        tempVo.setPhone(vo.getPhone());
                        tempVo.setCreateDate(vo.getCreateDate());
                        tempVo.setIsPay(vo.getIsPay());
                        tempVo.setServiceValidityPeriod(vo.getServiceValidityPeriod());
                        tempVo.setReservationPeriod(vo.getReservationPeriod());
                        tempVo.setActivateDate(vo.getActivateDate());
                        tempVo.setStatus("退会员费");
                        tempList.add(tempVo);
                    }
                    vo.setStatus("已退款");
                }
        	}
            list.addAll(tempList);
            return list;
        }
        return new ArrayList<MemberservicerelItemservicerelRelationVo>();
    }

    @Override
    public Boolean customerWithdrawMember(String memberservicerel_itemservicerel_relation_id, float price, String describe) {
        Integer memberId = Integer.parseInt(memberservicerel_itemservicerel_relation_id);
        MemberservicerelItemservicerelRelationVo vo = memberservicerelItemservicerelRelationDao.selectByPrimaryKey(memberId);
        if (vo.getSysUserId() != null && !vo.getSysUserId().equals("")) {

            AccountInfo accountInfo = accountService.findAccountInfoByUserId(vo.getSysUserId());
            //如果没有账户则先创建账户
            if (accountInfo == null) {
                accountInfo = new AccountInfo();
                accountInfo.setId(IdGen.uuid());
                accountInfo.setUserId(vo.getSysUserId());
                accountInfo.setOpenId("");
                accountInfo.setBalance(Float.parseFloat("0"));
                accountInfo.setCreatedBy(vo.getSysUserId());
                accountInfo.setCreateTime(new Date());
                accountInfo.setStatus("normal");
                accountInfo.setType("1");
                accountInfo.setUpdatedTime(new Date());
                accountService.saveOrUpdateAccountInfo(accountInfo);
            }

            //更改账户金额
            accountService.updateBalanceByUser(vo.getSysUserId(), price);
            //生成记录
            PayRecord payRecord = new PayRecord();
            payRecord.setUserId(vo.getSysUserId());
            payRecord.setId(IdGen.uuid());
            payRecord.setCreatedBy("1");
            payRecord.setStatus("return");
            payRecord.setMemberservicerel_itemservicerel_relation_id(memberId);
            payRecord.setFeeType("kefu");
            payRecord.setAmount(price);
            payRecord.setPayDate(new Date());
            payRecord.setReceiveDate(new Date());
            payRecordService.insertPayInfo(payRecord);
            //更改服务关系表status 为 shixiao
            vo.setStatus("yituikuan");
            vo.setId(memberId);
            vo.setRemark(describe);
            memberservicerelItemservicerelRelationDao.updateByPrimaryKey(vo);
            return true;
        }
        return false;
    }


    /**
     * 获取会员服务状态
     *
     * @return 状态1，会员服务已过期，免费卷已过期，免费卷没用完
     * 状态2，会员服务已过期，免费卷已过期，免费卷已用完
     * 状态10，会员服务已过期，免费卷已过期，免费卷没用完，且用户从没有过订单
     * <p/>
     * 状态3，会员服务已过期，免费卷没过期，免费卷已用完
     * 状态4，会员服务已过期，免费卷没过期，免费卷没用完
     * <p/>
     * <p/>
     * 状态7，会员服务没过期，免费卷没过期，免费卷已用完
     * 状态8，会员服务没过期，免费卷没过期，免费卷没用完
     * <p/>
     * 状态5，会员服务没过期，免费卷已过期，免费卷没用完
     * 状态6，会员服务没过期，免费卷已过期，免费卷已用完
     * 状态9，会员服务没过期，免费卷已过期，免费卷没用完，且用户从没有过订单
     */
    @Override
    public String getMemberServiceStatus(String openid) {
        String flag = "";//返回参数
        String flagStatus1 = "1";//免费券有没有用完判断参数

        Calendar ca = Calendar.getInstance();
        MemberservicerelItemservicerelRelationVo vo = new MemberservicerelItemservicerelRelationVo();
        if (openid.equals("")) {
            vo.setSysUserId(UserUtils.getUser().getId());
        } else {
            vo.setOpenid(openid);

        }
        //获取会员服务信息
        List<MemberservicerelItemservicerelRelationVo> memberProperty = memberservicerelItemservicerelRelationDao.getMemberServiceInfo(vo);
        if (memberProperty != null && memberProperty.size() > 0) {
            //查询会员服务有没有过期
            for (MemberservicerelItemservicerelRelationVo iVo : memberProperty) {
                if (flag.equals("")) {
                    if (iVo.getPeriodUnit().equals("day")) {
                        ca.add(Calendar.DATE, iVo.getPeriod());
                        Date afterDate = ca.getTime();
                        if (DateUtils.getDistanceMillisecondOfTwoDate(iVo.getActivateDate(), afterDate) > 0) {//如果会员没过期
                            for (MemberservicerelItemservicerelRelationVo mvo : memberProperty) {//会员没过期，免费券没过期
                                if (flag.equals("") && !flagStatus1.equals("3")) {
                                    if (DateUtils.getDistanceMillisecondOfTwoDate(new Date(), mvo.getEndDate()) > 0) {//免费券没过期
                                        flagStatus1 = "3";
                                    }
                                }
                            }
                            if (flagStatus1.equals("3")) {//免费券没过期
                                for (MemberservicerelItemservicerelRelationVo mvo : memberProperty) {//会员没过期，免费券没过期
                                    if (mvo.getLeftTimes() > 0) {
                                        flag = "8";
                                    }
                                }
                                if (flag.equals("")) {//免费券没过期，免费券用完了
                                    flag = "7";
                                }
                            } else {//免费券过期了
                                for (MemberservicerelItemservicerelRelationVo mvo : memberProperty) {
                                    if (mvo.getLeftTimes() > 0) {//免费券没有用完
                                        flag = "5";
                                    }
                                }
                                if (flag.equals("")) {//免费券用完了
                                    flag = "6";
                                }
                            }

                        }
                    }
                }
            }
            //会员已过期
            if (flag.equals("")) {
                //免费券没过期
                for (MemberservicerelItemservicerelRelationVo dvo : memberProperty) {
                    if (DateUtils.getDistanceMillisecondOfTwoDate(new Date(), dvo.getEndDate()) > 0) {//免费券没过期
                        if (!flagStatus1.equals("4") && flag.equals("")) {
                            flagStatus1 = "4";
                        }
                    }
                }
                if (flagStatus1.equals("4")) {//免费券没过期
                    for (MemberservicerelItemservicerelRelationVo mvo : memberProperty) {
                        if (mvo.getLeftTimes() > 0 && flag.equals("")) {
                            flag = "4";//免费券没用完
                        }
                    }
                    if (flag.equals("")) {//免费券用完了
                        flag = "3";
                    }
                } else {
                    if (flag.equals("")) {//免费券过期了
                        for (MemberservicerelItemservicerelRelationVo lvo : memberProperty) {
                            if (lvo.getLeftTimes() > 0 && flag.equals("")) {//免费券没有用完
                                flag = "1";
                            }
                        }
                    }
                    if (flag.equals("")) {//免费券过期了，免费券用完了
                        flag = "2";
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public Integer produceChargeMemberByMemberType(String memberType, String openId, String userId, String source, String remark) {
        MemberservicerelItemservicerelRelationVo vo = new MemberservicerelItemservicerelRelationVo();
        Calendar ca = Calendar.getInstance();
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("userId", userId);
        searchMap = userDao.findUserDetailInfoByUserIdExecute(searchMap);
        //生成会员与服务关系
        MemberServiceRelationVo memberServiceRelationVo = new MemberServiceRelationVo();
        memberServiceRelationVo.setSysUserId(userId);//"ac6287c2126c4db59c07f1e2fb20a8ab"
        memberServiceRelationVo.setCreateBy(userId);//"ac6287c2126c4db59c07f1e2fb20a8ab"
        memberServiceRelationVo.setOpenid(openId);
        memberServiceRelationVo.setCreateDate(new Date());
        memberServiceRelationVo.setPhone((String) searchMap.get("phone"));

        if (source.equals("appoint")) {
            memberServiceRelationVo.setSysActivityId(1);
        } else if (source.equals("week")) {
            memberServiceRelationVo.setSysActivityId(2);
        } else if (source.equals("month")) {
            memberServiceRelationVo.setSysActivityId(3);
        } else if (source.equals("quarter")) {
            memberServiceRelationVo.setSysActivityId(4);
        } else if (source.equals("bankend")) {
            memberServiceRelationVo.setSysActivityId(6);
        } else if (source.equals("zengsong")) {
            memberServiceRelationVo.setSysActivityId(5);
        }

        if ("week".equals(memberType)) {
            ca.add(Calendar.DATE, 7);
            vo.setLeftTimes(1);
            vo.setMemberItemserviceRelationId(1);
            memberServiceRelationVo.setMemberServiceId(1);//固定参数
        } else if ("month".equals(memberType)) {
            ca.add(Calendar.DATE, 31);
            vo.setLeftTimes(1);
            vo.setMemberItemserviceRelationId(2);
            memberServiceRelationVo.setMemberServiceId(2);//固定参数
        } else if ("quarter".equals(memberType)) {
            ca.add(Calendar.DATE, 93);
            vo.setLeftTimes(2);
            vo.setMemberItemserviceRelationId(3);
            memberServiceRelationVo.setMemberServiceId(3);//固定参数
        }
        if (memberServiceRelationVo != null) {
            memberServiceRelationDao.insert(memberServiceRelationVo);
            //生成  （会员与服务关系） 与 （服务与服务项关系） 关系表
            vo.setActivateDate(new Date());
            vo.setCreateDate(new Date());
            Date date = ca.getTime();
            vo.setSysUserId(userId);
            vo.setEndDate(date);
            vo.setRemark(remark);
            vo.setCreateBy(userId);
            vo.setMemberServiceRelationId(memberServiceRelationVo.getId());
            vo.setStatus("youxiao");
            memberservicerelItemservicerelRelationDao.insert(vo);
            return vo.getId();
        }
        return null;
    }

    /**
     * url推送会员专用  一点击链接就生成会员
     *
     * @param memberType
     * @param openId
     * @param source
     * @param remark
     * @return
     */
    @Override
    public Integer produceChargeMemberByMemberTypeForURL(String memberType, String openId, String source, String remark) {
        MemberservicerelItemservicerelRelationVo vo = new MemberservicerelItemservicerelRelationVo();
        Calendar ca = Calendar.getInstance();
        HashMap<String, Object> searchMap = new HashMap<String, Object>();

        //生成会员与服务关系
        MemberServiceRelationVo memberServiceRelationVo = new MemberServiceRelationVo();
        memberServiceRelationVo.setOpenid(openId);
        memberServiceRelationVo.setCreateDate(new Date());
        if (source.equals("appoint")) {
            memberServiceRelationVo.setSysActivityId(1);
        } else if (source.equals("week")) {
            memberServiceRelationVo.setSysActivityId(2);
        } else if (source.equals("month")) {
            memberServiceRelationVo.setSysActivityId(3);
        } else if (source.equals("quarter")) {
            memberServiceRelationVo.setSysActivityId(4);
        } else if (source.equals("bankend")) {
            memberServiceRelationVo.setSysActivityId(6);
        } else if (source.equals("zengsong")) {
            memberServiceRelationVo.setSysActivityId(5);
        }

        if ("week".equals(memberType)) {
            ca.add(Calendar.DATE, 7);
            vo.setLeftTimes(1);
            vo.setMemberItemserviceRelationId(1);
            memberServiceRelationVo.setMemberServiceId(1);//固定参数
        } else if ("month".equals(memberType)) {
            ca.add(Calendar.DATE, 31);
            vo.setLeftTimes(1);
            vo.setMemberItemserviceRelationId(2);
            memberServiceRelationVo.setMemberServiceId(2);//固定参数
        } else if ("quarter".equals(memberType)) {
            ca.add(Calendar.DATE, 93);
            vo.setLeftTimes(2);
            vo.setMemberItemserviceRelationId(3);
            memberServiceRelationVo.setMemberServiceId(3);//固定参数
        }
        if (memberServiceRelationVo != null) {
            memberServiceRelationDao.insert(memberServiceRelationVo);
            //生成  （会员与服务关系） 与 （服务与服务项关系） 关系表
            vo.setActivateDate(new Date());
            vo.setCreateDate(new Date());
            Date date = ca.getTime();
            vo.setEndDate(date);
            vo.setRemark(remark);
            vo.setMemberServiceRelationId(memberServiceRelationVo.getId());
            vo.setStatus("youxiao");
            memberservicerelItemservicerelRelationDao.insert(vo);
            return vo.getId();
        }
        return null;
    }

    /**
     * 根据openid查询userid phone更新会员记录
     * @param userId
     * @param openId
     * @return
     */
    @Override
    public Boolean updateMemberService(String userId, String openId) {
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("userId", userId);
        searchMap = userDao.findUserDetailInfoByUserIdExecute(searchMap);

        MemberservicerelItemservicerelRelationVo vo = new MemberservicerelItemservicerelRelationVo();
        MemberServiceRelationVo memberServiceRelationVo = new MemberServiceRelationVo();

        //根据openid查询 MemberServiceRelation 中 sys_user_id 和 phone为空的记录
        memberServiceRelationVo.setOpenid(openId);
        List<MemberServiceRelationVo> resultList = memberServiceRelationDao.selectByOpenId(memberServiceRelationVo);

        memberServiceRelationVo.setSysUserId(userId);
        memberServiceRelationVo.setCreateBy(userId);
        memberServiceRelationVo.setPhone((String) searchMap.get("phone"));
        vo.setSysUserId(userId);
        vo.setCreateBy(userId);

        if (resultList != null && resultList.size() > 0) {
            //正常情况下resultList只有一条记录
            for (MemberServiceRelationVo memberServiceRelation : resultList) {
                //更新MemberServiceRelation记录
                memberServiceRelation.setSysUserId(userId);
                memberServiceRelation.setCreateBy(userId);
                memberServiceRelation.setPhone((String) searchMap.get("phone"));
                memberServiceRelationDao.updateByPrimaryKeySelective(memberServiceRelation);
                //更新MemberservicerelItemservicerelRelation记录
                vo.setMemberServiceRelationId(memberServiceRelation.getId());
                vo.setSysUserId(userId);
                vo.setCreateBy(userId);
                memberservicerelItemservicerelRelationDao.updateByMemberServiceRelationId(vo);
            }
        }
        return true;
    }


    @Override
    public void sendMemberURLMessage(String openid) {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

        Map<String, Object> parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        //在此处根据openid给用户推送周会员推广消息
        String st = "感谢您对宝大夫的关注，宝大夫现对新用户推出周会员免费体验服务！\n" +
                "点击后登录并领取\n" +
                "【<a href='" + sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getTitanWebUrl()
                + "/titan/wechatInfo/getUserWechatMenId?url=23'>查看详情</a>】\n ";
//        WechatUtil.sendMsgToWechat(token, openid, st);
    }

    @Override
    public void insertMemberSendMessage(String openid, String status) {
        MemberSendMessageVo memberSendMessageVo;
        memberSendMessageVo = new MemberSendMessageVo();
        memberSendMessageVo.setStatus(status);//status 为1 表示消息已推送
        memberSendMessageVo.setCreateBy(UserUtils.getUser().getId());
        memberSendMessageVo.setCreateDate(new Date());
        memberSendMessageVo.setOpenid(openid);
        memberSendMessageDao.insert(memberSendMessageVo);

    }

    /**
     * 运维中添加会员
     * sunxiao
     *
     * @param vo
     * @return
     */
    @Override
    public Boolean giftMember(MemberservicerelItemservicerelRelationVo vo) {
        String phone = vo.getPhone();
        String source = vo.getSource();
        String type = vo.getType();
        User user = new User();
        user.setLoginName(phone);
        Map map = userDao.getUserByLoginName(user);
        String openid = "";
        String id = "";
        if (map == null) {
            // 如果没有查询到患者信息，则新建一个用户
            String uid = IdGen.uuid();
            User nuser = new User();
            nuser.setId(uid);
            nuser.setLoginName(phone);
            nuser.setPhone(phone);
            nuser.setCreateDate(new Date());
            userDao.insert(nuser);
            PatientVo patientVo = new PatientVo();
            String spid = IdGen.uuid();
            patientVo.setId(spid);
            patientVo.setSysUserId(uid);
            patientVo.setStatus("2");
            patientVo.setAccountNumber(0);
            userInfoService.savePatient(patientVo);
            openid = phone;
            id = uid;
        } else {
            openid = (String) map.get("openid");
            id = (String) map.get("id");
        }
        return produceExtendMember(type, openid, id, source, vo.getRemark());
    }

    /**
     * 会员列表中查看会员使用详情
     * sunxiao
     *
     * @param id
     * @return
     */
    @Override
    public List<Map<String, String>> memberUsageDetail(Integer id) {
        List<Map<String, String>> list = memberservicerelItemservicerelRelationDao.getMemberUsageDetail(id);
        for (Map<String, String> map : list) {
            if ("1".equals(map.get("status"))) {
                map.put("status", "待就诊");
            } else if ("2".equals(map.get("status"))) {
                map.put("status", "待评价");
            } else if ("3".equals(map.get("status"))) {
                map.put("status", "待分享");
            } else if ("6".equals(map.get("status"))) {
                map.put("status", "已取消");
            }

            if ("1".equals(map.get("keepChance"))) {
                map.put("keepChance", "是");
            } else {
                map.put("keepChance", "否");
            }
        }
        return list;
    }

}
