package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/10/18.
 */

public class AskFirendBean extends BaseBean {
    private String names;
    private String imgUrl;
    private String times;

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
