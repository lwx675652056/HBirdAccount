package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/10/11.
 */

public class XiaoXiBean extends BaseBean {
    private String xImgUrl;
    private String xTitle;
    private String xContent;
    private String times;
    private int nums;

    public String getxImgUrl() {
        return xImgUrl;
    }

    public void setxImgUrl(String xImgUrl) {
        this.xImgUrl = xImgUrl;
    }

    public String getxTitle() {
        return xTitle;
    }

    public void setxTitle(String xTitle) {
        this.xTitle = xTitle;
    }

    public String getxContent() {
        return xContent;
    }

    public void setxContent(String xContent) {
        this.xContent = xContent;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }
}
