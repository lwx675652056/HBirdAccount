package com.hbird.base.mvp.model.login;

import com.hbird.base.mvp.model.http.UserApiService;
import com.hbird.base.mvp.model.imodel.login.IRegisterModel;
import com.ljy.devring.DevRing;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class RegisterModel implements IRegisterModel {

    @Override
    public Observable register(String mobile, String password, String verifycode
            ,String mobileSystem,String mobileSystemVersion,String mobileDevice,String channelName,String mobileType) {
        String jsonInfo = "{\"mobile\":\"" + mobile + "\", \"password\":\"" + password + "\", \"verifycode\":\"" + verifycode + "\"" +
                ", \"mobileSystem\":\"" + mobileSystem + "\", \"mobileSystemVersion\":\"" + mobileSystemVersion + "\"," +
                " \"mobileDevice\":\"" + mobileDevice + "\", \"androidChannel\":\"" + channelName + "\" , \"mobileManufacturer\":\"" + mobileType + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        return DevRing.httpManager().getService(UserApiService.class).register(body);
    }

    @Override
    public Observable getRegisterVerifycode(String mobile) {
        String jsonInfo = "{\"mobile\":\"" + mobile + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        return DevRing.httpManager().getService(UserApiService.class).getRegisterVerifycode(body);
    }
}