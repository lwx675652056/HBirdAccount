package com.hbird.base.mvp.model;

import com.hbird.base.mvp.model.entity.event.CollectCountEvent;
import com.hbird.base.mvp.model.entity.table.MovieCollect;
import com.hbird.base.mvp.model.imodel.ICollectModel;
import com.ljy.devring.DevRing;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


public class CollectModel implements ICollectModel {


    @Override
    public Observable getAllCollect() {
        //Observable RXjava用法 先创建Observable对象 然后通过subscribe()订阅
        return Observable.create(new ObservableOnSubscribe<List<MovieCollect>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MovieCollect>> emitter) throws Exception {
                List<MovieCollect> list = DevRing.tableManager(MovieCollect.class).loadAll();
                emitter.onNext(list);
            }
        });
    }


    @Override
    public void deleteFromMyCollect(MovieCollect movieCollect) {
        DevRing.tableManager(MovieCollect.class).deleteOne(movieCollect);
    }


    @Override
    public int getCollectCount() {
        return (int) DevRing.tableManager(MovieCollect.class).count();
    }

    @Override
    public void updateMenuCollectCount() {
        DevRing.busManager().postEvent(new CollectCountEvent(getCollectCount()));
    }
}
