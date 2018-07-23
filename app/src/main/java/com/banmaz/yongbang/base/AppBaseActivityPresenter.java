package com.banmaz.yongbang.base;

import android.content.Context;
import com.mdroid.PausedHandler;
import com.mdroid.lib.core.base.BaseFragment;
import com.mdroid.lib.core.base.BasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

/** Description：MVP模式中，此app对应的所有P的基类 */
public abstract class AppBaseActivityPresenter<T> extends BasePresenter<T> {

  protected Context mContext;
  protected PausedHandler mHandler;
  protected LifecycleProvider<ActivityEvent> mLifecycleProvider;

  public AppBaseActivityPresenter(
      LifecycleProvider<ActivityEvent> provider, PausedHandler handler) {
    mContext = App.getInstance();
    mLifecycleProvider = provider;
    mHandler = handler;
  }

  @Override
  protected void onDestroy() {
    mContext = null;
    mLifecycleProvider = null;
    mHandler = null;
    destroy();
  }

  protected abstract void destroy();
}
