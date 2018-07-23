package com.banmaz.yongbang.utils;

import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 * @author Sam
 */
public class ValidateUtils {

  private static final String TAG = ValidateUtils.class.getSimpleName();

  /**
   * 验证图片是否符合分享规则
   *
   * @param imgUrl 图片后缀仅支持JPEG、GIF、PNG格式
   */
  public static boolean validateImgUrl(String imgUrl) {
    // 分享到新浪微博。要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M
    return imgUrl.endsWith(".jpeg")
        || imgUrl.endsWith(".gif")
        || imgUrl.endsWith(".png")
        || imgUrl.endsWith(".JPEG")
        || imgUrl.endsWith(".GIF")
        || imgUrl.endsWith(".PNG");
  }

  /**
   * 验证手机号码是否合法
   *
   * @param number 需要做验证的手机号码
   * @return 返回true表示合法，false表示非法
   */
  public static boolean isValidMobilePhone(String number) {
    /*
     * 国家号码段分配如下：
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * 186/187/188是4G号码
     * 注：手机号码必须是11位数
     */
    Pattern p =
        Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,2,5-9])|(17[0-9]))\\d{8}$"); // 获得一个匹配模式
    Matcher m = p.matcher(number); // 获取一个匹配规则
    return m.matches();
  }

  public static boolean isValidMobile(String userMobile) {
    Pattern p = Pattern.compile("^1[0-9]{10}$");
    Matcher m = p.matcher(userMobile);
    return m.matches();
  }

  public static boolean isEmptyList(List list) {
    if (list == null) return true;
    return list.size() == 0;
  }

  public static boolean isNotEmptyList(List list) {
    return !isEmptyList(list);
  }

  public static boolean isNotEmptyMap(Map map) {
    return !isEmptyMap(map);
  }

  public static boolean isEmptyMap(Map map) {
    if (map == null) return true;
    return map.size() == 0;
  }

  public static boolean isEmptyText(CharSequence str) {
    return TextUtils.isEmpty(str);
  }

  public static boolean isNotEmptyText(String str) {
    if (str == null) return false;
    if (TextUtils.isEmpty(str)) return false;
    return true;
  }

  public static String handleEmptyString(String str) {
    if (isNotEmptyText(str)) return str;
    return "";
  }

  public static boolean isInValidTime(String startTime) {
    if (isEmptyText(startTime)) {
      return true;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
    try {
      sdf.parse(startTime);
      return false;
    } catch (Exception e) {
      return true;
    }
  }
}
