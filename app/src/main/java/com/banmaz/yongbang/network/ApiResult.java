package com.banmaz.yongbang.network;

import com.banmaz.yongbang.bean.apibean.BaseBean;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/8/14 09:54.
 */
public class ApiResult<T> {

  public static final int OK = 1;
  public static final int FORBIDDEN = 403;
  public static final int NOT_FOUND = 404;
  public static final int TOKEN_ERROR = 502;

  public Meta meta;
  public T data;

  public static class Meta extends BaseBean {
    public int code;
    public int cost;
    public String desc;
    public String hostname;
    public String timestamp;
  }

  public int getCode() {
    return meta.code;
  }

  public boolean isSuccess() {
    return meta.code == OK;
  }

  public boolean isForbidden() {
    return meta.code == FORBIDDEN;
  }

  public boolean isTokenError() {
    return meta.code == TOKEN_ERROR;
  }

  public boolean isNotFound() {
    return meta.code == NOT_FOUND;
  }

  public String getMessage() {
    return meta.desc;
  }
}
