package com.blockchain.base.data;

import io.shardingsphere.core.keygen.DefaultKeyGenerator;

public class IdGenerator {
  private static DefaultKeyGenerator defaultKeyGenerator = new DefaultKeyGenerator();

  public long genId() {
    return defaultKeyGenerator.generateKey().longValue();
  }

  public String genOrderNo() {
    int orderNo = defaultKeyGenerator.generateKey().intValue();

    if (orderNo < 0)
      orderNo = -orderNo;

    return orderNo + "";
  }
}
