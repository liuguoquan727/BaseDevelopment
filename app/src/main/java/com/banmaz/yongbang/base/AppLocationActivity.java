package com.banmaz.yongbang.base;

import android.content.Intent;
import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/12/1 14:25.
 */
public abstract class AppLocationActivity<
    V extends AppBaseView, T extends AppBaseActivityPresenter<V>>
    extends PermissionActivity<V, T> {

  protected void activate() {
  }

  protected void deactivate() {
  }

  protected void showOpenGpsDialog() {
    new MaterialDialog.Builder(this)
        .title("提示")
        .cancelable(false)
        .content("应用需要开启GPS定位")
        .positiveText("开启")
        .onPositive(
            new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
              }
            })
        .negativeText("已经开启")
        .onNegative(
            new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                requestLocationWithCheck();
              }
            })
        .show();
  }
}
