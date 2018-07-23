package com.banmaz.yongbang.network;

import android.support.annotation.NonNull;
import com.banmaz.yongbang.base.App;
import com.banmaz.yongbang.base.Constants;
import com.banmaz.yongbang.network.api.CommonApi;
import com.banmaz.yongbang.network.api.LineApi;
import com.banmaz.yongbang.network.api.LoginApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/10 09:52.
 */
public class Api {

  private static Retrofit sNormalRetrofit;

  public static LoginApi getLoginApi() {
    return getRetrofit().create(LoginApi.class);
  }

  public static CommonApi getCommonApi() {
    return getRetrofit().create(CommonApi.class);
  }

  public static LineApi getLineApi() {
    return getRetrofit().create(LineApi.class);
  }

  @NonNull
  private static Retrofit getRetrofit() {
    if (sNormalRetrofit == null) {
      sNormalRetrofit =
          new Retrofit.Builder()
              .baseUrl(Constants.HOST)
              .addConverterFactory(GsonConverterFactory.create(App.getAppGson()))
              .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
              .client(HttpClient.getDefaultHttpClient())
              .build();
    }
    return sNormalRetrofit;
  }
}
