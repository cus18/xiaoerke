package com.cxqm.xiaoerke.modules.account.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.account.entity.WithdrawRecord;
import com.cxqm.xiaoerke.modules.account.exception.AccountNotExistException;
import com.cxqm.xiaoerke.modules.account.exception.BalanceNotEnoughException;
import com.cxqm.xiaoerke.modules.account.service.*;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.wechat.service.WeChatInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 关于订单管理
 * @author frank
 * @date 2015-10-14
 */
@Controller
@RequestMapping(value = "account/doctor/")
public class AccountDoctorController {

    @Autowired
    private AccountService accountService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private SystemService systemService;

    @RequestMapping(value = "withDrawList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getWithDrawlList(@RequestParam Integer pageSize, @RequestParam Integer pageNo) {
    	HashMap<String, Object> response = new HashMap<String, Object>(4);
    	String userId = UserUtils.getUser().getId();
    	Page<WithdrawRecord> pageParam = new Page<WithdrawRecord>(pageNo, pageSize);
    	Page<WithdrawRecord> records = accountService.getWithdrawRecords(userId, pageParam);
    	List<WithdrawRecord> recordsList = records.getList();
    	for(WithdrawRecord record : recordsList) {
    		record.setUserId(null);
    		record.setAccountBindingId(null);
    		record.setPartnerTradeNo(null);
    		record.setOperateType(null);
    	}
    	response.put("withDrawList", recordsList);
    	response.put("pageSize", pageSize);
    	response.put("pageNo", pageNo);
    	response.put("totalNum", records.getCount());
        return response;
    }
    
    @RequestMapping(value = "withdraw", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> withdraw(@RequestParam Float money) {
    	HashMap<String, Object> response = new HashMap<String, Object>(4);
    	String userId = UserUtils.getUser().getId();
    	try {
			accountService.withdraw(userId, money);
		} catch (AccountNotExistException e) {
			response.put("result", "failure");
			response.put("message", e.getMessage());
			return response;
		} catch (BalanceNotEnoughException e) {
			response.put("result", "failure");
			response.put("message", e.getMessage());
			return response;
		}
    	
    	Map<String, Object> tokenMap = systemService.getDoctorWechatParameter();
        String token = (String) tokenMap.get("token");
		messageService.sendDoctorWithDrawMessage(userId, money, token);
    	response.put("result", "success");
		response.put("message", "success");
        return response;
    }
}
