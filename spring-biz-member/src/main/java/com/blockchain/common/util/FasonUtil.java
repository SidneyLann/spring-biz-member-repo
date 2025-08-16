package com.blockchain.common.util;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

public class FasonUtil {

  public static JSONObject json2JsonObj(String source) {
    return JSON.parseObject(source);
  }

  public static <T extends Object> T json2Object(String source, Class<T> clazz) {
    return JSON.parseObject(source, clazz);
  }

  public static String obj2Json(Object source) {
    return JSON.toJSONString(source);
  }

  public static <T extends Object> T obj2Obj(Object source, Class<T> clazz) {
    return json2Object(JSON.toJSONString(source), clazz);
  }

  public static <T extends Object> List<T> obj2List(Object source, Class<T> clazz) {
    return JSON.parseArray(obj2Json(source),clazz);
  }
}
