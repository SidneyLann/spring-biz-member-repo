package com.blockchain.common.values;

public class FeeRates {
  public static final float IGNORE_START_FEE_PER_SPECS = 3;
  public static final float KILOMITER = 1000;
  public static final float KILOGRAM = 1000;

  public static final float RATE_COMMODITY_SETTLE = 0.985f;
  public static final float RATE_DELIVARY_FEE_DELIVERYMAN = 0.9f;
  public static final float RATE_BIGGEST_DIVIDE = 0.3f;
  public static final float RATE_PAYMENT_FEE = 0.006f;
  public static final float RATE_COMMISSION_BONUS = 0.001f;
  public static final float RATE_COMMISSION_OPERATOR = 0.002f;
  public static final float RATE_COMMISSION_PARENT_SALEMAN = 0.001f;
  public static final float RATE_COMMISSION_SALEMAN = 0.005f;
  
  public static final float RATE_COUNTY_COMMISSION = 0.0005f;// 现长业绩奖励和业务员提成的相当，总收入是业务员的两倍
  public static final float RATE_CITY_COMMISSION = 0.0002f;// 如果奖励系数是0.0001, 是长业绩奖励是现长的两倍，总收入是业务员的三倍。
                                                           // 但是长只能拿到50元以上的订单的业绩奖励，所以把是长的奖励系数提高到正常的2倍，理论上是长总收入是业务员的三倍
  public static final float RATE_PROVINCE_COMMISSION = 0.00006f;// 如果奖励系数是0.00002, 眚长业绩奖励是是长的两倍，总收入是业务员的五倍。
                                                                // 但眚长只能拿到100元以上的订单的业绩奖励，所以把眚长的奖励系数提高到正常的3倍，理论上眚长总收入是业务员的五倍
  ////////////////////即时送运费, 整体上是普通快递费的2倍
  public static final float START_QTY_DISTANCE_IM = 3;
  public static final float START_PRICE_DISTANCE_IM = 3.0f;
  public static final float STEP_QTY_DISTANCE_IM = 1;
  public static final float STEP_PRICE_DISTANCE_IM = 1.8f;

  public static final float START_QTY_MONEY_AMOUNT_IM = 20;
  public static final float START_PRICE_MONEY_AMOUNT_IM = 1.0f;
  public static final float STEP_QTY_MONEY_AMOUNT_IM = 1;
  public static final float STEP_PRICE_MONEY_AMOUNT_IM = 0.12f;

  public static final float START_QTY_QUANTITY_IM = 3;
  public static final float START_PRICE_QUANTITY_IM = 1.0f;
  public static final float STEP_QTY_QUANTITY_IM = 1;
  public static final float STEP_PRICE_QUANTITY_IM = 0.6f;

  public static final float START_QTY_WEIGHT_IM = 3;
  public static final float START_PRICE_WEIGHT_IM = 1.0f;
  public static final float STEP_QTY_WEIGHT_IM = 1;
  public static final float STEP_PRICE_WEIGHT_IM = 0.6f;

  ////////////////////普通快递运费
  public static final float START_QTY_DISTANCE = 3;
  public static final float START_PRICE_DISTANCE = 1.5f;
  public static final float STEP_QTY_DISTANCE = 1;
  public static final float STEP_PRICE_DISTANCE = 0.6f;

  public static final float START_QTY_MONEY_AMOUNT = 20;
  public static final float START_PRICE_MONEY_AMOUNT = 1.0f;
  public static final float STEP_QTY_MONEY_AMOUNT = 1;
  public static final float STEP_PRICE_MONEY_AMOUNT = 0.06f;

  public static final float START_QTY_QUANTITY = 3;
  public static final float START_PRICE_QUANTITY = 1.0f;
  public static final float STEP_QTY_QUANTITY = 1;
  public static final float STEP_PRICE_QUANTITY = 0.3f;

  public static final float START_QTY_WEIGHT = 3;
  public static final float START_PRICE_WEIGHT = 1.0f;
  public static final float STEP_QTY_WEIGHT = 1;
  public static final float STEP_PRICE_WEIGHT = 0.3f;
}
