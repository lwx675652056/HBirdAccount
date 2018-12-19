package com.hbird.http.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

/**
 * Created by dxx on 2017/11/8.
 */

public interface GankDataService {

    // 获取收入类型标签
    @GET("getHadABType/android")
    Observable<String> getHadABType(@Header("X-AUTH-TOKEN") String token);

//    @GET("api/data/福利/{size}/{index}")
//    Observable<GirlsData> getFuliData(@Path("size") String size, @Path("index") String index);
//
//    @GET("api/data/Android/{size}/{index}")
//    Observable<NewsData> getAndroidData(@Path("size") String size, @Path("index") String index);

}
