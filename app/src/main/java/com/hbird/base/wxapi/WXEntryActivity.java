package com.hbird.base.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbird.base.app.RingApplication;
import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.base.mvc.activity.AccountSafeActivity;
import com.hbird.base.mvp.event.WxLoginCodeEvent;
import com.hbird.base.mvp.event.WxLoginEvent;
import com.hbird.base.mvp.model.http.UserApiService;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.RingToast;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sing.common.util.LogUtil;
import sing.util.SharedPreferencesUtil;

/**
 * WXEntryActivity是一个Activity，用来接收微信的响应信息。这里有几个需要注意的地方：

 它必须在"包名.wxapi"这个包下，如：你的应用包名为：com.xx.test，
 则WXEntryActivity所在的包名必须为com.xx.test.wxapi。
 这里和签名一样，很重要，你如果名字错了，或者包名的位置错了，
 都是不能回调的，切记
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
    public static void setListener(OnItemClickListener l){
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
                    boolean getCode = (boolean) SharedPreferencesUtil.get("get_weixin_code",false);
                    if (getCode){
                        if (listener != null){
                            listener.onClick(0,code,0);
                        }

                        SharedPreferencesUtil.put("get_weixin_code",false);// 标记为不是获取code，每次获取都设置
                        finish();
                        return;
                    }else{
                        Activity activity = DevRing.activityListManager().findActivity(AccountSafeActivity.class);
                        if(activity!=null){
                            //账户安全中 绑定微信时的判断流程
                            DevRing.busManager().postEvent(new WxLoginCodeEvent(code));
                            finish();
                            return;
                        }
                        //执行微信登录
                        getAccessToken(code);
                        //loginByWeChat(code);
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
         * @param token
         * @param openid
         * @return
         * String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
        + access_token
        + "&openid="
        + openid;
         */
        @GET("userinfo")
        Call<ResponseBody> getUserInof(@Query("access_token") String token, @Query("openid") String openid);

    }

    private void getAccessToken(String code) {
        String deviceId = Utils.getDeviceInfo(this);
        LogUtil.e(deviceId);
        String channelName =getChannelName();
        String mobileType = android.os.Build.MODEL;
        String jsonInfo = "{\"code\":\"" + code + "\", \"mobileDevice\":\"" + deviceId + "\", \"mobileManufacturer\":\"" + mobileType + "\", \"androidChannel\":\"" + channelName + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Call<ResponseBody> call = userApiService.loginByWeChat(body);
        //
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String strJson = null;
                try {
                    //{"code":"200","msg":"success","result":{"expire":"2592000","X-AUTH-TOKEN":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNTUwMTIzMzc3MCIsInN1YiI6IjE1NTAxMjMzNzcwIiwiaWF0IjoxNTMwMDAxODUxfQ.ZTpgbPp0tRWa7D_ViQ1XjEtMA83AtaIIGTdpqgZSjHw"}}
                    strJson = response.body().string();
                    JSONObject jsonObj = JSON.parseObject(strJson);
                    String strCode   = jsonObj.getString("code");
                    String strMsg    = jsonObj.getString("msg");
                    String strResult = jsonObj.getString("result");
                    LogUtil.e(strJson);
                    //登录成功
                    if (strCode.equals("200")) {
                        DevRing.busManager().postEvent(new WxLoginEvent(strResult));
                    }
                    else{
                        RingToast.show(strMsg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                RingToast.show("登录失败");
                t.printStackTrace();
                finish();
            }
        });
    }

//    private void loginByWeChat(String code){
//        Call<ResponseBody> call = RingApplication.wxService.getAccess_token(CommonTag.WEIXIN_APP_ID, CommonTag.APP_SECRET, code,"authorization_code");
//        //
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    String strResponses =response.body().string();
//                    try {
//                        JSONObject jsonObject = JSONObject.parseObject(strResponses);
//                        //
//                        String access_token = jsonObject.getString("access_token").toString().trim();
//                        String openid = jsonObject.getString("openid").toString().trim();
//                        //
//                        //getUserInof(access_token, openid);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                RingToast.show("登录失败");
//                t.printStackTrace();
//            }
//        });
//    }
//
//    /**
//     * 获取微信的个人信息
//     * @param strAccess_token
//     * @param strOpenid
//     */
//    private void getUserInof(final String strAccess_token, final String strOpenid){
//
//        Call<ResponseBody> call = RingApplication.wxService.getUserInof(strAccess_token, strOpenid);
//        //
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//
//                    /*
//                    {
//                    "openid": "oyKdssyO1mojkjkjqMU9H_i8923g7d8",
//                    "nickname": "大白菜",
//                    "sex": 1,
//                    "language": "zh_CN",
//                    "city": "Haidian",
//                    "province": "Beijing",
//                    "country": "CN",
//                    "headimgurl": "http:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/DYAIOgq83epibLzNRmobZKzsrJCNibyRdafsdfa8iagCHjadfasdfasfeN9sMlj72kJaqib826iax10IgUJJyUupEbQ\/132",
//                    "privilege": [],
//                    "unionid": "omJ3M0fMUdqE3ljgJHJHJDLDDD"
//                    }
//                    * */
//                    String strResponses = response.body().string();
//                    String openid = null;
//                    String nickName = null;
//                    String sex = null;
//                    String city = null;
//                    String province = null;
//                    String country = null;
//                    String headimgurl = null;
//                    String unionid = null;
//                    RingToast.show(strResponses);
//                    try {
//                        JSONObject jsonObject = JSONObject.parseObject(strResponses);
//                        openid = jsonObject.getString("openid");
//                        nickName = jsonObject.getString("nickname");
//                        sex = jsonObject.getString("sex");
//                        city = jsonObject.getString("city");
//                        province = jsonObject.getString("province");
//                        country = jsonObject.getString("country");
//                        headimgurl = jsonObject.getString("headimgurl");
//                        unionid = jsonObject.getString("unionid");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                //Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                t.printStackTrace();
//            }
//        });
//    }

    public  String getChannelName() {
        if (getApplicationContext() == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.
                        getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelName;
    }
}
