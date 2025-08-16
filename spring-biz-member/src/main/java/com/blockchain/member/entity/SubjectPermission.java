package com.blockchain.member.entity;

import com.blockchain.base.data.RootEntity;
import com.blockchain.common.values.CommonValues;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "mem_permission_subject")
public class SubjectPermission extends RootEntity {
  @Column(name = "user_id")
  @NotNull
  private Long userId;

  @Column(name = "permission_id")
  @NotNull
  private Long permissionId;

  @Column(name = "org_type")
  @NotNull
  private Short orgType = CommonValues.ORG_TYPE_MB;

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

  public Short getOrgType() {
    return orgType;
  }

  public void setOrgType(Short orgType) {
    this.orgType = orgType;
  }
  
}
