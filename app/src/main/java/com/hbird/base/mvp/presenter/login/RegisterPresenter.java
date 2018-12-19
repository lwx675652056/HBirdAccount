package com.hbird.base.mvp.presenter.login;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.growingio.android.sdk.collection.GrowingIO;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.iview.login.IRegisterView;
import com.hbird.base.mvp.model.imodel.login.IRegisterModel;
import com.hbird.base.util.SPUtil;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.RingToast;
import com.ljy.devring.util.RxLifecycleUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

import okhttp3.ResponseBody;

public class RegisterPresenter extends BasePresenter<IRegisterView, IRegisterModel> {
    Context context;
    public RegisterPresenter(IRegisterView iView, IRegisterModel iModel) {
        super(iView, iModel);
        this.context = (Context) iView;
    }


    /**
     * 手机验证码登录
     * @param mobile
     * @param verifyCode
     */
    public void register(final String mobile, String password, String verifyCode, String mobileSystem,
                         String mobileSystemVersion, String mobileDevice,String channelName,String mobileType) {

        DevRing.httpManager().commonRequest(mIModel.register(mobile, password, verifyCode,mobileSystem,
                mobileSystemVersion,mobileDevice,channelName,mobileType),
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
                                MobclickAgent.onProfileSignIn("手机注册登录", mobile);
                                // 设置登录用户ID API（GrowingIO统计）
                                GrowingIO.getInstance().setUserId(mobile);
                                //
                                if (mIView != null) {
                                    mIView.registerSuccess();
                                }
                            }
                            else{
                                RingToast.show(strMsg);
                            }

                        } catch (IOException e) {
                            mIView.registerFaild();
                            e.printStackTrace();
                            RingToast.show("注册失败！");
                        }
                        //RingToast.show(strJson);
                    }
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
                        if (mIView != null) {
                            mIView.registerFaild();
                        }
                    }
                }, RxLifecycleUtil.bindUntilEvent(mIView, ActivityEvent.DESTROY));
    }

    /**
     * 获取手机注册验证码
     * @param mobile
     */
    public void getRegisterVerifycode(String mobile) {
        DevRing.httpManager().commonRequest(mIModel.getRegisterVerifycode(mobile),
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
                        //RingToast.show(strJson);
                    }
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
                        RingToast.show("获取验证码失败！");
                    }
                }, RxLifecycleUtil.bindUntilEvent(mIView, ActivityEvent.DESTROY));
    }

    /**
     * 登录成功后保存token信息
     * @param strResult
     */
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
        //String strExpireTest = DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
        // getString(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN_EXPIRE, "");
        SPUtil.setPrefString(context, CommonTag.GLOABLE_TOKEN,strToken);
        SPUtil.setPrefString(context, CommonTag.GLOABLE_TOKEN_EXPIRE,strExpire);
    }

}
