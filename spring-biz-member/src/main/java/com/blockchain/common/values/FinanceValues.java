package com.blockchain.common.values;

public class FinanceValues {
  public static final short CASH_TRADE_TYPE_AWARD = 1;
  public static final short CASH_TRADE_TYPE_BONUS = 3;
  public static final short CASH_TRADE_TYPE_COMMISSION = 5;
  public static final short CASH_TRADE_TYPE_FREIGHT = 7;
  public static final short CASH_TRADE_TYPE_REFUND = 9;
  public static final short CASH_TRADE_TYPE_RECHARGE = 11;
  public static final short CASH_TRADE_TYPE_SETTLE = 13;
  public static final short CASH_TRADE_TYPE_USE_FEE = 15;
  public static final short CASH_TRADE_TYPE_WITHDRAW = 17;
  public static final short CASH_TRADE_TYPE_POINT = 19;

  public static final String PAYMENT_CODE_FAIL = "FAIL";
  public static final String PAYMENT_CODE_SUCCESS = "SUCCESS";

  public static final String CODE_SUCCESS_FAIL = "FAIL";
  public static final String CODE_SUCCESS_RESULT = "SUCCESS";
  public static final String CODE_SUCCESS_RETURN = "<xml><return_code><![CDATA[SUCCESS]]></return/update_code><return_msg><![CDATA[OK]]></return/update_msg></xml>";

  public static final String DIVIDE_DESC_MB = "会员分账";
  public static final String DIVIDE_DESC_HQ = "总部分账";
  public static final String DIVIDE_DESC_OP = "运营中心分账";
  public static final String DIVIDE_DESC_SP = "供应商解冻";

  public static final String DIVIDE_TYPE_MERCHANT_ID = "MERCHANT_ID";
  public static final String DIVIDE_TYPE_PERSONAL_OPENID = "PERSONAL_OPENID";
  public static final String DIVIDE_TYPE_PERSONAL_SUB_OPENID = "PERSONAL_SUB_OPENID";

  public static final short FIN_RECORD_TYPE_CASH = 1;
  public static final short FIN_RECORD_TYPE_POINT = 3;

  public static final short PAYMENT_STATUS_WAIT_PAY = 1;// 待付款
  public static final short PAYMENT_STATUS_PAY = 5;// 已付款

  public static final String RELATION_TYPE_PARTNER = "PARTNER";
  public static final String RELATION_TYPE_USER = "USER";

  public static final short WITHDRAW_OP_TYPE_REFUND = 1;
  public static final short WITHDRAW_OP_TYPE_DIVIDE = 3;
  public static final short WITHDRAW_OP_TYPE_UNFREEZE = 5;

  public static final short WITHDRAW_STATUS_NEW = 1;
  public static final short WITHDRAW_STATUS_APPROVE_FAIL = 3;
  public static final short WITHDRAW_STATUS_APPROVED = 5;
  public static final short WITHDRAW_STATUS_FINISHED = 9;

  public static String getWithdrawStatuName(short code) {
    String name = null;
    switch (code) {
    case WITHDRAW_STATUS_NEW:
      name = "未审批";
      break;
    case WITHDRAW_STATUS_APPROVE_FAIL:
      name = "未通过";
      break;
    case WITHDRAW_STATUS_APPROVED:
      name = "审批通过";
      break;
    case WITHDRAW_STATUS_FINISHED:
      name = "已完成";
      break;
    default:
      name = "";
    }

    return name;
  }
}
