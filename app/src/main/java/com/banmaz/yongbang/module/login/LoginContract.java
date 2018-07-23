package com.banmaz.yongbang.module.login;

import com.mdroid.PausedHandler;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.banmaz.yongbang.base.AppBaseActivityPresenter;
import com.banmaz.yongbang.base.AppBaseView;
import com.banmaz.yongbang.bean.apibean.User;

/**
 * Description:
 *
 * Created by liuguoquan on 2018/5/9 09:47.
 */
public interface LoginContract {
  interface ILoginView extends AppBaseView<ILoginPresenter> {
    void showCodeSuccess();

    void showLoginSuccess(User user);
  }

  abstract class ILoginPresenter extends AppBaseActivityPresenter<ILoginView> {

    public ILoginPresenter(LifecycleProvider provider,
        PausedHandler handler) {
      super(provider, handler);
    }

    public abstract void getVerifyCode(LoginCondition condition);

    public abstract void login(LoginCondition condition);
  }
}
