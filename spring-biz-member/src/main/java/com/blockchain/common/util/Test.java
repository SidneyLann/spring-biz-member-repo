package com.blockchain.common.util;

import java.math.BigInteger;

public class Test {
  private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  public static String numberToShortString(long number) {
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

  public static long shortStringToNumber(String shortStr) {
    BigInteger result = BigInteger.ZERO;
    BigInteger base = BigInteger.valueOf(62);

    for (int i = 0; i < shortStr.length(); i++) {
      char c = shortStr.charAt(i);
      int digit = BASE62.indexOf(c);
      result = result.multiply(base).add(BigInteger.valueOf(digit));
    }

    return result.longValue();
  }

  public static void main(String[] args) {
    long originalNumber = 123456789012345L;
    String shortStr = numberToShortString(originalNumber);
    long restoredNumber = shortStringToNumber(shortStr);

    System.out.println("Original: " + originalNumber);
    System.out.println("Short string: " + shortStr);
    System.out.println("Restored: " + restoredNumber);
  }
}
