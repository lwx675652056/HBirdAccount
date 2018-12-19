package com.hbird.base.mvp.presenter.login;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.iview.login.IForgetPasswordView;
import com.hbird.base.mvp.model.imodel.login.IForgetPasswordModel;
import com.hbird.base.util.SPUtil;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.RingToast;
import com.ljy.devring.util.RxLifecycleUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ForgetPasswordPresenter extends BasePresenter<IForgetPasswordView, IForgetPasswordModel> {
    Context context;
    public ForgetPasswordPresenter(IForgetPasswordView iView, IForgetPasswordModel iModel) {
        super(iView, iModel);
        this.context = (Context) iView;
    }

    /**
     * 手机验证码登录
     * @param mobile
     * @param verifyCode
     */
    public void resetPassword(String mobile, String password, String verifyCode) {
        DevRing.httpManager().commonRequest(mIModel.resetpwd(mobile, password, verifyCode),
                new CommonObserver<ResponseBody>() {
                    @Override
                    public void onResult(ResponseBody result) {
                        String strJson = null;
                        try {
                            //{"code":"200","msg":"success","result":{"expire":"2592000","X-AUTH-TOKEN":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNTUwMTIzMzc3MCIsInN1YiI6IjE1NTAxMjMzNzcwIiwiaWF0IjoxNTMwMDAxODUxfQ.ZTpgbPp0tRWa7D_ViQ1XjEtMA83AtaIIGTdpqgZSjHw"}}
                            strJson = result.string();
                            JSONObject jsonObj = JSON.parseObject(strJson);
                            String strCode   = jsonObj.getString("code");
                            String strMsg    = jsonObj.getString("msg");
                            String strResult = jsonObj.getString("result");

                            //登录成功
                            if (strCode.equals("200")) {
                                saveToken(strResult);
                                //
                                if (mIView != null) {
                                    mIView.resetPasswordSuccess();
                                }
                            }
                            else{
                                RingToast.show(strMsg);
                            }

                        } catch (IOException e) {
                            if (mIView != null) {
                                mIView.resetPasswordFaild();
                            }
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
                        if (mIView != null) {
                            mIView.resetPasswordFaild();
                        }
                    }
                }, RxLifecycleUtil.bindUntilEvent(mIView, ActivityEvent.DESTROY));
    }

    /**
     * 获取 找回密码验证码
     * @param mobile
     */
    public void getVerifycodeToResetpwd(String mobile) {
        DevRing.httpManager().commonRequest(mIModel.getVerifycodeToResetpwd(mobile),
                new CommonObserver<ResponseBody>() {
                    @Override
                    public void onResult(ResponseBody result) {
                        String strJson = null;
                        try {
                            //{"code":"200","msg":"success","result":{"expire":"2592000","X-AUTH-TOKEN":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNTUwMTIzMzc3MCIsInN1YiI6IjE1NTAxMjMzNzcwIiwiaWF0IjoxNTMwMDAxODUxfQ.ZTpgbPp0tRWa7D_ViQ1XjEtMA83AtaIIGTdpqgZSjHw"}}
                            strJson = result.string();
                            JSONObject jsonObj = JSON.parseObject(strJson);
                            String strCode   = jsonObj.getString("code");
                            String strMsg    = jsonObj.getString("msg");
                            String strResult = jsonObj.getString("result");

                            //登录成功
                            if (strCode.equals("200")) {
                                RingToast.show("获取验证码成功！");
                            }
                            else{
                                RingToast.show(strMsg);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            RingToast.show("获取验证码失败！");
                        }
                    }
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
                        RingToast.show("获取验证码失败！");
                    }
                }, RxLifecycleUtil.bindUntilEvent(mIView, ActivityEvent.DESTROY));
    }

    private void saveToken(String strResult){
        //登录成功保存Token及过期时间
        JSONObject jsonObjToken = JSON.parseObject(strResult);
        String strExpire = jsonObjToken.getString("expire");
        String strToken = jsonObjToken.getString("X-AUTH-TOKEN");
        //
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                put(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN, strToken);
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                put(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN_EXPIRE, strExpire);
        SPUtil.setPrefString(context, CommonTag.GLOABLE_TOKEN,strToken);
        SPUtil.setPrefString(context, CommonTag.GLOABLE_TOKEN_EXPIRE,strExpire);
    }

}
