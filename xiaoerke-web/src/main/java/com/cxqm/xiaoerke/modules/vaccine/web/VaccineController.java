package com.cxqm.xiaoerke.modules.vaccine.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyInfoVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineStationVo;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
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
@RequestMapping(value = "vaccine")
public class VaccineController {

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private SystemService systemService;

    /**
     * 插入疫苗提醒用户信息
     * <p/>
     * params:{"openId":"12312312","birthday":"yyyy-MM-dd","name":"某女子","sex":"0",
     * "babySeedNumber":"123123","vaccineStationId":1,"vaccineStationName":"朝阳区疫苗站"}
     * <p/>
     * response:
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败
     */
    @RequestMapping(value = "/saveBabyVaccine", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> saveBabyVaccine(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String openId = String.valueOf(params.get("openId"));
        VaccineBabyInfoVo vaccineBabyInfoVo = new VaccineBabyInfoVo();
        vaccineBabyInfoVo.setBirthday(DateUtils.StrToDate(String.valueOf(params.get("birthday")), "date"));
        vaccineBabyInfoVo.setBabyName(String.valueOf(params.get("name")));
        vaccineBabyInfoVo.setSysUserId(openId);
        vaccineBabyInfoVo.setBabySex(String.valueOf(params.get("sex")));
        vaccineBabyInfoVo.setBabySeedNumber(String.valueOf(params.get("babySeedNumber")));
        vaccineBabyInfoVo.setVaccineStationId(Integer.valueOf(String.valueOf(params.get("vaccineStationId"))));
        vaccineBabyInfoVo.setCreateBy(openId);
        vaccineBabyInfoVo.setCreateTime(new Date());
        vaccineBabyInfoVo.setVaccineStationName(String.valueOf(params.get("vaccineStationName")));
        int insertFlag = vaccineService.insertSelective(vaccineBabyInfoVo);
        if (insertFlag > 0) {
            response.put("status", "success");
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            String content = "恭喜你，疫苗提醒开通成功！请注意给宝宝按时接种疫苗哦~~";
            WechatUtil.sendMsgToWechat(token, openId, content);
        } else {
            response.put("status", "failure");
        }

        return response;
    }

    /**
     * 获取疫苗站信息
     * <p/>
     * params:{}
     * <p/>
     * response:
     * {
     * "vaccineStationInfo":{[]}
     * }
     */
    @RequestMapping(value = "/getVaccineStation", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getVaccineStation(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        List<VaccineStationVo> vaccineStationVos = vaccineService.selectByVaccineStationVo(new VaccineStationVo());
        response.put("vaccineStationInfo", vaccineStationVos);
        return response;
    }


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
//        babyCoinInit(session, request);
//        getBabyCoinInfo(session, request);
    }

}
