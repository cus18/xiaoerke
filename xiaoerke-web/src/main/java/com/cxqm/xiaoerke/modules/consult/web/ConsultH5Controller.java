package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.service.ConsultH5Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by jiangzhongge on 2016-4-21.
 */
@Controller
@RequestMapping(value = "consult/h5")
public class ConsultH5Controller extends BaseController{

    @Autowired
    private ConsultH5Service consultH5Service;
    /**
     * H5咨询上传文件
     * @param file
     * @param data
     * @return {"status","success"}
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="/uploadMediaFile",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody HashMap<String,Object> UploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("data") String data) throws UnsupportedEncodingException {
        HashMap<String, Object> response = consultH5Service.uploadH5MediaFile(file, data);
        return response;
    }
}
