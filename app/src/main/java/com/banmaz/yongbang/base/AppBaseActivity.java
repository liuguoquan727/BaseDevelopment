package com.banmaz.yongbang.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.mdroid.lib.core.base.BaseActivity;
import com.mdroid.lib.core.base.BaseView;
import com.mdroid.lib.core.eventbus.EventBusEvent;
import com.mdroid.lib.core.utils.UIUtil;
import com.banmaz.yongbang.dialog.ProcessDialog;
import com.banmaz.yongbang.utils.ToolBarUtils;

/** Description： */
public abstract class AppBaseActivity<V extends AppBaseView, T extends AppBaseActivityPresenter<V>>
    extends BaseActivity<V, T> implements BaseView<T>, EventBusEvent.INotify {
  private ProcessDialog mProcessDialog;

  private Unbinder unbinder;

  @Override protected void bind() {
    unbinder = ButterKnife.bind(this);
  }

  @Override protected void unbind() {
    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  /**
   * 数据等加载指示器，默认空实现
   *
   * @param isActive 是否正在处理
   */
  @Override
  public void setLoadingIndicator(boolean isActive) {
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mProcessDialog = null;
  }

  protected void showProcessDialog() {
    showProcessDialog(false);
  }

  protected void showProcessDialog(boolean isCancelable) {
    if (mProcessDialog == null) {
      mProcessDialog = ProcessDialog.create(this, isCancelable);
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

  protected void requestBaseInit(Toolbar toolBar, String title) {
    TextView tvTitle = UIUtil.setCenterTitle(toolBar, title);
    ToolBarUtils.updateTitleText(tvTitle);
    toolBar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
          }
        });
  }
}
