package com.hbird.base.di.component.activity;

import com.hbird.base.di.module.activity.MovieActivityModule;
import com.hbird.base.mvp.view.activity.MovieActivity;
import com.ljy.devring.di.scope.ActivityScope;

import dagger.Component;

/**
 * author:  admin
 * date:    2018/3/21
 * description: MovieActivity的Component
 */
@ActivityScope
@Component(modules = MovieActivityModule.class)
public interface MovieActivityComponent {
    void inject(MovieActivity movieActivity);
}
