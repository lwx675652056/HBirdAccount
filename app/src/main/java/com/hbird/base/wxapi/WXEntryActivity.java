package com.hbird.base.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hbird.base.app.RingApplication;
import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.base.mvc.activity.AccountSafeActivity;
import com.hbird.base.mvp.event.WxLoginCodeEvent;
import com.hbird.base.mvp.model.http.UserApiService;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.RingToast;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sing.util.LogUtil;
import sing.util.SharedPreferencesUtil;

/**
 * WXEntryActivity是一个Activity，用来接收微信的响应信息。这里有几个需要注意的地方：
 * <p>
 * 它必须在"包名.wxapi"这个包下，如：你的应用包名为：com.xx.test，
 * 则WXEntryActivity所在的包名必须为com.xx.test.wxapi。
 * 这里和签名一样，很重要，你如果名字错了，或者包名的位置错了，
 * 都是不能回调的，切记
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    String strWeixinUrl = "https://api.weixin.qq.com/sns/";
    static UserApiService userApiService = null;
    Context context;

    public WXEntryActivity() {
        //
        if (RingApplication.wxetrofit == null) {
            RingApplication.wxetrofit = new Retrofit.Builder()
                    .baseUrl(strWeixinUrl)
                    .build();
        }
        if (RingApplication.wxService == null) {
            RingApplication.wxService = RingApplication.wxetrofit.create(WeixinService.class);
        }

        if (RingApplication.appRetrofit == null) {
            RingApplication.appRetrofit = new Retrofit.Builder()
                    .baseUrl(UrlConstants.PLATFORM_CONFIG_URL)
                    .build();
        }

        if (userApiService == null) {
            userApiService = RingApplication.appRetrofit.create(UserApiService.class);
        }

    }

    private static OnItemClickListener listener;

    public static void setListener(OnItemClickListener l) {
        listener = l;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //如果没回调onResp，八成是这句没有写
        RingApplication.mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        RingApplication.mWxApi.handleIntent(intent, this);//必须调用此句话
    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq req) {
    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {

        int type = resp.getType();//回调类型 1是微信登录
        if (type == 2) {
            //微信分享结果
            int errCode = resp.errCode;
            if (errCode == 0) {
                RingToast.show("分享成功");
            } else if (errCode == -2) {
                RingToast.show("取消分享");
            }
            finish();
            return;
        }

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                //RingToast.show("ERR_OK");
                if (sendResp != null) {
                    String code = sendResp.code;
                    LogUtil.e(code);
                    boolean getCode = (boolean) SharedPreferencesUtil.get("get_weixin_code", false);
                    if (getCode) {
                        if (listener != null) {
                            listener.onClick(0, code, 0);
                        }

                        SharedPreferencesUtil.put("get_weixin_code", false);// 标记为不是获取code，每次获取都设置
                        finish();
                        return;
                    } else {
                        Activity activity = DevRing.activityListManager().findActivity(AccountSafeActivity.class);
                        if (activity != null) {
                            //账户安全中 绑定微信时的判断流程
                            DevRing.busManager().postEvent(new WxLoginCodeEvent(code));
                            finish();
                            return;
                        }

                        if (listener != null) {
                            listener.onClick(0, code, 0);
                        }
                        finish();
                        return;
                    }
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //RingToast.show("ERR_USER_CANCEL");
                //发送取消
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //RingToast.show("ERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                //RingToast.show("default");
                //发送返回
                break;
        }
    }

    public interface WeixinService {

        /**
         * 获取openid accessToken值用于后期操作
         *
         * @param appid
         * @param secret
         * @param code
         * @return
         */
        @GET("oauth2/access_token")
        Call<ResponseBody> getAccess_token(@Query("appid") String appid,
                                           @Query("secret") String secret,
                                           @Query("code") String code,
                                           @Query("grant_type") String grant_type);

        /**
         * 获取个人信息
         *
         * @param token
         * @param openid
         * @return String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
         * + access_token
         * + "&openid="
         * + openid;
         */
        @GET("userinfo")
        Call<ResponseBody> getUserInof(@Query("access_token") String token, @Query("openid") String openid);
    }
}
