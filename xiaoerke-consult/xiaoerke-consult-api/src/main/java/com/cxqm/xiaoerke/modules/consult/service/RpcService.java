package com.cxqm.xiaoerke.modules.consult.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public interface RpcService {

    public String getMessage(String param);

    public String sendMessage(String param);
}
