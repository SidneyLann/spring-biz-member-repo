package com.blockchain.common.base;

public final class MyResponeEntity {
  private String code;
  private String message;

  private MyResponeEntity(Builder b) {
    this.code = b.code;
    this.message = b.message;
  }

  public static class Builder {
    private String code;
    private String message;

    public Builder() {

    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public MyResponeEntity build() {
      return new MyResponeEntity(this);
    }
  }
}
