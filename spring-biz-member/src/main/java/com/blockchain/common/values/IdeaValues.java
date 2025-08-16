package com.blockchain.common.values;

public class IdeaValues {
  public static final int MAX_SENTENCE_LENGTH = 512;
  public static final int LABEL_INDX_PADDING = 0;
  public static final int LABEL_INDX_OUT = 1;// values based on label.txt
  public static final int LABEL_INDX_CLS = 2;
  public static final int LABEL_INDX_SEP = 3;
//  public static final int LABEL_INDX_TIME = 4;
//  public static final int LABEL_INDX_LOCALE = 6;
  public static final int LABEL_INDX_PERSON = 4;
  public static final int LABEL_INDX_ORGANIZATION = 6;
  public static final int LABEL_INDX_PRODUCT = 8;
  public static final int LABEL_INDX_BUSINESS = 10;
  public static final int LABEL_INDX_POSITIVE = 80;
  public static final int LABEL_INDX_NEGATIVE = 90;

  public static final int COUNT_CHECKED_ENTITY = 18;
  
  public static final short RECORD_TYPE_INIT = 1;//不参与idea的预测
  public static final short RECORD_TYPE_CRAWL = 3;//参与idea的预测
  public static final short RECORD_TYPE_PREDICT = 5;//预测生成的idea
  
  public static final String ENTITY_NAME="Entity";
  
  public static final String TABLE_NAMED_ENTITY = "named_entity";
  public static final String TABLE_NAMED_ENTITY_ID = "named_entity_id";
  public static final String TABLE_RELATION_SHIP = "relation_ship";
  
  public static final String ENTITY_STATUS_NEW = "1";
  public static final String ENTITY_STATUS_IN_HBASE = "3";
  public static final String ENTITY_STATUS_IN_NEO4J = "5";
}
