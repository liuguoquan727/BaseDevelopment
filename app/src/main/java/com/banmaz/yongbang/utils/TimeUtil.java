package com.banmaz.yongbang.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/10/18 17:45.
 */
public class TimeUtil {
  public static String getReserveTime(long mills) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    return sdf.format(new Date(mills));
  }

  public static String getCountDownTime(long mills) {
    long distance = System.currentTimeMillis() - mills;
    if (distance < 0) {
      distance = 0;
    }
    long fiftyMills = 15 * 60 * 1000;
    long div = fiftyMills - distance;
    if (div < 0) {
      return getTime(0, 0);
    }
    return getTime(fiftyMills, distance);
  }

  public static String getChargingTime(long mills) {
    return getTime(System.currentTimeMillis(), mills);
  }

  public static String getWaitingTime(long mills) {
    return getTime(System.currentTimeMillis(), mills);
  }

  public static String getTime(long base, long mills) {
    int seconds = (int) ((base - mills) / 1000);
    StringBuilder sb = new StringBuilder();
    int hourCount = getHours(seconds);
    if (hourCount > 0 && hourCount < 10) {
      sb.append("0");
      sb.append(hourCount);
      sb.append(" : ");
    } else if (hourCount > 10) {
      sb.append(hourCount);
      sb.append(" : ");
    }
    int minuteCount = getMinutes(seconds);
    if (minuteCount >= 0 && minuteCount < 10) {
      sb.append("0");
      sb.append(minuteCount);
      sb.append(" : ");
    } else if (minuteCount >= 10) {
      sb.append(minuteCount);
      sb.append(" : ");
    }
    int secondCount = getSeconds(seconds);
    if (secondCount >= 0 && secondCount < 10) {
      sb.append("0");
      sb.append(secondCount);
    } else {
      sb.append(secondCount);
    }
    return sb.toString();
  }

  public static int getHours(int seconds) {
    int hour = seconds / 60 / 60;
    return hour;
  }

  public static int getMinutes(int seconds) {
    int leftSecond = seconds - getHours(seconds) * 60 * 60;
    int minutes = leftSecond / 60;
    return minutes;
  }

  public static int getSeconds(int seconds) {
    int leftSeconds = seconds - getHours(seconds) * 60 * 60 - getMinutes(seconds) * 60;
    return leftSeconds;
  }
}
