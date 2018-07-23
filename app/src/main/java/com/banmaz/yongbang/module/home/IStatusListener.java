package com.banmaz.yongbang.module.home;

/**
 * Description:
 *
 * Created by liuguoquan on 2018/5/21 11:54.
 */
public interface IStatusListener {
  void onPrepare();

  void onDeparture();

  void onArrival();
}
