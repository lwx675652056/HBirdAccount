package com.hbird.ui.assets;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbird.bean.AssetsBean;
import com.hbird.http.ApiClient;
import com.hbird.http.HttpResult;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sing.common.base.BaseViewModel;
import sing.common.util.LogUtil;
import sing.util.ToastUtil;

public class ActAssetsDetailModle extends BaseViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    {
        ABSENT.setValue(null);
    }

    public ActAssetsDetailModle(@NonNull Application application) {
        super(application);
    }

    // 获取消费结构比
    public void getAssets(String token,int assetsType,CallBack callBack) {
        ApiClient.getGankDataService()
                .getAssets(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> showDialog(""))
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<String> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());

                        if (value.code == 200) {
                            JSONObject jsonObject = JSON.parseObject(value.result);
                            JSONArray array = jsonObject.getJSONArray("assets");
                            List<AssetsBean> l = JSON.parseArray(array.toJSONString(),AssetsBean.class);

                            for (int i = 0; i < l.size(); i++) {
                                if (assetsType == l.get(i).assetsType){
                                    callBack.success(l.get(i).money);
                                    return;
                                }
                            }
                        } else {
                            ToastUtil.showShort(value.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                        ToastUtil.showShort(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public interface CallBack {
        void success(double money);
    }
}
