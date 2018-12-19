package com.hbird.base.mvc.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.ReturnBean.GeRenInfoReturn;
import com.hbird.base.mvc.bean.ReturnBean.HeaderInfoReturn;
import com.hbird.base.mvc.widget.RoundImageView;
import com.hbird.base.mvc.widget.cycleView;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.ImageUtils;
import com.ljy.devring.image.support.GlideApp;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.SocializeUtils;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindDimen;
import butterknife.BindView;

import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN_CIRCLE;
import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by Liul on 2018/7/19.
 */

public class ShaiChengJiuActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener{

    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mTitle2;
    @BindView(R.id.tv_xiazai)
    TextView mXiaZai;
    @BindView(R.id.tv_wxs)
    TextView mWxs;
    @BindView(R.id.tv_wxf)
    TextView mWxf;
    @BindView(R.id.tv_month_day)
    TextView mMonthDay;
    /*@BindView(R.id.tv_day_day)
    TextView mDayday;*/
    @BindView(R.id.tv_account_num)
    TextView mAccountNum;
    @BindView(R.id.tv_names)
    TextView mNames;
    @BindView(R.id.ll_image)
    LinearLayout mImage;
    @BindView(R.id.textView)
    ScrollView tv;
    @BindView(R.id.iv_avata)
    cycleView mAvata;
    @BindView(R.id.rl_bgs)
    ImageView mBgs;
    @BindView(R.id.tv_tuijianf)
    TextView mTuijian;

    private String picPath;
    private Runnable runnable;
    private String jzdays;
    private String zongjz;
    private String inviteUsers;
    private String tuiJFriend;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_chengjiu;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("晒成就");
        mTitle2.setVisibility(View.GONE);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String jsons = getIntent().getStringExtra("JSON");
        jzdays = getIntent().getStringExtra("JZDAYS");
        zongjz = getIntent().getStringExtra("ZONGJZ");
        tuiJFriend = getIntent().getStringExtra("FENXIANG");


        String headurl = getIntent().getStringExtra("HEADYRL");//头像
        GlideApp.with(ShaiChengJiuActivity.this)
                .load(headurl)
                .placeholder(R.mipmap.ic_me_normal_headr)
                .centerCrop()
                .error(R.mipmap.ic_me_normal_headr)
                .into(mAvata);
        GlideApp.with(ShaiChengJiuActivity.this)
                .load(headurl)
                .placeholder(R.mipmap.ic_me_normal_headr)
                .centerCrop()
                .error(R.mipmap.ic_me_normal_headr)
                .into( mBgs);
        mBgs.setAlpha(20);
        String name = getIntent().getStringExtra("NAME");
        mNames.setText(name);
        if(!TextUtils.isEmpty(jsons)) {
            HeaderInfoReturn info = new Gson().fromJson(jsons, HeaderInfoReturn.class);
            String daysCount = info.getResult().getDaysCount();
            mMonthDay.setText(jzdays+"/"+daysCount.split("/")[1]);
            //mDayday.setText(info.getResult().getClockInDays()+"");
            mAccountNum.setText(zongjz);
            mTuijian.setText(tuiJFriend);
        }

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        ImageUtils.layoutView(mImage,width,height);
        final ScrollView tv = (ScrollView) mImage.findViewById(R.id.textView);
        runnable = new Runnable() {
            @Override
            public void run() {
               /* picPath = ImageUtils.viewSaveToImage(tv, "fengniao");
                Uri imageUri = Uri.fromFile(new File(picPath));
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                intent.setType("image");
                startActivity(Intent.createChooser(intent,"分享到 "));*/
                Bitmap cachebmp = ImageUtils.getCachebmp(tv);
                UMImage imagelocal = new UMImage(ShaiChengJiuActivity.this,  cachebmp);
                imagelocal.setThumb(new UMImage(ShaiChengJiuActivity.this, cachebmp));
                new ShareAction(ShaiChengJiuActivity.this).withMedia(imagelocal )
                        .setPlatform(WEIXIN)
                        .setCallback(shareListener).share();
            }
        };
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
        mXiaZai.setOnClickListener(this);
        mWxs.setOnClickListener(this);
        mWxf.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_xiazai:
                playVoice(R.raw.changgui02);
                File files = ImageUtils.viewSaveTo2Image(tv, "fengniao");
                showMessage("下载成功");
                try {
                    //通知系统相册更新
                    updatePhotoMedia(files,ShaiChengJiuActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tv_wxs:
                //showMessage("微信");
                playVoice(R.raw.changgui02);
                new Handler().post(runnable);
                break;
            case R.id.tv_wxf:
                //showMessage("微信圈");
                //picPath = ImageUtils.viewSaveToImage(tv, "fengniao");
                playVoice(R.raw.changgui02);
                Bitmap cachebmp = ImageUtils.getCachebmp(tv);
                UMImage imagelocal = new UMImage(ShaiChengJiuActivity.this,  cachebmp);
                imagelocal.setThumb(new UMImage(ShaiChengJiuActivity.this, cachebmp));
                new ShareAction(ShaiChengJiuActivity.this).withMedia(imagelocal )
                        .setPlatform(WEIXIN_CIRCLE)
                        .setCallback(shareListener).share();
                break;
        }
    }
    //更新图库
    private static void updatePhotoMedia(File file ,Context context){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            //showMessage("成功了");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            //showMessage("失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //showMessage("取消");

        }
    };


}
