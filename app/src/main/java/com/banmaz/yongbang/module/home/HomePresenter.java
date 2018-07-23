package com.banmaz.yongbang.module.home;

import com.mdroid.PausedHandler;
import com.mdroid.lib.core.rxjava.PausedHandlerScheduler;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.banmaz.yongbang.bean.apibean.LinePlan;
import com.banmaz.yongbang.bean.apibean.Version;
import com.banmaz.yongbang.bean.localbean.Reason;
import com.banmaz.yongbang.network.Api;
import com.banmaz.yongbang.network.ApiResult;
import com.banmaz.yongbang.network.body.line.LineBody;
import com.banmaz.yongbang.network.body.line.ReadyBody;
import com.banmaz.yongbang.network.body.line.ReasonBody;
import com.banmaz.yongbang.utils.ExceptionUtil;
import com.banmaz.yongbang.utils.TimeUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/10 09:21.
 */
public class HomePresenter extends HomeContract.IHomePresenter {

  public HomePresenter(LifecycleProvider<ActivityEvent> provider, PausedHandler handler) {
    super(provider, handler);
  }

  @Override
  protected void destroy() {
  }
}
