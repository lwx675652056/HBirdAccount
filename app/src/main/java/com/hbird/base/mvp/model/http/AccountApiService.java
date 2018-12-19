package com.hbird.base.mvp.model.http;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 记账相关
 */
public interface AccountApiService {

    /**
     * 获取支出类目标签
     * X-AUTH-TOKEN	String	  是	   密文	令牌
     * @Headers({"Content-Type: application/json","Accept: application/json"})
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getSpendTypeList/")
    Observable<ResponseBody> getSpendTypeList(@Header("X-AUTH-TOKEN") String x_auth_token);
}
