package com.banmaz.yongbang.base;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.banmaz.yongbang.R;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public abstract class PermissionActivity<
    V extends AppBaseView, T extends AppBaseActivityPresenter<V>>
    extends AppBaseActivity<V, T> {

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    PermissionActivityPermissionsDispatcher.onRequestPermissionsResult(
        this, requestCode, grantResults);
  }

  // 定位
  public void requestLocationWithCheck() {
    PermissionActivityPermissionsDispatcher.requestLocationWithPermissionCheck(this);
  }

  @NeedsPermission({ "android.permission.ACCESS_FINE_LOCATION" })
  protected void requestLocation() {
  }

  @OnShowRationale({ "android.permission.ACCESS_FINE_LOCATION" })
  protected void showRationaleForLocation(PermissionRequest request) {
    request.proceed();
  }

  @OnPermissionDenied({ "android.permission.ACCESS_FINE_LOCATION" })
  void showDeniedForLocation() {
    showSettingDialog("定位");
  }

  @OnNeverAskAgain({ "android.permission.ACCESS_FINE_LOCATION" })
  void showNeverAskForLocation() {
    showSettingDialog("定位");
  }

  //// 相机权限请求
  // public void showCameraWithCheck() {
  //  PermissionActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);
  // }
  //
  // @NeedsPermission({ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
  // public void showCamera() {
  //  // 需要重写
  // }
  //
  // @OnShowRationale({ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
  // void showRationaleForCamera(PermissionRequest request) {
  //  request.proceed();
  // }
  //
  // @OnPermissionDenied({ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
  // void showDeniedForCamera() {
  //
  // }
  //
  // @OnNeverAskAgain({ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
  // void showNeverAskForCamera() {
  //  showSettingDialog("拍照");
  // }

  // 媒体文件权限请求
  public void showMediaSelectWithCheck(Bundle bundle, int requestCode) {
    PermissionActivityPermissionsDispatcher.showMediaSelectWithPermissionCheck(
        this, bundle, requestCode);
  }

  @NeedsPermission({ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
  public void showMediaSelect(Bundle bundle, int requestCode) {
  }

  @OnShowRationale({ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
  void showRationaleForMediaSelect(PermissionRequest request) {
    request.proceed();
  }

  @OnPermissionDenied({ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
  void showDeniedForMediaSelect() {
  }

  @OnNeverAskAgain({ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
  void showNeverAskForMediaSelect() {
    showSettingDialog("相机和存储");
  }

  // 存储权限请求
  public void showWriteStorageWithCheck() {
    PermissionActivityPermissionsDispatcher.showWriteStorageWithPermissionCheck(this);
  }

  @NeedsPermission({ Manifest.permission.WRITE_EXTERNAL_STORAGE })
  public void showWriteStorage() {
    // 需要重写
  }

  @OnShowRationale({ Manifest.permission.WRITE_EXTERNAL_STORAGE })
  void showRationaleForWriteStorage(PermissionRequest request) {
    request.proceed();
  }

  @OnPermissionDenied({ Manifest.permission.WRITE_EXTERNAL_STORAGE })
  void showDeniedForWriteStorage() {
  }

  @OnNeverAskAgain({ Manifest.permission.WRITE_EXTERNAL_STORAGE })
  void showNeverAskForWriteStorage() {
    showSettingDialog("存储");
  }

  // 电话权限
  public void showReadPhoneWithCheck() {
    PermissionActivityPermissionsDispatcher.showReadPhoneWithPermissionCheck(this);
  }

  @NeedsPermission({ Manifest.permission.READ_PHONE_STATE })
  public void showReadPhone() {
    // 需要重写
  }

  @OnShowRationale({ Manifest.permission.READ_PHONE_STATE })
  void showRationaleForReadPhone(PermissionRequest request) {
    request.proceed();
  }

  @OnPermissionDenied({ Manifest.permission.READ_PHONE_STATE })
  void showDeniedForReadPhone() {
  }

  @OnNeverAskAgain({ Manifest.permission.READ_PHONE_STATE })
  void showNeverAskForReadPhone() {
    showSettingDialog("电话");
  }

  protected void showSettingDialog(String feature) {
    String name = getString(R.string.app_name);
    new MaterialDialog.Builder(this)
        .title("提示")
        .content(String.format("在设置-应用-%s-权限中开启%s权限, 以正常使用%s功能", name, feature, name))
        .cancelable(false)
        .positiveText("去设置")
        .onPositive(
            new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                try {
                  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                  intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
                  startActivity(intent);
                } catch (Exception e) {
                  Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                  startActivity(intent);
                }
              }
            })
        .show();
  }
}
