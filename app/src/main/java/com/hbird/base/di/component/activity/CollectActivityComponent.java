package com.hbird.base.di.component.activity;

import com.hbird.base.di.module.activity.CollectActivityModule;
import com.hbird.base.mvp.view.activity.CollectActivity;
import com.ljy.devring.di.scope.ActivityScope;

import dagger.Component;

/**
 * author:  admin
 * date:    2018/3/21
 * description: CollectActivity的Component
 */
@ActivityScope
@Component(modules = CollectActivityModule.class)
public interface CollectActivityComponent {
    void inject(CollectActivity collectActivity);
}
