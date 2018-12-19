package com.hbird.base.di.component.fragment;

import com.hbird.base.di.module.fragment.MovieFragmentModule;
import com.hbird.base.mvp.view.fragment.MovieFragment;
import com.ljy.devring.di.scope.FragmentScope;

import dagger.Component;

/**
 * author:  admin
 * date:    2018/3/21
 * description: MovieFragment的Component
 */
@FragmentScope
@Component(modules = MovieFragmentModule.class)
public interface MovieFragmentComponent {
    void inject(MovieFragment movieFragment);
}
