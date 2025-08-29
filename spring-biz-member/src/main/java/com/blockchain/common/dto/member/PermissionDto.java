package com.blockchain.common.dto.member;

import java.util.List;

import com.blockchain.common.base.RootDto;

public class PermissionDto extends RootDto {
  private Long userId;

  private Long permissionId;
  
  private Long orgId;
  
  private String orgName;
  
  private Short orgType;

  private List<Long> permissionIds;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(Long permissionId) {
    this.permissionId = permissionId;
  }

  public Long getOrgId() {
    return orgId;
  }

  public void setOrgId(Long orgId) {
    this.orgId = orgId;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public Short getOrgType() {
    return orgType;
  }

  public void setOrgType(Short orgType) {
    this.orgType = orgType;
  }

  public List<Long> getPermissionIds() {
    return permissionIds;
  }

  public void setPermissionIds(List<Long> permissionIds) {
    this.permissionIds = permissionIds;
  }
  
}
