package com.hbird.http.service;

import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.bean.AccountBean;
import com.hbird.bean.ConsumptionRatioBean;
import com.hbird.bean.EditAddressBean;
import com.hbird.bean.SaveMoneyBean;
import com.hbird.bean.UserInfo;
import com.hbird.http.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


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

    // 获取登录验证码
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("verifycodeToLogin/")
    Observable<HttpResult<String>> getLoginVerifycode(@Body RequestBody body);
    // 获取找回密码验证码
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("verifycodeToResetpwd/")
    Observable<HttpResult<String>> getVerifycodeToResetpwd(@Body RequestBody body);

    // 手机号注册
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("register/android")
    Observable<HttpResult<String>> register(@Body RequestBody body);

    // 密码找回（重置）
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("resetpwd/")
    Observable<HttpResult<String>> resetpwd(@Body RequestBody body);

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


    // 分接口-获取消费结构比
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getconsumptionstructureratiov2/android")
    Observable<HttpResult<List<ConsumptionRatioBean>>> getConsumptionStructureRatio(@Header("X-AUTH-TOKEN") String token, @Query("month") String month, @Query("abId") Object abId);

    // 分接口-获取存钱效率接口
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getsavingefficiencyv2/android") // range 查询范围 可选值 3/6/12 不传默认为3
    Observable<HttpResult<SaveMoneyBean>> getSaveEfficientMoney(@Header("X-AUTH-TOKEN") String token, @Query("range") int range, @Query("month") int month, @Query("abId") Object abId);



    // 手机号密码登录
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("login/")
    Observable<HttpResult<String>> login(@Body RequestBody body);

    // 通过短信验证码登录
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("loginByCode/")
    Observable<HttpResult<String>> loginByVerifyCode(@Body RequestBody body);

    // 获取我的账本接口--->切换账本页请求
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getABAll/android")
    Observable<HttpResult<List<AccountBean>>> getAllzb(@Header("X-AUTH-TOKEN") String token);

    // 获取七牛云的鉴权凭证
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("getQiNiuAuth/android")
    Observable<HttpResult<String>> postQiNiuToken(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    // 个人信息管理
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PUT("updateUserInfo/android")
    Observable<HttpResult<String>> putPersionnalInfo(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    // 微信登录
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("loginByWeChat/android")
    Observable<HttpResult<String>> loginByWeChat(@Body RequestBody body);


    // 获取收入类型标签
    @GET("getHadABType/android")
    Observable<String> getHadABType(@Header("X-AUTH-TOKEN") String token);



}
