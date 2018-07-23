package com.banmaz.yongbang.network.api;

import com.banmaz.yongbang.bean.apibean.Version;
import com.banmaz.yongbang.network.ApiResult;
import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/10 14:59.
 */
public interface CommonApi {

  /**
   * 检测升级
   *
   * @return
   */
  @GET("sys/version/query")
  Flowable<ApiResult<Version>> checkUpdate();
}
