package com.hbird.base.mvc.fragement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.Html5Date;
import com.hbird.base.mvc.bean.ReturnBean.SaveMoney2Return;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.InvitationDialog;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Util;
import com.hbird.base.wxapi.WXEntryActivity;
import com.hbird.common.Constants;
import com.hbird.ui.MainActivity;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
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
import sing.common.util.StatusBarUtil;
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

    @BindView(R.id.web_view)
    com.github.lzyzsd.jsbridge.BridgeWebView webView;

    private String url = UrlConstants.BASE_H5_URL;

    private int mm = 1;//默认
    private int yyyy;
    private String aa = "";
    private String bb = "";
    private String ids;
    private String name;
    private MainActivity activity;

    private boolean isIndex = true;// 是否在首页

    @Override
    public int setContentId() {
        return R.layout.act_get_ticket;
    }

    @Override
    public void initView() {
        LinearLayout ll = getActivity().findViewById(R.id.ll_parent);
        ll.setPadding(0, StatusBarUtil.getStateBarHeight(getActivity()), 0, 0);

        String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        String version = Utils.getVersion(getActivity());
        url = url + token + "&currentVersion=" + version;
        LogUtil.e("领票票URL:---" + url);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            toSencoud(false);

            StatusBarUtil.clearStatusBarDarkMode(getActivity().getWindow()); // 导航栏白色字体

            String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
            getXiaoLvNet(token);
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

        // 邀请好友
        webView.registerHandler("inviteFriends", (data, function) -> showDialog());
        // 记一笔
        webView.registerHandler("writeANote", (data, function) -> {
            playVoice(R.raw.changgui02);
            startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
        });
        // 绑定手机号/微信号
        webView.registerHandler("bindingNumber", (data, function) -> {
            String phones = SPUtil.getPrefString(getActivity(), CommonTag.H5PRIMKEYPHONE, "");
            String weiXin = SPUtil.getPrefString(getActivity(), CommonTag.H5PRIMKEYWEIXIN, "");
            playVoice(R.raw.changgui02);
            Intent intent3 = new Intent();
            intent3.setClass(getActivity(), AccountSafeActivity.class);
            intent3.putExtra("PHONE", phones);
            intent3.putExtra("WEIXIN", weiXin);
            startActivity(intent3);
        });
        // 设置预算
        webView.registerHandler("setBudget", (data, function) -> {
            playVoice(R.raw.changgui02);
            Intent intent3 = new Intent(getActivity(), BudgetActivity.class);
            intent3.putExtra("MONTH", mm + "");
            intent3.putExtra("YEAR", yyyy + "");
            String money = SPUtil.getPrefString(getActivity(), CommonTag.H5PRIMKEYMONEY, "");
            intent3.putExtra("MONEY", money);
            String accountBookId = SPUtil.getPrefString(getActivity(), CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
            intent3.putExtra("accountBookId", accountBookId);
            startActivity(intent3);
        });
        // 设置存钱效率
        webView.registerHandler("settingEfficiency", (data, function) -> {
            playVoice(R.raw.changgui02);
            Intent intent1 = new Intent();
            intent1.setClass(getActivity(), CanSettingMoneyActivity.class);
            intent1.putExtra("MONTH", mm + "");
            intent1.putExtra("YEAR", yyyy + "");
            intent1.putExtra("LargeExpenditure", aa);
            intent1.putExtra("LifeExpenditure", bb);
            startActivity(intent1);
        });
        // 完善个人资料
        webView.registerHandler("setPersonalInformation", (data, function) -> {
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
        });
        // 轮播图
        webView.registerHandler("jumpLink", (data, function) -> {
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

        });

        // 积分商城
        webView.registerHandler("toPointsMallPage", (data, function) -> toSencoud(true));

        // 查看签到情况
        webView.registerHandler("toCheckInCalendarPage", (data, function) -> toSencoud(true));
        // 丰丰票说明
        webView.registerHandler("toTicketDescriptionPage", (data, function) -> toSencoud(true));
        // 积分记录
        webView.registerHandler("toPointrecordPage", (data, function) -> toSencoud(true));
        // 回到首页
        webView.registerHandler("goBankIndex", (data, function) -> toSencoud(false));

        // 日签
        webView.registerHandler("signIn", (data, function) -> toSencoud(true));

        // 获取微信code
        webView.registerHandler("confirmToExchangeRedEnvelope", (data, function) -> getCode());

        // 下载
        webView.registerHandler("downloadImg", (data, function) -> {
            try {
                Utils.decoderBase64File(data, Constants.IMAGE_PATH + "/temp.png");
                ToastUtil.showShort("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showShort("保存失败");
            }
        });
        // 分享
        webView.registerHandler("shareImg", (data, function) -> {
            try {
                Utils.decoderBase64File(data, Constants.IMAGE_PATH + "/temp.png");
                playVoice(R.raw.changgui02);
                File file = new File(Constants.IMAGE_PATH + "/temp.png");
                UMImage imagelocal = new UMImage(getActivity(), file);
                imagelocal.setThumb(new UMImage(getActivity(), file));
                new ShareAction(getActivity()).withMedia(imagelocal).setPlatform(WEIXIN_CIRCLE).setCallback(shareListener).share();
            } catch (Exception e) {
                e.printStackTrace();
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
    public void initListener() {
    }

    @Override
    public void loadData() {
    }

    // 邀请码弹框
    private void showDialog() {
        playVoice(R.raw.changgui02);
        InvitationDialog dialog = new InvitationDialog(getActivity());
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_web:
                webView.goBack();
                break;
        }
    }

    private void getXiaoLvNet(String token) {
        NetWorkManager.getInstance().setContext(getActivity()).getSaveEfficients(mm, 3, token, new NetWorkManager.CallBack() {
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

    // 邀请好友
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

    private void toSencoud(boolean isSencoud) {
        LogUtil.e("URL：" + webView.getUrl());
        if (isSencoud) {// 二级页面
            activity.setBottomDH2Gone();
            isIndex = false;
        } else {// 首页
            activity.setBottomDH2Visiable();
            isIndex = true;
        }
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (!isIndex){
            if (webView.canGoBack()){
                webView.goBack();
                isIndex = !webView.canGoBack();// 还能后退就说明是二级页面
            }else{
                isIndex = true;
            }
        }else{// 当前在首页
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                activity.finish();
                DevRing.activityListManager().exitApp();
            }
        }

        toSencoud(!isIndex);
    }
}