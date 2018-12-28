package com.cxs.core.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class JsonUtil {

    private JsonUtil(){}

    public static String toJsonString(List<Object> list) {
        return JSONObject.toJSONString(list);
    }

    public static List<Object> toBeanList(String json, Class cls) {
        return JSONObject.parseArray(json, cls);
    }

    public static Object toBean(String json, Class cls) {
        return JSONObject.parseArray(json, cls);
    }
}
