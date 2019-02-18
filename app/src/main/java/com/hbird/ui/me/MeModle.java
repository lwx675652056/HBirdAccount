package com.hbird.ui.me;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.UserInfo;
import com.hbird.common.Constants;
import com.hbird.http.ApiClient;
import com.hbird.http.HttpResult;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sing.common.base.BaseViewModel;
import sing.common.util.LogUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

public class MeModle extends BaseViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    {
        ABSENT.setValue(null);
    }

    public MeModle(@NonNull Application application) {
        super(application);
    }

    // 获取个人信息
    public void getUserInfo(String token,CallBack callBack) {
        ApiClient.getGankDataService()
                .getPersionalInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<UserInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<UserInfo> value) {
                        LogUtil.e(value.toString());

                        UserInfo info = value.result;

                        // 手机号新注册的，是空的没有值的
                        SharedPreferencesUtil.put(Constants.USER_ID, String.valueOf(info.id));// 用户id
                        SharedPreferencesUtil.put(Constants.REGISTER_DATE, info.registerDate);// 注册时间

                        // 旧的也保存一份   避免出错
                        SPUtil.setPrefString(getApplication(), CommonTag.ACCOUNT_USER_HEADER, info.avatarUrl);
                        SPUtil.setPrefString(getApplication(), CommonTag.ACCOUNT_USER_NICK_NAME, info.nickName);

                        SPUtil.setPrefString(getApplication(), CommonTag.H5PRIMKEYPHONE,info.mobile);
                        SPUtil.setPrefString(getApplication(), CommonTag.H5PRIMKEYWEIXIN, info.wechatAuth);
                        SPUtil.setPrefString(getApplication(), CommonTag.H5PRIMKEYZILIAO, info.toString());
                        SPUtil.setPrefString(getApplication(), CommonTag.H5PRIMKEYIDS, String.valueOf(info.id));
                        SPUtil.setPrefString(getApplication(), CommonTag.H5PRIMKEYNAME,info.nickName);

                        SharedPreferencesUtil.put(Constants.USER_MOBILE, info.mobile == null ? "" : info.mobile);// 新保存的

                        SharedPreferencesUtil.put(Constants.FENGFENG_ID, String.valueOf(info.id));// 保存峰峰id
                        //保存id 极光推送需要用到
                        JPushInterface.setAlias(getApplication(),1,String.valueOf(info.id));

                        SPUtil.setPrefString(getApplication(), CommonTag.KEFUIMG,info.avatarUrl);

                        callBack.success(info);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showShort(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("danxx", "onComplete------>");
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public interface CallBack {
        void success(UserInfo info);
    }
}
