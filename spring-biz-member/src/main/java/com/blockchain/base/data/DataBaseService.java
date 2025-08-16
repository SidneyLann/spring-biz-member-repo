package com.blockchain.base.data;

import com.blockchain.common.base.BizzException;
import com.blockchain.common.base.OpResult;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.mysql.MySQLQueryFactory;

import jakarta.annotation.Resource;

public class DataBaseService extends BaseService {
  @Resource
  protected JPAQueryFactory jpaQuery;

  @Resource
  protected MySQLQueryFactory sqlQuery;

  protected void checkResult(OpResult opResult) {
    if (opResult.getCode() != OpResult.CODE_COMM_0_SUCCESS)
      throw new BizzException(opResult);
  }

  protected void throwBizzException(OpResult opResult) {
    throw new BizzException(opResult);
  }

  protected void throwBizzException(int errorCode) {
    throw new BizzException(errorCode);
  }

  protected void throwBizzException(String message) {
    throw new BizzException(message);
  }
}
