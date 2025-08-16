package com.blockchain.common.values;

public class CommodityValues {
  public static final short DELIVERY_TYPE_EXPRESS = 1;
  public static final short DELIVERY_TYPE_EXPRESS_CHANGE = 3;
  public static final short DELIVERY_TYPE_IMMEDIATELY = 5;
  public static final short DELIVERY_TYPE_SELF_PICKUP = 7;
  public static final short DELIVERY_TYPE_ONLINE = 9;

  public static final short BRAND_STATUS_NEW = 1;
  public static final short BRAND_STATUS_APPROVE_FAIL = 3;
  public static final short BRAND_STATUS_APPROVED = 5;

  public static final short PAY_TYPE_ONLINE = 1;
  public static final short PAY_TYPE_OFFLINE = 3;

	public static final short STATUS_NEW = 1;// 等待编辑
	public static final short STATUS_WAIT_APPROVE = 10;// 等待审核
	public static final short STATUS_APPROVE_FAIL = 20;// 审核失败
	public static final short STATUS_ON_SALE = 30;// 在售商品
	public static final short STATUS_NO_STOCK = 40;// 缺货商品
	public static final short STATUS_OFF_SALE = 50;// 下架商品
	public static final short STATUS_VIOLATION = 60;// 违规下架
  
  public static final short TRADE_TYPE_NORMAL = 1;
  public static final short TRADE_TYPE_RECOMMEND = 3;
  public static final short TRADE_TYPE_GROUPON = 5;
  
  public static String getDeliveryLabel(Short deliveryType) {
    String label ="unknown";
    switch(deliveryType) {
    case DELIVERY_TYPE_EXPRESS:
      label = "快递";
      break;
    case DELIVERY_TYPE_EXPRESS_CHANGE:
      label = "转运";
      break;
    case DELIVERY_TYPE_IMMEDIATELY:
      label = "即送";
      break;
    case DELIVERY_TYPE_SELF_PICKUP:
      label = "自提";
      break;
    case DELIVERY_TYPE_ONLINE:
      label = "网配";
      break;
    }
      
    return label;
  }
}
