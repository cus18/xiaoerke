/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.webapp.controller;

import com.cxqm.xiaoerke.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;


/**
 * 会员Controller
 *
 * @author deliang
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "consult/wechat")
public class ConsultWechatController extends BaseController {

    @RequestMapping(value = "/conversation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> conversation(@RequestParam(required=true) String openId,
                                     @RequestParam(required=true) String messageType,
                                     @RequestParam(required=false) String messageContent,
                                     @RequestParam(required=false) String mediaId) {

        if(messageType.equals("text")){

        }else{

        }

        try {
            System.out.println(URLDecoder.decode(messageContent, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, Object> response = new HashMap<String, Object>();

        return response;
    }

}
