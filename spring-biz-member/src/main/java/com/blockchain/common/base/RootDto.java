package com.blockchain.common.base;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

public class RootDto implements java.io.Serializable {
  private Long id;

  private Short action;

  private Short status;

  private List<Short> statuses;

  private Short pageNo = 1;

  private Short pageSize = 50;

  private Short mallType;

  private Short sorting;

  private boolean forUpdate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Short getAction() {
    return action;
  }

  public void setAction(Short action) {
    this.action = action;
  }

  public Short getStatus() {
    return status;
  }

  public void setStatus(Short status) {
    this.status = status;
  }

  public List<Short> getStatuses() {
    return statuses;
  }

  public void setStatuses(List<Short> statuses) {
    this.statuses = statuses;
  }

  public Short getPageNo() {
    return pageNo;
  }

  public void setPageNo(Short pageNo) {
    this.pageNo = pageNo;
  }

  public Short getPageSize() {
    return pageSize;
  }

  public void setPageSize(Short pageSize) {
    this.pageSize = pageSize;
  }

  public Short getMallType() {
    return mallType;
  }

  public void setMallType(Short mallType) {
    this.mallType = mallType;
  }

  public Short getSorting() {
    return sorting;
  }

  public void setSorting(Short sorting) {
    this.sorting = sorting;
  }

  public boolean isForUpdate() {
    return forUpdate;
  }

  public void setForUpdate(boolean forUpdate) {
    this.forUpdate = forUpdate;
  }
  
}
