package com.hbird.base.mvp.model.imodel.login;

import com.hbird.base.mvp.model.imodel.base.IBaseModel;

import io.reactivex.Observable;


public interface IRegisterModel extends IBaseModel {
    public Observable register(String mobile, String password, String verifycode,String mobileSystem,
                               String mobileSystemVersion,String mobileDevice,String channelName,String mobileType);
    public Observable getRegisterVerifycode(String mobile);
}
