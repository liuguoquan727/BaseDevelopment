package com.banmaz.yongbang.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.mdroid.lib.core.base.BaseActivity;
import com.mdroid.lib.core.base.BaseExtraKeys;
import com.mdroid.lib.core.base.BasePresenter;
import com.mdroid.lib.core.base.BaseWebView;
import com.mdroid.lib.core.base.Status;
import com.banmaz.yongbang.R;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/8/30 20:15.
 */
public class AppBaseBrowseActivity extends BaseActivity {
  protected ProgressBar mProgressBar;
  protected BaseWebView mWebView;
  protected Toolbar mToolbar;

  protected String mUrl;
  protected String mTitle;
  protected TextView mTitleView;

  private Unbinder unbinder;

  @Override protected void bind() {
    unbinder = ButterKnife.bind(this);
  }

  @Override protected void unbind() {
    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  @Override
  public Status getCurrentStatus() {
    return null;
  }

  @Override
  public String getPageTitle() {
    return mTitle;
  }

  @Override
  public int getContentView() {
    return R.layout.lib_activity_content_base_browse;
  }

  @Override
  public BasePresenter initPresenter() {
    return null;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    getWindow()
        .setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      mUrl = bundle.getString(BaseExtraKeys.KEY_URL);
      mTitle = bundle.getString(BaseExtraKeys.KEY_TITLE);
    }
    mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    mWebView = (BaseWebView) findViewById(R.id.webview);
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    if (mWebView != null) {
      mWebView.loadUrl(mUrl);
    }
  }

  @Override
  public void onBackPressed() {
    if (mWebView != null && mWebView.canGoBack()) {
      mWebView.goBack();
      return;
    }
    super.onBackPressed();
  }

  @Override
  public void onPause() {
    super.onPause();
    if (mWebView != null) {
      mWebView.onPause();
    }
  }

  @Override
  public void onResume() {
    if (mWebView != null) {
      mWebView.onResume();
    }
    super.onResume();
  }

  @Override
  public void onDestroy() {
    if (mWebView != null) {
      mWebView.destroy();
      mWebView = null;
    }
    mTitleView = null;
    mProgressBar = null;
    super.onDestroy();
  }
}
