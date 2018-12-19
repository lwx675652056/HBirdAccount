package com.hbird.base.mvp.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.app.GestureUtil;
import com.hbird.base.mvc.global.ACache;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.mvp.view.activity.login.GestureLoginActivity;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvp.view.activity.base.BaseActivity;

import com.hbird.base.mvp.view.iview.login.IShouShiPasswordView;
import com.hbird.base.mvp.presenter.login.ShouShiPasswordPresenter;

import com.hbird.base.R;
import com.hbird.base.util.SPUtil;
import com.ljy.devring.DevRing;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手势密码设置（开启、关闭、修改）
 */
public class ShouShiPasswordActivity extends BaseActivity<ShouShiPasswordPresenter> implements View.OnClickListener{

    @BindView(R.id.tv_btn)
    TextView mBtnText;
    @BindView(R.id.iv_btn)
    com.hbird.base.mvc.widget.IosLikeToggleButton mIvBtn;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRight_title2;

    private boolean isOpen;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shou_shi_password;
    }

    @Override
    protected void initView(Bundle bundle) {
        mCenterTitle.setText("手势密码");
        mRight_title2.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //根据sp中存的值 显示是否打开
        isOpen = DevRing.cacheManager().spCache(CommonTag.SPCACH).getBoolean(CommonTag.SHOUSHI_PASSWORD_OPENED, false);
        if(isOpen){
            mBtnText.setText("关闭手势密码");
            mIvBtn.setChecked(true);
        }else {
            mBtnText.setText("开启手势密码");
            mIvBtn.setChecked(false);
        }
    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.ll_open_password,R.id.ll_modifi_password,R.id.iv_back,R.id.iv_btn})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            //开启 or 关闭 手势密码
            case R.id.iv_btn:
            case R.id.ll_open_password:
                playVoice(R.raw.changgui02);
                //showMessage("开启手势密码");
                ACache aCache = ACache.get(this);
                isOpen = DevRing.cacheManager().spCache(CommonTag.SPCACH).getBoolean(CommonTag.SHOUSHI_PASSWORD_OPENED, false);
                if(isOpen){//如果当前是开启状态，则设置为关闭
                    DevRing.cacheManager().spCache(CommonTag.SPCACH).put(CommonTag.SHOUSHI_PASSWORD_OPENED, false);
                    mIvBtn.setChecked(false);
                    //aCache.clear();
                }else {//如果当前是关闭状态，则开启
                    //得到当前用户的手势密码
                    byte[] gesturePassword = aCache.getAsBinary(com.hbird.base.mvc.global.CommonTag.GESTURE_PASSWORD);
                    //如果当前手势密码为空，则打开设置手势密码界面
                    if (gesturePassword==null || gesturePassword.length<=0){
                        Intent intent = new Intent(this, CreateGestureActivity.class);
                        intent.putExtra("comfrom","open_password_close");
                        startActivity(intent);
                        finish();
                    }else{
                        DevRing.cacheManager().spCache(CommonTag.SPCACH).put(CommonTag.SHOUSHI_PASSWORD_OPENED, true);
                        mIvBtn.setChecked(true);
                    }
                }
                break;
            //修改手势密码
            case R.id.ll_modifi_password:
                playVoice(R.raw.changgui02);
                //showMessage("修改手势密码");
                byte[] gesturePassword = ACache.get(this).getAsBinary(com.hbird.base.mvc.global.CommonTag.GESTURE_PASSWORD);
                if (gesturePassword==null || gesturePassword.length<=0){
                    showMessage("请先开启并设置手势密码");
                    return;
                }
                if(GestureUtil.beOverMaxErrNum()){
                    DialogUtils du = new DialogUtils(this).builder().
                            setTitle("提示").
                            setMsg("您已超过允许错误次数，系统将清空并关闭手势密码，请重新登录").
                            setSureButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //确认
                                    forgetGesturePasswrod();
                                }
                            });
                    du.show();
                }
                else {
                    playVoice(R.raw.changgui02);
                    Intent intent = new Intent(this, GestureLoginActivity.class);
                    intent.putExtra("comfrom", "modify");
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    void forgetGesturePasswrod() {
        Intent intent = getIntent();//获取传来的intent对象
        String data = intent.getStringExtra("comfrom");
        Intent intentNew = new Intent(ShouShiPasswordActivity.this, loginActivity.class);
        //if (data!=null && data.equals("modify")) {//来自于修改界面,验证成功后进入创建手势密码界面
            //打开创建手势密码界面
            intentNew.putExtra("comfrom","modify_forget");
        //}
        startActivity(intentNew);
        finish();
    }
}
