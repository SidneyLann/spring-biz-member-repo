package com.blockchain.base.data;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
public class CacheClient {
  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  public void set(String key, Object value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public void set(String key, Object value, long time) {
    if (time > 0)
      redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    else
      redisTemplate.opsForValue().set(key, value, 1, TimeUnit.HOURS);
  }

  public void setSecond(String key, Object value, long time) {
    redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
  }

  public void setHour(String key, Object value, long time) {
    redisTemplate.opsForValue().set(key, value, time, TimeUnit.HOURS);
  }

  public void setDay(String key, Object value, long time) {
    redisTemplate.opsForValue().set(key, value, time, TimeUnit.DAYS);
  }

  public Object get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public String getString(String key) {
    return (String) get(key);
  }

  public Integer getInteger(String key) {
    return (Integer) get(key);
  }

  public Short getShort(String key) {
    return (Short) get(key);
  }

  public Long getLong(String key) {
    return (Long) get(key);
  }

  public Float getFloat(String key) {
    return (Float) get(key);
  }

  public Double getDouble(String key) {
    return (Double) get(key);
  }

  public Boolean delete(String key) {
    return redisTemplate.delete(key);
  }

  public Long deleteAll(String key) {
    Set<String> keys = redisTemplate.keys(key);
    System.out.println(key + " delete all " + keys);
    return redisTemplate.delete(keys);
  }

}
