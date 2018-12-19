package com.hbird.base.mvp.model.imodel.login;

import com.hbird.base.mvp.model.imodel.base.IBaseModel;

import io.reactivex.Observable;


public interface IForgetPasswordModel extends IBaseModel {
    public Observable resetpwd(String mobile, String password, String verifycode);
    public Observable getVerifycodeToResetpwd(String mobile);
}
