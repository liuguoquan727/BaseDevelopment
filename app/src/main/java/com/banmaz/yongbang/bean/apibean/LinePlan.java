package com.banmaz.yongbang.bean.apibean;

/**
 * Description:
 *
 * Created by liuguoquan on 2018/5/12 18:01.
 */
public class LinePlan extends BaseBean {

  public static final int STATUS_DEPARTURE = 1;
  public static final int STATUS_DRIVING = 2;
  public static final int STATUS_EMERGENCY = 3;
  public static final int STATUS_STOP = 4;

  public int busId;
  public String busNo;
  public String code;
  public int driverId;
  public String driverName;
  public int linePlanId;
  /**
   * 1是待发车状态，
   * 2是行驶中，
   * 3是紧急事故，
   * 4是班车结束
   */
  public int status;
  public int nextStationId;
  public String nextStationName = "";
  /**
   * 乘客人数
   */
  public int passengersNum;
  /**
   * 下车人数
   */
  public int willGetOffNum;
  /**
   * 上车人数
   */
  public int willGoOnNum;
  public long startTime;

  @Override public String toString() {
    return "LinePlan{" +
        "busId=" + busId +
        ", busNo='" + busNo + '\'' +
        ", code='" + code + '\'' +
        ", driverId=" + driverId +
        ", driverName='" + driverName + '\'' +
        ", linePlanID=" + linePlanId +
        ", nextStationId=" + nextStationId +
        ", nextStationName='" + nextStationName + '\'' +
        ", passengersNum=" + passengersNum +
        ", willGetOffNum=" + willGetOffNum +
        ", willGoOnNum=" + willGoOnNum +
        '}';
  }
}
