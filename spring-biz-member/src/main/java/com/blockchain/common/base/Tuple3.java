package com.blockchain.common.base;

import java.io.Serializable;


public class Tuple3<A, B, C> implements Serializable {
  private A first;
  private B second;
  private C third;

  public Tuple3() {}
  
  public Tuple3(A a, B b, C c) {
    this.first = a;
    this.second = b;
    this.third = c;
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

  public C getThird() {
    return third;
  }

  public void setThird(C third) {
    this.third = third;
  }
  
}
