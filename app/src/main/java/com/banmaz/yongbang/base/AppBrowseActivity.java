package com.banmaz.yongbang.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mdroid.lib.core.base.BaseChromeClient;
import com.mdroid.lib.core.base.BaseExtraKeys;
import com.mdroid.lib.core.base.BaseWebView;
import com.mdroid.lib.core.utils.UIUtil;
import com.banmaz.yongbang.R;
import com.banmaz.yongbang.utils.ToolBarUtils;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/8/17 16:26.
 */
public class AppBrowseActivity extends AppBaseBrowseActivity {

  private ImageView mShare;
  private boolean mIsShare;

  @Override
  public void initData(Bundle savedInstanceState) {
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      mUrl = bundle.getString(BaseExtraKeys.KEY_URL);
      mTitle = bundle.getString(BaseExtraKeys.KEY_TITLE);
      mIsShare = bundle.getBoolean(Constants.ExtraKey.KET_SHARE, false);
    }
    mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    mWebView = (BaseWebView) findViewById(R.id.webview);
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    requestBaseInit(mToolbar, mTitle);
    mShare.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
          }
        });
    if (mIsShare) {
      mShare.setVisibility(View.VISIBLE);
    } else {
      mShare.setVisibility(View.GONE);
    }
    if (mWebView != null) {
      WebSettings webSettings = mWebView.getSettings();
      webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
      webSettings.setAppCacheEnabled(false);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
      }
      mWebView.setWebViewClient(new WebViewClient());
      mWebView.setWebChromeClient(new BaseChromeClient(mProgressBar));
      mWebView.loadUrl(mUrl);
    }
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
