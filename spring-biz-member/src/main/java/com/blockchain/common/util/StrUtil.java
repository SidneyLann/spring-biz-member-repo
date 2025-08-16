package com.blockchain.common.util;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.UUID;

public class StrUtil {
  private static final NumberFormat numberFormat = NumberFormat.getNumberInstance();
  public static final String SEPARATOR = "~-~";
  private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  public static String encodeLong(long number) {
    BigInteger bigNum = BigInteger.valueOf(number);
    BigInteger base = BigInteger.valueOf(62);
    StringBuilder result = new StringBuilder();

    while (bigNum.compareTo(BigInteger.ZERO) > 0) {
      BigInteger[] divRem = bigNum.divideAndRemainder(base);
      result.insert(0, BASE62.charAt(divRem[1].intValue()));
      bigNum = divRem[0];
    }

    return result.length() == 0 ? "0" : result.toString();
  }

  public static long dencodeLong(String shortStr) {
    BigInteger result = BigInteger.ZERO;
    BigInteger base = BigInteger.valueOf(62);

    for (int i = 0; i < shortStr.length(); i++) {
      char c = shortStr.charAt(i);
      int digit = BASE62.indexOf(c);
      result = result.multiply(base).add(BigInteger.valueOf(digit));
    }

    return result.longValue();
  }

  public static String uuidString() {
    return String.valueOf(UUID.randomUUID()).replace("-", "");
  }

  public static String float2String(double value) {
    return numberFormat.format(value);
  }

  public static String long2String(long value) {
    return String.valueOf(value);
  }
}
