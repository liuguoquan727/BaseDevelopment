package com.banmaz.yongbang.network;

import com.google.gson.JsonSyntaxException;
import com.mdroid.lib.core.eventbus.EventBus;
import com.mdroid.lib.core.eventbus.EventBusEvent;
import com.banmaz.yongbang.base.App;
import com.banmaz.yongbang.bus.EventType;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Description: 处理 token 失效
 *
 * <p>Created by liuguoquan on 2017/8/17 14:23.
 */
public class TokenValidationInterceptor implements Interceptor {
  @Override
  public Response intercept(Chain chain) throws IOException, JsonSyntaxException {
    Response response = chain.proceed(chain.request());
    if (response != null) {
      int code = response.code();
      if (code >= 200 && code <= 500) {
        ResponseBody body = response.peekBody(Integer.MAX_VALUE);
        ApiResult result = App.getAppGson().fromJson(body.string(), ApiResult.class);
        if (result != null && result.isTokenError()) {
          EventBus.bus().post(new EventBusEvent(EventType.TYPE_TOKEN_ERROR));
        }
      }
    }
    return response;
  }
}
