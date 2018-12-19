package com.hbird.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hbird.http.ApiClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: LiangYX
 * @ClassName: ChooseAccountTypeViewModel
 * @date: 2018/12/18 17:05
 * @Description: 选择账户类别
 */
public class ChooseAccountTypeViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();

    {
        ABSENT.setValue(null);
    }

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public ChooseAccountTypeViewModel(@NonNull Application application) {
        super(application);
    }

    public void getHadABType(String token) {
        ApiClient.getGankDataService()
                .getHadABType(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(String value) {
                        mCallBack.getSuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallBack.getFailure();
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

    private CallBack mCallBack;

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        void getSuccess(String loginData);

        void getFailure();
    }
}