package com.hbird.base.di.component.activity;

import com.hbird.base.di.module.activity.UploadActivityModule;
import com.hbird.base.mvp.view.activity.UploadActivity;
import com.ljy.devring.di.scope.ActivityScope;

import dagger.Component;

/**
 * author:  admin
 * date:    2018/3/23
 * description: MovieActivity的Component
 */
@ActivityScope
@Component(modules = UploadActivityModule.class)
public interface UploadActivityComponent {
    void inject(UploadActivity uploadActivity);
}
