package com.blockchain.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtil {
  private static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
  private static SimpleDateFormat dateFmt2 = new SimpleDateFormat("yyyyMMddHHmmss");
  private static SimpleDateFormat dateFmt3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private Calendar calendar = new GregorianCalendar();

  public static Date addMinutes(Date source, int minutes) {
    Calendar calendar = new GregorianCalendar();
    if (source != null)
      calendar.setTime(source);
    calendar.add(Calendar.MINUTE, minutes);

    return calendar.getTime();
  }

  public static Date addHours(int hours) {
    Calendar calendar = new GregorianCalendar();
    calendar.add(Calendar.HOUR_OF_DAY, hours);

    return calendar.getTime();
  }

  public static Date addDays(int days) {
    Calendar calendar = new GregorianCalendar();
    calendar.add(Calendar.DAY_OF_YEAR, days);

    return calendar.getTime();
  }

  public static Date addDays(Date source, int days) {
    Calendar calendar = new GregorianCalendar();
    if (source != null)
      calendar.setTime(source);
    calendar.add(Calendar.DAY_OF_YEAR, days);

    return calendar.getTime();
  }

  public static Date addMonths(Date source, int months) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(source);
    calendar.add(Calendar.MONTH, months);

    return calendar.getTime();
  }

  public static String addDays(Calendar calendar, int days) {
    calendar.add(Calendar.DAY_OF_YEAR, days);

    return dateFmt.format(calendar.getTime());
  }

  public static String addMonths(Calendar calendar, int months) {
    calendar.add(Calendar.MONTH, months);

    return dateFmt.format(calendar.getTime());
  }

  public static Date getDatePart(Date source) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(source);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);

    return calendar.getTime();
  }

  public static Timestamp getTimestampPart(Date source) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(source);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);

    return new Timestamp(calendar.getTimeInMillis());
  }

  public static Date getLastDatePart(Date source) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(source);
    calendar.add(Calendar.DAY_OF_YEAR, -1);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);

    return calendar.getTime();
  }

  public static Date getNextDatePart(Date source) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(source);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.add(Calendar.DAY_OF_YEAR, 1);

    return calendar.getTime();
  }

  public static Timestamp getNextTimestampPart(Date source) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(source);
    calendar.add(Calendar.DAY_OF_YEAR, 1);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);

    return new Timestamp(calendar.getTimeInMillis());
  }

  public static String dateString() {
    Date curr = new Date();
    return dateFmt.format(curr);
  }

  public static String dateString(Date date) {
    return dateFmt.format(date);
  }

  public static Date dateStr2Date(String dateStr) throws ParseException {
    return dateFmt.parse(dateStr);
  }

  public static String dateTimeString(Date date) {
    return dateFmt2.format(date);
  }

  public static Date dateTimeStr2Date(String dateTimeStr) throws ParseException {
    return dateFmt2.parse(dateTimeStr);
  }

  public static String dateTimeStringColon(Date date) {
    return dateFmt3.format(date);
  }

  public static Date dateTimeStringColon2Date(String dateTimeStr) throws ParseException {
    return dateFmt3.parse(dateTimeStr);
  }

  public static Date[] getDateRanges(short unit, short qty) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(new Date());
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.DAY_OF_MONTH, 1);

    Date[] result = new Date[qty + 1];
    Date firstDate = calendar.getTime();
    result[0] = firstDate;
    int month = unit * qty;// 按月
//    if (unit == 3)//按一年
//      month = 12;
//    else if (unit == 5)//按10年
//      month = 120;
    for (int i = 1; i <= qty; i++) {
      calendar.add(Calendar.MONTH, month);
      result[i] = calendar.getTime();
    }

    return result;
  }

  public static long getDays(String startDateStr, String endDateStr) throws ParseException {
    Date startDate = dateStr2Date(startDateStr);
    Date endDate = dateStr2Date(endDateStr);
    long timeInterval = endDate.getTime() - startDate.getTime();

    return timeInterval / 24 / 60 / 60 / 1000;
  }

  public static int getMinutes(Date startDate, Date endDate) {
    long timeInterval = endDate.getTime() - startDate.getTime();

    return (int) timeInterval / 60 / 1000;
  }

  public static long getDays(Date startDate, Date endDate) {
    long timeInterval = endDate.getTime() - startDate.getTime();

    return timeInterval / 24 / 60 / 60 / 1000;
  }

  public int getYear() {
    return calendar.get(Calendar.YEAR);
  }

  public int getMonth() {
    return calendar.get(Calendar.MONTH) + 1;
  }

  public static void main(String[] args) {
    try {
      List<String> params = new ArrayList<>();
      String endMonth = "202211";
      String currDateStr = "20220601";
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(DateUtil.dateStr2Date(currDateStr));
      String currMonthStr;
      int months = 1;
      while (true) {
        currMonthStr = currDateStr.substring(0, 6);
        params.add(currMonthStr);
        if (endMonth == null || currMonthStr.equals(endMonth) || months > 120) {
          break;
        }

        currDateStr = DateUtil.addMonths(calendar, 1);
      }
    } catch (Exception e) {

    }
  }
}
