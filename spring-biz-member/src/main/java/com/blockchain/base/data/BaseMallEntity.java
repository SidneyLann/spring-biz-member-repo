package com.blockchain.base.data;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@MappedSuperclass
public abstract class BaseMallEntity extends BaseEntity {

  @Column(name = "mall_type")
  @NotNull
  @Min(1)
  private Short mallType;

  public Short getMallType() {
    return mallType;
  }

  public void setMallType(Short mallType) {
    this.mallType = mallType;
  }
  
}
