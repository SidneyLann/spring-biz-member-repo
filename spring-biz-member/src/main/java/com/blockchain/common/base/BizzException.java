package com.blockchain.common.base;

public class BizzException extends RuntimeException {
  private OpResult opResult = new OpResult();

  public BizzException(int code) {
    this.opResult.setCode(code);
  }

  public BizzException(String message) {
    this.opResult.setMessage(message);
  }

  public BizzException(OpResult opResult) {
    this.opResult = opResult;
  }

  public OpResult getOpResult() {
    return opResult;
  }

  @Override
  public String getMessage() {
    return this.opResult.getMessage();
  }
}
