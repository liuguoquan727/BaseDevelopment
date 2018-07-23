package com.banmaz.yongbang.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.mdroid.lib.core.base.BasePresenter;
import com.mdroid.lib.core.base.Status;
import com.banmaz.yongbang.R;
import com.banmaz.yongbang.base.App;
import com.banmaz.yongbang.base.AppBaseActivity;
import com.banmaz.yongbang.bean.apibean.User;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/14 19:16.
 */
public class UserInfoActivity extends AppBaseActivity {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.tv_car_no)
  TextView mCarNo;

  @BindView(R.id.tv_name)
  TextView mName;

  public static void launch(Context context) {
    Intent intent = new Intent(context, UserInfoActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected Status getCurrentStatus() {
    return null;
  }

  @Override
  protected String getPageTitle() {
    return "个人信息";
  }

  @Override
  protected BasePresenter initPresenter() {
    return null;
  }

  @Override
  protected int getContentView() {
    return R.layout.module_activity_user_info_ui;
  }

  @Override
  protected void initData(Bundle savedInstanceState) {
    mToolbar.setNavigationIcon(R.drawable.ic_back);
    mToolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            finish();
          }
        });
    User user = App.getInstance().getUser();
    if (user != null) {
      mCarNo.setText(user.busNo);
      mName.setText(user.name);
    }
  }
}
