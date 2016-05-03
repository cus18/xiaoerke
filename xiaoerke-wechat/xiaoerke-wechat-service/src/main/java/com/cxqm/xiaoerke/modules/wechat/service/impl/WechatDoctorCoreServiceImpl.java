package com.cxqm.xiaoerke.modules.wechat.service.impl;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.EmojiFilter;
import com.cxqm.xiaoerke.common.utils.ReceiveXmlProcess;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.sys.entity.*;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatInfoDao;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatDoctorCoreService;
import com.cxqm.xiaoerke.modules.wechat.service.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 微信公众账号核心服务类
 * 提供微信公众号的响应事件
 * @author wangbaowei
 * @date 2015-11-04
 */

@Service
@Transactional(readOnly = false)
public class WechatDoctorCoreServiceImpl implements WechatDoctorCoreService {

    @Autowired
    private WechatInfoDao wechatInfoDao;

    @Autowired
    private MongoDBService<WechatAttention> mongoDBService;

    @Autowired
    private MongoDBService<MongoLog> mongoLogService;
    
    @Autowired
    private SystemService systemService;

    private String mongoEnabled = Global.getConfig("mongo.enabled");

    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    public  String processDoctorRequest(HttpServletRequest request) throws IOException {

        String respMessage = null;

        /** 读取接收到的xml消息 */
        StringBuffer sb = new StringBuffer();
        InputStream is = request.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        String xml = sb.toString();	//次即为接收到微信端发送过来的xml数据

        /** 解析xml数据 */
        ReceiveXmlEntity xmlEntity = new ReceiveXmlProcess().getMsgEntity(xml);
        String msgType = xmlEntity.getMsgType();
        if(xmlEntity.getMsgType().equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT))
        {
            // xml请求解析
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = xmlEntity.getEvent();
                if(eventType.equals(MessageUtil.SCAN)){
                    String EventKey = xmlEntity.getEventKey();
                    Date updateDate = new Date();
                    String openId = xmlEntity.getFromUserName();
                    String marketer = EventKey.replace("qrscene_", "");
                    HashMap<String,Object> updateTimeMap = new HashMap<String, Object>();
                    updateTimeMap.put("openId",openId);
                    updateTimeMap.put("updateTime",updateDate);
                    updateTimeMap.put("doctorMarketer",marketer);
                    //new zdl
                    wechatInfoDao.updateAttentionInfo(updateTimeMap);
                }
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    String EventKey = xmlEntity.getEventKey();
                    HashMap<String,Object> map = new HashMap<String, Object>();
                    String openId = xmlEntity.getFromUserName();
                    String marketer = EventKey.replace("qrscene_", "");
                    map.put("openId",openId);
                    map.put("marketer",marketer);
                    String id = UUID.randomUUID().toString().replaceAll("-", "");
                    map.put("id", id);
                    map.put("status", "0");
                    Map<String,Object> parameter = systemService.getWechatParameter();
                    String token = (String)parameter.get("token");
                    WechatBean wechatBean = WechatUtil.getWechatName(token, openId);
                    map.put("nickname", EmojiFilter.coverEmoji(wechatBean.getNickname()));
                    int attentionNum = wechatInfoDao.checkAttention(map);
                    if(attentionNum > 0)
                    {
                        map.put("ispay",0);
                    }
                    else
                    {
                        map.put("ispay",1);
                    };
                    wechatInfoDao.insertAttentionInfo(map);

                    if("true".equalsIgnoreCase(mongoEnabled))
                    {
                        WechatAttention attention = new WechatAttention();
                        attention.setId(id);
                        attention.setDate(new Date());
                        attention.setMarketer(marketer);
                        attention.setNickname(wechatBean.getNickname());
                        attention.setOpenid(openId);
                        attention.setStatus("0");
                        mongoDBService.insert(attention);

                        //将数据插入到mongodb中的sys_log表中
                        MongoLog mongoLog = new MongoLog();
                        mongoLog.setId(id);
                        mongoLog.setCreate_date(new Date());
                        mongoLog.setCreate_by(UserUtils.getUser().getId());
                        mongoLog.setTitle("00000001");//微信宝大夫用户版公众平台关注
                        mongoLog.setIsPay((String)map.get("ispay"));
                        mongoLog.setNickname(wechatBean.getNickname());
                        mongoLog.setOpen_id(openId);
                        mongoLog.setMarketer(marketer);
                        mongoLog.setStatus((String) map.get("status"));
                        mongoLogService.insert(mongoLog);
                    }

                    HttpSession session = request.getSession();
                    session.setAttribute("openId", xmlEntity.getFromUserName());
                    String st = "";
                    TextMessage textMessage = new TextMessage();
                    textMessage.setToUserName(xmlEntity.getFromUserName());
                    textMessage.setFromUserName(xmlEntity.getToUserName());
                    textMessage.setCreateTime(new Date().getTime());
                    textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
                    textMessage.setFuncFlag(0);
                    LogUtils.saveLog(request, "00000092");
                    st = "医生您好，宝大夫专家版，专为您精心打造，让我们一起用自己的专业能力来帮助更多人！如需帮助请致电:400-623-7120.";
                    textMessage.setContent(st);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                    WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(),st);
                }
                // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE))
                {
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                    HttpSession session = request.getSession();
                    session.setAttribute("openId", xmlEntity.getFromUserName());
                    LogUtils.saveLog(request,"00000088");//微信宝大夫医生版公众平台取消关注
                    String EventKey = xmlEntity.getEventKey();
                    HashMap<String,Object> map = new HashMap<String, Object>();
                    String openId = xmlEntity.getFromUserName();
                    map.put("openId", openId);
                    String marketer = EventKey.replace("qrscene_", "");
                    map.put("marketer", marketer);
                    String id = UUID.randomUUID().toString().replaceAll("-", "");
                    map.put("id", id);
                    map.put("status","1");
                    wechatInfoDao.insertAttentionInfo(map);

                    if("true".equalsIgnoreCase(mongoEnabled))
                    {
                        WechatAttention attention = new WechatAttention();
                        attention.setId(id);
                        attention.setDate(new Date());
                        attention.setMarketer(marketer);
                        attention.setOpenid(openId);
                        attention.setStatus("1");
                        mongoDBService.insert(attention);

                        //将数据插入到mongodb中的sys_log表中
                        MongoLog mongoLog = new MongoLog();
                        mongoLog.setId(id);
                        mongoLog.setCreate_date(new Date());
                        mongoLog.setCreate_by(UserUtils.getUser().getId());
                        mongoLog.setTitle("00000087");//微信宝大夫医生版公众平台取消关注
                        mongoLog.setOpen_id(openId);
                        mongoLog.setMarketer(marketer);
                        mongoLog.setStatus((String) map.get("status"));
                        mongoLogService.insert(mongoLog);
                    }
                }
                else if (eventType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION))
                {
                    LogUtils.saveLog(request, "00000093");//获取地理位置信息
                    HashMap<String,Object> map = new HashMap<String, Object>();
                    map.put("id",UUID.randomUUID().toString().replaceAll("-", ""));
                    map.put("latitude",xmlEntity.getLatitude());
                    map.put("precision",xmlEntity.getPrecision());
                    map.put("createTime",xmlEntity.getCreateTime());
                    map.put("openid",xmlEntity.getFromUserName());
                    map.put("longitude",xmlEntity.getLongitude());
                    wechatInfoDao.insertCustomerLocation(map);
                }
            }
        }
        else
        {
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(xmlEntity.getFromUserName());
            textMessage.setFromUserName(xmlEntity.getToUserName());
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.TRANSFER_CUSTOMER_SERVICE);
            textMessage.setFuncFlag(0);
            respMessage = MessageUtil.textMessageToXml(textMessage);
        }
        return respMessage;
    }
}
