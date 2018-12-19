package com.hbird.base.di.component.other;

import com.hbird.base.di.module.other.GreenDBModule;
import com.hbird.base.di.module.other.NativeDBModule;
import com.hbird.base.di.scope.DBScope;
import com.hbird.base.mvp.model.db.greendao.GreenDBManager;
import com.hbird.base.mvp.model.db.nativedao.NativeDBManager;
import com.ljy.devring.di.component.RingComponent;

import dagger.Component;

/**
 * author:  admin
 * date:    2018/3/10
 * description: GreenDao和原生数据库的Component
 */

@DBScope
@Component(modules = {GreenDBModule.class, NativeDBModule.class}, dependencies = RingComponent.class)
public interface DBComponent {

    void inject(GreenDBManager greenDbManager);

    void inject(NativeDBManager nativeDBManager);
}
