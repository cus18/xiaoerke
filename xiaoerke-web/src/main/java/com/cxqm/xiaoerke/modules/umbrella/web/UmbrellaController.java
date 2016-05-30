package com.cxqm.xiaoerke.modules.umbrella.web;


import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "umbrella")
public class UmbrellaController  {

    @Autowired
    private BabyUmbrellaInfoService babyUmbrellaInfoSerivce;


    @Autowired
    private UtilService utilService;

  /**
  *获取保护伞首页信息
  */
  @RequestMapping(value = "/firstPageData", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
    Map<String, Object>  firstPageData() {
      Map<String, Object> map=new HashMap<String, Object>();
      Integer count = babyUmbrellaInfoSerivce.getBabyUmbrellaInfoTotal(map);
      Integer totalUmbrellaMoney = babyUmbrellaInfoSerivce.getTotalBabyUmbrellaInfoMoney(map);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      map.put("today",sdf.format(new Date()));
      Integer todayCount = babyUmbrellaInfoSerivce.getBabyUmbrellaInfoTotal(map);
      Map<String, Object> result = new HashMap<String, Object>();
      result.put("count", count);
      result.put("todayCount", todayCount);
      result.put("totalUmbrellaMoney", totalUmbrellaMoney);
     return result;
  }

    /**
     * 加入保护伞
     */
    @RequestMapping(value = "/joinUs", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  joinUs(HttpServletRequest request,HttpSession session) {
        Map<String, Object> map=new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
        map.put("openid",openid);
        List<Map<String, Object>> list = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map);
        if(list.size()>0){
            Map<String, Object> m = list.get(0);
            if(m.get("baby_id")!=null&&!m.get("baby_id").equals("")){
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("result",3);
                result.put("umbrella",m);
                return result;
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", 2);
            result.put("umbrella", m);
            return result;
        }
        BabyUmbrellaInfo babyUmbrellaInfo=new BabyUmbrellaInfo();
        babyUmbrellaInfo.setOpenid(openid);
        babyUmbrellaInfo.setUmberllaMoney(200000);
        Integer res = babyUmbrellaInfoSerivce.saveBabyUmbrellaInfo(babyUmbrellaInfo);
        Map<String, Object> result=new HashMap<String, Object>();
        result.put("result",res);
        result.put("id",babyUmbrellaInfo.getId());
        return result;
    }

    /**
     *更新保护伞用户信息
     */
    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  updateInfo(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException {
        Map<String, Object> result=new HashMap<String, Object>();

        String phone=params.get("phone").toString();
        String code=params.get("code").toString();
        String openid= WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
        String res = utilService.bindUser(phone,code,openid);
        if(res.equals("0")){
            result.put("result","3");
            return result;
        }
        BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
        babyUmbrellaInfo.setBabyId(params.get("babyId").toString());
        babyUmbrellaInfo.setParentIdCard(params.get("idCard").toString());
        babyUmbrellaInfo.setParentPhone(params.get("phone").toString());
        babyUmbrellaInfo.setParentName(URLDecoder.decode(params.get("parentName").toString(),"UTF-8"));
        babyUmbrellaInfo.setParentType(Integer.parseInt(params.get("parentType").toString()));
        babyUmbrellaInfo.setId(Integer.parseInt(params.get("umbrellaId").toString()));
//        babyUmbrellaInfo.setUmberllaMoney(Integer.parseInt(params.get("umberllaMoney").toString()));
        res=babyUmbrellaInfoSerivce.updateBabyUmbrellaInfo(babyUmbrellaInfo)+"";

        result.put("result",res);
        return result;
    }

    /**
     * 获取用户分享数
     */
    @RequestMapping(value = "/userShareNum", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  userShareNum(HttpServletRequest request,HttpSession session) {
        Map<String, Object> map=new HashMap<String, Object>();
        String openid= WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
        map.put("openid",openid);
        String  id=babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map).get(0).get("id").toString();
        Map<String, Object> result=new HashMap<String, Object>();
        result.put("count",babyUmbrellaInfoSerivce.getUserShareNums(id));
        return result;
    }

    /**
     * 获取用户专属临时二维码
     */
    @RequestMapping(value = "/getUserQRCode", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  getUserQRCode(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) {
//        Map<String, Object> map=new HashMap<String, Object>();
//        String openid= WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
//        map.put("openid",openid);
//        String  id=babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map).get(0).get("id").toString();
        String id = params.get("id").toString();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("qrcode",babyUmbrellaInfoSerivce.getUserQRCode(id));
        return result;
    }

    /**
     * 根据openid 获取用户关注情况
     */
    @RequestMapping(value = "/getOpenidStatus", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  getOpenidStatus(HttpServletRequest request,HttpSession session) {
        String openid= WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String,Object> openIdStatus = babyUmbrellaInfoSerivce.getOpenidStatus(openid);
        if(openIdStatus != null){
            result.put("status","1");
            result.put("openid",openid);
            return  result;
        }
        String id = openIdStatus.get("status").toString();
        result.put("status",id);
        result.put("openid",openid);
        return result;
    }


    /**
     * 判断用户是否存在过数据
     */
    @RequestMapping(value = "/ifExistOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  ifExistOrder(HttpServletRequest request,HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        String openid= WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
        map.put("openid",openid);
        List<Map<String, Object>> list = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map);
        if(list.size()>0){
            Map<String, Object> m = list.get(0);
            if(m.get("baby_id")!=null&&!m.get("baby_id").equals("")){
                result.put("result",3);
                result.put("umbrella",m);
                return result;
            }
            result.put("result",2);
            result.put("umbrella",m);
            return result;
        }
        result.put("result",1);
        return result;
    }
}
