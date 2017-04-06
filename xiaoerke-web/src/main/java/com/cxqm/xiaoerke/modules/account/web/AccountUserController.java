package com.cxqm.xiaoerke.modules.account.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.service.ServiceException;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import com.cxqm.xiaoerke.modules.consult.service.BabyCoinService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultMemberRedsiCacheService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 关于账户管理
 *
 * @author frank
 * @date 2015-10-14
 */
@Controller
public class AccountUserController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PatientRegisterService patientRegisterService;

    @Autowired
    private ConsultPhonePatientService consultPhonePatientService;

    @Autowired
    private BabyCoinService babyCoinService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Autowired
    private ConsultMemberRedsiCacheService consultMemberRedsiCacheService;

    @Autowired
    private SystemService systemService;

    private static Lock lock = new ReentrantLock();

    /**
     * 用户的账户信息
     */
    @RequestMapping(value = "/account/user/accountInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> accountInfo() {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        HashMap<String, Object> response = new HashMap<String, Object>();
        accountService.getUserAccountInfo(response);
        return response;
    }

    /**
     * 调用企业支付接口用户用户退款
     */
    @RequestMapping(value = "/account/user/returnPay", method = {RequestMethod.POST, RequestMethod.GET})
    public synchronized
    @ResponseBody
    Map<String, Object> returnPay(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        lock.lock();
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            int withDrawNum = accountService.checkUserWithDraw(UserUtils.getUser().getId());
            if (withDrawNum < 4) {
                accountService.returnUserMoney(params, response, request, session);
                return response;
            } else {
                response.put("return_msg", "每天提现不能超过3次");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("return_msg", "账户异常,请重试或联系客服");
        } finally {
            lock.unlock();
        }
        return response;
    }

    /**
     * js支付
     */
    @RequestMapping(value = "/account/user/userPay", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String userPay(HttpServletRequest request, HttpSession session) throws Exception {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        //查询当前订单状态是否是待支付
        String patientRegisterId = request.getParameter("patientRegisterId");

        //获取统一支付接口参数
        Map prepayInfo = accountService.getPrepayInfo(request, session, "appointService");

        //拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
        String userId = UserUtils.getUser().getId();//patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);

        PerAppDetInfoVo Info = new PerAppDetInfoVo();
        Info.setId(patientRegisterId);
        Map<String, Object> responseMap = patientRegisterService.findPersonAppointDetailInfo(Info);
        String payParameter = accountService.assemblyPayParameter(request, prepayInfo, session, userId,
                responseMap != null ? (String) responseMap.get("doctorId") : "");
        return payParameter;
    }

    /**
     * js支付
     */
    @RequestMapping(value = "/account/user/antiDogPay", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String antiDogPay(HttpServletRequest request, HttpSession session) throws Exception {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        //获取统一支付接口参数
        Map prepayInfo = accountService.getPrepayInfo(request, session, "insuranceService");
        prepayInfo.put("feeType", "insurance");
        System.out.println("feeType:" + prepayInfo.get("feeType").toString());
        //拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
        String userId = UserUtils.getUser().getId();//patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);

        String payParameter = accountService.assemblyPayParameter(request, prepayInfo, session, userId, null);
        return payParameter;
    }


    /**
     * js支付
     */
    @RequestMapping(value = "/account/user/customerPay", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String customerPay(HttpServletRequest request, HttpSession session) throws Exception {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        //获取统一支付接口参数
        Map prepayInfo = accountService.getPrepayInfo(request, session, "customerService");
        prepayInfo.put("feeType", "customer");
        System.out.println("feeType:" + prepayInfo.get("feeType").toString());
        //拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
        String userId = UserUtils.getUser().getId();//patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);
        String payParameter = accountService.assemblyPayParameter(request, prepayInfo, session, userId, null);
        return payParameter;
    }

    /**
     * js支付
     */
    @RequestMapping(value = "/account/user/consultPhonePay", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String consultPhonePay(HttpServletRequest request, HttpSession session) throws Exception {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        String consultPhoneServiceId = request.getParameter("patientRegisterId");
        Map<String, Object> consultMap = consultPhonePatientService.getPatientRegisterInfo(Integer.parseInt(consultPhoneServiceId));
        if ("待支付".equals(consultMap.get("state"))) {
            request.setAttribute("payPrice", consultMap.get("price"));
            //获取统一支付接口参数
            Map prepayInfo = accountService.getPrepayInfo(request, session, "consultPhone");
            prepayInfo.put("feeType", "consultPhone");
            System.out.println("feeType:" + prepayInfo.get("feeType").toString());
            //拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
            String userId = UserUtils.getUser().getId();//patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);
            String payParameter = accountService.assemblyPayParameter(request, prepayInfo, session, userId, null);
            return payParameter;
        }
        SortedMap<Object, Object> params = new TreeMap<Object, Object>();
        params.put("agent", "7");
        return JSONObject.fromObject(params).toString();
    }


    /**
     * js支付
     */
    @RequestMapping(value = "/account/user/umbrellaPay", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String umbrellaPay(HttpServletRequest request, HttpSession session) throws Exception {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        //获取统一支付接口参数
        Map prepayInfo = accountService.getPrepayInfo(request, session, "umbrellaService");
        prepayInfo.put("feeType", "umbrella");
        System.out.println("feeType:" + prepayInfo.get("feeType").toString());
        //拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
        String userId = UserUtils.getUser().getId();//patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);

        String payParameter = accountService.assemblyPayParameter(request, prepayInfo, session, userId, null);
        return payParameter;
    }

    /**
     * js支付
     */
    @RequestMapping(value = "/account/user/lovePlanPay", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String lovePlanPay(HttpServletRequest request, HttpSession session) throws Exception {
        Integer donationType = null;
        if (StringUtils.isNotNull(request.getParameter("donationType"))) {
            donationType = Integer.valueOf(request.getParameter("donationType"));
        }

        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        //获取统一支付接口参数
        request.setAttribute("feeType", "lovePlan");
        request.setAttribute("donationType", donationType);
        Map prepayInfo = accountService.getPrepayInfo(request, session, "lovePlanService");
        prepayInfo.put("feeType", "lovePlan");
        System.out.println("feeType:" + prepayInfo.get("feeType").toString());
        //拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
        String userId = UserUtils.getUser().getId();
        String payParameter = accountService.assemblyPayParameter(request, prepayInfo, session, userId, null);
        return payParameter;
    }

    /**
     * js支付
     */
    @RequestMapping(value = "/account/user/doctorConsultPay", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String doctorConsultPay(HttpServletRequest request, HttpSession session) throws ServiceException {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        HashMap<String, Object> response = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session, request);
        Double payCount = Double.valueOf(String.valueOf(request.getParameter("payPrice")));//支付的金额
        Double babyCoinNumber = Double.valueOf(String.valueOf(request.getParameter("babyCoinNumber")));//使用宝宝币抵扣的金额
        BabyCoinRecordVo babyCoinRecordVo = new BabyCoinRecordVo();
        BabyCoinVo babyCoinVo = new BabyCoinVo();
        babyCoinVo.setOpenId(openId);
        babyCoinVo = babyCoinService.getBabyCoin(response, openId);
        String totleTime  = "";

        Double PayType1SumMoney = Double.valueOf(sysPropertyVoWithBLOBsVo.getPayType1SumMoney());
        Double PayType1UseBabycoin = Double.valueOf(sysPropertyVoWithBLOBsVo.getPayType1UseBabycoin());
        Double payType1ActualMoney = (Double) (PayType1SumMoney * 10 - PayType1UseBabycoin) / 10 * 100 / 100;
        Double PayType2SumMoney = Double.valueOf(sysPropertyVoWithBLOBsVo.getPayType2SumMoney());
        Double PayType2UseBabycoin = Double.valueOf(sysPropertyVoWithBLOBsVo.getPayType2UseBabycoin());
        Double payType2ActualMoney = (Double) ((PayType2SumMoney * 10 - PayType2UseBabycoin) / 10 * 100) / 100;

        Double PayType3SumMoney = Double.valueOf(sysPropertyVoWithBLOBsVo.getPayType3SumMoney());
        Double PayType3UseBabycoin = Double.valueOf(sysPropertyVoWithBLOBsVo.getPayType3UseBabycoin());
        Double payType3ActualMoney = (Double) ((PayType3SumMoney * 10 - PayType3UseBabycoin) / 10 * 100) / 100;



        //此支付只有四种情况 1、9.9金额 0宝宝币 2、4.9金额 50宝宝币 3、25金额 0宝宝币  4、12.5金额 125宝宝币
        //新增一种支付 128元 0宝宝币 64元   640宝宝币
        boolean canPay1 = (payCount.equals(PayType1SumMoney) && babyCoinNumber == 0f);
        boolean canPay2 = (payCount.equals(payType1ActualMoney) && babyCoinNumber.equals(PayType1UseBabycoin)) && babyCoinNumber  <= babyCoinVo.getCash();
        boolean canPay3 = payCount.equals(PayType2SumMoney)&& babyCoinNumber == 0f;
        boolean canPay4 = (payCount.equals(payType2ActualMoney) && babyCoinNumber.equals(PayType2UseBabycoin) && babyCoinNumber  <= babyCoinVo.getCash());
        boolean canPay5 = payCount.equals(PayType3SumMoney) && babyCoinNumber == 0f;
        boolean canPay6 = (payCount.equals(payType3ActualMoney) && babyCoinNumber.equals(PayType3UseBabycoin) && babyCoinNumber  <= babyCoinVo.getCash());
        if (canPay1 || canPay3 || canPay5) {
            //获取统一支付接口参数
            request.setAttribute("payPrice", payCount.floatValue());
            request.setAttribute("feeType", "doctorConsultPay");
            Map prepayInfo = accountService.getPrepayInfo(request, session, "doctorConsultPay");

            //拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
            String userId = UserUtils.getUser().getId();
            prepayInfo.put("feeType", "doctorConsultPay");
            prepayInfo.put("operateType", String.valueOf(babyCoinNumber));

            String payParameter = accountService.assemblyPayParameter(request, prepayInfo, session, userId, null);

            return payParameter;
        } else if (canPay2) {
            //用宝宝币全额支付
            babyCoinVo.setCash(babyCoinVo.getCash() - PayType1UseBabycoin.longValue());
            babyCoinRecordVo.setBalance(-PayType1UseBabycoin);
            totleTime = sysPropertyVoWithBLOBsVo.getConsultMemberTimeType2();
        }else if (canPay4) {
            babyCoinVo.setCash(babyCoinVo.getCash() - PayType2UseBabycoin.longValue());
            babyCoinRecordVo.setBalance(-PayType2UseBabycoin);
            totleTime = sysPropertyVoWithBLOBsVo.getConsultMemberTime();
        }else if (canPay6) {
            babyCoinVo.setCash(babyCoinVo.getCash() - PayType3UseBabycoin.longValue());
            babyCoinRecordVo.setBalance(-PayType3UseBabycoin);
            totleTime = sysPropertyVoWithBLOBsVo.getConsultMemberTimeType3();
        }
        babyCoinService.updateBabyCoinByOpenId(babyCoinVo);
        babyCoinRecordVo.setCreateTime(new Date());
        babyCoinRecordVo.setCreateBy(openId);
        babyCoinRecordVo.setOpenId(openId);
        babyCoinRecordVo.setSessionId(sessionRedisCache.getSessionIdByUserId(openId));
        babyCoinRecordVo.setSource("consultPay");
        babyCoinService.insertBabyCoinRecord(babyCoinRecordVo);
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        consultMemberRedsiCacheService.payConsultMember(openId, totleTime, "0", token);
//                   mysql 增加会员记录,延长redis的时间
        return "payByBabycoin";
    }


    /**
     * js支付
     */
    @RequestMapping(value = "/account/user/nonRealTimeConsultPay", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String nonRealTimeConsultPay(HttpServletRequest request, HttpSession session) throws Exception {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        //获取统一支付接口参数
        Map prepayInfo = accountService.getPrepayInfo(request, session, "nonRealTimeConsult");
        prepayInfo.put("feeType", "nonRealTimeConsult");
        System.out.println("feeType:" + prepayInfo.get("feeType").toString());
        //拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
        String userId = UserUtils.getUser().getId();//patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);
        String payParameter = accountService.assemblyPayParameter(request, prepayInfo, session, userId, null);
        return payParameter;
    }
}
