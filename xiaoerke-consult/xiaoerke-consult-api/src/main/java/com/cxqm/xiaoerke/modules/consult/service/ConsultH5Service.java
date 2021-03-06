package com.cxqm.xiaoerke.modules.consult.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by jiangzhongge on 2016-4-21.
 *
 */
public interface ConsultH5Service {

    //咨询H5上传文件
    HashMap<String, Object> uploadH5MediaFile(@RequestParam("file") MultipartFile file, @RequestParam("data") String data) throws UnsupportedEncodingException;
}
