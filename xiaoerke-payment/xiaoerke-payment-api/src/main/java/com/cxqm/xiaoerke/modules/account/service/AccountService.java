package com.cxqm.xiaoerke.modules.account.service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.account.entity.AccountInfo;
import com.cxqm.xiaoerke.modules.account.entity.Record;
import com.cxqm.xiaoerke.modules.account.entity.WithdrawRecord;
import com.cxqm.xiaoerke.modules.account.exception.AccountNotExistException;
import com.cxqm.xiaoerke.modules.account.exception.BalanceNotEnoughException;
import com.cxqm.xiaoerke.modules.account.exception.BusinessPaymentExceeption;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 15/11/5.
 */
@Transactional(readOnly = false)
public interface AccountService {

    Float accountFund(String userId);

    List<Record> getAccountDetail(String userId);

    String returnPay(Float returnMoney, String openid, String ip) throws Exception;

    void payByRemainder(Float amount, String openId, String patientRegisterId,String doctorId);

    WithdrawRecord getWithdrawRecordDetail(String id);

    Page<WithdrawRecord> getWithdrawRecords(String userId, Page<WithdrawRecord> page);

    Page<WithdrawRecord> getAllWithdrawRecords(WithdrawRecord param, Page<WithdrawRecord> page);

    void withdrawSave(WithdrawRecord withdrawRecord);

    void withdrawDelete(String id);

    int createAccountInfo(String userId, Float amount);

    int deleteAccountInfo(String userId);

    void getUserAccountInfo(HashMap<String, Object> response);

    void returnUserMoney(Map<String, Object> params, Map<String, Object> response, HttpServletRequest request, HttpSession session) throws BalanceNotEnoughException, BusinessPaymentExceeption;

    Map<String,String> getPrepayInfo(HttpServletRequest request, HttpSession session,String serviceType);

    int withdraw(String userId, Float amount) throws AccountNotExistException, BalanceNotEnoughException ;

    String assemblyPayParameter(HttpServletRequest request, Map<String, String> PrepayInfo, HttpSession session, String userId, String doctorId);

    int checkUserWithDraw(String userId);

    int checkUserPayRecod(String userId);
    
    AccountInfo findAccountInfoByUserId(String userId);

    int saveOrUpdateAccountInfo(AccountInfo record);


    //更新账户信息
    Float updateAccount(Float amount, String order, HashMap<String, Object> response, boolean isEvaluation, String userId, String reason);

    int updateByPrimaryKey(AccountInfo record);

    int updateBalanceByUser(@Param("userId") String userId, @Param("balance") Float balance);

    boolean checkAppointmentPayState(String out_trade_no);

//    boolean updatePayInfoByBabyCoin(BabyCoinVo babyCoinVo, String openId,float babyCoin);


}
