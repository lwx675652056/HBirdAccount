package com.hbird.ui.analysis;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hbird.bean.ConsumptionRatioBean;
import com.hbird.bean.SaveMoneyBean;
import com.hbird.bean.YuSuan1Bean;
import com.hbird.bean.YuSuan2Bean;
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

public class AnalysisModle extends BaseViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    {
        ABSENT.setValue(null);
    }

    public AnalysisModle(@NonNull Application application) {
        super(application);
    }

    // 获取消费结构比
    public void getConsumptionStructureRatio(boolean showDialog,String token,String month,Object abld,CallBack callBack) {
        //  abld 为空时查询个人信息，为-1时查询总账本
        ApiClient.getGankDataService()
                .getConsumptionStructureRatio(token,month,abld)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (showDialog){
                        showDialog("");
                    }
                })
                .subscribe(new Observer<HttpResult<List<ConsumptionRatioBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<List<ConsumptionRatioBean>> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            callBack.success(value.result);
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
                        Log.i("danxx", "onComplete------>");
                    }
                });
    }

    // 获取存钱效率
    public void getSaveEfficients(boolean showDialog,String token,int type,int mm,Object abld,EfficientsCallBack callBack){
        //  abld 为空时查询个人信息，为-1时查询总账本
        ApiClient.getGankDataService()
                .getSaveEfficientMoney(token,type,mm,abld)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (showDialog){
                        showDialog("");
                    }
                })
                .subscribe(new Observer<HttpResult<SaveMoneyBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<SaveMoneyBean> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            callBack.success(value.result);
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
                        Log.i("danxx", "onComplete------>");
                    }
                });
    }

    // 获取预算完成率  日常账本
    public void getYuSuanFinish1(String month, String range, String abId, String token,YuSuanCallBack callBack){
        ApiClient.getGankDataService()
                .getYSFinish1(token,month,range,abId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showDialog(""))
                .subscribe(new Observer<HttpResult<List<YuSuan1Bean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<List<YuSuan1Bean>> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            callBack.success(value.result);
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
                        Log.i("danxx", "onComplete------>");
                    }
                });
    }

    // 获取预算完成率  场景账本
    public void getYuSuanFinish2(String month, String range, String abId, String token,YuSuanCallBack1 callBack){
        ApiClient.getGankDataService()
                .getYSFinish2(token,month,range,abId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showDialog(""))
                .subscribe(new Observer<HttpResult<YuSuan2Bean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<YuSuan2Bean> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            callBack.success(value.result);
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
        void success(List<ConsumptionRatioBean> ratioList);
    }

    public interface EfficientsCallBack {
        void success(SaveMoneyBean bean);
    }

    public interface YuSuanCallBack {
        void success(List<YuSuan1Bean> list);
    }
    public interface YuSuanCallBack1 {
        void success(YuSuan2Bean b);
    }
}
