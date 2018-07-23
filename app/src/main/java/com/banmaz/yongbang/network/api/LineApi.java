package com.banmaz.yongbang.network.api;

import com.banmaz.yongbang.bean.apibean.LinePlan;
import com.banmaz.yongbang.bean.localbean.Reason;
import com.banmaz.yongbang.network.ApiResult;
import com.banmaz.yongbang.network.body.line.LineBody;
import com.banmaz.yongbang.network.body.line.ReadyBody;
import com.banmaz.yongbang.network.body.line.ReasonBody;
import io.reactivex.Flowable;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/12 17:59.
 */
public interface LineApi {

  /**
   * 获取司机最近的一个车次信息
   *
   * @return
   */
  @GET("car/linePlan/query")
  Flowable<ApiResult<LinePlan>> query();

  /**
   * 准备发车
   *
   * @return
   */
  @POST("car/linePlan/ready")
  Flowable<ApiResult<LinePlan>> ready(@Body ReadyBody body);

  /**
   * 准备发车
   *
   * @return
   */
  @POST("car/linePlan/start")
  Flowable<ApiResult<LinePlan>> start(@Body LineBody body);

  /**
   * 到站
   *
   * @return
   */
  @POST("car/linePlan/arrival")
  Flowable<ApiResult<LinePlan>> arrival(@Body LineBody body);

  /**
   * 停止运营
   *
   * @return
   */
  @POST("car/linePlan/stop")
  Flowable<ApiResult<LinePlan>> stop(@Body ReasonBody body);

  /**
   * 获取停运原因
   *
   * @return
   */
  @GET("car/linePlan/stopReason/list")
  Flowable<ApiResult<List<Reason>>> queryReson();
}
