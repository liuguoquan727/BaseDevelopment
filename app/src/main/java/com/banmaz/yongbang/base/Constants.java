package com.banmaz.yongbang.base;

import com.mdroid.lib.core.base.BaseExtraKeys;
import com.banmaz.yongbang.BuildConfig;

/** 常量配置 */
public class Constants {

  private static final int ENVIRONMENT_F = 0; // 正式环境
  private static final int ENVIRONMENT_D = 1; // 开发环境
  private static final int ENVIRONMENT_T = 2; // 测试环境
  private static final String H5_FACTORY = "https://www.youdianbus.cn/";
  private static final String H5_TEST = "http://www.youdianbus.cn/";
  private static final String HOST_FACTORY = "https://www.dotransit.info/uplus/driver/";
  private static final String HOST_TEST = "https://www.uplus.dotransit.net/uplus/driver/";
  private static final String WSS_FACTORY = "wss://www.dotransit.info/uplus/im/";
  private static final String WSS_TEST = "wss://www.uplus.dotransit.net/uplus/im/";

  public static final String HOST = isF() ? HOST_FACTORY : isD() ? HOST_TEST : HOST_TEST;
  public static final String WSS = isF() ? WSS_FACTORY : isD() ? WSS_TEST : WSS_TEST;
  public static final String H5_HOST = isF() ? H5_FACTORY : H5_TEST;

  public static boolean isF() {
    return BuildConfig.serviceEnvironment == ENVIRONMENT_F;
  }

  public static boolean isD() {
    return BuildConfig.serviceEnvironment == ENVIRONMENT_D;
  }

  public static boolean isT() {
    return BuildConfig.serviceEnvironment == ENVIRONMENT_T;
  }

  /** 跳转activity时附带数据的key值 */
  public interface ExtraKey extends BaseExtraKeys {
    String KET_SHARE = "share";
  }

  public interface NormalCons {
    /** 分页--每页10条数据 */
    int LIMIT_10 = 10;
    /** 分页--每页20条数据 */
    int LIMIT_20 = 20;
  }
}
