package com.blockchain.common.base;

import java.util.Date;

public class BaseDto extends RootDto {

  private Long createBy;

  private Date createTime;

  private Date createTime2;

  private String remark;

  private Long updateBy;

  private Date updateTime;

  private Date updateTime2;

  private Short orderBy;

  public Long getCreateBy() {
    return createBy;
  }

  public void setCreateBy(Long createBy) {
    this.createBy = createBy;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getCreateTime2() {
    return createTime2;
  }

  public void setCreateTime2(Date createTime2) {
    this.createTime2 = createTime2;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Long getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(Long updateBy) {
    this.updateBy = updateBy;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Date getUpdateTime2() {
    return updateTime2;
  }

  public void setUpdateTime2(Date updateTime2) {
    this.updateTime2 = updateTime2;
  }

  public Short getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(Short orderBy) {
    this.orderBy = orderBy;
  }

}
