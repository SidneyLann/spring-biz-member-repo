package com.blockchain.common.values;

public class OrderValues {

  public static final String CONTENT_NEW = "已下单，等待买家支付";
  public static final String CONTENT_PAY = "买家支付已支付，等待卖家发货";
  // public static final String CONTENT_GROUPON_SUCCESS = "拼团成功";
  // public static final String CONTENT_GROUPON_FAIL = "拼团失败";
  public static final String CONTENT_SUPPLIER_SHIP = "卖家已发货，等待买家收货";
  public static final String CONTENT_RECEIVE_COMMODITY = "买家已收货，等待买家评价";
  public static final String CONTENT_EVALUATION = "买家已评价";
  public static final String CONTENT_RETURN_APPLY = "已提退货申请，等待供应商审批";
  public static final String CONTENT_RETURN_APPROVED = "供应商已批准退货申请，等待买家发货";
  public static final String CONTENT_RETURN_APPROVE_FAILED = "供应商已拒绝退货申请";
  public static final String CONTENT_BUYER_SHIP = "买家已发货，等待卖家收货";
  public static final String CONTENT_SELLER_RECEIVE = "卖家确认收货超时，系统代为拒绝收货";
  public static final String CONTENT_RETURN_RECEIVED = "供应商已收货，待第3方退款";
  public static final String CONTENT_RETURN_RECEIVE_FAIL = "供应商未收到货或拒绝收货";
  public static final String CONTENT_RETURN_REFUND = "退款已完成，购物款已原路返还";
  public static final String CONTENT_WAIT_RETURN_TO_SETTLE = "订单待结算";
  public static final String CONTENT_SETTLE = "订单已结算";

  public static final short BUY_IMMEDIATELY = 1;

  public static final short STATUS_WAIT_PAY = 1;// 待付款
  public static final short STATUS_WAIT_GROUPON = 10208;// 待拼团
  public static final short STATUS_WAIT_SHIP = 10308;// 待发货
  public static final short STATUS_WAIT_DELIVER = 10408;// 待送货
  public static final short STATUS_WAIT_TAKE = 10508;// 待收货
  public static final short STATUS_WAIT_COMMENT = 10608;// 待评价
  public static final short STATUS_WAIT_RETURN = 10708;// 待退货
  public static final short STATUS_WAIT_RETURN_APPROVE = 10808;// 退货待审
  public static final short STATUS_RETURN_APPROVE_FAILED = 10908;// 退审失败
  public static final short STATUS_WAIT_RETURN_DELIVER = 11008;// 待发退货，如果超时不发，则订单进入结算流程
  public static final short STATUS_WAIT_RECEIVE_RETURN = 11108;// 待收退货
  public static final short STATUS_RETURN_RECEIVE_FAILED = 11208;// 退货失败
  public static final short STATUS_WAIT_RETURN_REFUND = 11308;// 待退款
  public static final short STATUS_ORDER_CANCELED = 11408;// 已取消
  public static final short STATUS_ORDER_REFUNDED = 11508;// 已退款
  public static final short STATUS_WAIT_SETTLE = 11608;// 待结算
  public static final short STATUS_SET_TRADE_FEE = 11708;// 设置手续费
  public static final short STATUS_ORDER_SETTLED = 11808;// 已结算
  public static final short STATUS_DIVIDING = 11908;// 分账中
  public static final short STATUS_DIVIDE_FAIL = 12008;// 分账失败
  public static final short STATUS_DIVIDED = 12108;// 已分账
  public static final short STATUS_TOBE_RELEASE = 12208;// 待解冻
  public static final short STATUS_RELEASING = 12308;// 解冻中
  public static final short STATUS_RELEASE_FAIL = 12408;// 解冻失败
  public static final short STATUS_RELEASED = 12508;// 已解冻
  
  public static final short PAY_TYPE_WX = 11;
  public static final short PAY_TYPE_ALI = 21;
  public static final short PAY_TYPE_ALLIN = 31;
  public static final short PAY_TYPE_TLINX = 41;
  public static final short PAY_TYPE_OFFLINE = 91;

  public static final short RETURN_TYPE_ORDER_CANCEL = 1;
  public static final short RETURN_TYPE_GROUP_CANCEL = 3;
  public static final short RETURN_TYPE_COMMODITY_RETURN = 5;
  
  public static final short SHIP_TYPE_SELLER = 1;// 卖家发货
  public static final short SHIP_TYPE_BUYER = 3;// 买家发货

}
