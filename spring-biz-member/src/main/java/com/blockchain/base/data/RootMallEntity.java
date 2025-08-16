package com.blockchain.base.data;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@MappedSuperclass
public abstract class RootMallEntity extends RootEntity {

  @Column(name = "mall_type")
  @Min(1)
  @Max(100)
  private Short mallType;

  public Short getMallType() {
    return mallType;
  }

  public void setMallType(Short mallType) {
    this.mallType = mallType;
  }
  
}
