package com.banmaz.yongbang.module.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.mdroid.DBUtils;
import com.mdroid.lib.core.base.Status;
import com.mdroid.lib.core.rxjava.PausedHandlerScheduler;
import com.mdroid.utils.AndroidUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.banmaz.yongbang.R;
import com.banmaz.yongbang.base.App;
import com.banmaz.yongbang.base.DBKeys;
import com.banmaz.yongbang.base.PermissionActivity;
import com.banmaz.yongbang.bean.apibean.User;
import com.banmaz.yongbang.module.home.HomeActivity;
import com.banmaz.yongbang.module.user.UserInfoActivity;
import com.banmaz.yongbang.utils.ToolBarUtils;
import com.banmaz.yongbang.utils.ValidateUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.concurrent.TimeUnit;

/**
 * Description: 登录
 *
 * <p>Created by liuguoquan on 2018/5/9 09:47.
 */
public class LoginActivity
    extends PermissionActivity<LoginContract.ILoginView, LoginContract.ILoginPresenter>
    implements LoginContract.ILoginView {

  @BindView(R.id.et_username)
  EditText mUserNameView;

  @BindView(R.id.delete)
  ImageView mDeletView;

  @BindView(R.id.divide_phone)
  View mDividePhoneView;

  @BindView(R.id.divide_code)
  View mDivideCodeView;

  @BindView(R.id.et_code)
  EditText mCodeView;

  @BindView(R.id.tv_verify_code)
  TextView mSendCodeView;

  @BindView(R.id.tv_login)
  TextView mLoginView;

  private LoginCondition mCondition = new LoginCondition();

  @Override
  protected Status getCurrentStatus() {
    return null;
  }

  @Override
  protected String getPageTitle() {
    return "登录";
  }

  @Override
  protected LoginContract.ILoginPresenter initPresenter() {
    return new LoginPresenter(mLifecycleProvider, getHandler());
  }

  @Override
  protected int getContentView() {
    return R.layout.module_activity_login_ui;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    ToolBarUtils.requestStatusBarLight(this, true);
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onStart() {
    super.onStart();
    showReadPhoneWithCheck();
  }

  @Override
  public void showReadPhone() {
    super.showReadPhone();
  }

  @Override
  protected void initData(Bundle savedInstanceState) {
    getWindow()
        .setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    String userName = DBUtils.read(DBKeys.KEY_USER_PHONE);
    if (!TextUtils.isEmpty(userName)) {
      mUserNameView.setText(userName);
      mUserNameView.setSelection(userName.length());
      mUserNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
      mSendCodeView.setTextColor(getResources().getColor(R.color.main_color_normal));
    }

    mUserNameView.setOnFocusChangeListener(
        new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
              mDividePhoneView.setBackgroundResource(R.color.main_color_normal);
            } else {
              mDividePhoneView.setBackgroundResource(R.color.gray_d3d3d3);
            }
            mDivideCodeView.setBackgroundResource(R.color.gray_d3d3d3);
            mDeletView.setVisibility(
                mUserNameView.getText().length() > 0 ? View.VISIBLE : View.INVISIBLE);
          }
        });
    mCodeView.setOnFocusChangeListener(
        new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
              mDivideCodeView.setBackgroundResource(R.color.main_color_normal);
            } else {
              mDivideCodeView.setBackgroundResource(R.color.gray_d3d3d3);
            }
            mDividePhoneView.setBackgroundResource(R.color.gray_d3d3d3);
          }
        });

    ToolBarUtils.renderEditText(mUserNameView, mDeletView);

    mUserNameView.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            resetLoginBtn();
          }

          @Override
          public void afterTextChanged(Editable s) {
          }
        });
    mCodeView.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            resetLoginBtn();
          }

          @Override
          public void afterTextChanged(Editable s) {
          }
        });
  }

  private void resetLoginBtn() {
    String phone = mUserNameView.getText().toString().trim();
    String code = mCodeView.getText().toString().trim();
    if (mSendCodeView.isEnabled()) {
      if (!TextUtils.isEmpty(phone)) {
        mSendCodeView.setTextColor(getResources().getColor(R.color.main_color_normal));
      } else {
        mSendCodeView.setTextColor(getResources().getColor(R.color.text_primary_black));
      }
    }
    if (!TextUtils.isEmpty(phone)
        && !TextUtils.isEmpty(code)
        && (code.length() == 6)
        && (phone.length() == 11)) {
      mLoginView.setEnabled(true);
    } else {
      mLoginView.setEnabled(false);
    }
  }

  @OnClick({ R.id.tv_verify_code, R.id.tv_login, R.id.main_layout })
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_verify_code:
        doSendCode();
        break;
      case R.id.tv_login:
        doLogin();
        break;
      case R.id.main_layout:
        if (mUserNameView != null) {
          AndroidUtils.hideInputMethod(this, mUserNameView);
        }
        break;
    }
  }

  private void doSendCode() {
    String userName = mUserNameView.getText().toString();
    if (!ValidateUtils.isValidMobile(userName)) {
      toastMsg("请输入11位有效电话号码");
      return;
    }
    mCondition.phoneNumber = userName;
    showProcessDialog();
    mPresenter.getVerifyCode(mCondition);
  }

  private void doLogin() {
    if (mUserNameView != null) {
      AndroidUtils.hideInputMethod(this, mUserNameView);
    }
    String userName = mUserNameView.getText().toString();
    if (TextUtils.isEmpty(userName)) {
      toastMsg("请输入手机号");
      return;
    }
    String code = mCodeView.getText().toString();
    if (TextUtils.isEmpty(code)) {
      toastMsg("请输入验证码");
      return;
    }
    showProcessDialog();
    DBUtils.write(DBKeys.KEY_USER_PHONE, userName);
    mCondition.phoneNumber = userName;
    mCondition.code = code;
    mPresenter.login(mCondition);
  }

  @Override
  public void showCodeSuccess() {
    toastMsg("发送验证码成功");
    dismissProcessDialog();
    final int countTime = 60;
    Observable.interval(1, TimeUnit.SECONDS)
        .subscribeOn(AndroidSchedulers.mainThread())
        .map(
            new Function<Long, Integer>() {
              @Override
              public Integer apply(@NonNull Long increaseTime) throws Exception {
                return countTime - increaseTime.intValue();
              }
            })
        .take(countTime + 1)
        .observeOn(PausedHandlerScheduler.from(getHandler()))
        .compose(mLifecycleProvider.<Integer>bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe(
            new Consumer<Integer>() {
              @Override
              public void accept(@NonNull Integer integer) throws Exception {
                onCountDown(integer.intValue());
              }
            },
            new Consumer<Throwable>() {
              @Override
              public void accept(@NonNull Throwable throwable) throws Exception {
                mSendCodeView.setEnabled(true);
                mSendCodeView.setTextColor(getResources().getColor(R.color.text_primary_black));
                mSendCodeView.setText("重发验证码");
              }
            });
  }

  public void onCountDown(long sec) {
    if (sec > 0) {
      mSendCodeView.setEnabled(false);
      mSendCodeView.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
      mSendCodeView.setText(String.format("%1$ds后重发", sec));
    } else {
      mSendCodeView.setEnabled(true);
      mSendCodeView.setText("获取验证码");
      if (!TextUtils.isEmpty(mUserNameView.getText().toString().trim())) {
        mSendCodeView.setTextColor(getResources().getColor(R.color.main_color_normal));
      } else {
        mSendCodeView.setTextColor(getResources().getColor(R.color.text_primary_black));
      }
    }
  }

  @Override
  public void showLoginSuccess(User user) {
    App.getInstance().setUser(user);
    dismissProcessDialog();
    login();
  }

  private void login() {
    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
    overridePendingTransition(R.anim.fade_in_center, R.anim.fade_out_center);
  }

  @Override
  public void showError(String error) {
    dismissProcessDialog();
    if (!TextUtils.isEmpty(error)) {
      toastMsg(error);
    }
  }

  @SuppressLint("MissingSuperCall")
  @Override
  public void onBackPressed() {
    finish();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}
