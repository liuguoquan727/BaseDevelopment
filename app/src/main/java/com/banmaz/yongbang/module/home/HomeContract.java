package com.banmaz.yongbang.module.home;

import com.mdroid.PausedHandler;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.banmaz.yongbang.base.AppBaseActivityPresenter;
import com.banmaz.yongbang.base.AppBaseView;
import com.banmaz.yongbang.bean.apibean.LinePlan;
import com.banmaz.yongbang.bean.apibean.Version;
import com.banmaz.yongbang.bean.localbean.Reason;
import java.util.List;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/10 09:20.
 */
public interface HomeContract {
  interface IHomeView extends AppBaseView<IHomePresenter> {
  }

  abstract class IHomePresenter extends AppBaseActivityPresenter<IHomeView> {
    public IHomePresenter(LifecycleProvider<ActivityEvent> provider, PausedHandler handler) {
      super(provider, handler);
    }
  }
}
