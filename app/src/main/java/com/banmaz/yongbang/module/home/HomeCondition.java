package com.banmaz.yongbang.module.home;

/**
 * Description:
 *
 * Created by liuguoquan on 2018/5/12 18:11.
 */
public class HomeCondition {

  /**
   * 1 准备发车
   * 2 发车
   * 3 到站
   * 4 退出
   */
  public static final int TYPE_READY = 1;
  public static final int TYPE_DEPARTURE = 2;
  public static final int TYPE_ARRIVAL = 3;
  public static final int TYPE_STOP = 4;
  public static final int TYPE_QUERY = 5;

  public int linePlanId;
  public String reason;
  public double lat;
  public double lng;
  public long startTime;
  public int reasonId;
}
