package com.banmaz.yongbang.base;

import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import com.mdroid.DBUtils;
import com.mdroid.lib.core.base.BaseApp;
import com.mdroid.lib.core.json.DoubleAdapter;
import com.mdroid.lib.core.json.IntegerAdapter;
import com.mdroid.lib.core.json.LongAdapter;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.crashreport.CrashReport;
import com.banmaz.yongbang.BuildConfig;
import com.banmaz.yongbang.bean.apibean.User;
import com.banmaz.yongbang.utils.SystemUtil;
import java.lang.reflect.Modifier;

public class App extends BaseApp {

  private static App instance;
  private Gson mAppGson;
  private User mUser;

  public static synchronized Gson getAppGson() {
    if (instance.mAppGson == null) {
      instance.mAppGson =
          new GsonBuilder()
              .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.TRANSIENT)
              .registerTypeAdapterFactory(
                  TypeAdapters.newFactory(int.class, Integer.class, new IntegerAdapter()))
              .registerTypeAdapterFactory(
                  TypeAdapters.newFactory(double.class, Double.class, new DoubleAdapter()))
              .registerTypeAdapterFactory(
                  TypeAdapters.newFactory(long.class, Long.class, new LongAdapter()))
              .serializeNulls()
              .create();
    }
    return instance.mAppGson;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    FormatStrategy strategy = PrettyFormatStrategy.newBuilder().tag("Pingche").build();
    Logger.addLogAdapter(
        new AndroidLogAdapter(strategy) {
          @Override
          public boolean isLoggable(int priority, String tag) {
            return BuildConfig.DEBUG;
          }
        });
    initBugly();
  }

  private void initBugly() {
    if (!BuildConfig.DEBUG) {
      Context context = getApplicationContext();
      // 获取当前包名
      String packageName = context.getPackageName();
      // 获取当前进程名
      String processName = SystemUtil.getProcessName(Process.myPid());
      // 设置是否为上报进程
      CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
      strategy.setUploadProcess(processName == null || processName.equals(packageName));
      CrashReport.initCrashReport(getApplicationContext(), "53f130a513", false, strategy);
    } else {
      Context context = getApplicationContext();
      String packageName = context.getPackageName();
      String processName = SystemUtil.getProcessName(Process.myPid());
      CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
      strategy.setUploadProcess(processName == null || processName.equals(packageName));
      CrashReport.initCrashReport(getApplicationContext(), "53f130a513", false, strategy);
    }
  }

  public synchronized void setUser(User user) {
    mUser = user;
    DBUtils.write(DBKeys.KEY_USER, user);
  }

  public synchronized User getUser() {
    if (mUser == null) {
      mUser = DBUtils.read(DBKeys.KEY_USER);
    }
    return mUser;
  }

  public synchronized boolean isLogin() {
    User user = getUser();
    if (user == null) {
      return false;
    }
    if (TextUtils.isEmpty(mUser.token)) {
      return false;
    }
    return true;
  }

  public synchronized int getUserId() {
    User user = getUser();
    if (user != null) {
      return user.driverId;
    }
    return 0;
  }

  public synchronized String getToken() {
    User user = getUser();
    if (user != null) {
      return user.token;
    }
    return "";
  }

  public synchronized void logout() {
    mUser = null;
    DBUtils.delete(DBKeys.KEY_USER);
  }

  @Override
  public boolean isDebug() {
    return false;
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(base);
  }

  public static App getInstance() {
    return instance;
  }
}
