package com.cxqm.xiaoerke.common.utils;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.util.List;


public class JsonUtil {

    // Java object to JSON String
    public static String getJsonStrFromObj(Object obj) {
        JSONSerializer serializer = new JSONSerializer();
        return serializer.serialize(obj);
    }

    // Java object to JSON String
    public static String getJsonStrFromObj(Object obj, String... params) {
        JSONSerializer serializer = new JSONSerializer();
        serializer = serializer.include(params);
        return serializer.serialize(obj);
    }

    // List to JSON String
    public static String getJsonStrFromList(List<Object> objs, String rootName,
            String... params) {
        JSONSerializer serializer = new JSONSerializer();
        serializer = serializer.include(params);
        serializer.rootName(rootName);
        return serializer.serialize(objs);
    }

    /**

     * 将List转化为json字符串

     * @param objs list对象

     * @return

     */
    public static String getJsonStrFromList(List<Object> objs){
        return new JSONSerializer().exclude("class").serialize(objs);
    }

    // Json String to Java object
    public static <T> T getObjFromJsonStr(String source, Class<T> bean) {
        return new JSONDeserializer<T>().deserialize(source, bean);
    }
    
    
}