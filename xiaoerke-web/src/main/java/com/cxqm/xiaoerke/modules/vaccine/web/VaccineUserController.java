package com.cxqm.xiaoerke.modules.vaccine.web;

import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.vaccine.entity.*;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by Administrator on 2016/09/05 0024.
 */
@Controller
@RequestMapping(value = "vaccineUser")
public class VaccineUserController {

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private SystemService systemService;

    @Autowired
    private OlyGamesService olyGamesService;


    /**
     * 获取宝宝币，以及详细记录
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void test(HttpSession session, HttpServletRequest request) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("openId", "oogbDwH3DY2AMBHgFTY78zFGB43k");
//        params.put("birthday", "2016-01-01");
//        params.put("name", "超级大梅梅");
//        params.put("sex", "1");
//        params.put("QRCode", "YM_01");
//        params.put("babySeedNumber", "123123");
//        params.put("vaccineStationId", "1");
//        params.put("vaccineStationName", "朝阳区疫苗站");
//        saveBabyVaccine(params);
//        babyVaccineRemind();
        String userQRCode = olyGamesService.getUserQRCode("YM_01");//二维码
        System.out.println("=================" + userQRCode + "==============");
    }

    /**
     * 插入疫苗提醒用户信息
     * <p/>
     * params:{"openId":"12312312","birthday":"yyyy-MM-dd","name":"某女子","sex":"0","QRCode":"YM_01",
     * "babySeedNumber":"123123","vaccineStationId":1,"vaccineStationName":"朝阳区疫苗站"}
     * <p/>
     * response:
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败 UserInfoAlready 已经存在
     */
    @RequestMapping(value = "/saveBabyVaccine", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> saveBabyVaccine(@RequestBody Map<String, Object> params) {

        HashMap<String, Object> response = new HashMap<String, Object>();
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
        String token = (String) userWechatParam.get("token");
        String openId = String.valueOf(params.get("openId"));
        String QRCode = String.valueOf(params.get("QRCode"));
        searchMap.put("QRCode", QRCode.split("_")[2]);
        searchMap.put("openId", openId);
        VaccineBabyInfoVo vaccineBabyInfoVo = new VaccineBabyInfoVo();
        vaccineBabyInfoVo.setSysUserId(openId);
        VaccineBabyInfoVo insertCheckVo = vaccineService.selectByVaccineBabyInfoVo(vaccineBabyInfoVo);
        if (null != insertCheckVo && StringUtils.isNotBlank(insertCheckVo.getBabyName())) {
            response.put("status", "UserInfoAlready");
        } else {
            try {
                vaccineBabyInfoVo.setBirthday(DateUtils.StrToDate(String.valueOf(params.get("birthday")), "date"));
                vaccineBabyInfoVo.setBabyName(URLDecoder.decode(String.valueOf(params.get("name")), "UTF-8"));
                vaccineBabyInfoVo.setBabySex(String.valueOf(params.get("sex")));
                vaccineBabyInfoVo.setBabySeedNumber(String.valueOf(params.get("babySeedNumber")));
                vaccineBabyInfoVo.setVaccineStationId(Integer.valueOf(String.valueOf(params.get("vaccineStationId"))));
                vaccineBabyInfoVo.setCreateBy(openId);
                vaccineBabyInfoVo.setCreateTime(new Date());
                vaccineBabyInfoVo.setVaccineStationName(URLDecoder.decode(String.valueOf(params.get("vaccineStationName")), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int insertFlag = vaccineService.insertSelective(vaccineBabyInfoVo);
            if (insertFlag > 0) {
                String content = "恭喜你，疫苗提醒开通成功！请注意给宝宝按时接种疫苗哦~~";
                WechatUtil.sendMsgToWechat(token, openId, content);
            } else {
                response.put("status", "failure");
            }
        }

        List<HashMap<String, Object>> resultList = vaccineService.getUserWillVaccination(searchMap);
        if (resultList != null && resultList.size() > 0) {
            //保存疫苗接种记录
            saveVaccineBabyRecord(openId, resultList);
            StringBuffer stringBuffer = new StringBuffer();
            String vaccination = "";
            int count = 0;
            for (Map map : resultList) {
                if (map != null && StringUtils.isNotBlank(String.valueOf(map.get("willVaccineName")))) {
                    count++;
                    stringBuffer.append(map.get("willVaccineName"));
                    stringBuffer.append("、");
                    vaccination = stringBuffer.toString();

                    String sendContent;
                    Calendar sendTime = Calendar.getInstance();
                    Calendar tempTime = Calendar.getInstance();
                    Date birthday = (Date) map.get("birthday");

                    Integer nextLastTimeInterval = Integer.valueOf(String.valueOf(map.get("nextLastTimeInterval")));
                    Integer allVaccineInterval = Integer.valueOf(ConstantUtil.ALL_VACCINE_INTERVAL.getVariable());
                    Integer nextVaccineMiniumAge = Integer.valueOf(String.valueOf(map.get("nextVaccineMiniumAge")));
                    //下次接种间隔>=30
                    if (nextLastTimeInterval >= allVaccineInterval)
                        tempTime.add(Calendar.DAY_OF_MONTH, nextLastTimeInterval);
                    else
                        tempTime.add(Calendar.DAY_OF_MONTH, allVaccineInterval);

                    double passDayByBirthday = DateUtils.getDistanceOfTwoDate(birthday, new Date(tempTime.getTimeInMillis()));

                    //下次接种疫苗的最小接种月龄>=宝宝到了下次接种间隔时的月龄,按最小接种月龄计算
                    if (nextVaccineMiniumAge > passDayByBirthday)
                        sendTime.setTimeInMillis(birthday.getTime() + nextVaccineMiniumAge * 24 * 3600 * 1000);
                    else
                        sendTime.setTimeInMillis(birthday.getTime() + Math.round(passDayByBirthday * 24 * 3600 * 1000));

                    Date inoculationTime = sendTime.getTime();
                    //保存提前七天提醒消息
                    sendTime.add(Calendar.DAY_OF_MONTH, -7);
                    sendContent = "待办任务提醒\n  宝宝该打疫苗了！！\n  待办事项:宝宝在" + DateUtils.formatDate(new Date(sendTime.getTimeInMillis())) +
                            "后需要接种" + map.get("willVaccineName") + "疫苗\n  优先级：很高哦！\n  接种疫苗可以帮助宝宝抵抗疾病，爸爸妈妈千万不要大意哦";
                    Integer vaccineId = Integer.valueOf(String.valueOf(map.get("nextVaccineId")));
                    saveVaccineMessage(vaccineId, openId, sendContent, sendTime.getTime(), "7",inoculationTime);

                    //保存提前一天提醒消息
                    sendTime.add(Calendar.DAY_OF_MONTH, 6);
                    sendContent = "待办任务提醒\n  宝宝该打疫苗了！！\n" +
                            "  待办事项:明天宝宝需要接种" + map.get("willVaccineName") + "疫苗\n  优先级：很高哦！\n  接种疫苗可以帮助宝宝抵抗疾病，爸爸妈妈千万不要大意哦";
                    saveVaccineMessage(vaccineId, openId, sendContent, sendTime.getTime(), "1",inoculationTime);

                }
            }

            vaccination = vaccination.toString().substring(0, vaccination.length() - 1);
            if (count >= 2 && count != 0) {
                vaccination = vaccination.substring(0, vaccination.lastIndexOf("、")) + "和" + vaccination.substring(vaccination.lastIndexOf("、") + 1, vaccination.length());
            }
            WechatUtil.sendMsgToWechat(token, openId, "你的宝宝即将接种" + vaccination);
            response.put("status", "success");
        }

        return response;
    }


    private void saveVaccineMessage(Integer nextVaccineId, String openId, String sendContent, Date sendTime, String msgType,Date inoculationTime) {
        VaccineSendMessageVo vaccineSendMessageVo = new VaccineSendMessageVo();
        vaccineSendMessageVo.setVaccineId(nextVaccineId);
        vaccineSendMessageVo.setSysUserId(openId);
        vaccineSendMessageVo.setMsgType(msgType);
        List<VaccineSendMessageVo> vaccineSendMessageVos = vaccineService.selectByVaccineSendMessageInfo(vaccineSendMessageVo);
        if (vaccineSendMessageVos == null || vaccineSendMessageVos.size() == 0) {
            vaccineSendMessageVo.setContent(sendContent);
            vaccineSendMessageVo.setCreateBy(openId);
            vaccineSendMessageVo.setSendTime(sendTime);
            vaccineSendMessageVo.setValidFlag(ConstantUtil.VACCINEVALID.getVariable());
            vaccineSendMessageVo.setCreateTime(new Date());
            vaccineSendMessageVo.setInoculationTime(inoculationTime);
            vaccineService.insertVaccineSendMessage(vaccineSendMessageVo);
        }
    }

    private void saveVaccineBabyRecord(String openId, List<HashMap<String, Object>> resultList) {
        HashMap<String, Object> recordMap = resultList.get(0);
        VaccineBabyRecordVo vaccineBabyRecordVo = new VaccineBabyRecordVo();
        vaccineBabyRecordVo.setSysUserId(openId);
        vaccineBabyRecordVo.setVaccineInfoId(Integer.valueOf(String.valueOf(recordMap.get("vaccineInfoId"))));
        List<VaccineBabyRecordVo> vaccineBabyRecordVos = vaccineService.selectByVaccineBabyRecord(vaccineBabyRecordVo);
        if (vaccineBabyRecordVos == null || vaccineBabyRecordVos.size() == 0) {
            vaccineBabyRecordVo.setBabyName(String.valueOf(recordMap.get("babyName")));
            vaccineBabyRecordVo.setBabySeedNumber(String.valueOf(recordMap.get("babySeedNumber")));
            vaccineBabyRecordVo.setCreateBy(openId);
            vaccineBabyRecordVo.setCreateTime(new Date());
            vaccineBabyRecordVo.setVaccineInfoName(String.valueOf(recordMap.get("vaccineInfoName")));
            vaccineService.insertVaccineBabyRecord(vaccineBabyRecordVo);
        }
    }

    /**
     * 获取疫苗站信息
     * <p/>
     * params:{}
     * <p/>
     * "vaccineStationInfo":
     * [
     * {"vaccineStationName":"朝阳区疫苗站","vaccineStationId":1},
     * {"vaccineStationName":"海淀区疫苗站","vaccineStationId":2},
     * {"vaccineStationName":"昌平区疫苗站","vaccineStationId":3}
     * ]
     */
    @RequestMapping(value = "/getVaccineStation", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getVaccineStation() {
        HashMap<String, Object> response = new HashMap<String, Object>();
        List<VaccineStationVo> vaccineStationVos = vaccineService.selectByVaccineStationVo(new VaccineStationVo());
        for (VaccineStationVo vaccineStationVo : vaccineStationVos) {
            vaccineStationVo.setVaccineStationId(String.valueOf(vaccineStationVo.getId()));
            vaccineStationVo.setVaccineStationName(vaccineStationVo.getName());
        }
        response.put("vaccineStationInfo", vaccineStationVos);
        return response;
    }

    /**
     * 2016-12-12 15:24:16 jiangzg add
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getVaccineNameList", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public HashMap getVaccineNameList(HttpServletRequest request , HttpServletResponse response) {
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        VaccineScanCodeVo vaccineScanCodeVo = new VaccineScanCodeVo();
//        List<VaccineScanCodeVo> vaccineScanList =  vaccineService.findVaccineScanCodeList(vaccineScanCodeVo);
        vaccineScanCodeVo.setId(1111);
        vaccineScanCodeVo.setAge("12");
        vaccineScanCodeVo.setIsfree("free");
        vaccineScanCodeVo.setName("test");
        List<VaccineScanCodeVo> vaccineScanList = new ArrayList<VaccineScanCodeVo>();
        vaccineScanList.add(vaccineScanCodeVo);
        if(vaccineScanList != null && vaccineScanList.size() > 0){
            List dataList = new ArrayList();
            for(VaccineScanCodeVo vo : vaccineScanList){
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("id",vo.getId());
                if(vo.getIsfree().equalsIgnoreCase("免费")){
                    map.put("isFree","0");
                }else{
                    map.put("isFree","1");
                }
                map.put("age",vo.getAge());
                map.put("name",vo.getName());
                /*map.put("attention",vo.getAttention());
                map.put("diseasePrevention",vo.getDiseasePrevention());
                map.put("informedForm",vo.getInformedForm());*/
                dataList.add(map);
            }
            resultMap.put("dataList",dataList);
            resultMap.put("status","success");
        }else{
            resultMap.put("status","failure");
        }
        return  resultMap ;
    }


    @RequestMapping(value = "/getVaccineInfoList", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public HashMap getVaccineInfoList(HttpServletRequest request , HttpServletResponse response) {
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        VaccineScanCodeVo vaccineScanCodeVo = new VaccineScanCodeVo();
        vaccineScanCodeVo.setId(Integer.valueOf(String.valueOf(request.getParameter("id"))));
        List<VaccineScanCodeVo> vaccineScanList =  vaccineService.findVaccineScanCodeList(vaccineScanCodeVo);
        if(vaccineScanList != null && vaccineScanList.size() > 0){
            List dataList = new ArrayList();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id", vaccineScanList.get(0).getId());
            if(vaccineScanList.get(0).getIsfree().equalsIgnoreCase("免费")){
                map.put("isFree","0");
            }else{
                map.put("isFree","1");
            }
            map.put("age",vaccineScanList.get(0).getAge());
            map.put("name",vaccineScanList.get(0).getName());
            map.put("attention",vaccineScanList.get(0).getAttention());
            map.put("diseasePrevention",vaccineScanList.get(0).getDiseasePrevention());
            map.put("informedForm",vaccineScanList.get(0).getInformedForm());
            dataList.add(map);
            resultMap.put("dataList", dataList);
            resultMap.put("failure", "success");
        }else{
            resultMap.put("failure", "failure");
        }
        return  resultMap ;
    }
}
