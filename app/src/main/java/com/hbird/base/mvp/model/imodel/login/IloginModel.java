package com.hbird.base.mvp.model.imodel.login;

import com.hbird.base.mvp.model.imodel.base.IBaseModel;

import io.reactivex.Observable;


public interface IloginModel extends IBaseModel {
    public Observable login(String mobile, String password);
    public Observable loginByVerifyCode(String mobile, String verifycode);
    public Observable getVerifyCode(String mobile);
}
