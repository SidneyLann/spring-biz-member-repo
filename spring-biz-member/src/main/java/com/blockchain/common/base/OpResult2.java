package com.blockchain.common.base;

public class OpResult2 {
  public static final int CODE_COMM_0_SUCCESS = 0;
  public static final int CODE_COMM_EXCEPTION = 5;
  public static final int CODE_COMM_NETWORK_EXCEPTION = 10;
  public static final int CODE_COMM_NULL_EXCEPTION = 15;
  public static final int CODE_COMM_GRANT_NULL = 20;
  public static final int CODE_COMM_GRANT_EXPIRED = 21;
  public static final int CODE_COMM_GRANT_FAIL = 23;
  public static final int CODE_COMM_GRANT_INVALID_USER = 25;
  public static final int CODE_COMM_GRANT_INVALID_OP = 26;
  public static final int CODE_COMM_GRANT_NOT_ENOUGH = 27;
  public static final int CODE_COMM_GRANT_MULTI = 29;
  public static final int CODE_COMM_INPUT_ERROR = 70;
  public static final int CODE_COMM_RECORD_EXISTS = 80;
  public static final int CODE_COMM_RECORD_NOT_EXISTS = 90;
  public static final int CODE_COMM_RECORD_INVALID_STATUS = 95;

  public static final int CODE_AUTH_VALIDATION_IMAGE_FAIL = 151;
  public static final int CODE_AUTH_VALIDATION_SMS_FAIL = 153;

  public static final int CODE_COMMODITY_CAN_NOT_EDIT = 281;
  public static final int CODE_COMMODITY_NO_WEIGHT = 283;
  public static final int CODE_COMMODITY_OFF_SALE = 285;
  public static final int CODE_COMMODITY_SETTLE_RATE = 290;

  public static final int CODE_FIN_PAYMENT_FAIL = 351;
  public static final int CODE_FIN_BALANCE_NOT_ENOUGH = 356;

  public static final int CODE_MEMBER_ORG_EXISTS = 401;
  public static final int CODE_SALEMAN_NOT_EXISTS = 405;

  public static final int CODE_ORDER_INVALID_ORDER_TYPE = 551;
  public static final int CODE_ORDER_INVALID_QTY_SIZE = 553;
  public static final int CODE_ORDER_STOCK_NOT_ENOUGH = 555;
  public static final int CODE_ORDER_GROUPON_TIMEOUT = 557;

  private int code=-99;
  private String message;
  private Integer totalRecords;
  private Object body;

  public int getCode() {
      return code;
  }

  public void setCode(int code) {
      this.code = code;
      switch (code) {
          case CODE_COMM_0_SUCCESS:
              message = "操作成功/Operation successful";
              break;
          case CODE_COMM_EXCEPTION:
              message = "系统繁忙，请稍后再试/The system is busy, please try again later";
              break;
          case CODE_COMM_NETWORK_EXCEPTION:
              message = "网络异常，请稍后再试/Network malfunction, please try again later";
              break;
          case CODE_COMM_NULL_EXCEPTION:
            message = "数据不能为空/Data cannot be empty";
            break;
          case CODE_COMM_GRANT_NULL:
              message = "缺少授权信息/Lack of authorization information";
              break;
          case CODE_COMM_GRANT_EXPIRED:
              message = "授权过期，请重新登录/Authorization expired, please log in again";
              break;
          case CODE_COMM_GRANT_FAIL:
              message = "授权失败/Privilege grant failed";
              break;
          case CODE_COMM_GRANT_INVALID_USER:
              message = "登录名和/或密码错误/Login name and/or password incorrect";
              break;
          case CODE_COMM_GRANT_INVALID_OP:
              message = "操作被禁止/Operation is prohibited";
              break;
          case CODE_COMM_GRANT_NOT_ENOUGH:
              message = "权限不足/Insufficient permissions";
              break;
          case CODE_COMM_GRANT_MULTI:
              message = "该账号已在另一台机器登录/This account is already logged in on another machine";
              break;
          case CODE_COMM_INPUT_ERROR:
              message = "输入错误/Input error";
              break;
          case CODE_COMM_RECORD_EXISTS:
              message = "记录已存在/Record already exists";
              break;
          case CODE_COMM_RECORD_NOT_EXISTS:
              message = "记录不存在/Record does not exist";
              break;
          case CODE_COMM_RECORD_INVALID_STATUS:
              message = "记录不处于可操作状态/The record is not in an operable state";
              break;
          case CODE_AUTH_VALIDATION_IMAGE_FAIL:
              message = "图片验证未通过/Image verification failed";
              break;
          case CODE_AUTH_VALIDATION_SMS_FAIL:
              message = "验证码不匹配/Verification code mismatch";
              break;
          case CODE_COMMODITY_CAN_NOT_EDIT:
              message = "在售商品不能编辑/Products on sale cannot be edited";
              break;
          case CODE_COMMODITY_NO_WEIGHT:
              message = "商品未设置重量/The weight of the product has not been set";
              break;
          case CODE_COMMODITY_OFF_SALE:
              message = "下列商品已下架/The following products have been taken down：";
              break;
          case CODE_COMMODITY_SETTLE_RATE:
              message = "结算比例不能低于0.7/The settlement ratio cannot be lower than 0.7";
              break;
          case CODE_FIN_PAYMENT_FAIL:
              message = "支付失败/Payment failed";
              break;
          case CODE_FIN_BALANCE_NOT_ENOUGH:
              message = "余额不足/Insufficient Balance";
              break;
          case CODE_MEMBER_ORG_EXISTS:
              message = "您已是某机构的工作人员，不能同时加入两个机构/You are already a staff member of a certain organization and cannot join two organizations at the same time";
              break;
          case CODE_SALEMAN_NOT_EXISTS:
              message = "对应片区的业务员不存在/The salesperson for the corresponding area does not exist";
              break;
          case CODE_ORDER_INVALID_ORDER_TYPE:
              message = "b2b和b2c不能同时下单/B2B and B2C cannot place orders simultaneously";
              break;
          case CODE_ORDER_INVALID_QTY_SIZE:
              message = "商品数量不匹配/Product quantity mismatch";
              break;
          case CODE_ORDER_STOCK_NOT_ENOUGH:
              message = "库存不足/Insufficient inventory";
              break;
          case CODE_ORDER_GROUPON_TIMEOUT:
              message = "以下商品限时促销已过/The limited time promotion for the following products has expired：";
              break;
          default:
              message = "未知错误/Unkown Error";
      }
  }

  public String getMessage() {
      return message;
  }

  public void setMessage(String message) {
      this.message = message;
  }

  public Integer getTotalRecords() {
      return totalRecords;
  }

  public void setTotalRecords(Integer totalRecords) {
      this.totalRecords = totalRecords;
  }

  public Object getBody() {
      return body;
  }

  public void setBody(Object body) {
      this.body = body;
  }
}
