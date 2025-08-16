package com.blockchain.common.base;

import java.io.Serializable;

public class Tuple2<A, B> implements Serializable {
  private A first;
  private B second;

  public Tuple2(A a, B b) {
    this.first = a;
    this.second = b;
  }

  public A getFirst() {
    return first;
  }

  public void setFirst(A first) {
    this.first = first;
  }

  public B getSecond() {
    return second;
  }

  public void setSecond(B second) {
    this.second = second;
  }
  
}
