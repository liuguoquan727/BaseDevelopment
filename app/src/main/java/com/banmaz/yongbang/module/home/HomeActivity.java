package com.banmaz.yongbang.module.home;

import android.os.Bundle;
import com.banmaz.yongbang.R;
import com.banmaz.yongbang.base.AppLocationActivity;
import com.mdroid.lib.core.base.Status;

public class HomeActivity
    extends AppLocationActivity<HomeContract.IHomeView, HomeContract.IHomePresenter>
    implements HomeContract.IHomeView {

  @Override
  protected Status getCurrentStatus() {
    return null;
  }

  @Override
  protected String getPageTitle() {
    return null;
  }

  @Override
  protected HomeContract.IHomePresenter initPresenter() {
    return new HomePresenter(mLifecycleProvider, getHandler());
  }

  @Override
  protected int getContentView() {
    return R.layout.module_activity_main_ui;
  }

  @Override
  protected void initData(Bundle savedInstanceState) {
  }

  @Override
  public void showError(String error) {
  }
}
