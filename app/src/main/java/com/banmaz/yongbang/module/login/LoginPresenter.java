package com.banmaz.yongbang.module.login;

import com.mdroid.PausedHandler;
import com.mdroid.lib.core.rxjava.PausedHandlerScheduler;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.banmaz.yongbang.bean.apibean.User;
import com.banmaz.yongbang.network.Api;
import com.banmaz.yongbang.network.ApiResult;
import com.banmaz.yongbang.network.body.login.LoginBody;
import com.banmaz.yongbang.utils.ExceptionUtil;
import com.banmaz.yongbang.utils.TimeUtil;
import com.banmaz.yongbang.utils.MD5Utils;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/9 09:59.
 */
public class LoginPresenter extends LoginContract.ILoginPresenter {

  public LoginPresenter(LifecycleProvider provider, PausedHandler handler) {
    super(provider, handler);
  }

  private String signSecret(String apiName, String secret, String userName) {
    StringBuilder sb = new StringBuilder();
    sb.append(apiName).append(secret).append("phoneNum").append("=").append(userName);
    String tempSignstr = sb.toString();
    String tempSign = MD5Utils.md5(tempSignstr);
    sb.delete(0, sb.length());
    sb.append(tempSign);
    sb.insert(7, secret);
    return MD5Utils.md5(sb.toString());
  }

  @Override
  public void getVerifyCode(LoginCondition condition) {
    String sign = signSecret("/reg_code/query", "123", condition.phoneNumber);
    Api.getLoginApi()
        .getVerifyCode(condition.phoneNumber, sign)
        .subscribeOn(Schedulers.io())
        .onBackpressureDrop()
        .observeOn(PausedHandlerScheduler.from(mHandler))
        .compose(mLifecycleProvider.<ApiResult<String>>bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe(
            new Consumer<ApiResult<String>>() {
              @Override
              public void accept(ApiResult<String> result) throws Exception {
                if (result.isSuccess()) {
                  mView.showCodeSuccess();
                } else {
                  mView.showError(result.getMessage());
                }
              }
            },
            new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                mView.showError(ExceptionUtil.getMessage(throwable));
              }
            });
  }

  @Override
  public void login(LoginCondition condition) {
    LoginBody body = new LoginBody();
    body.phoneNum = condition.phoneNumber;
    body.regCode = condition.code;
    Api.getLoginApi()
        .login(body)
        .subscribeOn(Schedulers.io())
        .onBackpressureDrop()
        .observeOn(PausedHandlerScheduler.from(mHandler))
        .compose(mLifecycleProvider.<ApiResult<User>>bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe(
            new Consumer<ApiResult<User>>() {
              @Override
              public void accept(ApiResult<User> result) throws Exception {
                if (result.isSuccess()) {
                  mView.showLoginSuccess(result.data);
                } else {
                  mView.showError(result.getMessage());
                }
              }
            },
            new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                mView.showError(ExceptionUtil.getMessage(throwable));
              }
            });
  }

  @Override
  protected void destroy() {
  }
}
