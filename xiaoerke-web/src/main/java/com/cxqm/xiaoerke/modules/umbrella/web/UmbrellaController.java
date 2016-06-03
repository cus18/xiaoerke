package com.cxqm.xiaoerke.modules.umbrella.web;


import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaFamilyInfo;
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
import java.util.*;


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
        Map<String, Object> numm=new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
        map.put("openid",openid);
        List<Map<String, Object>> list = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map);
        if(list.size()>0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> m = list.get(0);
            if(m.get("baby_id")!=null&&!m.get("baby_id").equals("")){
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("result",3);
                result.put("umbrella",m);
                numm.put("userNums","1");
                numm.put("date",sdf.format(new Date()));
//                result.put("num",babyUmbrellaInfoSerivce.getBabyUmbrellaInfoTotal(numm));
                return result;
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", 2);
            result.put("umbrella", m);

            numm.put("date",sdf.format(new Date()));
            numm.put("userNums","1");
//            result.put("num",babyUmbrellaInfoSerivce.getBabyUmbrellaInfoTotal(numm));
            return result;
        }
        BabyUmbrellaInfo babyUmbrellaInfo=new BabyUmbrellaInfo();
        babyUmbrellaInfo.setOpenid(openid);
        babyUmbrellaInfo.setUmberllaMoney(200000);
        Integer res = babyUmbrellaInfoSerivce.saveBabyUmbrellaInfo(babyUmbrellaInfo);


        Map<String, Object> result=new HashMap<String, Object>();
        result.put("result",res);
        result.put("id",babyUmbrellaInfo.getId());
        result.put("umbrella","");
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
//        String res=utilService.bindUser(phone,code,openid);
//        if(res.equals("0")){
//            result.put("result","3");
//            return result;
//        }
        BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
        babyUmbrellaInfo.setBabyId(params.get("babyId").toString());
        babyUmbrellaInfo.setParentIdCard(params.get("idCard").toString());
        babyUmbrellaInfo.setParentPhone(params.get("phone").toString());
        babyUmbrellaInfo.setParentName(URLDecoder.decode(params.get("parentName").toString(),"UTF-8"));
        babyUmbrellaInfo.setParentType(Integer.parseInt(params.get("parentType").toString()));
        babyUmbrellaInfo.setId(Integer.parseInt(params.get("umbrellaId").toString()));
        if(params.get("truePayMoney")!=null){
            babyUmbrellaInfo.setTruePayMoneys(params.get("truePayMoney").toString());
        }
        String res=babyUmbrellaInfoSerivce.updateBabyUmbrellaInfo(babyUmbrellaInfo)+"";

        //插入家庭成员的信息
        UmbrellaFamilyInfo familyInfo = new UmbrellaFamilyInfo();
        familyInfo.setName(URLDecoder.decode(params.get("parentName").toString(), "UTF-8"));
        familyInfo.setUmbrellaId(Integer.parseInt(params.get("umbrellaId").toString()));
        familyInfo.setSex(Integer.parseInt(params.get("parentType").toString()));
        String idCard = params.get("idCard").toString();
        Date birthDay = DateUtils.StrToDate(idCard.substring(6,14),"yyyyMMdd");
        familyInfo.setBirthday(birthDay);
        babyUmbrellaInfoSerivce.saveFamilyUmbrellaInfo(familyInfo);


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
            String status=openIdStatus.get("status").toString();
            if(status.equals("0")) {
                result.put("status", "0");
                result.put("openid", openid);
                return result;
            }else{
                String id = openIdStatus.get("status").toString();
                result.put("status",id);
                result.put("openid",openid);
                return result;
            }
        }else {
            result.put("status", "1");
            result.put("openid", openid);
            return result;
        }
    }


    /**
     * 判断用户是否存在过数据
     */
    @RequestMapping(value = "/ifExistOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  ifExistOrder(HttpServletRequest request,HttpSession session) {
        Map<String, Object> map=new HashMap<String, Object>();
        Map<String, Object> result=new HashMap<String, Object>();
        String openid= WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
        map.put("openid",openid);
        List<Map<String, Object>> list=babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map);
        if(list.size()>0){
            Map<String, Object> m=list.get(0);
            if(m.get("pay_result")!=null&&!m.get("pay_result").equals("fail")) {
                if (m.get("baby_id") != null && !m.get("baby_id").equals("")) {
                    result.put("result", 3);
                    result.put("umbrella", m);
                    return result;
                }
                result.put("phone", UserUtils.getUser().getPhone());
                result.put("result", 2);
                result.put("umbrella", m);
                return result;
            }else{
                result.put("result",1);
                return result;
            }
        }
        result.put("result",1);
        return result;
    }

    /**
     * 判断用户是否存在过数据
     */
    @RequestMapping(value = "/updateActivationTime", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  updateActivationTime(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) {
        Map<String, Object> map=new HashMap<String, Object>();
        Map<String, Object> result=new HashMap<String, Object>();
        BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
        babyUmbrellaInfo.setId(Integer.parseInt(params.get("id").toString()));
        babyUmbrellaInfo.setActivationTime(new Date());
        String res=babyUmbrellaInfoSerivce.updateBabyUmbrellaInfoById(babyUmbrellaInfo)+"";
        result.put("result",res);
        return result;
    }

    /**
     * 判断用户是否存在过数据
     */
    @RequestMapping(value = "/updateTruePayMoney", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  updateTruePayMoney(@RequestBody Map<String, Object> params) {
        Map<String, Object> map=new HashMap<String, Object>();
        Map<String, Object> result=new HashMap<String, Object>();
        BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
        babyUmbrellaInfo.setId(Integer.parseInt(params.get("id").toString()));
        babyUmbrellaInfo.setTruePayMoneys(params.get("truePayMoneys").toString());
        String res=babyUmbrellaInfoSerivce.updateBabyUmbrellaInfoById(babyUmbrellaInfo)+"";
        result.put("result",res);
        return result;
    }


    /**
     * 随机立减
     */
    @RequestMapping(value = "/randomMoney", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  randomMoney(HttpServletRequest request,HttpSession session) {
        Map<String, Object> map=new HashMap<String, Object>();
        Map<String, Object> numm=new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
        map.put("openid",openid);
        List<Map<String, Object>> list = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map);
        if(list.size()>0){
            Map<String, Object> m = list.get(0);
            if(m.get("true_pay_moneys")!=null){
                Map<String, Object> result=new HashMap<String, Object>();
                String res=m.get("true_pay_moneys").toString();
                result.put("type","pay");
                result.put("result",res);
                result.put("id",m.get("id").toString());
                return result;
            }else{
                Map<String, Object> result=new HashMap<String, Object>();
                String type=list.get(0).get("true_pay_moneys").toString();
                result.put("type","free");
                return result;
            }

        }
        Map<String, Object> result=new HashMap<String, Object>();
        String res=String.format("%.0f", Math.random() * 5);
        BabyUmbrellaInfo babyUmbrellaInfo=new BabyUmbrellaInfo();
        babyUmbrellaInfo.setOpenid(openid);
        babyUmbrellaInfo.setUmberllaMoney(200000);
        babyUmbrellaInfo.setTruePayMoneys(res);
        if(res.equals("0")){
            babyUmbrellaInfo.setPayResult("success");
            babyUmbrellaInfo.setActivationTime(new Date());
        }else {
            babyUmbrellaInfo.setPayResult("fail");
        }
        Integer ssss = babyUmbrellaInfoSerivce.saveBabyUmbrellaInfo(babyUmbrellaInfo);
        result.put("type","pay");
        result.put("result",res);
        result.put("id",babyUmbrellaInfo.getId());
        return result;
    }

/**
 * 家庭版保护伞增加成员
 */
    @RequestMapping(value = "/addFamily", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> addFamilyUmbrella(@RequestBody Map<String, Object> params){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        UmbrellaFamilyInfo familyInfo = new UmbrellaFamilyInfo();
        Date birthDay = DateUtils.StrToDate(params.get("birthDay").toString(), "yyyy-MM-dd");
        familyInfo.setBirthday(birthDay);
        familyInfo.setUmbrellaId(Integer.parseInt((String)params.get("id")));
        familyInfo.setName(params.get("name").toString());
        familyInfo.setSex(Integer.parseInt((String) params.get("sex")));
        int reusltStatus = babyUmbrellaInfoSerivce.saveFamilyUmbrellaInfo(familyInfo);
        resultMap.put("reusltStatus",reusltStatus);
      return resultMap;
    }

    /**
     * 家庭版保护成员列表
     */
    @RequestMapping(value = "/familyList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> familyUmbrellaList(@RequestBody Map<String, Object> params){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer id = Integer.parseInt((String) params.get("id"));
        List<UmbrellaFamilyInfo> list = new ArrayList<UmbrellaFamilyInfo>();
        list = babyUmbrellaInfoSerivce.getFamilyUmbrellaList(id);
        BabyBaseInfoVo babyInfo = babyUmbrellaInfoSerivce.getBabyBaseInfo(id);
        if(babyInfo!=null){
            UmbrellaFamilyInfo familyInfo = new UmbrellaFamilyInfo();
            familyInfo.setSex(Integer.parseInt(babyInfo.getSex()));
            familyInfo.setName(babyInfo.getName());
            familyInfo.setBirthday(babyInfo.getBirthday());
            list.add(familyInfo);
        }
        resultMap.put("familyList",list);
        return resultMap;
    }


//    /**
//     * 付费加入保护伞
//     */
//    @RequestMapping(value = "/payJoinUs", method = {RequestMethod.POST, RequestMethod.GET})
//    public
//    @ResponseBody
//    Map<String, Object>  payJoinUs(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) {
//        Map<String, Object> map=new HashMap<String, Object>();
//        String openid = WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
//
//
//        Map<String, Object> result=new HashMap<String, Object>();
//        result.put("result",res);
//        result.put("id",babyUmbrellaInfo.getId());
//        return result;
//    }
}
