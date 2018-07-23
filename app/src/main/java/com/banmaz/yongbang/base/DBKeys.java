package com.banmaz.yongbang.base;

/** 常量(Key 都放在这, 防止重复) */
public interface DBKeys {
  /** 权限对话框是否已经显示 */
  String FIRST_USE = "is_first_use";
  /** 引导页是否显示 */
  String GUIDE_SHOW = "is_guide_show";

  String UPDATE_APK_ID = "download_id";
  String UPDATE_APK_NAME = "apk_name";
  // 更新间隔时间
  String UPDATE_INTERVAL_TIME = "update_interval_time";

  String KEY_USER = "key_user";
  String KEY_USER_PHONE = "key_user_phone";
}
