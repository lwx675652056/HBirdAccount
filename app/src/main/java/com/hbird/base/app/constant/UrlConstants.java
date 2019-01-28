package com.hbird.base.app.constant;

/**
 * author:  Liul
 * date:    2017/9/25
 * description: 网络地址常量类
 */

public interface UrlConstants {
    boolean IS_RELEASE = false;//是否正式环境运行 true为正式，分享到小程序用到了

//    String PLATFORM_CONFIG_URL = "https://api.fengniaojizhang.com/rest/api/v1/";//生产环境
//    String BASE_H5_URL ="https://api.fengniaojizhang.com/h5/index.html?token=";//正式领票H5

    String PLATFORM_CONFIG_URL = "https://api.galaxyhouse.cn/rest/api/v1/";//测试环境
    String BASE_H5_URL ="http://api.galaxyhouse.cn:8203?token=";//测试 领票H5

//    String PLATFORM_CONFIG_URL = "https://api.fengniaojizhang.cn/rest/api/v1/";//灰度环境
//    String BASE_H5_URL ="http://api.fengniaojizhang.cn:8089?token=";//灰度 领票H5


//    String PLATFORM_CONFIG_URL = "http://192.168.3.221:8080/jeecg/rest/api/v1/";//测试环境
//    String BASE_H5_URL ="http://192.168.2.112:8085?token=";//测试 领票H5
//    String PLATFORM_CONFIG_URL = "https://wxa.galaxyhouse.cn/rest/api/v1/";//测试环境

    public static boolean LOG_BUTTON = !IS_RELEASE;

    //使用手册
    String USER_HAND_BOOK = "https://api.fengniaojizhang.com/staticPages/manual.html";
    //邀请好友
    String INVITE_FIRENDS_URL="https://api.fengniaojizhang.com/staticPages/inviteFriends.html";
    //使用协议
    String USE_AGREEMENT_URL="https://api.fengniaojizhang.com/staticPages/useAgreement.html";

    String GET_PLAYING_MOVIE = "v2/movie/in_theaters";
    String GET_COMMING_MOVIE = "v2/movie/coming_soon";

    String UPLOAD = "http://upload.qiniu.com/";
    String DOWNLOAD = "http://ucan.25pp.com/Wandoujia_web_seo_baidu_homepage.apk";
}
