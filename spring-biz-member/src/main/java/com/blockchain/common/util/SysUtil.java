package com.blockchain.common.util;

import org.apache.commons.lang3.StringUtils;

public class SysUtil {
  public static final int BATCH_SIZE = 8;
  public static final int NUM_CLASS_EDGE = 9;

  public static int getIntProperty(String key, int defaultValue) {
    String strValue = System.getProperty(key);
    if (StringUtils.isNotBlank(strValue)) {
      return Integer.valueOf(strValue.trim());
    } else {
      return defaultValue;
    }
  }

  public static double getDoubleProperty(String key, double defaultValue) {
    String strValue = System.getProperty(key);
    if (StringUtils.isNotBlank(strValue)) {
      return Double.valueOf(strValue.trim());
    } else {
      return defaultValue;
    }
  }

  public static void callShellScript(String[] commands) {
    try {
      ProcessBuilder processBuilder = new ProcessBuilder(commands);
      processBuilder.redirectErrorStream(true);
      processBuilder.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
