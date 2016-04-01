package com.cxqm.xiaoerke.modules.member.service;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.service.ServiceException;
import com.cxqm.xiaoerke.modules.member.entity.MemberservicerelItemservicerelRelationVo;

import java.util.List;
import java.util.Map;

public interface MemberService {

    /**
     * 生成会员（来源：收费）
     * @param memberType  会员类型 day、week、month、quarter、year
     * @return status : 成功 true  失败  false
     */
    Boolean produceChargeMember(String memberType, String openId, String userId,String source,String remark);

    /**
     * 生成会员（来源：关注、扫普通码）
     * @param memberType  会员类型 day、week、month、quarter、year
     * @return status : 成功 true  失败  false
     */
    Boolean produceExtendMember(String memberType, String openId, String userId,String source,String remark);

    /**
     * 获取会员属性列表
     * @return list
     */
    List<MemberservicerelItemservicerelRelationVo> findMemberProperty();

    /**
     * 根据会员服务id获取服务属性
     *
     * @return list
     */
    MemberservicerelItemservicerelRelationVo findMemberProperty(String memberServiceId);

    /**
     * 获取会员属性列表(有效会员)
     * @return list
     */
    List<MemberservicerelItemservicerelRelationVo> findMemberPropertyAppAvailable();

    List<MemberservicerelItemservicerelRelationVo> findMemberPropertyAppAvailable(String Userid);

    /**
     * 需求：首次 点击咨询 点击预约 点击知识库 推消息
     **/
    void sendExtendOldMemberWechatMessage(String openid) throws ServiceException;
    /**
     * 扫码推送消息
     * @param openid
     */
    void sendExtendScanMemberWechatMessage(String openid);

    /**
     * 生成会员推送消息记录
     * @param openid
     * @param status 1 已推送 0 待推送
     * @return true 成功 false 失败
     */
    Boolean produceMemberWechatMessageRecord(String openid, String status);

    /**
     * 更新会员可用次数
     * @param id  服务表主键
     * @param Times 可正可负  如-1，表示会员次数-1  如1，表示会员次数+1
     */
    void updateMemberLeftTimes(String id, String Times) throws ServiceException;

    /**
     *获取会员服务状态
     * @return
     * 状态1，会员服务已过期，免费卷已过期，免费卷没用完
     * 状态2，会员服务已过期，免费卷已过期，免费卷已用完
     * 状态3，会员服务已过期，免费卷没过期，免费卷已用完
     * 状态4，会员服务已过期，免费卷没过期，免费卷没用完
     * 状态5，会员服务没过期，免费卷已过期，免费卷没用完
     * 状态6，会员服务没过期，免费卷已过期，免费卷已用完
     * 状态7，会员服务没过期，免费卷没过期，免费卷已用完
     * 状态8，会员服务没过期，免费卷没过期，免费卷没用完
     * 状态9，会员服务没过期，免费卷已过期，免费卷没用完，且用户从没有过订单 (需在controller层判断 调用 patientRegisterService.judgeUserOrderRealtion 即可)
     * 状态10，会员服务已过期，免费卷已过期，免费卷没用完，且用户从没有过订单(需在controller层判断)
     */

    String getMemberServiceStatus(String openid) throws ServiceException;


    /**
     * 分页查询会员列表
     * @return
     */
    Page<MemberservicerelItemservicerelRelationVo> findMemberServiceList(Page<MemberservicerelItemservicerelRelationVo> page,MemberservicerelItemservicerelRelationVo vo);

    /**
     * 获取所有的会员服务信息
     * sunxiao
     * @return
     */
    List<MemberservicerelItemservicerelRelationVo> getAllMemberServiceList(MemberservicerelItemservicerelRelationVo page,String flag);

    Integer produceChargeMemberByMemberType(String memberType, String openId, String userId, String source, String remark);

    /**
     * 根据openid生成会员  url推送会员专用,一点击链接就生成会员
     * @param memberType
     * @param openId
     * @param source
     * @param remark
     * @return
     */
    Integer produceChargeMemberByMemberTypeForURL(String memberType, String openId, String source, String remark);

    /**
     * 根据openid、userid查询更新会员记录（phone，sys_user_id）
     * @param userId
     * @param openId
     * @return
     */
    Boolean updateMemberService(String userId, String openId);

    void sendMemberURLMessage(String openid);

    void insertMemberSendMessage(String openid, String status);

    Boolean giftMember(MemberservicerelItemservicerelRelationVo vo);

    /**
     * 客服退会员
     * @param memberservicerel_itemservicerel_relation_id 会员服务关系表主键
     * @param price 所退金额
     * @param describe 描述 退服务原因
     * @return 成功： true 失败: false
     */

    Boolean customerWithdrawMember(String memberservicerel_itemservicerel_relation_id,float price,String describe);

    List<Map<String, String>> memberUsageDetail(Integer id);
}
