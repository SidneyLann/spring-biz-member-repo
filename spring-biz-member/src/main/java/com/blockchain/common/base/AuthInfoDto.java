package com.blockchain.common.base;

public class AuthInfoDto {
  private Long currUserId;
  private String currUserName;
  private Long currOrgId;
  private String currOrgName;
  private Short currOrgType;
  private Long receiptOrgId;
  private Long receiptMemId;
  private String token;
  public Long getCurrUserId() {
    return currUserId;
  }
  public void setCurrUserId(Long currUserId) {
    this.currUserId = currUserId;
  }
  public String getCurrUserName() {
    return currUserName;
  }
  public void setCurrUserName(String currUserName) {
    this.currUserName = currUserName;
  }
  public Long getCurrOrgId() {
    return currOrgId;
  }
  public void setCurrOrgId(Long currOrgId) {
    this.currOrgId = currOrgId;
  }
  public String getCurrOrgName() {
    return currOrgName;
  }
  public void setCurrOrgName(String currOrgName) {
    this.currOrgName = currOrgName;
  }
  public Short getCurrOrgType() {
    return currOrgType;
  }
  public void setCurrOrgType(Short currOrgType) {
    this.currOrgType = currOrgType;
  }
  public Long getReceiptOrgId() {
    return receiptOrgId;
  }
  public void setReceiptOrgId(Long receiptOrgId) {
    this.receiptOrgId = receiptOrgId;
  }
  public Long getReceiptMemId() {
    return receiptMemId;
  }
  public void setReceiptMemId(Long receiptMemId) {
    this.receiptMemId = receiptMemId;
  }
  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }
  
}
