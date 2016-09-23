package com.cxqm.xiaoerke.modules.account.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.account.dao.AccountInfoDao;
import com.cxqm.xiaoerke.modules.account.dao.PayRecordDao;
import com.cxqm.xiaoerke.modules.account.dao.WithdrawRecordDao;
import com.cxqm.xiaoerke.modules.account.entity.AccountInfo;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.entity.Record;
import com.cxqm.xiaoerke.modules.account.entity.WithdrawRecord;
import com.cxqm.xiaoerke.modules.account.exception.AccountNotExistException;
import com.cxqm.xiaoerke.modules.account.exception.BalanceNotEnoughException;
import com.cxqm.xiaoerke.modules.account.exception.BusinessPaymentExceeption;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by wangbaowei on 15/11/5.
 */
@Service
@Transactional(readOnly = false)
public class AccountServiceImpl implements AccountService {

    public static String WITHDRAW_STATUS_SUCCESS = "success";

    @Autowired
    private PayRecordDao payRecordDao;

    @Autowired
    private WithdrawRecordDao withdrawRecordDao;

    @Autowired
    private AccountInfoDao accountInfoDao;

    @Autowired
    private SystemService systemService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;


    /**
     * 账户详情列表接口
     * 根据登录的userId 查询当前用户的记录
     */
    @Override
    public void getUserAccountInfo(HashMap<String, Object> response) {
        String userId = UserUtils.getUser().getId();
        //账户余额信息
        Float account = accountFund(userId);
        response.put("accountFund", account);

        //账户支入支出
        List<Record> itemizedAccountList = getAccountDetail(userId);
        List<Map> returnList = new ArrayList<Map>();
        for (Record record : itemizedAccountList) {
            Map<String, Object> detailBean = new HashMap<String, Object>();
            if (record instanceof WithdrawRecord) {
                WithdrawRecord withdrawRecord = (WithdrawRecord) record;
                Date createTime = withdrawRecord.getReceiveDate();
                String week = DateUtils.getWeekOfDate(createTime);
                String time = DateUtils.formatDateToStr(createTime, "yyyy/MM/dd");
                String hour = DateUtils.DateToStr(createTime, "time");
                detailBean.put("time", time + " " + week + " " + hour);
                detailBean.put("amount", "-" + withdrawRecord.getAmount() / 100);
                detailBean.put("title", withdrawRecord.getOperateType());
            } else if (record instanceof PayRecord) {
                PayRecord payRecord = (PayRecord) record;
                Date createTime = payRecord.getReceiveDate();
                String week = DateUtils.getWeekOfDate(createTime);
                String time = DateUtils.formatDateToStr(createTime, "yyyy/MM/dd");
                String hour = DateUtils.DateToStr(createTime, "time");
                payRecord.getAmount();
                String title = payRecord.getOperateType();
                if ("success".equals(payRecord.getStatus())) {
                    if ("wx".equals(payRecord.getPayType())) continue;
                    title = "预约了" + payRecord.getName() + "医生";
//                    title = "预约了医生";
                    detailBean.put("amount", "-" + payRecord.getAmount() / 100);
                } else if ("return".equals(payRecord.getStatus())) {
                    title = "取消预约" + payRecord.getName() + "医生";
//                    title = "取消预约医生";
                    detailBean.put("amount", "+" + payRecord.getAmount() / 100);
                } else if ("evaluation".equals(payRecord.getStatus())) {
                    title = "评价" + payRecord.getName() + "医生";
                    detailBean.put("amount", "+" + payRecord.getAmount() / 100);
                }
                detailBean.put("time", time + " " + week + " " + hour);
                detailBean.put("title", title);
            }
            returnList.add(detailBean);
        }
        response.put("itemizedAccountList", returnList);
    }

    /**
     * 调用企业支付接口,实现用户提现功能
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnUserMoney(Map<String, Object> params, Map<String, Object> response, HttpServletRequest request, HttpSession session)
            throws BalanceNotEnoughException, BusinessPaymentExceeption {
        Integer takeCashOut = Integer.parseInt((String) params.get("takeCashOut"));
        Float returnMoney = Float.valueOf(takeCashOut) * 100;
        //判断用于余额
        Float accountMoney = accountFund(UserUtils.getUser().getId());
        if (accountMoney >= returnMoney) {
            String openid = (String) session.getAttribute("openId");
            if (null == openid) {
                openid = CookieUtils.getCookie(request, "openId");
            }
            String ip = request.getRemoteAddr();
            String payResult = returnPay(returnMoney, openid, ip);
            response.put("return_msg", payResult);
            response.put("amount", returnMoney);
            if ("SUCCESS".equals(payResult.toUpperCase())) {
                Map tokenMap = systemService.getWechatParameter();
                String token = (String) tokenMap.get("token");
                PatientMsgTemplate.withdrawalsSuccess2Wechat(openid, token, "", String.valueOf(returnMoney / 100), DateUtils.DateToStr(new Date(), "date"));
            }
        } else {
            response.put("return_msg", "提现额度过大");
        }
    }

    //更新账户信息
    @Override
    public Float updateAccount(Float amount, String order, HashMap<String, Object> response, boolean isEvaluation, String userId, String reason) {
        //将本次订单的交易记录查询出来
        PayRecord payRecord = payRecordDao.selectSuccessOrderByOrderId(order);
        if (payRecord != null) {
            AccountInfo payInfo = accountInfoDao.selectAccountInfoByUserId(userId);
            if (amount == 0f) {
                amount = payRecord.getAmount();//操作记录支付金额
            }

            response.put("amount", amount / 100);
            if (payInfo == null)//如果账户为空则创建账户
            {
                payInfo = new AccountInfo();
                payInfo.setId(IdGen.uuid());
                payInfo.setUserId(userId);
                payInfo.setOpenId("");
                payInfo.setBalance(amount);
                payInfo.setCreatedBy(UserUtils.getUser().getId());
                payInfo.setCreateTime(new Date());
                payInfo.setStatus("normal");
                payInfo.setType("1");
                payInfo.setUpdatedTime(new Date());
            } else//账户不为空
            {
                payInfo.setBalance(payInfo.getBalance() + amount);
                payInfo.setUpdatedTime(new Date());
            }
            //将入账的资金加入账户表  更新或者创建账户表
            accountInfoDao.saveOrUpdate(payInfo);
            //更新入账信息表状态
            payRecord.setId(IdGen.uuid());
            if (isEvaluation) {
                payRecord.setStatus("evaluation");
                payRecord.setUserId(userId);
            } else {
                payRecord.setAmount(amount);
                payRecord.setUserId(userId);
                payRecord.setStatus("return");
            }
            if (StringUtils.isNotNull(reason)) {
                payRecord.setReason(reason);
            }
            payRecord.setReceiveDate(new Date());
            payRecordDao.insertSelective(payRecord);
        }
        return amount;
    }

    @Override
    public int updateByPrimaryKey(AccountInfo record) {
        return accountInfoDao.updateByPrimaryKey(record);
    }

    @Override
    public int updateBalanceByUser(@Param("userId") String userId, @Param("balance") Float balance) {
        return accountInfoDao.updateBalanceByUser(userId, balance);
    }

    @Override
    public Float accountFund(String userId) {
        AccountInfo payInfo = accountInfoDao.selectAccountInfoByUserId(userId);
        if (payInfo != null) {
            return payInfo.getBalance();
        }
        return 0f;
    }

    @Override
    public List<Record> getAccountDetail(String userId) {
        List<Record> payRecord = payRecordDao.selectPayRecordByUserId(userId);
        List<Record> withRecord = withdrawRecordDao.selectWithDrawRecordInfoByUserId(userId);
        payRecord.addAll(withRecord);
        Collections.sort(payRecord);
        return payRecord;
    }

    @Override
    public String returnPay(Float returnMoney, String openid, String ip) throws BusinessPaymentExceeption, BalanceNotEnoughException {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        //向withDraw表添加提现记录
        String userId = UserUtils.getUser().getId();
        //更改账户表account_info数据
        AccountInfo payInfo = accountInfoDao.selectAccountInfoByUserId(userId);
        if ((payInfo.getBalance() - returnMoney) >= 0) {
            String partner_trade_no = IdGen.uuid();//生成随机字符串
            WithdrawRecord withdrawRecord = buildWithdrawRecord(userId, returnMoney);
            withdrawRecord.setId(partner_trade_no);//将本地数据库和商户平台关联
            //保存用户提取记录
            withdrawRecordDao.insertSelective(withdrawRecord);
            payInfo.setBalance(payInfo.getBalance() - returnMoney);
            payInfo.setUpdatedTime(new Date());
            int status = accountInfoDao.saveOrUpdate(payInfo);
            if (status == 0) {
                throw new BalanceNotEnoughException();
            }
            //调用企业统一支付接口对用户进行退款

            SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
            parameters.put("mch_appid", sysPropertyVoWithBLOBsVo.getAppId());//APPid
            parameters.put("mchid",sysPropertyVoWithBLOBsVo.getPartner());
            String nonce_str = IdGen.uuid(); //Sha1Util.getNonceStr();//商户订单号
            parameters.put("nonce_str", nonce_str);
            parameters.put("partner_trade_no", partner_trade_no);
            parameters.put("check_name", "NO_CHECK");
            parameters.put("amount", returnMoney.toString());//金额
            parameters.put("desc", "退款");
            parameters.put("spbill_create_ip", ip);
            parameters.put("openid", openid);
            String sign = JsApiTicketUtil.createSign("UTF-8", parameters);
            parameters.put("sign", sign);
            String requestXML = JsApiTicketUtil.getRequestXml(parameters);
            try {
                String result = HttpRequestUtil.clientCustomSSLS(sysPropertyVoWithBLOBsVo.getTransfers(), requestXML,sysPropertyVoWithBLOBsVo);
                Map<String, String> returnMap = XMLUtil.doXMLParse(result);//解析微信返回的信息，以Map形式存储便于取值
                if (!"SUCCESS".equals(returnMap.get("result_code"))) {
                    LogUtils.saveLog(Servlets.getRequest(), "00000040", "用户微信提现失败:" + result);//用户微信提现失败
                    throw new BusinessPaymentExceeption();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessPaymentExceeption();
            }

            //判断返回值状态,如果失败则抛出异常 回滚事物
            LogUtils.saveLog(Servlets.getRequest(), "00000041", "用户微信提现:" + partner_trade_no);
            return "success";
        } else {
            throw new BusinessPaymentExceeption();
        }
    }

    @Override
    public void payByRemainder(Float amount, String openId, String patientRegisterId, String doctorId) {
        AccountInfo payInfo = accountInfoDao.selectAccountInfoByUserId(UserUtils.getUser().getId());

        //sql方面优化一下
        payInfo.setBalance(payInfo.getBalance() - amount);
        payInfo.setUpdatedTime(new Date());
        accountInfoDao.saveOrUpdate(payInfo);

        PayRecord payRecord = new PayRecord();
        User user = UserUtils.getUser();
        if (user != null) {
            payRecord.setUserId(user.getId());
        }
        String payRecordId = IdGen.uuid();
        payRecord.setId(payRecordId);
        payRecord.setOpenId(openId);
        payRecord.setOrderId(patientRegisterId);
        payRecord.setAmount(amount);
        payRecord.setPayType("selfAccount");
        payRecord.setStatus("success");
        payRecord.setFeeType("会员服务费");
        payRecord.setPayDate(new Date());
        payRecord.setReceiveDate(new Date());
        payRecord.setCreatedBy(user.getId());

        LogUtils.saveLog(Servlets.getRequest(), "00000039", "用户自身余额支付:" + payRecordId);//用户自身余额支付
        payRecord.setDoctorId(doctorId);
        payRecordDao.insertSelective(payRecord);
    }

    @Override
    public WithdrawRecord getWithdrawRecordDetail(String id) {
        WithdrawRecord record = withdrawRecordDao.selectDataById(id);
        return record;
    }

    @Override
    public Page<WithdrawRecord> getWithdrawRecords(String userId, Page<WithdrawRecord> page) {

        Page<WithdrawRecord> records = withdrawRecordDao.selectPageByUserId(userId, page);
        return records;
    }

    @Override
    public Page<WithdrawRecord> getAllWithdrawRecords(WithdrawRecord param, Page<WithdrawRecord> page) {
        Page<WithdrawRecord> records = withdrawRecordDao.selectPage(param, page);
        return records;
    }

    @Override
    public void withdrawSave(WithdrawRecord withdrawRecord) {
        withdrawRecordDao.updateByPrimaryKeySelective(withdrawRecord);
    }

    @Override
    public void withdrawDelete(String id) {
        withdrawRecordDao.deleteByPrimaryKey(id);
    }

    @Override
    public int withdraw(String userId, Float amount) throws AccountNotExistException, BalanceNotEnoughException {
        WithdrawRecord withdrawRecord = buildWithdrawRecord(userId, amount);
        AccountInfo accountInfo = accountInfoDao.selectAccountInfoByUserId(userId);
        if (accountInfo == null)
            throw new AccountNotExistException("account not exist.");

        int updatedItemsNum = accountInfoDao.updateBalanceByUser(userId, -amount);
        if (updatedItemsNum == 0)
            throw new BalanceNotEnoughException("balance not enough.");

        updatedItemsNum += withdrawRecordDao.insertSelective(withdrawRecord);

        return updatedItemsNum;
    }

    @Override
    public int createAccountInfo(String userId, Float amount) {
        AccountInfo payInfo = new AccountInfo();
        payInfo.setId(IdGen.uuid());
        payInfo.setUserId(userId);
        payInfo.setOpenId("");
        payInfo.setBalance(amount == null ? 0.0F : amount);
        payInfo.setCreatedBy(userId);
        payInfo.setCreateTime(new Date());
        payInfo.setStatus("normal");
        payInfo.setType("1");
        payInfo.setUpdatedTime(new Date());
        return accountInfoDao.insertSelective(payInfo);
    }

    @Override
    public int deleteAccountInfo(String userId) {
        return accountInfoDao.deleteByUser(userId);
    }


    /**
     * 调用微信的统一下单接口获取prepayInfo信息
     */
    @Override
    public Map<String, String> getPrepayInfo(HttpServletRequest request, HttpSession session, String serviceType) {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map<String, String> resultMap = new HashMap<String, String>();
        String openId = WechatUtil.getOpenId(session, request);
        //获取需要支付的金额  单位(分)
        String order_price = "";
        if(request.getAttribute("payPrice")!=null){
            float price = (Float.valueOf(request.getAttribute("payPrice").toString()))*100;
            order_price = String.valueOf((int) price);
        }else{
            order_price = request.getParameter("payPrice");
        }
        //生成的商户订单号
        String out_trade_no = IdGen.uuid();//Sha1Util.getNonceStr();
        String noncestr = IdGen.uuid();//Sha1Util.getNonceStr();//生成随机字符串\
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", sysPropertyVoWithBLOBsVo.getAppId());//微信服务号的appid
        parameters.put("mch_id", sysPropertyVoWithBLOBsVo.getPartner());//商户号
        parameters.put("nonce_str", noncestr);//随机字符串
        if (serviceType.equals("lovePlanService")) {
            parameters.put("body", "爱心捐款");//描述
        } else {
            parameters.put("body", "会员服务费");//描述
        }
        parameters.put("out_trade_no", out_trade_no);//商户订单号

        parameters.put("total_fee", order_price);//金额
        parameters.put("spbill_create_ip", request.getRemoteAddr());//终端ip

        if (serviceType.equals("appointService")) {
            parameters.put("notify_url", sysPropertyVoWithBLOBsVo.getNotifyAppointUrl());//通知地址
        } else if (serviceType.equals("insuranceService")) {
            parameters.put("notify_url", sysPropertyVoWithBLOBsVo.getNotifyInsuranceUrl());//通知地址
        } else if (serviceType.equals("customerService")) {
            parameters.put("notify_url", sysPropertyVoWithBLOBsVo.getNotifyCustomerUrl());//通知地址
        } else if (serviceType.equals("consultPhone")) {
            parameters.put("notify_url", sysPropertyVoWithBLOBsVo.getNotifyConsultphoneUrl());//通知地址
        } else if (serviceType.equals("umbrellaService")) {
            parameters.put("notify_url", sysPropertyVoWithBLOBsVo.getNotifyUmbrellaUrl());//通知地址
        } else if (serviceType.equals("lovePlanService")) {
            parameters.put("notify_url", sysPropertyVoWithBLOBsVo.getNotifyLoveplanUrl());//通知地址
        }
        if (serviceType.equals("doctorConsultPay")) {
            parameters.put("notify_url", sysPropertyVoWithBLOBsVo.getNotifyDoctorconsultpayUrl());//通知地址
        }
        parameters.put("trade_type", "JSAPI");//交易类型
        parameters.put("openid", openId);//用户标示
        //将上述参数进行签名
        String sign = JsApiTicketUtil.createSign("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = JsApiTicketUtil.getRequestXml(parameters);
        String result = HttpRequestUtil.httpsRequest(sysPropertyVoWithBLOBsVo.getGateurl(), "POST", requestXML);
        resultMap.put("result", result);
        resultMap.put("out_trade_no", out_trade_no);
        return resultMap;
    }

    @Override
    public String assemblyPayParameter(HttpServletRequest request, Map<String, String> PrepayInfo, HttpSession session, String userId, String doctorId) {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

        SortedMap<Object, Object> params = new TreeMap<Object, Object>();
        String patientRegisterId = request.getParameter("patientRegisterId");
        if (StringUtils.isNull(patientRegisterId) || "undefined".equals(patientRegisterId)) {
            patientRegisterId = "noData";
        }
        String orderPrice = request.getAttribute("payPrice") != null ? String.valueOf((Float.parseFloat(request.getAttribute("payPrice").toString())) * 100) : request.getParameter("payPrice");
        String outTradeNo = PrepayInfo.get("out_trade_no");
        String openId = WechatUtil.getOpenId(session, request);
        try {
            Map<String, Object> map = XMLUtil.doXMLParse(PrepayInfo.get("result"));
            if (!"FAIL".equals(map.get("return_code"))) {
                String noncestr = IdGen.uuid();//Sha1Util.getNonceStr();//生成随机字符串
                String timestamp = Sha1Util.getTimeStamp();//生成1970年到现在的秒数
                params.put("appId", sysPropertyVoWithBLOBsVo.getAppId());
                params.put("timeStamp", timestamp);
                params.put("nonceStr", noncestr);
                params.put("package", "prepay_id=" + map.get("prepay_id"));
                params.put("signType", sysPropertyVoWithBLOBsVo.getSignMethod());
                String paySign = JsApiTicketUtil.createSign("UTF-8", params);
                params.put("paySign", paySign); //paySign的生成规则和Sign的生成规则一致
                String userAgent = request.getHeader("user-agent");
                char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
                params.put("agent", new String(new char[]{agent}));//微信版本号，用于前面提到的判断用户手机微信的版本是否是5.0以上版本。

                User user = UserUtils.getUser();
                PayRecord payRecord = new PayRecord();
                if (user.getId() != null) {
                    payRecord.setUserId(user.getId());
                } else {
                    payRecord.setUserId(userId);
                }

                payRecord.setId(outTradeNo);
                payRecord.setOpenId(openId);
                payRecord.setOrderId(patientRegisterId);
                payRecord.setAmount(Float.parseFloat(orderPrice));
                payRecord.setPayType("wx");
                payRecord.setStatus("wait");
                payRecord.setPayDate(new Date());
                payRecord.setCreatedBy(user.getId());
                payRecord.setFeeType(PrepayInfo.get("feeType"));
                if ("lovePlan".equals(PrepayInfo.get("feeType"))) {
                    payRecord.setLeaveNote(URLDecoder.decode(request.getParameter("leaveNote"), "UTF-8"));
                }
                System.out.println("insert:" + PrepayInfo.get("feeType"));

                if ("lovePlan".equals(PrepayInfo.get("feeType"))) {
                    payRecord.setLeaveNote(URLDecoder.decode(request.getParameter("leaveNote"), "UTF-8"));
                } else {
                    LogUtils.saveLog(Servlets.getRequest(), "00000037", "用户发起微信支付:" + outTradeNo);//用户发起微信支付
                }

                payRecord.setDoctorId(doctorId);
                payRecordDao.insertSelective(payRecord);

            } else {
                LogUtils.saveLog(request, "00000038", "统一下单接口失败" + map);//统一下单接口失败
                params.put("agent", "6");
                params.put("false", "false");
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = JSONObject.fromObject(params).toString();
        return json;
    }

    private WithdrawRecord buildWithdrawRecord(String userId, Float amount) {
        Date now = new Date();
        WithdrawRecord withdrawRecord = new WithdrawRecord();
        withdrawRecord.setStatus(WITHDRAW_STATUS_SUCCESS);
        withdrawRecord.setAccountBindingId("wx");
        withdrawRecord.setCreatedBy(userId);
        withdrawRecord.setCreatedDate(now);
        withdrawRecord.setAmount(amount);
        withdrawRecord.setUserId(userId);
        withdrawRecord.setReceiveDate(now);
        String partner_trade_no = IdGen.uuid();//Sha1Util.getNonceStr();//商户订单号
        withdrawRecord.setPartnerTradeNo(partner_trade_no);
        return withdrawRecord;
    }

    /**
     * 查询用户每天的提现次数
     * 用户限制用户的提现次数
     */
    @Override
    public int checkUserWithDraw(String userId) {
        return withdrawRecordDao.getWithDrawRecordNumByUserId(userId);
    }

    @Override
    public int checkUserPayRecod(String userId) {
        return withdrawRecordDao.checkUserPayRecod(userId);
    }

    @Override
    public AccountInfo findAccountInfoByUserId(String userId) {
        return accountInfoDao.selectAccountInfoByUserId(userId);
    }

    @Override
    public int saveOrUpdateAccountInfo(AccountInfo record) {
        return accountInfoDao.saveOrUpdate(record);
    }

    @Override
    public boolean checkAppointmentPayState(String out_trade_no) {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

        SortedMap<Object, Object> params = new TreeMap<Object, Object>();
        params.put("appid", sysPropertyVoWithBLOBsVo.getAppId());
        params.put("mch_id", sysPropertyVoWithBLOBsVo.getPartner());
        params.put("out_trade_no", out_trade_no);
        String noncestr = IdGen.uuid();//Sha1Util.getNonceStr();//生成随机字符串
        params.put("nonce_str", noncestr);
        String sign = JsApiTicketUtil.createSign("UTF-8", params);
        params.put("sign", sign);
        //将上述参数进行签名
        String requestXML = JsApiTicketUtil.getRequestXml(params);
        String result = HttpRequestUtil.httpsRequest(sysPropertyVoWithBLOBsVo.getPayresult(), "POST", requestXML);
        try {
            Map<String, String> returnMap = XMLUtil.doXMLParse(result);//解析微信返回的信息，以Map形式存储便于取值
            String payState = returnMap.get("trade_state");
            if ("SUCCESS".equals(payState)) {
                return true;
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}