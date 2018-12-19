package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.global.ACache;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.widget.LockPatternView;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.mvp.view.iview.login.IGestureLoginView;
import com.hbird.base.util.LockPatternUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Liul on 2018/07/05.
 * 绘制手势密码
 */
public class GestureLoginActivity extends BaseActivity<BaseActivityPresenter> implements IGestureLoginView {

    private static final String TAG = "LoginGestureActivity";

    @BindView(R.id.lockPatternView)
    LockPatternView lockPatternView;
    @BindView(R.id.messageTv)
    TextView messageTv;
    @BindView(R.id.forgetGestureBtn)
    Button forgetGestureBtn;

    private ACache aCache;
    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_gesture_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    private void init() {
        aCache = ACache.get(GestureLoginActivity.this);
        //得到当前用户的手势密码
        gesturePassword = aCache.getAsBinary(CommonTag.GESTURE_PASSWORD);
        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
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
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        showMessage("登录成功！");
    }

    /**
     * 忘记手势密码（去账号登录界面）
     */
    @OnClick(R.id.forgetGestureBtn)
    void forgetGesturePasswrod() {
        Intent intent = new Intent(GestureLoginActivity.this, CreateGestureActivity.class);
        startActivity(intent);
        finish();
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
