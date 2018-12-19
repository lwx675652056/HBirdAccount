package com.hbird.base.mvp.view.iview;

import com.hbird.base.mvp.model.entity.res.MovieRes;
import com.hbird.base.mvp.view.iview.base.IBaseView;

import java.util.List;

/**
 * author:  admin
 * date:    2017/9/27
 * description:
 */

public interface IMovieView extends IBaseView {
    void getMovieSuccess(List<MovieRes> list, int type);

    void getMovieFail(int status, String desc, int type);

}
