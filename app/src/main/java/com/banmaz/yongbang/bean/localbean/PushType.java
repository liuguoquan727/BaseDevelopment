package com.banmaz.yongbang.bean.localbean;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/18 14:36.
 */
public interface PushType {

  String TYPE_PING = "ping";
  String TYPE_PONG = "pong";
  String TYPE_BUS = "bus";

  String EVENT_START = "startRemind";
  String EVENT_ROUTE = "routeUpdate";
  String EVENT_PASSENGER = "passengerInfo";
  String EVENT_GPS_UPLOAD = "gpsUpload";
}
