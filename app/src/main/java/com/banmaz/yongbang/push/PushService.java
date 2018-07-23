package com.banmaz.yongbang.push;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/19 12:00.
 */
public final class PushService extends Service {

  private int SERVICE_START_DELAYED = 5;

  @Override
  public void onCreate() {
    super.onCreate();
    cancelAutoStartService(this);
    Log.d("lgq", "PushService onCreate: ");
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d("lgq", "PushService onStartCommand: ");
    //PushFakeService.startForeground(this);
    PushClient.getInstance().setQuit(false);
    PushClient.getInstance().connect();
    return super.onStartCommand(intent, flags, startId);
  }

  /**
   * service停掉后自动启动应用
   *
   * @param context
   * @param delayed 延后启动的时间，单位为秒
   */
  private static void startServiceAfterClosed(Context context, int delayed) {
    AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarm.set(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis() + delayed * 1000,
        getOperation(context));
  }

  public static void cancelAutoStartService(Context context) {
    AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarm.cancel(getOperation(context));
  }

  private static PendingIntent getOperation(Context context) {
    Intent intent = new Intent(context, PushService.class);
    PendingIntent operation =
        PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    return operation;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d("lgq", "PushService onDestroy: ");
    if (!PushClient.getInstance().isQuit()) {
      startServiceAfterClosed(this, SERVICE_START_DELAYED); // 5s后重启
    }
  }
}
