package com.hbird.base.mvp.model.login;

import com.hbird.base.mvp.model.http.UserApiService;
import com.hbird.base.mvp.model.imodel.login.IForgetPasswordModel;
import com.ljy.devring.DevRing;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ForgetPasswordModel implements IForgetPasswordModel {


    @Override
    public Observable resetpwd(String mobile, String password, String verifycode) {
        //String jsonInfo = "{\"mobile\":\"15501233770\",\"password\":\"123456\"}";
        String jsonInfo = "{\"mobile\":\"" + mobile + "\", \"password\":\"" + password + "\", \"verifycode\":\"" + verifycode + "\" }";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        return DevRing.httpManager().getService(UserApiService.class).resetpwd(body);
    }

    @Override
    public Observable getVerifycodeToResetpwd(String mobile) {
        String jsonInfo = "{\"mobile\":\"" + mobile + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        return DevRing.httpManager().getService(UserApiService.class).getVerifycodeToResetpwd(body);
    }
}