package com.hbird.base.mvp.presenter.login;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.growingio.android.sdk.collection.GrowingIO;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.LoginReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.model.entity.res.HttpResult;
import com.hbird.base.mvp.model.entity.res.MovieRes;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.iview.login.IloginView;
import com.hbird.base.mvp.model.imodel.login.IloginModel;
import com.hbird.base.util.SPUtil;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.other.RingLog;
import com.ljy.devring.util.NetworkUtil;
import com.ljy.devring.util.RingToast;
import com.ljy.devring.util.RxLifecycleUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class loginPresenter extends BasePresenter<IloginView, IloginModel> {
    Context context;
    public loginPresenter(IloginView iView, IloginModel iModel) {
        super(iView, iModel);
        this.context = (Context) iView;
    }

    /**
     * 手机号密码登录
     * @param mobile
     * @param password
     */
    public void login(final String mobile, String password) {
        DevRing.httpManager().commonRequest(mIModel.login(mobile,password),
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
                                //统计用户时以设备为标准 统计应用自身的账号(友盟统计)
                                MobclickAgent.onProfileSignIn("手机号码登录", mobile);
                                // 设置登录用户ID API（GrowingIO统计）
                                GrowingIO.getInstance().setUserId(mobile);
                                if (mIView != null) {
                                    mIView.loginSuccess();
                                }
                            }
                            else{
                                RingToast.show(strMsg);
                            }

                        } catch (IOException e) {
                            mIView.loginFaild();
                            e.printStackTrace();
                        }
                        //RingToast.show(strJson);
                    }
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
                        if (mIView != null) {
                            mIView.loginFaild();
                        }
                    }
                }, RxLifecycleUtil.bindUntilEvent(mIView, ActivityEvent.DESTROY));
        /*
        NetWorkManager.getInstance().setContext((Context) mIView)
                .getLoginToken(mobile, password, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        LoginReturn bean = (LoginReturn) b;
                        //SPUtil.setPrefString(context, CommonTag.GLOABLE_TOKEN, bean.getResult().getXAUTHTOKEN());

                        mIView.loginSuccess();
                    }

                    @Override
                    public void onError(String s) {
                        mIView.loginFaild();
                    }
                });*/
    }



    /**
     * 手机验证码登录
     * @param mobile
     * @param verifyCode
     */
    public void loginByVerifyCode(final String mobile, String verifyCode) {
        DevRing.httpManager().commonRequest(mIModel.loginByVerifyCode(mobile,verifyCode),
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
                                //统计用户时以设备为标准 统计应用自身的账号
                                MobclickAgent.onProfileSignIn("手机验证码登录", mobile);
                                // 设置登录用户ID API（GrowingIO统计）
                                GrowingIO.getInstance().setUserId(mobile);
                                //
                                if (mIView != null) {
                                    mIView.loginSuccess();
                                }
                            }
                            else{
                                RingToast.show(strMsg);
                            }

                        } catch (IOException e) {
                            mIView.loginFaild();
                            e.printStackTrace();
                        }
                        //RingToast.show(strJson);
                    }
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
                        if (mIView != null) {
                            mIView.loginFaild();
                        }
                    }
                }, RxLifecycleUtil.bindUntilEvent(mIView, ActivityEvent.DESTROY));
    }

    public void getLoginVerifyCode1(String mobile) {
        DevRing.httpManager().commonRequest(mIModel.getVerifyCode(mobile),
                new CommonObserver<ResponseBody>() {
                    @Override
                    public void onResult(ResponseBody result) {
                        String strJson = null;
                        try {
                            //{"result":null,"code":"200","msg":"success"}
                            strJson = result.string();
                            JSONObject jsonObj = JSON.parseObject(strJson);
                            String strCode   = jsonObj.getString("code");
                            String strMsg    = jsonObj.getString("msg");

                            //登录成功
                            if (strCode.equals("200")) {
                                RingToast.show("获取验证码成功！");
                            }
                            else{
                                RingToast.show(strMsg);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //RingToast.show(strJson);
                    }
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
                        if (mIView != null) {
                            mIView.loginFaild();
                        }
                    }
                }, RxLifecycleUtil.bindUntilEvent(mIView, ActivityEvent.DESTROY));
    }

    /**
     * 登录成功后保存token信息
     * @param strResult
     */
    public void saveToken(String strResult){
        //登录成功保存Token及过期时间
        JSONObject jsonObjToken = JSON.parseObject(strResult);
        String strExpire = jsonObjToken.getString("expire");
        String strToken = jsonObjToken.getString("X-AUTH-TOKEN");
        //
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                put(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN, strToken);
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                put(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN_EXPIRE, strExpire);
        //String strExpireTest = DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
        // getString(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN_EXPIRE, "");
        SPUtil.setPrefString(context, CommonTag.GLOABLE_TOKEN,strToken);
        SPUtil.setPrefString(context, CommonTag.GLOABLE_TOKEN_EXPIRE,strExpire);
    }

}
