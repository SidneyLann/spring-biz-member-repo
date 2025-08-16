package com.blockchain.common.dto.member;

import com.blockchain.common.base.RootDto;

public class DefaultPermissionDto extends RootDto {

	private String permissionCode;

	private String permissionName;

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
