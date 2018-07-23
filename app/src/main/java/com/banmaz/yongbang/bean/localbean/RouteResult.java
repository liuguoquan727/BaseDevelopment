package com.banmaz.yongbang.bean.localbean;

import java.util.List;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/18 16:06.
 */
public class RouteResult extends BaseModel {
  public List<LocationInfo> routeList;

  @Override public String toString() {
    return "RouteResult{" +
        "routeList=" + routeList +
        '}';
  }
}
