package com.njust.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.Map;

public class JsonHelper {
    public static Map<String, Object> obj2Map(Object obj) {
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = (Map<String, Object>) oMapper.convertValue(obj, Map.class);
        return map;
    }

    public static String obj2Json(Object obj) {
        return JSONObject.valueToString(obj);
    }
}
