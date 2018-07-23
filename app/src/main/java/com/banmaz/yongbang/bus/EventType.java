package com.banmaz.yongbang.bus;

import com.mdroid.lib.core.eventbus.EventBusEvent;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/8/17 15:41.
 */
public class EventType extends EventBusEvent {

  public static final int TYPE_LOGIN_OUT = 300;
  public static final int TYPE_LOGIN = 100;
  /**
   * 发车
   */
  public static final int TYPE_START = 1000;
  /**
   * 线路更新
   */
  public static final int TYPE_ROUTE = 1001;
  /**
   * 乘客信息
   */
  public static final int TYPE_PASSENGER = 1002;
  public static final int TYPE_TOKEN_ERROR = 502;

  public EventType(int type) {
    this(type, null);
  }

  public EventType(int type, Object extra) {
    super(type, extra);
  }
}
