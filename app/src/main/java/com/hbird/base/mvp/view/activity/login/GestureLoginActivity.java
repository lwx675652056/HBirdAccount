package com.hbird.base.mvp.view.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.app.GestureUtil;
import com.hbird.base.app.RingApplication;
import com.hbird.base.mvc.activity.homeActivity;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.global.ACache;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.mvc.widget.LockPatternView;
import com.hbird.base.mvp.view.activity.base.BaseActivity;

import com.hbird.base.mvp.view.iview.login.IGestureLoginView;
import com.hbird.base.mvp.presenter.login.GestureLoginPresenter;

import com.hbird.base.R;
import com.hbird.base.util.LockPatternUtil;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.RingToast;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hbird.base.R.id.iv_back;

/**
 * Created by Liul on 2018/07/05.
 * 手势密码登录验证
 */
public class GestureLoginActivity extends BaseActivity<BaseActivityPresenter> implements IGestureLoginView,View.OnClickListener {

    private static final String TAG = "LoginGestureActivity";

    @BindView(R.id.lockPatternView)
    LockPatternView lockPatternView;
    @BindView(R.id.messageTv)
    TextView messageTv;
    @BindView(R.id.forgetGestureBtn)
    TextView forgetGestureBtn;
    @BindView(iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTitle;

    private ACache aCache;
    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_gesture_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        forgetGestureBtn.setVisibility(View.VISIBLE);
//        if (!checkTheLastErrNum(false)){
//            String strMsg = "您已连续错误3次，请通过密码找回！";
//            RingToast.show(strMsg);
//            return;
//        }
        mCenterTitle.setText("验证手势密码");
        mCenterTitle.setTextSize(16);
        mRightTitle.setVisibility(View.GONE);
        String comfrom = getIntent().getStringExtra("comfrom");
        if(TextUtils.equals(comfrom,"splash")){
            mBack.setVisibility(View.GONE);
        }
        init();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
    }

    private void init() {
        //
        aCache = ACache.get(GestureLoginActivity.this);
        //得到当前用户的手势密码
        gesturePassword = aCache.getAsBinary(CommonTag.GESTURE_PASSWORD);
        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(GestureLoginActivity.Status.DEFAULT);
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {



        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
           }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(pattern != null){
                if(LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                    updateStatus(Status.ERROR);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(GestureLoginActivity.Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                int nErrorNum = DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                        getInt(com.hbird.base.app.constant.CommonTag.GESTURE_ERROR_NUM, 0);
                if(4-nErrorNum>0){
                    messageTv.setText("密码错误，还可再次输入"+(4-nErrorNum)+"次");
                    messageTv.setTextColor(getResources().getColor(R.color.red_f4333c));
                }
                if(GestureUtil.beOverMaxErrNumByAdd()){
                    DialogUtils du = new DialogUtils(this).builder().
                            setTitle("提示").
                            setMsg("您已超过允许错误次数，系统将清空并关闭手势密码，请重新登录")
                            .setCancleButton("",new View.OnClickListener(){

                                @Override
                                public void onClick(View view) {
                                    forgetGesturePasswrod();
                                }
                            } )
                            .setSureButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //确认
                            forgetGesturePasswrod();
                        }
                    });
                    du.show();
                }

                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                //登录成功，清空当前 错误次数
                GestureUtil.clearMaxErrNum();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        Intent intent = getIntent();//获取传来的intent对象
        String data = intent.getStringExtra("comfrom");
        if (data!=null && data.equals("modify")){//来自于修改界面,验证成功后进入创建手势密码界面
            //打开创建手势密码界面
            intent = new Intent(this, CreateGestureActivity.class);
            startActivity(intent);
            finish();
        }
        else{//来自于登录界面,验证成功后进入到首页
            startActivity(new Intent(getApplicationContext(), homeActivity.class));
            finish();
        }
        //showMessage("登录成功！");
    }

    /**
     * 忘记手势密码（去账号登录界面）
     */
    @OnClick(R.id.forgetGestureBtn)
    void forgetGesturePasswrod() {
        //关闭手势密码开关
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH)
                .put(com.hbird.base.app.constant.CommonTag.SHOUSHI_PASSWORD_OPENED, false);
        //清除手势密码
        ACache aCache = ACache.get(this);
        aCache.clear();
        //清零错误次数
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                put(com.hbird.base.app.constant.CommonTag.GESTURE_ERROR_NUM, 0);

        Intent intent = getIntent();//获取传来的intent对象
        String data = intent.getStringExtra("comfrom");
        Intent intentNew = new Intent(GestureLoginActivity.this, loginActivity.class);
        if (data!=null && data.equals("modify")) {//来自于修改界面,验证成功后进入创建手势密码界面
            //打开创建手势密码界面
            intentNew.putExtra("comfrom","modify_forget");
        } else{
            if (data!=null && data.equals("splash")) {//来自于登录界面
                //登录界面输入手势密码次数超过最大次数，后进入登录界面，如果在登录界面登录成功需要清空手势密码
                intentNew.putExtra("comfrom","splash_overmaxnum");
            }
        }
        startActivity(intentNew);
        Activity activity = DevRing.activityListManager().findActivity(homeActivity.class);
        if(null!=activity){
            activity.finish();
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case iv_back:
                finish();
                break;
        }
    }

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }

}
