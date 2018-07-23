package com.banmaz.yongbang.bean.localbean;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/14 16:43.
 */
public class PushMessage<T> extends BaseModel {
  /** ping 心跳发送 pong 心跳接收 bus 数据传输 */
  public String type;

  public T data;
  /**
   * startRemind 发车提醒
   * routeUpdate 行驶路径
   * passengerInfo 乘客信息改变
   * gpsUpload GPS上报
   */
  public String event;

  @Override public String toString() {
    return "PushMessage{" +
        "type='" + type + '\'' +
        ", event='" + event + '\'' +
        '}';
  }
}
