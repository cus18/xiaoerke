package com.cxqm.xiaoerke.modules.order.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.PayRecordService;
import com.cxqm.xiaoerke.modules.order.dao.OrderPropertyDao;
import com.cxqm.xiaoerke.modules.order.dao.PatientRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.OrderPropertyVo;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.OrderPropertyService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by deliang
 */
@Service
@Transactional(readOnly = false)
public class OrderPropertyServiceImpl implements OrderPropertyService {

    @Autowired
    PatientRegisterServiceDao patientRegisterServiceDao;

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    OrderPropertyDao orderPropertyDao;

    private static ExecutorService threadExecutor = Executors.newCachedThreadPool();


    @Override
    public void saveOrderProperty(OrderPropertyVo orderPropertyVo) {
        Runnable thread = new OrderPropertyThread(orderPropertyVo);
        threadExecutor.execute(thread);
    }

    public class OrderPropertyThread extends Thread {
        private OrderPropertyVo propertyVo;

        public OrderPropertyThread(OrderPropertyVo orderPropertyVo) {
            this.propertyVo = orderPropertyVo;
        }

        public void run() {
            if ("kefu".equals(propertyVo.getOrderSource())) {//客服预约
                propertyVo.setYellowCattle("null");
                propertyVo.setFirstOrder("null");
                propertyVo.setScanCode("null");
                propertyVo.setCharge("no");
            } else {//用户预约
                saveOrderProperty();
            }
            //保存订单属性
            String date = DateUtils.DateToStr(new Date());
            propertyVo.setCreateDate(date);
            propertyVo.setId(IdGen.uuid());
            orderPropertyDao.insert(propertyVo);
        }

        private void saveOrderProperty() {
            Map map = new HashMap();
            map.put("openid", propertyVo.getOpenid());
            map.put("firstOrder", "no");

            //判断此订单是不是黄牛约的（规则：同一 openid 预约三个宝宝（不重名），则此用户是黄牛）
            List<PatientRegisterServiceVo> yellowCattleVo = patientRegisterServiceDao.findPageRegisterServiceByPatient(map);
            System.out.println("判断是不是黄牛？" + yellowCattleVo.size());
            if (yellowCattleVo.size() > 3) {
                propertyVo.setYellowCattle("yes");//是黄牛
                System.out.println("是黄牛:" + yellowCattleVo.size());
            } else {
                propertyVo.setYellowCattle("no");//不是黄牛
                System.out.println("不是黄牛:" + yellowCattleVo.size());
            }

            //判断此订单是不是此用户首次预约的（有效预约，根据openid查询之前有没有成功约过）
            map.put("firstOrder", "yes");
            HashMap hashMap = new HashMap();
            hashMap.put("unBindUserPhoneNum", UserUtils.getUser().getPhone());
            Page<PatientRegisterServiceVo> page = FrontUtils.generatorPage("1", "10");//暂设设固定值
            Page<PatientRegisterServiceVo> resultPage = patientRegisterServiceDao.getUnBindUserPhoneOrder(page, hashMap);

            List<PatientRegisterServiceVo> firstOrderVo = resultPage.getList();
            System.out.println("判断是不是首次预约？" + firstOrderVo.size());
            if (firstOrderVo.size() == 1) {
                propertyVo.setFirstOrder("yes");
                System.out.println("是首单:" + firstOrderVo.size());
            } else {
                propertyVo.setFirstOrder("no");
                System.out.println("不是首单:" + firstOrderVo.size());
            }

            //判断此订单是不是扫码预约用户（根据openid查marketer）
            PatientRegisterServiceVo patientRegisterServiceVo = new PatientRegisterServiceVo();
            patientRegisterServiceVo.setOpenId(propertyVo.getOpenid());
            patientRegisterServiceVo.setId(propertyVo.getPatientRegisterServiceId());
            List<PatientRegisterServiceVo> attentVos = patientRegisterServiceDao.findAttentionInfoByOpenIdLists(patientRegisterServiceVo);
            System.out.println("判断是不是扫码用户？" + attentVos.size());

            if (attentVos.size() > 0) {
                propertyVo.setScanCode("yes");//是扫码关注的
                System.out.println("是扫码用户" + attentVos.size());
            } else {
                propertyVo.setScanCode("no");
                System.out.println("不是扫码用户" + attentVos.size());

            }

            //判断此订单有没有付费（查询账户记录表success）
            System.out.println("订单主键？" + propertyVo.getPatientRegisterServiceId());
            PayRecord payRecord = payRecordService.findPayRecordByOrder(propertyVo.getPatientRegisterServiceId());
            if (payRecord != null) {
                propertyVo.setCharge("yes");//付费
                System.out.println("付费" + payRecord.getCreatedBy());
            } else {
                propertyVo.setCharge("no");//没付费
                System.out.println("没有付费" + payRecord);
            }
        }

    }
}
