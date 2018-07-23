package com.banmaz.yongbang.utils;

import android.util.Log;
import com.banmaz.yongbang.BuildConfig;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/14 16:26.
 */
public class LogUtils {

  public static void d(String tag, String info) {
    if (BuildConfig.DEBUG) {
      Log.d("PushMessage Driver " + tag, info);
    }
  }
}
