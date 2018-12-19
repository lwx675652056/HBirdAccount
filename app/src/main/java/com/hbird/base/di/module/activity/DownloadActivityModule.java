package com.hbird.base.di.module.activity;

import com.hbird.base.mvp.model.DownloadModel;
import com.hbird.base.mvp.model.imodel.IDownloadModel;
import com.hbird.base.mvp.presenter.DownloadPresenter;
import com.hbird.base.mvp.view.iview.IDownloadView;
import com.ljy.devring.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * author:  admin
 * date:    2018/3/23
 * description: 对DownloadActivity中的相关变量进行初始化
 */
@Module
public class DownloadActivityModule {
    private IDownloadView mIDownloadView;

    public DownloadActivityModule(IDownloadView iDownloadView) {
        mIDownloadView = iDownloadView;
    }

    @Provides
    @ActivityScope
    IDownloadView iDownloadView() {
        return mIDownloadView;
    }

    @Provides
    @ActivityScope
    IDownloadModel iDownloadModel() {
        return new DownloadModel();
    }

    @Provides
    @ActivityScope
    DownloadPresenter downloadPresenter(IDownloadView iDownloadView, IDownloadModel iDownloadModel) {
        return new DownloadPresenter(iDownloadView, iDownloadModel);
    }
}
