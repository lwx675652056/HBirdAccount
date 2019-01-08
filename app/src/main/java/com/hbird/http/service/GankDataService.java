package com.hbird.http.service;

import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.bean.EditAddressBean;
import com.hbird.bean.UserInfo;
import com.hbird.http.HttpResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface GankDataService {


    // 获取收货地址
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("consigneeAddress/android")
    Observable<HttpResult<EditAddressBean>> getAddress(@Header("X-AUTH-TOKEN") String token);

    // 修改收货地址
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("consigneeAddress/android")
    Observable<String> saveAddress(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    // 获取注册验证码
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("verifycodeToRegister/")
    Observable<HttpResult<String>> getRegisterCode(@Body RequestBody body);

    // 手机号注册
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("register/android")
    Observable<HttpResult<String>> register(@Body RequestBody body);

    /**
     * 获取个人信息详情
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("userInfo/android")
    Observable<HttpResult<UserInfo>> getPersionalInfo(@Header("X-AUTH-TOKEN") String token);

    // 启动页类目更新检查/获取同步间隔
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("checkSystemParamv2/android")
    Observable<HttpResult<SystemBiaoqReturn.ResultBean>> postCheckChargeTypes(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    // 获取收入类型标签
    @GET("getHadABType/android")
    Observable<String> getHadABType(@Header("X-AUTH-TOKEN") String token);

//    @GET("api/data/福利/{size}/{index}")
//    Observable<GirlsData> getFuliData(@Path("size") String size, @Path("index") String index);
//
//    @GET("api/data/Android/{size}/{index}")
//    Observable<NewsData> getAndroidData(@Path("size") String size, @Path("index") String index);

}
