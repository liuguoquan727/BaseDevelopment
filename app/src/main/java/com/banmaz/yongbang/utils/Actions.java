package com.banmaz.yongbang.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mdroid.DBUtils;
import com.mdroid.lib.core.utils.Toost;
import com.mdroid.utils.AndroidUtils;
import com.banmaz.yongbang.base.DBKeys;
import com.banmaz.yongbang.bean.apibean.Version;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/9/1 21:51.
 */
public class Actions {

  private static final String PACKAGE_NAME_DOWNLOAD_MANAGER = "com.android.providers.downloads";

  public static void updateTip(Context context, Version version) {
    updateTip(context, version, false);
  }

  public static void updateTip(Context context, Version version, final boolean isForce) {
    StringBuilder tips = new StringBuilder();
    tips.append(version.content);
    if (isForce) {
      showForceUpdate(context, version, tips.toString());
    } else {
      showUpdate(context, version, tips.toString());
    }
  }

  private static void showUpdate(final Context context, final Version version, String tip) {
    new MaterialDialog.Builder(context)
        .title("更新提示")
        .cancelable(false)
        .content(tip)
        .positiveText("立即下载")
        .onPositive(
            new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                doUpdate(context, version.androidCurrentPackageUrl);
              }
            })
        .negativeText("以后再说")
        .onNegative(
            new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                DBUtils.write(DBKeys.UPDATE_INTERVAL_TIME, System.currentTimeMillis());
              }
            })
        .show();
  }

  private static void showForceUpdate(final Context context, Version version, String tip) {
    new MaterialDialog.Builder(context)
        .title("强制更新")
        .cancelable(false)
        .content(tip)
        .positiveText("立即下载")
        .onPositive(
            new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                doUpdate(context, version.androidCurrentPackageUrl);
                android.os.Process.killProcess(android.os.Process.myPid());
              }
            })
        .show();
  }

  private static void doUpdate(final Context context, String url) {
    try {
      String name = String.format("udianbus_%s.apk", System.currentTimeMillis() + "");
      long id = handleDownload(context, url, name);
      DBUtils.write(DBKeys.UPDATE_APK_ID, id);
      DBUtils.write(DBKeys.UPDATE_APK_NAME, name);
      Toost.message("开始下载...");
    } catch (IllegalArgumentException e) {
      new MaterialDialog.Builder(context)
          .title("提示")
          .content("启用下载器")
          .positiveText("去设置")
          .onPositive(
              new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  AndroidUtils.openAppSettings(context, PACKAGE_NAME_DOWNLOAD_MANAGER);
                }
              })
          .negativeText("取消")
          .show();
    } catch (RuntimeException e) {
      // java.lang.SecurityException: No permission to write to
      // /storage/emulated/0/Download/chargerlink_270.apk:
      // Neither user 10167 nor current process has android.permission.WRITE_EXTERNAL_STORAGE.
    }
  }

  public static long handleDownload(
      final Context context, final String fromUrl, final String toFilename) {
    final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fromUrl));
    if (Build.VERSION.SDK_INT >= 16) {
      request.allowScanningByMediaScanner();
      request.setNotificationVisibility(
          DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    }
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, toFilename);
    request.setMimeType("application/vnd.android.package-archive");
    final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    try {
      return dm.enqueue(request);
    } catch (SecurityException e) {
      if (Build.VERSION.SDK_INT >= 16) {
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
      }
      return dm.enqueue(request);
    }
  }
}
