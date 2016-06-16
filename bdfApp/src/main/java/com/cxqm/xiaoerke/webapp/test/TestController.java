package com.cxqm.xiaoerke.webapp.test;

import com.cxqm.xiaoerke.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
@Controller
@RequestMapping(value="")
public class TestController extends BaseController {
    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String test(){
        return "testPage";
    }

}
