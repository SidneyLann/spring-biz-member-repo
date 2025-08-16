package com.blockchain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.blockchain.base.data.RootEntity;

@Entity
@Table(name = "mem_permission_default")
public class DefaultPermission extends RootEntity {

  @Column(name = "permission_code")
  @NotBlank
  @Size(min = 4, max = 10)
  private String permissionCode;

  @Column(name = "permission_name")
  @NotBlank
  @Size(min = 6, max = 20)
  private String permissionName;

  @Column(name = "org_type")
  @NotNull
  private Short orgType;

  public String getPermissionCode() {
    return permissionCode;
  }

  public void setPermissionCode(String permissionCode) {
    this.permissionCode = permissionCode;
  }

  public String getPermissionName() {
    return permissionName;
  }

  public void setPermissionName(String permissionName) {
    this.permissionName = permissionName;
  }

  public Short getOrgType() {
    return orgType;
  }

  public void setOrgType(Short orgType) {
    this.orgType = orgType;
  }
  
}
