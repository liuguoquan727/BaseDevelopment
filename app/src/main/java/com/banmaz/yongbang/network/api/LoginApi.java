package com.banmaz.yongbang.network.api;

import com.banmaz.yongbang.bean.apibean.User;
import com.banmaz.yongbang.network.ApiResult;
import com.banmaz.yongbang.network.body.login.LoginBody;
import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/10 09:54.
 */
public interface LoginApi {

  /**
   * 获取验证码
   *
   * @param
   * @return
   */
  @GET("szbus/driver/reg_code/query")
  Flowable<ApiResult<String>> getVerifyCode(@Query("phoneNum") String phoneNum,
      @Query("sign") String sign);

  /**
   * 登录
   *
   * @param
   * @return
   */
  @POST("szbus/driver/login")
  Flowable<ApiResult<User>> login(@Body LoginBody body);
}
