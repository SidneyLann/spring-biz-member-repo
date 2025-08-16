package com.blockchain.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.blockchain.common.values.CommonValues;

public class ValueUtil {
  private final static RoundingMode ROUND_MODE_DEFAULT = RoundingMode.HALF_UP;
  private final static RoundingMode ROUND_MODE_UNNECESSARY = RoundingMode.HALF_UP;
  private final static int INTERMEDIATE_RESULT_PRECISION = 64;
  private static Random ramdom = new Random();

  public static float divide(Float a, Float b, int c) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null)
      a = 0.0f;
    if (b == null)
      b = 0.0f;

    BigDecimal source = new BigDecimal(a, mc);
    BigDecimal result = new BigDecimal(b, mc);
    result = source.divide(result, mc).setScale(c, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float divideFloat(int p, Float a, Float b) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null)
      a = 0.0f;
    if (b == null)
      b = 1.0f;

    BigDecimal source = new BigDecimal(a, mc);
    BigDecimal result = new BigDecimal(b, mc);
    result = source.divide(result, mc).setScale(p, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float divideInt(int p, Integer a, Integer b) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null)
      a = 0;
    if (b == null)
      b = 1;

    BigDecimal source = new BigDecimal(a, mc);
    BigDecimal result = new BigDecimal(b, mc);
    result = source.divide(result, mc).setScale(p, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float divideLong(int p, Long a, Long b) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null)
      a = 0L;
    if (b == null)
      b = 1L;

    BigDecimal source = new BigDecimal(a, mc);
    BigDecimal result = new BigDecimal(b, mc);
    result = source.divide(result, mc).setScale(p, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float divideMultiply(Float a, Float b, Float c, int d) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null)
      a = 0.0f;
    if (b == null)
      b = 0.0f;
    if (c == null)
      c = 0.0f;

    BigDecimal source = new BigDecimal(a, mc);
    BigDecimal result = new BigDecimal(b, mc);
    result = source.divide(result, mc);
    source = new BigDecimal(c, mc);
    result = source.multiply(result, mc).setScale(d, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float subtractMultiply(int p, Float a, Float b, Float c) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null || b == null || c == null)
      return 0.0f;

    BigDecimal source = new BigDecimal(a, mc);
    BigDecimal result = new BigDecimal(b, mc);
    result = source.subtract(result, mc);
    source = new BigDecimal(c, mc);
    result = result.multiply(source, mc).setScale(p, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float divideMultiplyMultiply(Float a, Float b, Float c, Float d, int e) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null)
      a = 0.0f;
    if (b == null)
      b = 0.0f;
    if (c == null)
      c = 0.0f;
    if (d == null)
      d = 0.0f;

    BigDecimal source = new BigDecimal(a, mc);
    BigDecimal result = new BigDecimal(b, mc);
    result = source.divide(result, mc);
    source = new BigDecimal(c, mc);
    result = source.multiply(result, mc);
    source = new BigDecimal(d, mc);
    result = source.multiply(result, mc).setScale(e, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float multiply(Float a, Float b, int c) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null)
      a = 0.0f;
    if (b == null)
      b = 0.0f;
    BigDecimal source = new BigDecimal(a, mc);
    BigDecimal result = new BigDecimal(b, mc);
    result = source.multiply(result, mc).setScale(c, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float multiplyDivide(Float a, Float b, Float c, int d) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null)
      a = 0.0f;
    if (b == null)
      b = 0.0f;
    if (c == null)
      c = 0.0f;
    BigDecimal source = new BigDecimal(a, mc);
    BigDecimal result = new BigDecimal(b, mc);
    result = source.multiply(result, mc);
    source = new BigDecimal(c, mc);
    result = result.divide(source, mc).setScale(d, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float multiplyMultiply(Float a, Float b, Float c, int d) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null)
      a = 0.0f;
    if (b == null)
      b = 0.0f;
    if (c == null)
      c = 0.0f;

    BigDecimal source = new BigDecimal(a, mc);
    BigDecimal result = new BigDecimal(b, mc);
    result = source.multiply(result, mc);
    source = new BigDecimal(c, mc);
    result = result.multiply(source, mc).setScale(d, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float multiply1Subtract(int p, Float a, Float b, Float c, float... z) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    BigDecimal result;
    if (a == null || b == null) {
      result = new BigDecimal(0, mc);
    } else {
      BigDecimal ba = new BigDecimal(a, mc);
      BigDecimal bb = new BigDecimal(b, mc);
      result = ba.multiply(bb, mc);
    }

    if (c != null)
      result = result.subtract(new BigDecimal(c, mc));

    for (Float f : z) {
      if (f != null) {
        BigDecimal bf = new BigDecimal(f, mc);
        result = result.add(bf, mc);
      }
    }

    result = result.setScale(p, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float multiply2Subtract(int p, Float a, Float b, Float c, Float d, float... z) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    BigDecimal result;
    if (a == null || b == null) {
      result = new BigDecimal(0, mc);
    } else {
      BigDecimal ba = new BigDecimal(a, mc);
      BigDecimal bb = new BigDecimal(b, mc);
      result = ba.multiply(bb, mc);
    }

    if (c != null)
      result = result.subtract(new BigDecimal(c, mc));

    if (d != null)
      result = result.subtract(new BigDecimal(d, mc));

    for (Float f : z) {
      if (f != null) {
        BigDecimal bf = new BigDecimal(f, mc);
        result = result.add(bf, mc);
      }
    }

    result = result.setScale(p, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float multiplySubMultipy1Sub(int p, Float a, Float b, Float c, Float d, Float e, float... f) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    BigDecimal result = null;
    if (a == null || b == null) {
      result = new BigDecimal(0, mc);
    } else {
      BigDecimal source = new BigDecimal(a, mc);
      result = new BigDecimal(b, mc);
      result = source.multiply(result);
    }

    if (c != null && d != null) {
      BigDecimal bc = new BigDecimal(c, mc);
      BigDecimal bd = new BigDecimal(d, mc);
      result = result.subtract(bc.multiply(bd));
    }

    if (e != null) {
      BigDecimal be = new BigDecimal(c, mc);
      result = result.subtract(be);
    }

    for (Float n : f) {
      if (n != null) {
        BigDecimal bn = new BigDecimal(n, mc);
        result = result.add(bn, mc);
      }
    }

    result = result.setScale(p, ROUND_MODE_DEFAULT);

    return result.floatValue();
  }

  public static float round(Float a, int d) {
    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_UNNECESSARY);
    if (a == null)
      a = 0.0f;

    BigDecimal source = new BigDecimal(a, mc);
    source = source.setScale(d, ROUND_MODE_DEFAULT);

    return source.floatValue();
  }

  public static float sum0Sub(int p, Float... a) {
    float sum = 0.0f;

    for (Float f : a) {
      if (f == null)
        continue;

      sum += f;
    }

    return round(sum, p);
  }

  public static float sum1Sub(int p, Float m, Float... a) {
    float sum = 0.0f;
    if (m != null)
      sum -= m;

    for (Float f : a) {
      if (f == null)
        continue;

      sum += f;
    }

    return round(sum, p);
  }

  public static float sum2Sub(int p, Float m, Float n, Float... a) {
    float sum = 0.0f;
    if (m != null)
      sum -= m;

    if (n != null)
      sum -= n;

    for (Float f : a) {
      if (f == null)
        continue;
      sum += f;
    }

    return round(sum, p);
  }

  public static float sum3Sub(int p, Float m, Float n, Float k, Float... a) {
    float sum = 0.0f;
    if (m != null)
      sum -= m;

    if (n != null)
      sum -= n;

    if (k != null)
      sum -= k;

    for (Float f : a) {
      if (f == null)
        continue;
      sum += f;
    }

    return round(sum, p);
  }

  public static float sum0Add(int p, Float... a) {
    float sum = 0.0f;

    for (Float f : a) {
      if (f == null)
        continue;
      sum -= f;
    }

    return round(sum, p);
  }

  public static float sum1Add(int p, Float m, Float... a) {
    float sum = 0.0f;
    if (m != null)
      sum += m;

    for (Float f : a) {
      if (f == null)
        continue;
      sum -= f;
    }

    return round(sum, p);
  }

  public static float sum2Add(int p, Float m, Float n, Float... a) {
    float sum = 0.0f;
    if (m != null)
      sum += m;

    if (n != null)
      sum += n;

    for (Float f : a) {
      if (f == null)
        continue;
      sum -= f;
    }

    return round(sum, p);
  }

  public static float sum3Addition(int p, Float m, Float n, Float k, Float... a) {
    float sum = 0.0f;
    if (m != null)
      sum += m;

    if (n != null)
      sum += n;

    if (k != null)
      sum += k;

    for (Float f : a) {
      if (f == null)
        continue;
      sum -= f;
    }

    return round(sum, p);
  }

  public static boolean isBiggerThanZero(Float a) {
    if (a == null)
      return false;

    return a > CommonValues.NUM_FLOAT_TINY;
  }

  public static boolean isBiggerThanZero(Integer a) {
    if (a == null)
      return false;

    return a > 0;
  }

  public static boolean isBiggerThanZero(Long a) {
    if (a == null)
      return false;

    return a > 0L;
  }

  public static boolean equalsTo(Short a, Short b) {
    if (a == null)
      return false;
    else if (b == null)
      return false;

    return a.compareTo(b) == 0;
  }

  public static boolean equalsTo(Long a, Long b) {
    if (a == null)
      return false;
    else if (b == null)
      return false;

    return a.compareTo(b) == 0;
  }

  public static boolean equalsToZero(Short a) {
    if (a == null)
      return false;

    return a.shortValue() == (short) 0;
  }

  public static boolean equalsToZero(Long a) {
    if (a == null)
      return false;

    return a.longValue() == 0L;
  }

  public static boolean equalsToZero(Float a) {
    if (a == null)
      return false;

    a = Math.abs(a);

    return a < CommonValues.NUM_FLOAT_TINY;
  }

  public static short getActionValue() {
    Integer value = ramdom.nextInt(10, Short.MAX_VALUE);

    return value.shortValue();
  }

  public static short getNextShort(int min, int max) {
    Integer value = ramdom.nextInt(min, max);

    return value.shortValue();
  }

  public static int getNextInt(int min, int max) {
    Integer value = ramdom.nextInt(min, max);

    return value;
  }

  public static Integer getIntX100(Float amount) {
    if (amount == null)
      return null;

    amount *= 100;
    return amount.intValue();
  }

  public static Long getLongX100(Float amount) {
    if (amount == null)
      return null;

    amount *= 100;
    return amount.longValue();
  }

  public static int compareShort(Short num1, Short num2) {
    if (num1 == null) {
      return CommonValues.NUM_INT_THREE;
    } else if (num2 == null) {
      return CommonValues.NUM_INT_FOUR;
    }

    int result = num1.compareTo(num2);

    if (result > 0) {
      return CommonValues.NUM_INT_ONE;
    } else if (result < 0) {
      return CommonValues.NUM_INT_TWO;
    }

    return CommonValues.NUM_INT_ZERO;
  }

  public static int compareInt(Integer num1, Integer num2) {
    if (num1 == null) {
      return CommonValues.NUM_INT_THREE;
    } else if (num2 == null) {
      return CommonValues.NUM_INT_FOUR;
    }

    int result = num1.compareTo(num2);

    if (result > 0) {
      return CommonValues.NUM_INT_ONE;
    } else if (result < 0) {
      return CommonValues.NUM_INT_TWO;
    }

    return CommonValues.NUM_INT_ZERO;
  }

  public static int compareLong(Long num1, Long num2) {
    if (num1 == null) {
      return CommonValues.NUM_INT_THREE;
    } else if (num2 == null) {
      return CommonValues.NUM_INT_FOUR;
    }

    int result = num1.compareTo(num2);

    if (result > 0) {
      return CommonValues.NUM_INT_ONE;
    } else if (result < 0) {
      return CommonValues.NUM_INT_TWO;
    }

    return CommonValues.NUM_INT_ZERO;
  }

  public static int compareFloat(Float num1, Float num2) {
    if (num1 == null) {
      return CommonValues.NUM_INT_THREE;
    } else if (num2 == null) {
      return CommonValues.NUM_INT_FOUR;
    }

    int result = num1.compareTo(num2);

    if (result > 0) {
      return CommonValues.NUM_INT_ONE;
    } else if (result < 0) {
      return CommonValues.NUM_INT_TWO;
    }

    return CommonValues.NUM_INT_ZERO;
  }

  public <T extends Object> List<Field> getAllFields(Class<T> clazz) {
    if (clazz == null) {
      return Collections.emptyList();
    }

    List<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
    List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields()).filter(f -> Modifier.isPublic(f.getModifiers()) || Modifier.isProtected(f.getModifiers())).collect(Collectors.toList());
    result.addAll(filteredFields);

    return result;
  }

  public static Integer getInt(byte[] value) {
    if (value == null)
      return null;
    return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getInt();
  }

  public static Long getLong(byte[] value) {
    if (value == null)
      return null;
    return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getLong();
  }

  public static Float getFloat(byte[] value) {
    if (value == null)
      return null;
    return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getFloat();
  }

  public static Double getDouble(byte[] value) {
    if (value == null)
      return null;
    return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getDouble();
  }

  public static String getString(byte[] value) {
    if (value == null)
      return null;
    return new String(value, StandardCharsets.UTF_8);
  }

  public static void main(String[] args) {
    Double a = 1d;
    Float b = 3f;
    Float c = 3f;
    // ValueUtil.divideMultiply(a.floatValue(), b, c, 2);
    String s1 = "o9AjI5HYKCc_TH_-1i-QztXeu3z0";
    String s2 = "o9AjI5HYKCc_TH_-1i-QztXeu3z0";

    System.out.println(s1.equals(s2));

    MathContext mc = new MathContext(INTERMEDIATE_RESULT_PRECISION, ROUND_MODE_DEFAULT);
    BigDecimal source = new BigDecimal(2, mc);
    BigDecimal result = new BigDecimal(3, mc);
    result = source.divide(result, mc);
    source = new BigDecimal(4, mc);
    result = source.multiply(result, mc).setScale(2, ROUND_MODE_DEFAULT);

    System.out.println(result);
  }

}
