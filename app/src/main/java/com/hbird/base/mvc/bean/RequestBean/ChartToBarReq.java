package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/7/16.
 */

public class ChartToBarReq extends BaseBean {
    private String flag;
    private String beginTime;
    private String endTime;
    private String beginWeek;
    private String endWeek;

    private String dayTime;//日统计参数
    private String time;//周/月统计参数


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBeginWeek() {
        return beginWeek;
    }

    public void setBeginWeek(String beginWeek) {
        this.beginWeek = beginWeek;
    }

    public String getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(String endWeek) {
        this.endWeek = endWeek;
    }
}
