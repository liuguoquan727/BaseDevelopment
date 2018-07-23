package com.banmaz.yongbang.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.location.LocationManager;
import android.text.TextUtils;
import com.banmaz.yongbang.BuildConfig;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Description:
 *
 * Created by liuguoquan on 2018/5/16 11:33.
 */
public class SystemUtil {
  /**
   * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
   *
   * @param context
   * @return true 表示开启
   */
  public static boolean isLocationEnable(Context context) {
    LocationManager locationManager =
        (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    if (gps) {
      return true;
    }
    return false;
  }

  /**
   * 获取进程号对应的进程名
   *
   * @param pid 进程号
   * @return 进程名
   */
  public static String getProcessName(int pid) {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
      String processName = reader.readLine();
      if (!TextUtils.isEmpty(processName)) {
        processName = processName.trim();
      }
      return processName;
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }
    return null;
  }

  public static boolean isActivityForeground(Context context, String clazz) {
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
    String currentPackageName = cn.getPackageName();
    if (!TextUtils.isEmpty(currentPackageName)
        && currentPackageName.equals(BuildConfig.APPLICATION_ID)
        && cn.getClassName().equals(clazz)) {
      return true;
    }
    return false;
  }
}
