package com.hbird.base.mvp.event;


public class WxLoginEvent {

    public WxLoginEvent(String strResult){
        this.strResult = strResult;
    }

    private String strResult ;

    public String getStatus() {
        return this.strResult;
    }

    public void setStatus(String strResult) {
        this.strResult = strResult;
    }

}
