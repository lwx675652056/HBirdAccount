package com.hbird.base.mvc.fragement;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.mvc.activity.AboutOursActivity;
import com.hbird.base.mvc.activity.AccountAlertActivity;
import com.hbird.base.mvc.activity.ActChooseAccountType;
import com.hbird.base.mvc.activity.AskFriendsActivity;
import com.hbird.base.mvc.activity.ChooseAccountTypeActivity;
import com.hbird.base.mvc.activity.NotificationMessageActivity;
import com.hbird.base.mvc.activity.PersionnalInfoActivity;
import com.hbird.base.mvc.activity.SettingsActivity;
import com.hbird.base.mvc.activity.ShaiChengJiuActivity;
import com.hbird.base.mvc.activity.SuggestFanKuiActivity;
import com.hbird.base.mvc.activity.WebViewActivity;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.CheckVersionReturn;
import com.hbird.base.mvc.bean.ReturnBean.FengMessageReturn;
import com.hbird.base.mvc.bean.ReturnBean.GeRenInfoReturn;
import com.hbird.base.mvc.bean.ReturnBean.HeaderInfoReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.SharePop;
import com.hbird.base.mvc.view.dialog.updateDialog;
import com.hbird.base.mvc.widget.DownLoadDialog;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.mvp.view.activity.login.ShouShiPasswordActivity;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.SobotUtils;
import com.hbird.base.util.Util;
import com.hbird.base.util.Utils;
import com.hbird.common.Constants;
import com.ljy.devring.DevRing;
import com.ljy.devring.image.support.GlideApp;
import com.ljy.devring.util.FileUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import sing.common.util.DownLoadUtil;
import sing.common.util.LogUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

import static java.lang.Integer.parseInt;

/**
 * Created by Liul on 2018/6/28.
 */

public class MeFragement extends BaseFragement implements View.OnClickListener {
    //    @BindView(R.id.ll_me_info)
//    LinearLayout mInfos;
    @BindView(R.id.imagess)
    com.hbird.base.mvc.widget.cycleView mHeader;
    @BindView(R.id.tv_nicheng)
    TextView mNiCheng;
    @BindView(R.id.tv_id)
    TextView mID;
    @BindView(R.id.tv_month_day)
    TextView mMonthDay;
    /*@BindView(R.id.tv_day_day)
    TextView mDay_d;*/
    @BindView(R.id.tv_account_num)
    TextView mAccountNum;
    @BindView(R.id.ll_geren)
    LinearLayout mGeRen;
    @BindView(R.id.ll_shai)
    LinearLayout mShai;
    @BindView(R.id.left_title)
    TextView mLeftTitle;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title)
    TextView mRightTitle;
    /*@BindView(R.id.ll_voice)
    LinearLayout mVoice;*/
   /* @BindView(R.id.iv_btn)
    ImageView mVoiceImg;
    @BindView(R.id.toggle_btn)
    com.hbird.base.mvc.widget.IosLikeToggleButton mToggleButton;*/
    @BindView(R.id.tv_yaoqing)
    TextView mYaoqingNum;
    @BindView(R.id.tv_wans)
    TextView mWanShan;
    @BindView(R.id.iv_shezhi)
    ImageView mSheZhi;
    /*  @BindView(R.id.iv_xiaoxi)
      ImageView mXiaoXi;*/
    @BindView(R.id.ll_kefu)
    LinearLayout mKeFu;
    @BindView(R.id.iv_biaozhi)
    ImageView mBiaozhi;
    @BindView(R.id.tv_miaoshu)
    TextView mMiaoShu;
    @BindView(R.id.ll_des)
    LinearLayout mLineDes;
    @BindView(R.id.rl_yaoqing)
    RelativeLayout mYaoQing;
    @BindView(R.id.rl_shai01)
    RelativeLayout mShai01;
    @BindView(R.id.rl_shai02)
    RelativeLayout mShai02;
    @BindView(R.id.ll_fengfeng)
    LinearLayout fengf;
    @BindView(R.id.tv_te_num)
    TextView mNum;


    private GeRenInfoReturn b1;

    private int First = 0;
    private HeaderInfoReturn b1s;
    private String token;
    private String jzDays;
    private String accountId;
    private String zongJz;
    private String nickName;
    private int inviteUsers;


    @Override
    public int setContentId() {
        return R.layout.fragement_me;
    }

    @Override
    public void initView() {
        mLeftTitle.setVisibility(View.GONE);
        mCenterTitle.setVisibility(View.VISIBLE);
        mRightTitle.setVisibility(View.GONE);
        mCenterTitle.setText("我的");
        mSheZhi.setVisibility(View.VISIBLE);
        String strs = "<font color='#000000'>" + "qiao45820" + "</font>" + " ←  欢迎粘贴微信号咨询";
        mMiaoShu.setText(Html.fromHtml(strs));
    }

    @Override
    public void initData() {
        //账本id
        accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");

    }

    @Override
    public void initListener() {
        mGeRen.setOnClickListener(this);
        mRightTitle.setOnClickListener(this);
        mSheZhi.setOnClickListener(this);
        mKeFu.setOnClickListener(this);
        mLineDes.setOnClickListener(this);
        mYaoQing.setOnClickListener(this);
        mShai01.setOnClickListener(this);
        mShai02.setOnClickListener(this);
        fengf.setOnClickListener(this);

    }

    @Override
    public void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        loads();
        getGeRenInfo();
        getMessage();
    }

    @Override
    public void loadDataForNet() {
        super.loadDataForNet();

    }

    private void getGeRenInfo() {
        //获取个人信息
        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        NetWorkManager.getInstance().setContext(getActivity())
                .getPersionalInfos(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        b1 = (GeRenInfoReturn) b;
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYPHONE, b1.getResult().getMobile());
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYWEIXIN, b1.getResult().getWechatAuth());
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYZILIAO, new Gson().toJson(b1));
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYIDS, b1.getResult().getId() + "");
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYNAME, b1.getResult().getNickName());
                        nickName = b1.getResult().getNickName();
                        if (TextUtils.isEmpty(nickName)) {
                            String mobile = b1.getResult().getMobile();
                            nickName = Utils.getHiddenPhone(mobile);
                            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.KEFUNICKNAME, "");
                            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.KEFUPHONE, nickName);
                        } else {
                            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.KEFUNICKNAME, nickName);
                            String mobile = b1.getResult().getMobile();
                            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.KEFUPHONE, mobile);
                        }
                        if (null != mNiCheng) {
                            //报空指针 只能这样了
                            mNiCheng.setText(nickName);
                        } else {
                            TextView mNiCheng = (TextView) contentView.findViewById(R.id.tv_nicheng);
                            mNiCheng.setText(nickName);
                        }
                        Double precent = b1.getResult().getIntegrity();
                        if (precent == 1) {

                            mBiaozhi.setVisibility(View.VISIBLE);
                            mWanShan.setVisibility(View.GONE);
                        } else {
                            mBiaozhi.setVisibility(View.GONE);
                            mWanShan.setVisibility(View.VISIBLE);
                            mWanShan.setText("完善度" + precent * 100 + "%");
                        }
                        mID.setText(b1.getResult().getId() + "");
                        SharedPreferencesUtil.put(Constants.FENGFENG_ID,String.valueOf(b1.getResult().getId()));// 保存峰峰id
                        //保存id 极光推送需要用到
                        //JPushInterface.setAlias(getActivity(),1,b1.getResult().getId()+"");
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.FENG_NIAO_ID, b1.getResult().getId() + "");
                        String avatarUrl = b1.getResult().getAvatarUrl();
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.KEFUIMG, avatarUrl);
                        GlideApp.with(MeFragement.this)
                                .load(avatarUrl)
                                .placeholder(R.mipmap.ic_me_normal_headr)
                                .centerCrop()
                                .error(R.mipmap.ic_me_normal_headr)
                                .override(200, 200)
                                .into(mHeader);
                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                    }
                });
    }

    private void loads() {
        int yyyy = parseInt(DateUtils.getCurYear("yyyy"));
        String mMonth = DateUtils.date2Str(new Date(), "MM");
        String s = mMonth.substring(0, 2);
        String t = yyyy + "-" + s;
        String sql = "select count(day) from (select count(charge_date2) day  from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1) where account_book_id= '" + accountId + "' AND delflag = 0 AND strftime('%Y-%m', charge_date2) = '" + t + "' group by strftime('%Y-%m-%d', charge_date2)  );";
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
            jzDays = cursor.getString(0);

        }
        String sqls = "select count(id) As countNum from WATER_ORDER_COLLECT where account_book_id = '" + accountId + "' AND delflag = 0";
        Cursor cursor1 = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sqls, null);
        if (cursor1 != null) {
            cursor1.moveToFirst();
            zongJz = cursor1.getString(0);
        }

        String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        NetWorkManager.getInstance().setContext(getActivity())
                .getHeaderInfo(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        b1s = (HeaderInfoReturn) b;
                        HeaderInfoReturn.ResultBean result = b1s.getResult();

                        if (null != result) {
                            try {
                                String daysCount = b1s.getResult().getDaysCount();
                                mMonthDay.setText(jzDays + "/" + daysCount.split("/")[1]);
                                //mDay_d.setText(b1s.getResult().getClockInDays()+"");
                                inviteUsers = result.getInviteUsers();
                                mYaoqingNum.setText(inviteUsers + "");
                                mAccountNum.setText(zongJz);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                    }
                });
    }

    @OnClick({R.id.ll_type, R.id.ll_safe_key, R.id.ll_tixing
            , R.id.ll_firent, R.id.ll_suggest, R.id.ll_checkUpDate
            , R.id.ll_gongzhong})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_title:
                //记一笔
                startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
                break;
            case R.id.iv_shezhi:
                //showMessage("设置");
                playVoice(R.raw.changgui02);
                Intent intent5 = new Intent();
                intent5.setClass(getActivity(), SettingsActivity.class);
                if (null != b1) {
                    intent5.putExtra("PHONE", b1.getResult().getMobile());
                    intent5.putExtra("WEIXIN", b1.getResult().getWechatAuth());
                }
                startActivity(intent5);
                break;
            case R.id.ll_kefu:
                playVoice(R.raw.changgui02);
                //showMessage("联系客服");
                String fengfengId = (String) SharedPreferencesUtil.get(Constants.FENGFENG_ID,"");
                if (TextUtils.isEmpty(fengfengId)){
                    ToastUtil.showShort("丰丰ID为空");
                }else{
                    SobotUtils.startSobot(getActivity());
                }
                /*Intent intent3 = new Intent();
                intent3.setClass(getActivity(), KeFuActivity.class);
                startActivity(intent3);*/
                break;
            case R.id.ll_fengfeng:
                // showMessage("丰丰通知界面");
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), NotificationMessageActivity.class);
                startActivity(intent3);
                break;
           /* case R.id.iv_xiaoxi:
                playVoice(R.raw.changgui02);
                //showMessage("消息");
                Intent intent6 = new Intent();
                intent6.setClass(getActivity(), XiaoXiNotifiyActivity.class);
                startActivity(intent6);
                break;*/

            case R.id.ll_geren: // 个人详情
                try {
                    playVoice(R.raw.changgui02);
                    Intent intent1 = new Intent();
                    intent1.setClass(getActivity(), PersionnalInfoActivity.class);
                    String json = "";
                    if (null != b1) {
                        json = new Gson().toJson(b1);
                    }
                    intent1.putExtra("JSON", json);
                    if (!TextUtils.isEmpty(json)) {
                        startActivity(intent1);
                    }
                } catch (Exception e) {
                    //请求不到数据时 点击出现的崩溃 暂不处理
                }
                break;
            case R.id.rl_yaoqing: // 邀请好友
                playVoice(R.raw.changgui02);
                Intent intents = new Intent();
                intents.setClass(getActivity(), AskFriendsActivity.class);
                intents.putExtra("IDS", b1.getResult().getId() + "");
                intents.putExtra("NAME", nickName);
                startActivity(intents);
                break;
            case R.id.rl_shai01: //晒成就
                shaiChengJiu();
                break;
            case R.id.rl_shai02: //晒成就
                shaiChengJiu();
                break;
            case R.id.ll_type: // 类别设置
                playVoice(R.raw.changgui02);
                Intent intent = new Intent(getActivity(), ActChooseAccountType.class);
                startActivity(intent);
                break;
            case R.id.ll_safe_key: // 手势密码
                playVoice(R.raw.changgui02);
                startActivity(new Intent(getActivity(), ShouShiPasswordActivity.class));
                break;
            case R.id.ll_tixing: // 记账提醒
                playVoice(R.raw.changgui02);
                startActivity(new Intent(getActivity(), AccountAlertActivity.class));
                break;
            case R.id.ll_des:
                ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText("qiao45820");
                showMessage("已将微信号复制到粘贴板，请打开微信搜索添加");
                break;
            case R.id.ll_usefo:
                //showMessage("使用手册");
                playVoice(R.raw.changgui02);
                Intent intent2 = new Intent(getActivity(), WebViewActivity.class);
                intent2.putExtra("TYPE", "shouce");
                startActivity(intent2);
                break;
            case R.id.ll_firent: // 邀请好友
                playVoice(R.raw.changgui02);
                InviteYou();
                break;
            case R.id.ll_checkUpDate:   //检查更新
                playVoice(R.raw.changgui02);
                String appVersion = Utils.getVersion(getActivity());
                if (!TextUtils.isEmpty(appVersion)) {
                    //执行更新操作 获取不到token代表当前还没登录暂时不更新  token不校验 做登录之前更新操作
                    updates(appVersion, token);
                }
                break;
            case R.id.ll_suggest:
                //showMessage("意见反馈");
                playVoice(R.raw.changgui02);
                startActivity(new Intent(getActivity(), SuggestFanKuiActivity.class));
                break;
            case R.id.ll_about_us:
                //showMessage("关于我们");
                playVoice(R.raw.changgui02);
                startActivity(new Intent(getActivity(), AboutOursActivity.class));
                break;
            case R.id.ll_gongzhong:
                playVoice(R.raw.changgui02);
                ClipboardManager cmbs = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cmbs.setText("fengniaojizhang01");
                showMessage("已将公众号复制到粘贴板，请打开微信搜索关注");
                break;
        }
    }

    private void shaiChengJiu() {
        playVoice(R.raw.changgui02);
        Intent intent4 = new Intent();
        intent4.setClass(getActivity(), ShaiChengJiuActivity.class);
        String jsons = "";
        if (null != b1s) {
            jsons = new Gson().toJson(b1s);
        }
        intent4.putExtra("JSON", jsons);
        if (null != b1) {
            intent4.putExtra("HEADYRL", b1.getResult().getAvatarUrl());
            String name = b1.getResult().getNickName();
            if (TextUtils.isEmpty(name)) {
                String mobile = b1.getResult().getMobile();
                name = Utils.getHiddenPhone(mobile);
            }
            intent4.putExtra("NAME", name);
        }
        intent4.putExtra("JZDAYS", jzDays);
        intent4.putExtra("ZONGJZ", zongJz);
        intent4.putExtra("FENXIANG", inviteUsers + "");
        startActivity(intent4);
    }

    private void popWindows() {
        new SharePop(getActivity(), new SharePop.onMyClickListener() {
            @Override
            public void setWx() {
                playVoice(R.raw.changgui02);
                UMWeb web = new UMWeb(UrlConstants.INVITE_FIRENDS_URL);
                web.setTitle("蜂鸟记账");//标题
                web.setThumb(new UMImage(getActivity(), R.mipmap.ic_logo));  //缩略图
                web.setDescription("不乱花，更自在。Save small.Live smart.");//描述

                new ShareAction(getActivity())
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(shareListener)
                        .share();
            }

            @Override
            public void setWxq() {
                playVoice(R.raw.changgui02);
                UMWeb web = new UMWeb(UrlConstants.INVITE_FIRENDS_URL);
                web.setTitle("蜂鸟记账");
                web.setThumb(new UMImage(getActivity(), R.mipmap.ic_logo));
                web.setDescription("不乱花，更自在。Save small.Live smart.");
                new ShareAction(getActivity()).withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(shareListener).share();
            }
        }).showPopWindow();
    }

    String idsmn;

    private void InviteYou() {
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), com.hbird.base.app.constant.CommonTag.WEIXIN_APP_ID);
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl = "https://fengniaojizhang.com/";
        miniProgram.miniprogramType = UrlConstants.IS_RELEASE ? WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE : WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
        miniProgram.userName = "gh_84f06fbaa705";
        if (null != b1) {
            idsmn = b1.getResult().getId() + "";
        }
        if (TextUtils.isEmpty(idsmn)) {
            return;
        }
        miniProgram.path = "pages/details/index/main?scene=" + idsmn;
        WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
        mediaMessage.title = nickName + "向你推荐简单好用的记账软件!";
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

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            //showMessage("分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showMessage("分享失败");

        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showMessage("取消分享");
        }
    };

    private void updates(final String version, String token) {
        NetWorkManager.getInstance().setContext(getActivity()).getCheckVersion(token, version, new NetWorkManager.CallBack() {

            String url;

            @Override
            public void onSuccess(BaseReturn b) {
                CheckVersionReturn b1 = (CheckVersionReturn) b;
                if (null != b1.getResult()) {
                    final CheckVersionReturn.ResultBean result = b1.getResult();
                    String ver = result.getVersion();
                    url = result.getUrl();
                    if (ver.compareTo(version) > 0) {
                        //执行下载操作
                        new updateDialog(getActivity()).builder()
                                .setMsg(result.getUpdateLog())
                                .setUpdateButton(view -> {
                                    SPUtil.setPrefString(getActivity(), CommonTag.UPDATE_URL, url);
                                    downLoad(result.getInstallStatus() == 1);
                                })
                                .setCancleButton(view -> {
                                    int status = result.getInstallStatus();
                                    if (status == 1) { //强制更新  否则退出程序
                                        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(CommonTag.GLOABLE_TOKEN, "");
                                        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                                        DevRing.activityListManager().killAll();
                                        getActivity().finish();
                                        return;
                                    }
                                }).show();
                    } else {
                        showMessage("当前已经是最新版本了");
                    }
                } else {
                    showMessage("当前已经是最新版本了");
                }
            }

            @Override
            public void onError(String s) {
                showMessage(s);
            }
        });
    }

    private void downLoad(boolean force) {
        DownLoadDialog dialog = new DownLoadDialog(getActivity());

        File mFileSave = FileUtil.getFile(FileUtil.getExternalCacheDir(getActivity()), "fengniao.apk");
        String url = SPUtil.getPrefString(getActivity(), CommonTag.UPDATE_URL, "");

        DownLoadUtil util = new DownLoadUtil(getActivity(),url,mFileSave.getPath());
        util.downLoadFile(new DownLoadUtil.Callback() {
            @Override
            public void success(String url, String localPath) {
                dialog.dismiss();
                sing.common.util.Utils.installApk(getActivity(),localPath);
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                DevRing.activityListManager().killAll();
                getActivity().finish();
            }

            @Override
            public void failure(String url, String errorMsg) {
                ToastUtil.showShort("下载失败:" + errorMsg);
                dialog.dismiss();
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                DevRing.activityListManager().killAll();
                getActivity().finish();
            }

            @Override
            public void process(long current, long total) {
                dialog.start(current,total);
                LogUtil.e("当前已下载：" + current);
            }
        });
    }

    private void getMessage() {
        String userinfoid = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        NetWorkManager.getInstance().setContext(getActivity())
                .getFengMessage(userinfoid, "0", "0", token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        FengMessageReturn b1 = (FengMessageReturn) b;
                        List<FengMessageReturn.ResultBean.MessageListBean> messageList = b1.getResult().getMessageList();
                        int unreadMessageNumber = b1.getResult().getUnreadMessageNumber();
                        if (unreadMessageNumber > 0) {
                            mNum.setVisibility(View.VISIBLE);
                            mNum.setText(unreadMessageNumber + "");
                        } else {
                            mNum.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }
}
