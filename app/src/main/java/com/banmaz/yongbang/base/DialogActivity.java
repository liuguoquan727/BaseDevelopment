package com.banmaz.yongbang.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/9/1 22:56.
 */
public class DialogActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow()
        .setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    new MaterialDialog.Builder(this)
        .title("提示")
        .cancelable(false)
        .content("您的登录信息已过期, 请重新登录")
        .positiveText("好的")
        .onPositive(
            new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                Intent intent = new Intent(DialogActivity.this, null);
                intent.putExtra("ban", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
              }
            })
        .build()
        .show();
  }
}
