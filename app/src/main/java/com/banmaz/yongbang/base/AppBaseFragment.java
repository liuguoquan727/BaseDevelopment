package com.banmaz.yongbang.base;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.mdroid.lib.core.base.BaseFragment;
import com.mdroid.lib.core.base.BaseView;
import com.mdroid.lib.core.eventbus.EventBusEvent;
import com.mdroid.lib.core.utils.UIUtil;
import com.banmaz.yongbang.R;
import com.banmaz.yongbang.dialog.ProcessDialog;
import com.banmaz.yongbang.utils.ToolBarUtils;

/** Description： */
public abstract class AppBaseFragment<V extends AppBaseView, T extends AppBaseFragmentPresenter<V>>
    extends BaseFragment<V, T> implements BaseView<T>, EventBusEvent.INotify {
  private ProcessDialog mProcessDialog;

  /**
   * 数据等加载指示器，默认空实现
   *
   * @param isActive 是否正在处理
   */
  @Override
  public void setLoadingIndicator(boolean isActive) {
  }

  protected View initErrorView() {
    View errorView = mInflater.inflate(R.layout.lib_content_error, mContentContainer, false);
    setErrorView(errorView);
    return errorView;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mProcessDialog = null;
  }

  protected void showProcessDialog() {
    if (mProcessDialog == null) {
      mProcessDialog = ProcessDialog.create(getActivity());
    }
    mProcessDialog.show();
  }

  protected void dismissProcessDialog() {
    if (mProcessDialog != null) {
      mProcessDialog.dismiss();
    }
  }

  @Override
  public void onNotify(EventBusEvent event) {
  }

  /**
   * 初始化toolbar等通用基础view
   *
   * @param title 页面标题
   */
  protected void requestBaseInit(String title) {
    UIUtil.requestStatusBarLight(this, true);
    getToolBarShadow().setVisibility(View.GONE);
    Toolbar toolBar = getToolBar();
    getStatusBar().setBackgroundResource(R.color.white);
    TextView tvTitle = UIUtil.setCenterTitle(toolBar, title);
    ToolBarUtils.updateTitleText(tvTitle);
    toolBar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            getActivity().onBackPressed();
          }
        });
  }
}
