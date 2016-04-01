package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.operation.service.DoctorOperationService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.wechat.entity.DoctorAttentionVo;

import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WeChatInfoService;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 医生信息统计 实现
 *
 * @author deliang
 * @version 2015-11-05
 */
@Service("doctorInfoOperationService")
@Transactional(readOnly = false)
public class DoctorOperationServiceImpl implements DoctorOperationService {

    @Autowired
    private DoctorInfoService doctorInfoService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private RegisterService registerService;

    public List<DoctorAttentionVo> findDoctorInfo(DoctorAttentionVo searchVo) {
        //获取所有医生的doctorName,openid,phone
        HashMap<String, Object> newHash = new HashMap<String, Object>();
        List<DoctorAttentionVo> responseList = new ArrayList<DoctorAttentionVo>();
        List<HashMap<String, Object>> doctors = doctorInfoService.findDoctorByDoctorId(newHash);
//       可以获取到医生的openid时用这个
//        for (Map doctorMap : doctors) {
//
//            if (StringUtils.isNotNull((String) doctorMap.get("openid"))) {
//
//                HashMap<String, Object> searchMap = new HashMap<String, Object>();
//
//                if (doctorMap != null && doctorMap.size() > 0) {
//
//                    searchMap.put("openid", doctorMap.get("openid"));
//                    searchMap.put("startDate", startDate);
//                    searchMap.put("endDate", endDate);
//                    searchMap.put("id", doctorMap.get("id"));
//
//                    //根据openid查询这一段时间医生微信名、Openid、来源、关注时间、医生姓名、医生电话、医院、科室
//                    DoctorAttentionVo doctorAttentionVo = doctorDao.findDoctorAttentionVoInfo(searchMap);
//
//                    if (doctorAttentionVo != null && doctorAttentionVo.getDate() != null) {
//                        doctorAttentionVo.setDateDisplay(DateUtils.formatDateTime(doctorAttentionVo.getDate()));
//                    }
//                    //根据doctorId查询这一段时间该医生“号源总数”
//                    int registerServiceCount = registerServiceDao.findDoctorRegisterServiceByData(searchMap);
//
//                    //根据doctorId查询这一段时间该医生“已预约号源数”
//                    int appointNumberAlready = doctorDao.findDoctorAlreadyAppointNumber(searchMap);
//
//                    //根据doctorId查询这一段时间该医生“剩余预约号源数”
//                    int appointNumber = doctorDao.findDoctorCanAppointNumber(searchMap);
//if ("".equals(doctorAttentionVo.getMarketer()) || doctorAttentionVo.getMarketer() == null) {
//        doctorAttentionVo.setMarketer("无");
//    }
//    if ("".equals(doctorAttentionVo.getNickname()) || doctorAttentionVo.getNickname() == null) {
//        doctorAttentionVo.setNickname("无");
//    }
//    if ("".equals(doctorAttentionVo.getOpenid()) || (doctorAttentionVo.getOpenid() == null)) {
//        doctorAttentionVo.setOpenid("无");
//    }
//                    //组合
//                    if (doctorAttentionVo != null) {
//                        doctorAttentionVo.setAppointNumber(String.valueOf(appointNumber)==null?"无":String.valueOf(appointNumber));
//                        doctorAttentionVo.setAppointNumberAlready(String.valueOf(appointNumberAlready)==null?"无":String.valueOf(appointNumberAlready));
//                        doctorAttentionVo.setRegisterServiceCount(String.valueOf(registerServiceCount)==null?"无":String.valueOf(registerServiceCount));
//                        responseList.add(doctorAttentionVo);
//                    }
//                }
//            }
//        }
//        -----------------------不可以获取到医生的openid暂时用这个------------------------------
        for (Map doctorMap : doctors) {

            if (StringUtils.isNotNull((String) doctorMap.get("openid"))) {

                HashMap<String, Object> searchMap = new HashMap<String, Object>();

                if (doctorMap != null && doctorMap.size() > 0) {

                    searchMap.put("openid", doctorMap.get("openid"));
                    searchMap.put("startDate", searchVo.getStartDate());
                    searchMap.put("endDate", searchVo.getEndDate());
                    searchMap.put("id", doctorMap.get("id"));

                    //根据doctorId查询医生姓名、医生电话、医院、科室
                    DoctorAttentionVo doctorAttentionVo = wechatAttentionService.findDoctorAttentionVoInfoNoOpenId(searchMap);

                    if (doctorAttentionVo != null && doctorAttentionVo.getDate() != null) {
                        doctorAttentionVo.setDateDisplay(DateUtils.formatDateTime(doctorAttentionVo.getDate()));
                    }
                    //根据doctorId查询这一段时间该医生“号源总数”
                    int registerServiceCount = registerService.findDoctorRegisterServiceByData(searchMap);

                    //根据doctorId查询这一段时间该医生“已预约号源数”
                    int appointNumberAlready = doctorInfoService.findDoctorAlreadyAppointNumber(searchMap);

                    //根据doctorId查询这一段时间该医生“剩余预约号源数”
                    int appointNumber = doctorInfoService.findDoctorCanAppointNumber(searchMap);
                    //组合
                    if (doctorAttentionVo != null) {
                        doctorAttentionVo.setAppointNumber(String.valueOf(appointNumber) == null ? "无" : String.valueOf(appointNumber));
                        doctorAttentionVo.setAppointNumberAlready(String.valueOf(appointNumberAlready) == null ? "无" : String.valueOf(appointNumberAlready));
                        doctorAttentionVo.setRegisterServiceCount(String.valueOf(registerServiceCount) == null ? "无" : String.valueOf(registerServiceCount));
                        if ("".equals(doctorAttentionVo.getMarketer()) || doctorAttentionVo.getMarketer() == null) {
                            doctorAttentionVo.setMarketer("无");
                        }
                        if ("".equals(doctorAttentionVo.getNickname()) || doctorAttentionVo.getNickname() == null) {
                            doctorAttentionVo.setNickname("无");
                        }
                        if ("".equals(doctorAttentionVo.getOpenid()) || (doctorAttentionVo.getOpenid() == null)) {
                            doctorAttentionVo.setOpenid("无");
                        }
                        if ("".equals(doctorAttentionVo.getDate()) || (doctorAttentionVo.getDate() == null)) {
                            doctorAttentionVo.setDate(new Date());
                        }
                        responseList.add(doctorAttentionVo);
                    }
                }
            }
        }
        return responseList;
    }


}
