package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;

import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;

/**
 * Created by Liul on 2018/7/12.
 */

public class WebViewActivity extends BaseActivity<BaseActivityPresenter> {
    @BindView(R.id.wb)
    WebView mWb;
    private String url;
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mTitle2;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String type = getIntent().getStringExtra("TYPE");
        if (TextUtils.equals(type, "shouce")) {
            url = UrlConstants.USER_HAND_BOOK;
            mTitle2.setVisibility(View.GONE);
            mCenterTitle.setText("使用手册");
        } else if (TextUtils.equals(type, "firend")) {
            url = UrlConstants.INVITE_FIRENDS_URL;
            mTitle2.setText("分享");
            mCenterTitle.setText("邀请好友");
        } else if (TextUtils.equals(type, "zichan")) {
            url = UrlConstants.USE_AGREEMENT_URL;
            mTitle2.setVisibility(View.GONE);
            mCenterTitle.setText("资产说明");
        } else {
            url = UrlConstants.USE_AGREEMENT_URL;
            mTitle2.setVisibility(View.GONE);
            mCenterTitle.setText("使用协议");
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        WebSettings s = mWb.getSettings();
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setJavaScriptEnabled(true);//设置能与JS互调
        s.setGeolocationEnabled(true);
        s.setDomStorageEnabled(true);
        mWb.requestFocus();
        mWb.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        mWb.setWebChromeClient(new WebChromeClient());
        mWb.loadUrl(url);
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(view -> {
            playVoice(R.raw.changgui02);
            finish();
        });
        mTitle2.setOnClickListener(view -> {
            UMWeb web = new UMWeb(url);
            web.setTitle("蜂鸟记账");//标题
            web.setThumb(new UMImage(WebViewActivity.this, R.mipmap.ic_about_ours));  //缩略图
            web.setDescription("蜂鸟记账 \n不乱花，更自在");//描述

            new ShareAction(WebViewActivity.this)
                    .withMedia(web)
                    .setPlatform(WEIXIN)
                    .share();
        });
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
            showMessage("成功了");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showMessage("失败");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showMessage("取消了");
        }
    };

    @Override
    public void onBackPressed() {
        mWb.goBack();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWb.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}