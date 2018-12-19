package com.hbird.base.di.component.activity;

import com.hbird.base.di.module.activity.DownloadActivityModule;
import com.hbird.base.mvp.view.activity.DownloadActivity;
import com.ljy.devring.di.scope.ActivityScope;

import dagger.Component;

/**
 * author:  admin
 * date:    2018/3/23
 * description:  DownloadActivity的Component
 */
@ActivityScope
@Component(modules = DownloadActivityModule.class)
public interface DownloadActivityComponent {
    void inject(DownloadActivity downloadActivity);
}
