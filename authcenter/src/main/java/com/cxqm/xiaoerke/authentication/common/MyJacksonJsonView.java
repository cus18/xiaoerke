package com.cxqm.xiaoerke.authentication.common;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

/**
 * Created by Administrator on 2014/9/2.
 */
public class MyJacksonJsonView extends MappingJackson2JsonView {
    @Override
    protected Object filterModel(Map<String, Object> model) {
        if(!model.containsKey("resultCode"))
            model.put("resultCode","0");
        if(!model.containsKey("resultMsg"))
            model.put("resultMsg","OK");
        return super.filterModel(model);
    }
}
