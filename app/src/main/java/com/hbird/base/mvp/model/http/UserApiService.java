package com.hbird.base.mvp.model.http;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserApiService {

    /**
     * 手机号密码登录
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("login/")
    Observable<ResponseBody> login(@Body RequestBody body);

    /**
     * 通过短信验证码登录
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("loginByCode/")
    Observable<ResponseBody> loginByVerifyCode(@Body RequestBody body);

    /**
     * 获取登录验证码
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("verifycodeToLogin/")
    Observable<ResponseBody> getLoginVerifycode(@Body RequestBody body);

    /**
     * 手机号注册
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("register/android")
    Observable<ResponseBody> register(@Body RequestBody body);

    /**
     * 获取注册验证码
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("verifycodeToRegister/")
    Observable<ResponseBody> getRegisterVerifycode(@Body RequestBody body);

    /**
     * 密码找回（重置）
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("resetpwd/")
    Observable<ResponseBody> resetpwd(@Body RequestBody body);

    /**
     * 获取找回密码验证码
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("verifycodeToResetpwd/")
    Observable<ResponseBody> getVerifycodeToResetpwd(@Body RequestBody body);

    /**
     * 手机号密码登录
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("loginByWeChat/android")
    Call<ResponseBody> loginByWeChat(@Body RequestBody body);


}
