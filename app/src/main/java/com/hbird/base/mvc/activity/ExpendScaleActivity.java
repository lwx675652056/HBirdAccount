package com.hbird.base.mvc.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.ReturnBean.HeaderInfoReturn;
import com.hbird.base.mvc.widget.cycleView;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.ImageUtils;
import com.ljy.devring.image.support.GlideApp;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.BindView;

import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN_CIRCLE;
import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by Liul on 2018/8/16.
 * 我的消费结构 分享
 */

public class ExpendScaleActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener{

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

    @BindView(R.id.ll_image)
    LinearLayout mImage;
    @BindView(R.id.textView)
    ScrollView tv;

    //新
    @BindView(R.id.tv_text_precent)
    TextView mPrecentText;
    @BindView(R.id.tv_text_data)
    TextView mPrecentData;
    @BindView(R.id.iv_bg)
    RelativeLayout mBgs;


    private String picPath;
    private Runnable runnable;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_expend_scale;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("我的消费结构");
        mTitle2.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String precent = getIntent().getStringExtra("PRECENT");//百分比
        double precents = Double.parseDouble(precent);
        DecimalFormat df = new DecimalFormat("0");
        String ipp = df.format(precents);
        int pp = Integer.parseInt(ipp);
        String time = getIntent().getStringExtra("TIME");//时间
        mPrecentText.setText(ipp);
        String[] split = time.split("-");
        String s = split[0];
        String s2 = split[1];
        mPrecentData.setText("("+s+"年"+s2+"月"+")");
        if(pp>=71){
            //最富
            mBgs.setBackgroundResource(R.mipmap.ic_gz_zuifu);
        }else if (pp>=61 && pp<=70){
            //小资
            mBgs.setBackgroundResource(R.mipmap.ic_gz_xiaozi);
        }
        else if (pp>=51 && pp<=60){
            //白领
            mBgs.setBackgroundResource(R.mipmap.ic_gz_bailing);
        }
        else if (pp>=41 && pp<=50){
            //工作狂
            mBgs.setBackgroundResource(R.mipmap.ic_gz_work);
        }else {
            //吃货
            mBgs.setBackgroundResource(R.mipmap.ic_gz_chihuo);
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
                UMImage imagelocal = new UMImage(ExpendScaleActivity.this,  cachebmp);
                imagelocal.setThumb(new UMImage(ExpendScaleActivity.this, cachebmp));
                new ShareAction(ExpendScaleActivity.this).withMedia(imagelocal )
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
                File files = ImageUtils.viewSaveTo2Image(tv, "fengniao");
                showMessage("下载成功");
                try {
                    //通知系统相册更新
                    updatePhotoMedia(files,ExpendScaleActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tv_wxs:
                //showMessage("微信");
                new Handler().post(runnable);
                break;
            case R.id.tv_wxf:
                //showMessage("微信圈");
                //picPath = ImageUtils.viewSaveToImage(tv, "fengniao");
                Bitmap cachebmp = ImageUtils.getCachebmp(tv);
                UMImage imagelocal = new UMImage(ExpendScaleActivity.this,  cachebmp);
                imagelocal.setThumb(new UMImage(ExpendScaleActivity.this, cachebmp));
                new ShareAction(ExpendScaleActivity.this).withMedia(imagelocal )
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
