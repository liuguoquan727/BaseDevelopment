package com.banmaz.yongbang.module.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mdroid.DBUtils;
import com.mdroid.lib.core.base.BasePresenter;
import com.mdroid.lib.core.base.Status;
import com.banmaz.yongbang.BuildConfig;
import com.banmaz.yongbang.R;
import com.banmaz.yongbang.base.App;
import com.banmaz.yongbang.base.AppBaseActivity;
import com.banmaz.yongbang.base.DBKeys;
import com.banmaz.yongbang.module.home.HomeActivity;
import com.banmaz.yongbang.module.login.LoginActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/9 16:19.
 */
@RuntimePermissions
public class SplashActivity extends AppBaseActivity {

  @BindView(R.id.tv_version)
  TextView mVersionView;

  @Override
  protected Status getCurrentStatus() {
    return null;
  }

  @Override
  protected String getPageTitle() {
    return "启动页";
  }

  @Override
  protected BasePresenter initPresenter() {
    return null;
  }

  @Override
  protected int getContentView() {
    return R.layout.moduel_activity_splash_ui;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void initData(Bundle savedInstanceState) {
    mVersionView.setText("v " + BuildConfig.VERSION_NAME);
    showRationaleForPermission();
  }

  private void showRationaleForPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && DBUtils.read(DBKeys.FIRST_USE, true)) {
      DBUtils.write(DBKeys.FIRST_USE, false);
      new MaterialDialog.Builder(this)
          .cancelable(false)
          .title(getString(R.string.app_name) + "需要申请以下权限")
          .content("位置权限\n存储权限\n电话权限")
          .positiveText("好的")
          .positiveColorRes(R.color.main_color_normal)
          .onPositive(
              new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  dialog.dismiss();
                  SplashActivityPermissionsDispatcher.preStartWithPermissionCheck(
                      SplashActivity.this);
                }
              })
          .show();
    } else {
      preStart();
    }
  }

  @NeedsPermission({
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.READ_PHONE_STATE
  })
  void preStart() {
    if (App.getInstance().isLogin()) {
      getHandler().postDelayed(new LanuchHomeActivityRunnable(), 2000);
    } else {
      getHandler().postDelayed(new LanuchLoginActivityRunnable(), 2000);
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }

  @OnPermissionDenied({
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.READ_PHONE_STATE
  })
  void showDeniedForPermission() {
    deniedPermission();
  }

  @OnNeverAskAgain({
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.READ_PHONE_STATE,
  })
  void showNeverAskForPermission() {
    deniedPermission();
  }

  private void deniedPermission() {
    preStart();
    StringBuilder permission = new StringBuilder();
    if (!PermissionUtils.hasSelfPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
      permission.append("位置权限");
    }
    if (!PermissionUtils.hasSelfPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      if (permission.length() > 0) permission.append(", ");
      permission.append("存储权限");
    }
    if (!PermissionUtils.hasSelfPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
      if (permission.length() > 0) permission.append(", ");
      permission.append("电话权限");
    }
  }

  private class LanuchLoginActivityRunnable implements Runnable {
    @Override
    public void run() {
      Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
      startActivity(intent);
      finish();
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
  }

  private class LanuchHomeActivityRunnable implements Runnable {
    @Override
    public void run() {
      Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
      startActivity(intent);
      finish();
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
  }
}
