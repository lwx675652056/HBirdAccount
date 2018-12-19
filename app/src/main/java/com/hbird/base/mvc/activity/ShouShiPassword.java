package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Liul on 2018/7/5.
 * 手势密码
 */

public class ShouShiPassword extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener{
    @BindView(R.id.tv_btn)
    TextView mBtnText;
    @BindView(R.id.iv_btn)
    ImageView mIvBtn;
    @BindView(R.id.center_title)
    TextView mCenterTitle;


    private boolean isOpen;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shoushi_password;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("手势密码");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //根据sp中存的值 显示是否打开
        isOpen = SPUtil.getPrefBoolean(ShouShiPassword.this, CommonTag.SHOUSHI_PASSWORD, false);
        if(isOpen){
            mBtnText.setText("关闭手势密码");
            mIvBtn.setBackgroundResource(R.mipmap.icon_btn_open);
        }else {
            mBtnText.setText("开启手势密码");
            mIvBtn.setBackgroundResource(R.mipmap.icon_btn_close);
        }
    }

    @Override
    protected void initEvent() {

    }
    @OnClick({R.id.ll_open_password,R.id.ll_modifi_password,R.id.iv_back})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_open_password:
                //showMessage("开启手势密码");
                boolean ss = SPUtil.getPrefBoolean(ShouShiPassword.this, CommonTag.SHOUSHI_PASSWORD, false);
                if(ss){
                    SPUtil.setPrefBoolean(ShouShiPassword.this, CommonTag.SHOUSHI_PASSWORD,false);
                    mIvBtn.setBackgroundResource(R.mipmap.icon_btn_close);
                }else {
                    SPUtil.setPrefBoolean(ShouShiPassword.this, CommonTag.SHOUSHI_PASSWORD,true);
                    mIvBtn.setBackgroundResource(R.mipmap.icon_btn_open);
                }

                break;
            case R.id.ll_modifi_password:
                showMessage("修改手势密码");
                startActivity(new Intent(this,GestureLoginActivity.class));
                break;
        }
    }
}
