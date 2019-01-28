package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.global.modle.CuttingFrameView;
import com.hbird.base.mvc.global.modle.PerfectControlImageView;
import com.hbird.base.mvc.global.modle.SpinnerProgressDialoag;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.BitmapUtils;
import com.hbird.base.util.ConstantSet;
import com.hbird.base.util.SDCardUtils;

import java.io.ByteArrayOutputStream;

import sing.common.util.StatusBarUtil;

/**
 * 头像裁剪界面  create By liul on 2018/07/05;
 */

public class ClippingPageActivity extends BaseActivity<BasePresenter> {


    private PerfectControlImageView imageView;
    private CuttingFrameView cuttingFrameView;
    private Bitmap bitmap;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_clipping_page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarUtil.clearStatusBarDarkMode(getWindow());
        initBarColor(Color.parseColor("#F15C3C"), Color.parseColor("#F15C3C"));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initTitle();

        cuttingFrameView = (CuttingFrameView) findViewById(R.id.cutingFrame);
        imageView = (PerfectControlImageView) findViewById(R.id.targetImage);

        if ("takePicture".equals(getIntent().getStringExtra("type"))) {
            bitmap = BitmapUtils.DecodLocalFileImage(ConstantSet.LOCALFILE + ConstantSet.USERTEMPPIC, this);
        } else {
            String path = getIntent().getStringExtra("path");
            bitmap = BitmapUtils.DecodLocalFileImage(path, this);
        }

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void initEvent() {
    }

    /**
     * 初始化title
     */
    private void initTitle() {
        ImageView rightImage = (ImageView) findViewById(R.id.id_img_right);
        TextView title = (TextView) findViewById(R.id.id_title);
        TextView righttext = (TextView) findViewById(R.id.id_text_right);
        ImageView back = (ImageView) findViewById(R.id.id_back);
        rightImage.setVisibility(View.GONE);
        righttext.setVisibility(View.VISIBLE);
        title.setText("图片裁剪");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClippingPageActivity.this.finish();
            }
        });

        righttext.setText("确定");
        righttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerProgressDialoag sp = new SpinnerProgressDialoag(ClippingPageActivity.this);
                sp.show();
                Bitmap bitmap = cuttingFrameView.takeScreenShot(ClippingPageActivity.this);
                String url = SDCardUtils.saveMyBitmap(ConstantSet.LOCALFILE, ConstantSet.USERPIC, bitmap);
                Intent it = new Intent();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bitmapByte = baos.toByteArray();
                it.putExtra("result", bitmapByte);
                it.putExtra("url", url);
                setResult(RESULT_OK, it);
                sp.dismiss();
                ClippingPageActivity.this.finish();
            }
        });
    }
}