package com.cxqm.xiaoerke.modules.nonRealTimeConsult.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.healthRecords.service.HealthRecordsService;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultRecordVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultSessionVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.NonRealTimeConsultService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/09/05 0024.
 */
@Controller
@RequestMapping(value = "nonRealTimeConsultDoctor")
public class NonRealTimeConsultDoctorContorller {

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService;

    @Autowired
    private NonRealTimeConsultService nonRealTimeConsultService;

    @Autowired
    private HealthRecordsService healthRecordsService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UtilService utilService;

    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void test(HttpSession session, HttpServletRequest request) {
//        babyCoinInit(session, request);
//        getBabyCoinInfo(session, request);
    }

    /**
     * 获取医生的登陆状态
     * response:
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败
     *
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/GetDoctorLoginStatus", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map GetDoctorLoginStatus(HttpSession session, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        //根据openid查询当前医生
        Map param = new HashMap();
        String openId = "8ab94e95afe448dab66403fc5407d0ca";//WechatUtil.getOpenId(session, request)
        param.put("openId", openId);
        response.put("status", "failure");
        if(StringUtils.isNotNull(openId)){
            List<ConsultDoctorInfoVo> consultDoctorInfoVos = consultDoctorInfoService.getConsultDoctorByInfo(param);
            if (consultDoctorInfoVos != null && consultDoctorInfoVos.size() > 0 && StringUtils.isNotBlank(consultDoctorInfoVos.get(0).getUserId())) {
                response.put("status", "success");
            }
        }
        return response;
    }

    /**
     * 医生绑定接口
     * response:
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败
     *
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/doctorBinding", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map doctorBinding(@RequestBody Map<String, Object> params,HttpSession session, HttpServletRequest request) throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        String username = String.valueOf(params.get("username"));
        String openid = "oogbDwD_2BTQpftPu9QClr-mCw7U";//WechatUtil.getOpenId(session,request)
        String passeord = String.valueOf(params.get("password"));
        String status = utilService.bindUser4ConsultDoctor(username,passeord,openid);
        response.put("status","failure");
        if(status.equals("1") && StringUtils.isNotNull(passeord) && StringUtils.isNotNull(username)){
            User user = new User();
            user.setPhone(passeord);
            user = userInfoService.doctorOper(user);
            ConsultDoctorInfoVo consultDoctorInfoVo = new ConsultDoctorInfoVo();
            consultDoctorInfoVo.setOpenId(openid);
            consultDoctorInfoVo.setUserId(user.getId());
            consultDoctorInfoService.updateByphone(consultDoctorInfoVo);
            response.put("status", "success");
        }
        return response;
    }

    /**
     * 获取医生的服务（当前服务和全部服务）
     * <p/>
     * params:{"serviceType":"current","openId":"123sdfsf"}  //serviceType  currentService 当前服务 allService 全部服务
     * <p/>
     * response:S
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败
     *
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/GetDoctorService", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map GetDoctorService(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        String serviceType = (String) params.get("serviceType");
        String openId = (String) params.get("openId");
        String babyInfo = "";
        NonRealTimeConsultSessionVo realTimeConsultSessionVo = new NonRealTimeConsultSessionVo();
        realTimeConsultSessionVo.setCsUserId(openId);
        if (serviceType.equals("currentService")) {//查询当前服务
            realTimeConsultSessionVo.setStatus("ongoing");
            realTimeConsultSessionVo.setOrder("lastMessageTimeAsc");
        } else {
            realTimeConsultSessionVo.setOrder("lastMessageTimeDesc");
        }
        //查询当前医生的会话信息
        List<NonRealTimeConsultSessionVo> nonRealTimeConsultSessionVos =
                nonRealTimeConsultService.selectByNonRealTimeConsultSessionVo(realTimeConsultSessionVo);
        if (nonRealTimeConsultSessionVos != null && nonRealTimeConsultSessionVos.size() > 0) {
            for (NonRealTimeConsultSessionVo nonRealTimeConsultSessionVo : nonRealTimeConsultSessionVos) {
                if (DateUtils.pastHour(nonRealTimeConsultSessionVo.getLastMessageTime()) > 24) {
                    nonRealTimeConsultSessionVo.setDispalyTimes(DateUtils.DateToStr(nonRealTimeConsultSessionVo.getLastMessageTime(), "time"));
                } else {
                    nonRealTimeConsultSessionVo.setDispalyTimes(DateUtils.DateToStr(nonRealTimeConsultSessionVo.getLastMessageTime(), "monthDate"));
                }
                if (nonRealTimeConsultSessionVo.getStatus().equals("sessionend")) {
                    nonRealTimeConsultSessionVo.setLastMessageType("close");
                }

                //查询宝宝信息
                List<BabyBaseInfoVo> babyInfoList = healthRecordsService.getUserBabyInfolistByOpenId(openId);
                if (babyInfoList != null && babyInfoList.size() > 0) {
                    BabyBaseInfoVo babyBaseInfoVo = babyInfoList.get(0);
                    Date babyBirthday = babyBaseInfoVo.getBirthday();
                    int babyBirthdayYear = babyBirthday.getYear();
                    int babyBirthdayMonth = babyBirthday.getMonth();
                    Date nowDate = new Date();
                    int nowDateYear = nowDate.getYear();
                    int nowDateMonth = nowDate.getMonth();
                    int chaDate = 0;
                    if (nowDateMonth > babyBirthdayMonth) {
                        chaDate = nowDateMonth - babyBirthdayMonth;
                    } else if (babyBirthdayMonth > nowDateMonth) {
                        chaDate = babyBirthdayMonth - nowDateMonth;
                    }

                    babyInfo = babyBaseInfoVo.getName() + "," + (nowDateYear - babyBirthdayYear) + "岁" + chaDate + "个月";

                }else{
                    babyInfo = "暂无数据";
                }
                nonRealTimeConsultSessionVo.setBabyInfo(babyInfo);
            }
        }
        response.put("ListInfo",nonRealTimeConsultSessionVos);
        return response;
    }


    /**
     * 获取当前医生与用户的聊天记录
     * <p/>
     * params:{"csUserId":"sdfsdfsdfsdfsdf","userId":"123sdfsf"}
     * <p/>
     *
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/GetSessionRecord", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map GetSessionRecord(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        String csUserId = String.valueOf(params.get("csUserId"));
        String userId = String.valueOf(params.get("userId"));
        NonRealTimeConsultRecordVo nonRealTimeConsultRecordVo = new NonRealTimeConsultRecordVo();
        nonRealTimeConsultRecordVo.setCsUserId(csUserId);
        nonRealTimeConsultRecordVo.setSysUserId(userId);
        List<NonRealTimeConsultRecordVo> nonRealTimeConsultRecordVos = nonRealTimeConsultService.selectSessionRecordByVo(nonRealTimeConsultRecordVo);
        if (nonRealTimeConsultRecordVos != null && nonRealTimeConsultRecordVos.size() > 0) {
            for (NonRealTimeConsultRecordVo vo : nonRealTimeConsultRecordVos) {
                vo.setDisplayTimes(DateUtils.DateToStr(vo.getCreateTime(), "flag1"));
            }
        }
        response.put("nonRealTimeConsultRecordInfo", nonRealTimeConsultRecordVos);
        return response;
    }


}
