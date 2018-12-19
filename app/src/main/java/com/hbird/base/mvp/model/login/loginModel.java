package com.hbird.base.mvp.model.login;

import com.hbird.base.mvp.model.http.MovieApiService;
import com.hbird.base.mvp.model.http.UserApiService;
import com.hbird.base.mvp.model.imodel.login.IloginModel;
import com.ljy.devring.DevRing;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;

public class loginModel implements IloginModel {

    /**
     * 手机密码登录
     *
     * @param mobile            手机
     * @param password          密码
     */
    @Override
    public Observable login(String mobile, String password) {
        String jsonInfo = "{\"mobile\":\"" + mobile + "\", \"password\":\"" + password + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        return DevRing.httpManager().getService(UserApiService.class).login(body);
    }

    @Override
    public Observable loginByVerifyCode(String mobile, String verifycode) {
        String jsonInfo = "{\"mobile\":\"" + mobile + "\", \"verifycode\":\"" + verifycode + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        return DevRing.httpManager().getService(UserApiService.class).loginByVerifyCode(body);
    }

    @Override
    public Observable getVerifyCode(String mobile) {
        String jsonInfo = "{\"mobile\":\"" + mobile + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        return DevRing.httpManager().getService(UserApiService.class).getLoginVerifycode(body);
    }

}