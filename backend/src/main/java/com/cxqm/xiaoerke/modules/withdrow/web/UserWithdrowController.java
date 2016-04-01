package com.cxqm.xiaoerke.modules.withdrow.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用户提现Controller
 *
 * @author deliang
 * @version 2016-01-12
 */
@Controller
@RequestMapping(value = "${adminPath}/user/withdrow")
public class UserWithdrowController extends BaseController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PatientRegisterService patientRegisterService;

    private static Lock lock = new ReentrantLock();
    /**
     * 用户提现操作
     *
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"userWithdrow", ""})
    public synchronized
    String userWithdrow(PayRecord payRecord, Model model) {
        if (StringUtils.isNull(payRecord.getReason())) {
            return "withdrow/userWithdrow";
        } else {
            //查询根据订单号查询userid
            PatientRegisterServiceVo patientRegisterServiceVo = new PatientRegisterServiceVo();
            patientRegisterServiceVo.setRegisterNo(payRecord.getOrderId());
            PatientRegisterServiceVo pVo = patientRegisterService.selectOrderInfoByPatientId(patientRegisterServiceVo);
            String userId = pVo.getId();
            //退款前判断操作
            try {
                lock.lock();
                int withDrawNum = accountService.checkUserPayRecod(userId);
                if(withDrawNum<3){
                    //查询有没有账户，如果没有账户记录则创建账户记录
                    accountService.updateAccount(payRecord.getAmount()*100,pVo.getPatientRegisterServiceId(), new HashMap<String, Object>(), false, userId, payRecord.getReason());
                    model.addAttribute("message", "退款成功！");
                    model.addAttribute("payRecord", payRecord);
                }else{
                    model.addAttribute("message", "此用户每天退款不能超过2次");
                }
            }catch (Exception e){
                e.printStackTrace();
                model.addAttribute("message", "账户异常,请重试或联系客服");
            }finally {
                lock.unlock();
            }
        }
        return "withdrow/userWithdrow";
    }
}
