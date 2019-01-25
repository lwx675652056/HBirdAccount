package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.growingio.android.sdk.collection.GrowingIO;
import com.hbird.base.R;
import com.hbird.base.app.GestureUtil;
import com.hbird.base.app.RingApplication;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.event.WxLoginCodeEvent;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Utils;
import com.hbird.ui.login_register.ActLogin;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.RingToast;
import com.sobot.chat.SobotApi;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import sing.util.SharedPreferencesUtil;

import static com.hbird.base.R.color.text_468DE1;

/**
 * Created by Liul on 2018/7/5.
 * 账户安全
 */

public class AccountSafeActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.tv_wx_num)
    TextView mWxNum;
    @BindView(R.id.tv_phone_num)
    TextView mPhoneNum;
    @BindView(R.id.iv_arrows)
    ImageView mArrows;
    @BindView(R.id.ll_wx_unbind)
    LinearLayout mWxUnbind;
    @BindView(R.id.temp)
    View temp;
    @BindView(R.id.iv_arrow2)
    ImageView mArrows2;
    @BindView(R.id.tv_fangshi)
    TextView mFangshi;
    @BindView(R.id.ll_safekey)
    LinearLayout mLoginPasswords;
    @BindView(R.id.temp1)
    View temp1;
    @BindView(R.id.ll_modifi_phone)
    LinearLayout mModifiPhone;
    @BindView(R.id.temp2)
    View temp2;

    private String phone;
    private String phones;
    private String weiXin;
    private boolean hasBd = false;
    private boolean phoneClick = false;

    @org.greenrobot.eventbus.Subscribe //如果使用默认的EventBus则使用此@Subscribe
    @com.hbird.base.mvp.model.bus.support.Subscribe //如果使用RxBus则使用此@Subscribe
    public void handlerEvent(WxLoginCodeEvent event) {
        //处理事件
        String code = event.getCode();
        //调用绑定方法
        bindWeCharts(code);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_account_safe;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("账户安全");
        mRightTitle.setVisibility(View.GONE);
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //判断当前是微信登录还是手机登录 现只判断是否是手机号登录
        phones = getIntent().getStringExtra("PHONE");
        weiXin = getIntent().getStringExtra("WEIXIN");
        mLoginPasswords.setVisibility(View.VISIBLE);
        temp1.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(phones)) {
            phone = Utils.getHiddenPhone(phones);
            mPhoneNum.setText(phone);
            mModifiPhone.setVisibility(View.VISIBLE);
            temp2.setVisibility(View.VISIBLE);
            phoneClick = true;
            mFangshi.setText("当前登录方式（手机号）");
        } else {
            mPhoneNum.setText("去绑定");
            mPhoneNum.setTextColor(getResources().getColor(text_468DE1));
            mArrows.setVisibility(View.VISIBLE);
            mModifiPhone.setVisibility(View.GONE);
            temp2.setVisibility(View.GONE);
            phoneClick = false;
        }
        if (TextUtils.isEmpty(weiXin)) {
            hasBd = false;
            //doWeChatLogin();

        } else {
            mFangshi.setText("当前登录方式（微信）");
            mWxNum.setText("已绑定");
            mWxNum.setTextColor(getResources().getColor(R.color.text_808080));
            mArrows2.setVisibility(View.GONE);
            hasBd = true;
            mLoginPasswords.setVisibility(View.GONE);
            temp1.setVisibility(View.GONE);
        }

        // 是微信登录，且有绑定手机号
        if (!TextUtils.isEmpty(phones) && !TextUtils.isEmpty(weiXin)) {
            mWxUnbind.setVisibility(View.VISIBLE);
            temp.setVisibility(View.VISIBLE);
            String methods = SPUtil.getPrefString(AccountSafeActivity.this, CommonTag.CURRENT_LOGIN_METHOD, "sj");
            String ss = "手机号";
            if (TextUtils.equals(methods, "wx")) {
                ss = "微信";
                mLoginPasswords.setVisibility(View.GONE);
                temp1.setVisibility(View.GONE);
            }
            mFangshi.setText("当前登录方式（" + ss + ")");
        } else {
            mWxUnbind.setVisibility(View.GONE);
            temp.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
    }

    @OnClick({R.id.ll_phone, R.id.ll_weixin, R.id.ll_safekey, R.id.ll_modifi_phone
            , R.id.rl_bottom_btn, R.id.ll_wx_unbind})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.ll_phone: // 手机号
                playVoice(R.raw.changgui02);
                if (phoneClick) {
                    return;
                }
                Intent intent1 = new Intent(this, BindingActivity.class);
                startActivityForResult(intent1, 303);
                break;
            case R.id.ll_weixin:// 微信,判断微信当前的状态 处于已绑定状态时 则没有点击事件`
                if (hasBd) {
                    return;
                }
                playVoice(R.raw.changgui02);
                //先拿到微信code码
                doWeChatLogin();
                //startActivity(new Intent(this,RegisterViewActivity.class));
                break;
            case R.id.ll_safekey:// 修改登录密码
                playVoice(R.raw.changgui02);
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.ll_modifi_phone:// 修改手机号
                playVoice(R.raw.changgui02);
                Intent intent = new Intent();
                intent.setClass(this, BindingModifiActivity.class);
                intent.putExtra("PHONE", phones);
                startActivity(intent);
                break;
            case R.id.ll_wx_unbind:
                playVoice(R.raw.changgui02);
                String token = SPUtil.getPrefString(this, com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
                //解除绑定微信号
                showProgress("");
                NetWorkManager.getInstance().setContext(this).unBindWeiXin(token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                hideProgress();
                                //成功解除微信
                                mWxNum.setText("去绑定");
                                mWxNum.setTextColor(getResources().getColor(R.color.text_468DE1));
                                mArrows2.setVisibility(View.VISIBLE);
                                hasBd = false;
                                mWxUnbind.setVisibility(View.GONE);
                                temp.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(String s) {
                                hideProgress();
                                showMessage(s);
                            }
                        });
                break;
            case R.id.rl_bottom_btn:
                playVoice(R.raw.changgui02);
                //客服功能注销
                SobotApi.exitSobotChat(AccountSafeActivity.this);
                //退出登录  友盟统计登出
                MobclickAgent.onProfileSignOff();
                // 清除登录用户ID API（growingIo统计 登出）
                GrowingIO.getInstance().clearUserId();
                //清空本地缓存token
                DevRing.cacheManager().spCache(CommonTag.SPCACH).put(CommonTag.GLOABLE_TOKEN, "");
                DevRing.cacheManager().spCache(CommonTag.SPCACH).put(CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                //
                SPUtil.setPrefString(AccountSafeActivity.this, CommonTag.GLOABLE_TOKEN, "");
                SPUtil.setPrefString(AccountSafeActivity.this, CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                //
                //清空手势密码
                GestureUtil.clearGesturePassword();
                //关闭手势密码开关（必须重新打开设置）
                DevRing.cacheManager().spCache(CommonTag.SPCACH).put(CommonTag.SHOUSHI_PASSWORD_OPENED, false);

                SharedPreferencesUtil.put("get_weixin_code",false);

                //杀掉所有Activity，返回打开登录界面
                DevRing.activityListManager().killAll();
                startActivity(new Intent(getApplicationContext(), ActLogin.class));
                break;
        }
    }

    private void bindWeCharts(String code) {
        String token = SPUtil.getPrefString(this, com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        NetWorkManager.getInstance().setContext(this)
                .bindWeiXin(code, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        showMessage("绑定成功");
                        mWxNum.setText("已绑定");
                        mWxNum.setTextColor(getResources().getColor(R.color.text_808080));
                        mArrows2.setVisibility(View.GONE);
                        hasBd = true;
                        mWxUnbind.setVisibility(View.VISIBLE);
                        temp.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });
    }

    private void doWeChatLogin() {
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (!ActLogin.isWeChatAppInstalled(this)) {
            RingToast.show("您还未安装微信客户端");
        } else {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "diandi_wx_login";
            //像微信发送请求
            RingApplication.mWxApi.sendReq(req);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 303 && resultCode == 302) {
            //说明微信登录状态下 手机号绑定成功 更新UI
            String phone = data.getStringExtra("PHONE");
            String mobile = Utils.getHiddenPhone(phone);
            mPhoneNum.setText(mobile);
            mPhoneNum.setTextColor(getResources().getColor(R.color.text_808080));
            mArrows.setVisibility(View.GONE);
            phoneClick = true;
        }
    }
}
