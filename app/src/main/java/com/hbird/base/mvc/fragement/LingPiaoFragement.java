package com.hbird.base.mvc.fragement;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.app.RingApplication;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.base.mvc.activity.AccountSafeActivity;
import com.hbird.base.mvc.activity.BudgetActivity;
import com.hbird.base.mvc.activity.CanSettingMoneyActivity;
import com.hbird.base.mvc.activity.ChooseAccountTypeActivity;
import com.hbird.base.mvc.activity.H5WebViewActivity;
import com.hbird.base.mvc.activity.PersionnalInfoActivity;
import com.hbird.base.mvc.activity.homeActivity;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.Html5Date;
import com.hbird.base.mvc.bean.ReturnBean.SaveMoney2Return;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Util;
import com.hbird.base.wxapi.WXEntryActivity;
import com.hbird.common.Constants;
import com.hbird.util.Utils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import sing.util.LogUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

import static com.sobot.chat.api.apiUtils.SobotApp.getApplicationContext;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN_CIRCLE;

/**
 * Created by Liul on 2018/10/10.
 * 领票票
 */


public class LingPiaoFragement extends BaseFragement implements View.OnClickListener {
    /* @BindView(R.id.left_title)
     TextView mLeftTitle;
     @BindView(R.id.center_title)
     TextView mCenterTitle;*/
    @BindView(R.id.webView)
    com.github.lzyzsd.jsbridge.BridgeWebView webView;
  /*  @BindView(R.id.iv_back_web)
    ImageView mBack;*/

    private String url = UrlConstants.BASE_H5_URL;

    private int mm = 1;//默认
    private int yyyy;
    private String aa = "";
    private String bb = "";
    private String ids;
    private String name;
    private int firstCome = 0;
    homeActivity activity;
    private boolean aaa;

    @Override
    public int setContentId() {
        return R.layout.fragement_lingpiao;
    }

    @Override
    public void initView() {
        String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        String version = Utils.getVersion(getActivity());
        url = url + token + "&currentVersion=" + version;
        LogUtil.e("领票票URL:---" + url);
        activity = (homeActivity) getActivity();
    }

    @Override
    public void onResume() {
        firstCome = firstCome + 1;
        if (firstCome > 1) {
            webView.loadUrl(url);
            aaa = true;
            activity.setBottomDH2Visiable();
        }
        super.onResume();
    }

    public class A extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (view.canGoBack()) {
                ToastUtil.showShort("1");
            } else {
                ToastUtil.showShort("2");
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void initData() {
        WebSettings settings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        //当前年
        yyyy = Integer.parseInt(DateUtils.getCurYear("yyyy"));
        //当前月
        String m = DateUtils.date2Str(new Date(), "MM");
        String currentMonth = m.substring(0, 2);
        mm = Integer.parseInt(currentMonth);

        webView.setDefaultHandler(new DefaultHandler());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.loadUrl(url);


        webView.registerHandler("inviteFriends", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                playVoice(R.raw.changgui02);
                ids = SPUtil.getPrefString(getActivity(), CommonTag.H5PRIMKEYIDS, "");
                name = SPUtil.getPrefString(getActivity(), CommonTag.H5PRIMKEYNAME, "");
                inviteYou();
            }
        });
        webView.registerHandler("writeANote", new BridgeHandler() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void handler(String data, CallBackFunction function) {
                //showMessage("记一笔");

                playVoice(R.raw.changgui02);
                startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
            }
        });
        webView.registerHandler("bindingNumber", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //showMessage("绑定手机号/微信号");
                String phones = SPUtil.getPrefString(getActivity(), CommonTag.H5PRIMKEYPHONE, "");
                String weiXin = SPUtil.getPrefString(getActivity(), CommonTag.H5PRIMKEYWEIXIN, "");
                playVoice(R.raw.changgui02);
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), AccountSafeActivity.class);
                intent3.putExtra("PHONE", phones);
                intent3.putExtra("WEIXIN", weiXin);
                startActivity(intent3);
            }
        });
        webView.registerHandler("setBudget", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                // showMessage("设置预算");

                playVoice(R.raw.changgui02);
                Intent intent3 = new Intent(getActivity(), BudgetActivity.class);
                intent3.putExtra("MONTH", mm + "");
                intent3.putExtra("YEAR", yyyy + "");
                String money = SPUtil.getPrefString(getActivity(), CommonTag.H5PRIMKEYMONEY, "");
                intent3.putExtra("MONEY", money);
                String accountBookId = SPUtil.getPrefString(getActivity(), CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
                intent3.putExtra("accountBookId", accountBookId);
                startActivity(intent3);
            }
        });
        webView.registerHandler("settingEfficiency", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //showMessage("设置存钱效率");
                playVoice(R.raw.changgui02);
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), CanSettingMoneyActivity.class);
                intent1.putExtra("MONTH", mm + "");
                intent1.putExtra("YEAR", yyyy + "");
                intent1.putExtra("LargeExpenditure", aa);
                intent1.putExtra("LifeExpenditure", bb);
                startActivity(intent1);

            }
        });
        webView.registerHandler("setPersonalInformation", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //showMessage("完善个人资料");
                try {
                    String b1 = SPUtil.getPrefString(getActivity(), CommonTag.H5PRIMKEYZILIAO, "");
                    playVoice(R.raw.changgui02);
                    Intent intent1 = new Intent();
                    intent1.setClass(getActivity(), PersionnalInfoActivity.class);
                    intent1.putExtra("JSON", b1);
                    if (!TextUtils.isEmpty(b1)) {
                        startActivity(intent1);
                    }

                } catch (Exception e) {
                    //请求不到数据时 点击出现的崩溃 暂不处理
                }
            }
        });
        webView.registerHandler("jumpLink", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //showMessage("轮播图");
                LogUtil.e(data);
                Html5Date bean = new Gson().fromJson(data, Html5Date.class);
                String jumpType = bean.getJumpType();
                if (TextUtils.equals("0", jumpType)) {
                    String address = bean.getConnectionAddress();

                    switch (address) {
                        case "mxsy":
                            playVoice(R.raw.changgui02);

                            activity.setTiaozhuanFragement(0, 0);
                            break;
                        case "tbtj":
                            activity.setTiaozhuanFragement(1, 0);
                            break;
                        case "tbfx":
                            activity.setTiaozhuanFragement(1, 1);
                            break;
                        case "tbzc":
                            activity.setTiaozhuanFragement(1, 2);
                            break;
                        case "jzsy":
                            playVoice(R.raw.changgui02);
                            startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
                            break;
                    }
                } else if (TextUtils.equals("1", jumpType)) {
                    String address = bean.getConnectionAddress();
                    String shareTitle = bean.getShareTitle();
                    String shareImage = bean.getShareImage();
                    String shareContent = bean.getShareContent();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), H5WebViewActivity.class);
                    intent.putExtra("URL", address);
                    intent.putExtra("TITLE", shareTitle);
                    intent.putExtra("SHAREIMAGE", shareImage);
                    intent.putExtra("SHARECONTENT", shareContent);
                    startActivity(intent);
                }

            }
        });
        webView.registerHandler("toPointsMallPage", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                activity.setBottomDH2Gone();
                aaa = false;
            }
        });
        webView.registerHandler("toCheckInCalendarPage", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                activity.setBottomDH2Gone();
                aaa = false;
            }
        });
        webView.registerHandler("toTicketDescriptionPage", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                activity.setBottomDH2Gone();
                aaa = false;
            }
        });
        webView.registerHandler("toPointrecordPage", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                activity.setBottomDH2Gone();
                aaa = false;
            }
        });
        webView.registerHandler("goBankIndex", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                activity.setBottomDH2Visiable();
                aaa = true;
            }
        });


        webView.registerHandler("confirmToExchangeRedEnvelope", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
//                String weixin = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYWEIXIN, "");
                getCode();
            }
        });

        // 日签
        webView.registerHandler("signIn", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                activity.setBottomDH2Gone();
                aaa = false;
            }
        });

        // 下载
        webView.registerHandler("downloadImg", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    Utils.decoderBase64File(data, Constants.IMAGE_PATH+ "/temp.png");
                    ToastUtil.showShort("保存成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.showShort("保存失败");
                }
            }
        });
        // 分享
        webView.registerHandler("shareImg", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    Utils.decoderBase64File(data, Constants.IMAGE_PATH+ "/temp.png");
                    playVoice(R.raw.changgui02);
                    File file = new File(Constants.IMAGE_PATH+ "/temp.png");
                    UMImage imagelocal = new UMImage(getActivity(),  file);
                    imagelocal.setThumb(new UMImage(getActivity(), file));
                    new ShareAction(getActivity()).withMedia(imagelocal )
                            .setPlatform(WEIXIN_CIRCLE)
                            .setCallback(shareListener).share();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtil.showShort("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.showShort("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtil.showShort("分享取消");
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            activity.setBottomDH2Visiable();
            aaa = true;
        }
    }

    @Override
    public void initListener() {
        //mBack.setOnClickListener(this);
        webView.setOnKeyListener(backlistener);

    }

    @Override
    public void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_web:
                webView.goBack();
                break;
        }
    }

    @Override
    public void loadDate() {
        String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        getXiaoLvNet(token);
        //如果第一次 不执行此方法 。。。。。。
        if (firstCome > 1) {
            webView.loadUrl(url);
            aaa = true;
        }
        firstCome = firstCome + 1;
    }


    private void getXiaoLvNet(String token) {
        NetWorkManager.getInstance().setContext(getActivity())
                .getSaveEfficients(mm, 3, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        SaveMoney2Return b1 = (SaveMoney2Return) b;
                        try {
                            if (null != b1.getResult()) {
                                SaveMoney2Return.ResultBean.FixedSpendBean list = b1.getResult().getFixedSpend();
                                aa = list.getFixedLargeExpenditure() + "";
                                bb = list.getFixedLifeExpenditure() + "";
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                    }
                });
    }

    private void inviteYou() {
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), com.hbird.base.app.constant.CommonTag.WEIXIN_APP_ID);
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl = "https://fengniaojizhang.com/";
        miniProgram.miniprogramType = UrlConstants.IS_RELEASE ? WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE : WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
        miniProgram.userName = "gh_84f06fbaa705";

        if (TextUtils.isEmpty(ids)) {
            return;
        }
        miniProgram.path = "pages/details/index/main?scene=" + ids;
        WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
        mediaMessage.title = name + "向你推荐简单好用的记账软件!";
        mediaMessage.description = "不乱花，更自在。Save small.Live smart.";
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.wxmini);
        Bitmap sendBitmap = Bitmap.createScaledBitmap(bitmap, 500, 400, true);
        bitmap.recycle();
        mediaMessage.thumbData = Util.bmpToByteArray(sendBitmap, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "";
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.message = mediaMessage;
        api.sendReq(req);
    }

    private View.OnKeyListener backlistener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            boolean keyBack = false;
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    String url = webView.getUrl();
                    boolean index = url.contains("index");
                    if (index) {
                        return keyBack;
                    }
                    String title = webView.getTitle();
                    if (webView.canGoBack()) {
                        if (aaa) {
                            return keyBack;
                        }
                        webView.goBack();
                        if (webView.canGoBack()) {
                            String url1 = webView.getUrl();
                            boolean index2 = url1.contains("secondarypage");
                            if (index2) {
                                aaa = true;
                                activity.setBottomDH2Visiable();
                            }
                        } else {
                            activity.setBottomDH2Visiable();
                            aaa = true;
                            keyBack = false;
                        }
                        keyBack = true;
                    } else {
                        activity.setBottomDH2Visiable();
                        aaa = true;
                        keyBack = false;
                    }
                } else {
                    activity.setBottomDH2Visiable();
                    aaa = true;
                    keyBack = false;
                }
            } else {
                activity.setBottomDH2Visiable();
                keyBack = false;
            }
            return keyBack;
        }
    };

    private void getCode() {
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
//        if (!isWeChatAppInstalled(this)) {
//            RingToast.show("您还未安装微信客户端");
//        } else {
        WXEntryActivity.setListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, Object data, int type) {
                String code = (String) data;
                LogUtil.e("code = " + code);
                uploadCode(code);
            }
        });
        SharedPreferencesUtil.put("get_weixin_code", true);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        //像微信发送请求
        RingApplication.mWxApi.sendReq(req);
//        }
    }

    private void uploadCode(String code) {
        String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        NetWorkManager.getInstance().setContext(getActivity()).uploadCode(token, code, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                String result;// 返回前端的结果
                result = "{\"result\":0}";
                webView.loadUrl("javascript:getAndroidData(" + result + ")");
                LogUtil.e("返给前端的数据" + result);
            }

            @Override
            public void onError(String s) {
                sing.util.LogUtil.e(s);
                String result = " {\"result\":1}";
                LogUtil.e("返给前端的数据" + result);
                webView.loadUrl("javascript:getAndroidData(" + result + ")");
            }
        });
    }
}
