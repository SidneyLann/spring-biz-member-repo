package com.blockchain.base.data;

import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Resource;

public abstract class BaseService {
  @Resource
  protected RestTemplate restTemplate;

  @Resource
  protected IdGenerator idGenerator;

  @Resource
  protected CacheClient cacheClient;

  protected static int PAGE_COUNT = 30;
}
