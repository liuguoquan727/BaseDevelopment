package com.banmaz.yongbang.bean.localbean;

/**
 * Description:
 *
 * Created by liuguoquan on 2018/5/18 14:32.
 */
public class LocationInfo extends BaseModel {
  public double lat;
  public double lng;
  public float angle;
  public String stationName;
  public int busId;
  public int linePlanId;
  public String busNo;
  public long timestamp;

  @Override public String toString() {
    return "LocationInfo{" +
        "lat=" + lat +
        ", lng=" + lng +
        ", angle=" + angle +
        ", stationName='" + stationName + '\'' +
        ", busId=" + busId +
        ", linePlanId=" + linePlanId +
        ", busNo='" + busNo + '\'' +
        ", timestamp=" + timestamp +
        '}';
  }
}
