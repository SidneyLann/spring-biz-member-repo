package com.blockchain.member.entity;

import java.util.Date;

import com.blockchain.base.data.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "mem_member", uniqueConstraints = { @UniqueConstraint(columnNames = { "login_name", "action" }), @UniqueConstraint(columnNames = { "phone", "action" }),
    @UniqueConstraint(columnNames = { "email", "action" }) })
public class Member extends BaseEntity {
  @Column(name = "login_name")
  @Size(min = 6, max = 20)
  private String loginName;

  @Column(name = "we_id")
  private Long weId = 0L;

  @Column(name = "password")
  @Size(min = 6, max = 80)
  private String password;

  @Column(name = "real_name")
  @Size(min = 0, max = 10)
  private String realName;

  @Column(name = "nick_name")
  @Size(min = 0, max = 16)
  private String nickName;

  @Column(name = "avatar")
  @Size(min = 0, max = 200)
  private String avatar;
  
  @Column(name = "wx_nick_name")
  @Size(min = 0, max = 16)
  private String wxNickName;

  @Column(name = "wx_avatar")
  @Size(min = 0, max = 200)
  private String wxAvatar;
  
  @Column(name = "id_card_no")
  @Size(min = 0, max = 20)
  private String idCardNo;

  @Column(name = "sex")
  @NotNull
  private Boolean sex;

  @Column(name = "birth_day")
  private Date birthDay;

  @Column(name = "marital_status")
  @Max(5)
  private Short maritalStatus;

  @Column(name = "email")
  @Size(min = 0, max = 20)
  @Email
  private String email;

  @Column(name = "phone")
  @Size(min = 0, max = 20)
  private String phone;

  @Column(name = "wechat")
  @Size(min = 0, max = 20)
  private String wechat;

  @Column(name = "qq")
  @Size(min = 0, max = 20)
  private String qq;

  @Column(name = "region_id")
  private Long regionId;

  @Column(name = "detail_address")
  @Size(min = 0, max = 20)
  private String detailAddress;

  @Column(name = "parent_id")
  private Long parentId;

  @Column(name = "org_type")
  private Short orgType;

  @Column(name = "person_type")
  private Short personType;

  @Column(name = "org_id")
  private Long orgId;

  @Column(name = "org_name")
  @Size(min = 0, max = 30)
  private String orgName;

  @Column(name = "receipt_org_id")
  private Long receiptOrgId;

  @Column(name = "receipt_mem_id")
  private Long receiptMemId;

  @Transient
  private String smsCode;

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public Long getWeId() {
    return weId;
  }

  public void setWeId(Long weId) {
    this.weId = weId;
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

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getWxNickName() {
    return wxNickName;
  }

  public void setWxNickName(String wxNickName) {
    this.wxNickName = wxNickName;
  }

  public String getWxAvatar() {
    return wxAvatar;
  }

  public void setWxAvatar(String wxAvatar) {
    this.wxAvatar = wxAvatar;
  }

  public String getIdCardNo() {
    return idCardNo;
  }

  public void setIdCardNo(String idCardNo) {
    this.idCardNo = idCardNo;
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

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
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
