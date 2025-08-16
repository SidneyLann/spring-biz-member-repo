package com.blockchain.common.util;

import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).serializeNulls().create(); // DateNullAdapterFactory<>()).create();
  private static JsonParser parser = new JsonParser();
  private static String WHITE_SPACE = "<WHITE~SPACE>";
  private static byte NODE_TYPE_NODE = 1;
  private static byte NODE_TYPE_ELEMENT = 3;
  private byte type = NODE_TYPE_NODE;
  private JsonObject rootNode;
  private JsonObject currNode;
  private JsonObject prevNode;
  private JsonElement jsonElement;
  private JsonArray jsonArray;

  public JsonUtil(String source) {
    rootNode = parser.parse(source).getAsJsonObject();
    currNode = rootNode;
  }

  public static String object2Json(Object source) {
    return gson.toJson(source);
  }

  public static <T extends Object> T json2Object(String source, java.lang.Class<T> clazz) {
    return gson.fromJson(source, clazz);
  }

  public static <T extends Object> T object2Object(Object source, java.lang.Class<T> clazz) {
    return gson.fromJson(gson.toJson(source), TypeToken.get(clazz).getType());
  }

  public static <T extends Object> List<T> json2List(String source, java.lang.Class<T> clazz) {
    List<T> result = gson.fromJson(source, TypeToken.getParameterized(List.class, clazz).getType());

    return result;
  }

  public static <T extends Object> List<T> obj2List(Object source, java.lang.Class<T> clazz) {
    String json = gson.toJson(source);
    List<T> result = gson.fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());

    return result;
  }

  public static <T extends Object> Map<String, T> json2Map(String source, java.lang.Class<T> clazz) {
    Map<String, T> result = gson.fromJson(source, TypeToken.getParameterized(Map.class, clazz).getType());

    return result;
  }

  public static JsonObject json2Json(String source) {
    return parser.parse(source).getAsJsonObject();
  }

  public static String replaceWhiteSpace(String source) {
    return source.replace(" ", WHITE_SPACE);
  }

  public static String restoreWhiteSpace(String source) {
    return source.replace(WHITE_SPACE, " ");
  }

  public void moveToNode(String... names) {
    type = NODE_TYPE_NODE;
    int size = names.length;
    prevNode = currNode;
    for (int i = 0; i < size; i++) {
      currNode = currNode.getAsJsonObject(names[i]);
    }
  }

  public void moveToNode(int idx) {
    type = NODE_TYPE_NODE;
    currNode = jsonArray.get(idx).getAsJsonObject();
  }

  public void moveToElement(int idx) {
    type = NODE_TYPE_ELEMENT;
    jsonElement = jsonArray.get(idx);
  }

  public int moveToList(String... names) {
    int size = names.length;
    for (int i = 0; i < size - 1; i++) {
      currNode = currNode.getAsJsonObject(names[i]);
    }

    jsonArray = currNode.getAsJsonArray(names[size - 1]);

    return jsonArray.size();
  }

  public String getAsString(String name) {
    if (type == NODE_TYPE_ELEMENT)
      return jsonElement.getAsString();
    return currNode.get(name).getAsString();
  }

  public Short getAsShort(String name) {
    if (type == NODE_TYPE_ELEMENT)
      return jsonElement.getAsShort();
    return currNode.get(name).getAsShort();
  }

  public Integer getAsInt(String name) {
    if (type == NODE_TYPE_ELEMENT)
      return jsonElement.getAsInt();
    return currNode.get(name).getAsInt();
  }

  public Long getAsLong(String name) {
    if (type == NODE_TYPE_ELEMENT)
      return jsonElement.getAsLong();
    return currNode.get(name).getAsLong();
  }

  public BigInteger getAsBigInt(String name) {
    if (type == NODE_TYPE_ELEMENT)
      return jsonElement.getAsBigInteger();
    return currNode.get(name).getAsBigInteger();
  }

  public Float getAsFloat(String name) {
    if (type == NODE_TYPE_ELEMENT)
      return jsonElement.getAsFloat();
    return currNode.get(name).getAsFloat();
  }

  public Double getAsDouble(String name) {
    if (type == NODE_TYPE_ELEMENT)
      return jsonElement.getAsDouble();
    return currNode.get(name).getAsDouble();
  }

  public void reset() {
    currNode = rootNode;
  }

  public void prev() {
    currNode = prevNode;
  }
}
