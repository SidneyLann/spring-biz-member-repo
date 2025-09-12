package com.blockchain.common.dto.member;

import java.util.Date;

import com.blockchain.common.base.BaseDto;

public class MemberDto extends BaseDto {

  private String loginName;

  private String password;

  private String realName;

  private String nickName;

  private String wxNickName;

  private String idNo;

  private Boolean sex;

  private Date birthDay;

  private Short maritalStatus;

  private String email;

  private String phone;

  private String wechat;

  private String qq;

  private String avatar;

  private String wxAvatar;

  private Long regionId;

  private String detailAddress;

  private Short orgType;

  private Short personType;
  
  private Long orgId;

  private String orgName;

  private Long receiptOrgId;

  private Long receiptMemId;

  private String smsCode;
  
  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getWxNickName() {
    return wxNickName;
  }

  public void setWxNickName(String wxNickName) {
    this.wxNickName = wxNickName;
  }

  public String getIdNo() {
    return idNo;
  }

  public void setIdNo(String idNo) {
    this.idNo = idNo;
  }

  public Boolean getSex() {
    return sex;
  }

  public void setSex(Boolean sex) {
    this.sex = sex;
  }

  public Date getBirthDay() {
    return birthDay;
  }

  public void setBirthDay(Date birthDay) {
    this.birthDay = birthDay;
  }

  public Short getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(Short maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getWechat() {
    return wechat;
  }

  public void setWechat(String wechat) {
    this.wechat = wechat;
  }

  public String getQq() {
    return qq;
  }

  public void setQq(String qq) {
    this.qq = qq;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getWxAvatar() {
    return wxAvatar;
  }

  public void setWxAvatar(String wxAvatar) {
    this.wxAvatar = wxAvatar;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getDetailAddress() {
    return detailAddress;
  }

  public void setDetailAddress(String detailAddress) {
    this.detailAddress = detailAddress;
  }

  public Short getOrgType() {
    return orgType;
  }

  public void setOrgType(Short orgType) {
    this.orgType = orgType;
  }

  public Short getPersonType() {
    return personType;
  }

  public void setPersonType(Short personType) {
    this.personType = personType;
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

public String getSmsCode() {
	return smsCode;
}

public void setSmsCode(String smsCode) {
	this.smsCode = smsCode;
}
  
}
