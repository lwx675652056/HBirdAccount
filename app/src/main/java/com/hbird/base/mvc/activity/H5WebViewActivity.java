package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.view.SharePop;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;

import static com.hbird.base.R.id.webView;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;

/**
 * Created by Liul on 2018/10/22.
 * 领票票H5点击轮播图跳转到内部链接的界面
 */

public class H5WebViewActivity extends BaseActivity<BaseActivityPresenter> {
    @BindView(R.id.wb)
    WebView mWb;
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mTitle2;
    @BindView(R.id.right_img)
    ImageView rightView;
    private String url;
    private String title;
    private String shareimage;
    private String sharecontent;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_h5_web_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        url = getIntent().getStringExtra("URL");
        title = getIntent().getStringExtra("TITLE");
        shareimage = getIntent().getStringExtra("SHAREIMAGE");
        sharecontent = getIntent().getStringExtra("SHARECONTENT");

        mTitle2.setVisibility(View.GONE);
        mCenterTitle.setText(title);
        rightView.setVisibility(View.VISIBLE);
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

        mWb.setWebChromeClient(new WebChromeClient() {

            public boolean onConsoleMessage(ConsoleMessage cm) {

                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //bar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //mCenterTitle.setText(title);
            }

        });
        //使用内部的 WebView 加网页就要重写 shouldOverrideUrlLoading 方法，使其返回 true
        mWb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWb.loadUrl(url);
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice(R.raw.changgui02);
               finish();
            }
        });
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享到微信相关
                popWindows();
            }
        });
    }
    private void popWindows() {
        new SharePop(H5WebViewActivity.this, new SharePop.onMyClickListener() {
            @Override
            public void setWx() {
                playVoice(R.raw.changgui02);
                UMWeb web = new UMWeb(UrlConstants.INVITE_FIRENDS_URL);
                web.setTitle(title);//标题
                web.setThumb(new UMImage(H5WebViewActivity.this, shareimage));  //缩略图
                web.setDescription(sharecontent);//描述

                new ShareAction(H5WebViewActivity.this)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(shareListener)
                        .share();
            }

            @Override
            public void setWxq() {
                playVoice(R.raw.changgui02);
                UMWeb web = new UMWeb(UrlConstants.INVITE_FIRENDS_URL);
                web.setTitle(title);
                web.setThumb(new UMImage(H5WebViewActivity.this, shareimage));
                web.setDescription(sharecontent);
                new ShareAction(H5WebViewActivity.this).withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(shareListener).share();
            }
        }).showPopWindow();
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

}
