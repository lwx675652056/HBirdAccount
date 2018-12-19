package com.hbird.base.mvp.model;

import com.hbird.base.mvp.model.entity.event.CollectCountEvent;
import com.hbird.base.mvp.model.entity.table.MovieCollect;
import com.hbird.base.mvp.model.http.MovieApiService;
import com.hbird.base.mvp.model.imodel.IMovieMoel;
import com.ljy.devring.DevRing;

import io.reactivex.Observable;


public class MovieModel implements IMovieMoel{

    /**
     * 获取正在上映的电影
     *
     * @param start            请求的起始点
     * @param count            获取的电影数量
     */
    @Override
    public Observable getPlayingMovie(int start,int count) {
        return DevRing.httpManager().getService(MovieApiService.class).getPlayingMovie(start, count);
    }

    /**
     * 获取即将上映的电影
     *
     * @param start            请求的起始点
     * @param count            获取的电影数量
     */
    @Override
    public Observable getCommingMovie(int start,int count) {
        return DevRing.httpManager().getService(MovieApiService.class).getCommingMovie(start, count);
    }

    @Override
    public void addToMyCollect(MovieCollect movieCollect) {
        DevRing.tableManager(MovieCollect.class).insertOrReplaceOne(movieCollect);
    }

    @Override
    public void updateMenuCollectCount() {
        int count = (int) DevRing.tableManager(MovieCollect.class).count();
        DevRing.busManager().postEvent(new CollectCountEvent(count));
    }

}
