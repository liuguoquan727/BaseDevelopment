package com.banmaz.yongbang.base;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.mdroid.lib.core.base.Status;
import com.banmaz.yongbang.R;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/8/30 10:48.
 */
public class StateViewLayout extends com.mdroid.lib.core.view.StateViewLayout {
  public StateViewLayout(@NonNull Context context) {
    super(context);
  }

  public StateViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public StateViewLayout(
      @NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setBackgroundResource(R.color.window_background);
  }

  public void setErrorView(View errorView) {
    errorView.setVisibility(mErrorView == null ? View.GONE : mErrorView.getVisibility());
    removeView(mErrorView);
    addView(errorView);
    mErrorView = errorView;
  }

  public Status getStatus() {
    return mStatus;
  }

  public void setDesc(String desc) {
    if (getEmptyView() == null) {
      return;
    }
    TextView tv = getEmptyView().findViewById(R.id.desc);
    tv.setText(desc);
  }
}
