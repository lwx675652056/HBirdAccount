package com.hbird.base.app;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.growingio.android.sdk.collection.Configuration;
import com.growingio.android.sdk.collection.GrowingIO;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.mvp.model.bus.RxBusManager;
import com.hbird.base.mvp.model.db.greendao.GreenDBManager;
import com.hbird.base.util.SPUtil;
import com.hbird.base.wxapi.WXEntryActivity;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.FileUtil;
import com.sobot.chat.SobotApi;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.xiaomi.mipush.sdk.MiPushClient;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Retrofit;
import sing.util.LogUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * author:  admin
 * date:    2018/6/28
 * description: 做全局初始化操作
 *
 */

public class RingApplication extends Application {

    private static Context context;

    //微信API
    public static IWXAPI mWxApi;
    //
    public static Retrofit wxetrofit = null;
    public static WXEntryActivity.WeixinService wxService = null;
    //
    public static Retrofit appRetrofit = null;

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.init(true,"aaa1");
        ToastUtil.init(this);
        SharedPreferencesUtil.init(this,"ap_data");

//        Utils.init(this);
//        if (Utils.isAppDebug()) {
//            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
//            ARouter.openDebug();
//            ARouter.openLog();
//        }
//        ARouter.init(this);

        //获取Context
        context = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);

        //友盟分享
        UMConfigure.init(this,"5b46fa2ca40fa339f400011d","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        PlatformConfig.setWeixin(CommonTag.WEIXIN_APP_ID, CommonTag.APP_SECRET);
        UMConfigure.setLogEnabled(true);

//        MobclickAgent.setDebugMode(true);
//        MobclickAgent.setCatchUncaughtExceptions(true);
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        DevRing.init(this);


        //网络请求模块
        DevRing.configureHttp()//配置retrofit
                .setBaseUrl(UrlConstants.PLATFORM_CONFIG_URL)
                .setConnectTimeout(15)
                .setIsUseLog(false);// 打包时关闭

        //图片加载模块
        DevRing.configureImage()//配置默认的Glide
                .setLoadingResId(R.mipmap.ic_image_load)
                .setErrorResId(R.mipmap.ic_image_load)
                .setIsShowTransition(true)
                .setDiskCacheFile(FileUtil.getDirectory(FileUtil.getExternalCacheDir(this), "test_img_cache"))
                .setIsDiskCacheExternal(true);

        DevRing.configureBus(new RxBusManager());
        DevRing.configureDB(new GreenDBManager());//传入GreenDao数据库的管理者

        //缓存模块
        DevRing.configureCache()//配置缓存
                .setDiskCacheFolder(FileUtil.getDirectory(FileUtil.getExternalCacheDir(this), "test_disk_cache"));


        //其他模块
        DevRing.configureOther()//配置其他
                .setIsUseCrashDiary(true)
                .setIsShowRingLog(true);//设置是否显示Ringlog打印的内容，默认true     false:不显示

        DevRing.create();

        registerToWX();
        GrowingIO.startWithConfiguration(this, new Configuration()
                .trackAllFragments()
                .setDebugMode(false)
                .setTestMode(false)
                .setChannel("XXX应用商店")
        );
        /**
         * 初始化sdk  (智齿 客服功能)
         * @param context 上下文  必填
         * @param appkey  用户的appkey  必填 如果是平台版用户需要传总公司的appkey
         * @param uid     用户的唯一标识，不能传一样的值，可以为空
         */
        SobotApi.initSobotSDK(context, "615a726d25e941309939e5ce3a6d3d89", "");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        String ss = SPUtil.getPrefString(getApplicationContext(), CommonTag.FENG_NIAO_ID, "");
        JPushInterface.setAlias(getApplicationContext(),1,ss);
        MiPushClient.getRegId(getApplicationContext());

        // 打包时设置为false  控制日志
        CrashReport.initCrashReport(getApplicationContext(), "bd239c9a7f", true);
    }

    public static Context getContextObject(){
        return context;
    }

    private void registerToWX() {
        mWxApi = WXAPIFactory.createWXAPI(this, CommonTag.WEIXIN_APP_ID, true);
        mWxApi.registerApp(CommonTag.WEIXIN_APP_ID);
    }
    // 创建服务用于捕获崩溃异常
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            MobclickAgent.reportError(context, ex);
            MobclickAgent.onKillProcess(mContext);
        }
    };

}
