package com.banmaz.yongbang.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mdroid.lib.recyclerview.BaseRecyclerViewAdapter;
import com.mdroid.utils.AndroidUtils;
import com.mdroid.utils.Ln;
import com.mdroid.utils.ReflectUtils;
import com.banmaz.yongbang.R;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Description：放置一些公用的方法
 */
public class ToolBarUtils {

  /**
   * 给RecyclerView设置加载更多和加载更多失败时的属性
   *
   * @param mAdapter BaseRecyclerViewAdapter
   * @param recyclerView RecyclerView
   */
  public static void setRecyclerViewLoadMore(final BaseRecyclerViewAdapter mAdapter,
      RecyclerView recyclerView) {
    LayoutInflater inflater = LayoutInflater.from(recyclerView.getContext());
    mAdapter.setLoadMoreView(inflater.inflate(R.layout.item_load_more, recyclerView, false));
    View loadMoreFail = inflater.inflate(R.layout.item_load_more_failure, recyclerView, false);
    loadMoreFail.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mAdapter.reloadMore();
      }
    });
    mAdapter.setLoadMoreFailureView(loadMoreFail);
  }

  public static void addDivider2TabLayout(Context context, TabLayout tabLayout) {
    try {
      LinearLayout tabStrip = ReflectUtils.getFieldValue(tabLayout, "mTabStrip");
      tabStrip.setDividerPadding(AndroidUtils.dp2px(context, 13));
      tabStrip.setDividerDrawable(context.getResources().getDrawable(R.drawable.divider));
      tabStrip.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
    } catch (NoSuchFieldException e) {
      Ln.e(e);
    } catch (IllegalAccessException e) {
      Ln.e(e);
    }
  }

  /**
   * 把标题字体加粗、颜色白色
   *
   * @param titleView TextView
   */
  public static void updateTitleText(TextView titleView) {
    titleView.setTextColor(Color.WHITE);
    titleView.getPaint().setFakeBoldText(false);
    titleView.setMaxEms(10);
  }

  /**
   * 给toolbar右边添加一个按钮
   *
   * @param toolbar toolbar
   * @param resId 图片资源id
   * @return 被添加的ImageView
   */
  public static ImageView addBtn2ToolbarRight(Toolbar toolbar, int resId) {
    Context context = toolbar.getContext();
    ImageView img = new ImageView(context, null,
        android.support.v7.appcompat.R.attr.toolbarNavigationButtonStyle);
    img.setImageResource(resId);
    img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
        Toolbar.LayoutParams.MATCH_PARENT);
    params.gravity = Gravity.END | Gravity.CENTER;
    img.setLayoutParams(params);
    toolbar.addView(img);
    return img;
  }

  /***
   * 给toolbar右边增加一个按钮
   *
   * @param toolbar Toolbar
   * @param inflater LayoutInflater
   * @param btnText 按钮上的字
   * @return 按钮TextView
   */
  public static TextView addBtn2ToolbarRight(Toolbar toolbar, LayoutInflater inflater,
      String btnText) {
    TextView btn = (TextView) inflater.inflate(R.layout.view_toolbar_right_text, toolbar, false);
    ((Toolbar.LayoutParams) btn.getLayoutParams()).gravity = Gravity.END;
    btn.setText(btnText);
    toolbar.addView(btn);
    return btn;
  }

  /** @param isLight true: 白色背景, 深色文字 */
  public static void requestStatusBarLight(Activity fragment, boolean isLight) {
    requestStatusBarLight(fragment, isLight, isLight ? 0xffffffff : 0xffcccccc);
  }

  /**
   * @param isLight 6.0及以上系统生效
   * @param color 6.0以下系统生效
   */
  public static void requestStatusBarLight(Activity fragment, boolean isLight, int color) {
    View decorView = fragment.getWindow().getDecorView();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (isLight) {
        decorView.setSystemUiVisibility(
            decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      } else {
        decorView.setSystemUiVisibility(
            decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      }
      fragment.getWindow().setStatusBarColor(color);
      processPrivateAPI(fragment.getWindow(), isLight);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = fragment.getWindow();
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(0xff000000);
    }
  }

  private static void processPrivateAPI(Window window, boolean lightStatusBar) {
    try {
      processFlyMe(window, lightStatusBar);
    } catch (Exception e) {
      try {
        processMIUI(window, lightStatusBar);
      } catch (Exception e2) {
        //
      }
    }
  }

  /**
   * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上 Tested on: MIUIV7 5.0 Redmi-Note3
   *
   * @throws Exception
   */
  private static void processMIUI(Window window, boolean lightStatusBar) throws Exception {
    Class<? extends Window> clazz = window.getClass();
    int darkModeFlag;
    Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
    Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
    darkModeFlag = field.getInt(layoutParams);
    Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
    extraFlagField.invoke(window, lightStatusBar ? darkModeFlag : 0, darkModeFlag);
  }

  /**
   * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
   *
   * @throws Exception
   */
  private static void processFlyMe(Window window, boolean isLightStatusBar) throws Exception {
    WindowManager.LayoutParams lp = window.getAttributes();
    Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
    int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
    Field field = instance.getDeclaredField("meizuFlags");
    field.setAccessible(true);
    int origin = field.getInt(lp);
    if (isLightStatusBar) {
      field.set(lp, origin | value);
    } else {
      field.set(lp, (~value) & origin);
    }
  }

  /***
   * 给toolbar右边增加一个按钮
   *
   * @param toolbar Toolbar
   * @param inflater LayoutInflater
   * @param btnText 按钮上的字
   * @return 按钮TextView
   */
  public static TextView addBtn2ToolbarRight(Toolbar toolbar, LayoutInflater inflater,
      @StringRes int btnText) {
    TextView btn = (TextView) inflater.inflate(R.layout.view_toolbar_right_text, toolbar, false);
    ((Toolbar.LayoutParams) btn.getLayoutParams()).gravity = Gravity.END;
    btn.setText(btnText);
    toolbar.addView(btn);
    return btn;
  }

  /**
   * 停止刷新
   *
   * @param refreshLayout SwipeRefreshLayout
   */
  public static void stopRefresh(SwipeRefreshLayout refreshLayout) {
    if (refreshLayout != null && refreshLayout.isRefreshing()) {
      refreshLayout.setRefreshing(false);
    }
  }

  public static void renderEditText(final EditText edit, final View del) {
    if (del != null) {
      del.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              edit.setText("");
            }
          });
    }
    edit.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
          }

          @Override
          public void afterTextChanged(Editable editable) {
            if (del != null) {
              del.setVisibility(editable.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
            if (editable.length() > 0) {
              edit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
            } else {
              edit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
            }
          }
        });
  }
}
