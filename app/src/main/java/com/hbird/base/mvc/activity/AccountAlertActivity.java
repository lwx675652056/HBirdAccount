package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.alarmClock.AlarmManagerUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Liul on 2018/7/5.
 * 记账提醒
 */

public class AccountAlertActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener{
    @BindView(R.id.iv_btn)
    com.hbird.base.mvc.widget.IosLikeToggleButton mIvBtn;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.tv_time)
    TextView mTime;

    private boolean isOpen;
    private Intent intent;
    private TimePickerView pvTime;
    private String time;
    private int ring = 0;//0震动  1铃声

    @Override
    protected int getContentLayout() {
        return R.layout.activity_account_alert;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("记账提醒");
        mRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //根据sp中存的值 显示是否打开
        isOpen = SPUtil.getPrefBoolean(AccountAlertActivity.this, CommonTag.ACCOUNT_ALERT, false);
        if(isOpen){
            mIvBtn.setChecked(true);
        }else {
            mIvBtn.setChecked(false);
        }
        //根据记录的时间 填写提醒时间
        String alertTime = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.APP_ALERT_ACCOUNT, "20:30");
        mTime.setText(alertTime);
    }

    @Override
    protected void initEvent() {
        mIvBtn.setOnClickListener(this);
    }
    @OnClick({R.id.ll_open_password,R.id.ll_modifi_password,R.id.iv_back})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                onBackPressed();
                break;
            case R.id.iv_btn:
            case R.id.ll_open_password:// 开启记账提醒
                playVoice(R.raw.changgui02);
                boolean ss = SPUtil.getPrefBoolean(AccountAlertActivity.this, CommonTag.ACCOUNT_ALERT, false);
                time = mTime.getText().toString().trim();
                if(ss){
                    SPUtil.setPrefBoolean(AccountAlertActivity.this, CommonTag.ACCOUNT_ALERT,false);
                    mIvBtn.setChecked(false);
                    if(intent!=null){
                        stopService(intent);
                    }
                    setArmlock(false,time);
                }else {
                    SPUtil.setPrefBoolean(AccountAlertActivity.this, CommonTag.ACCOUNT_ALERT,true);
                    mIvBtn.setChecked(true);
                    setArmlock(true,time);
                }

                break;
            case R.id.ll_modifi_password:
                //showMessage("提醒时间");
                playVoice(R.raw.changgui02);
                pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
                pvTime.setTime(new Date());
                pvTime.setCyclic(false);
                pvTime.setCancelable(true);
                final boolean opens = SPUtil.getPrefBoolean(AccountAlertActivity.this, CommonTag.ACCOUNT_ALERT, false);
                //时间选择后回调
                pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
                        time = getTime(date);
                        setArmlock(opens,time);
                    }
                });
                pvTime.show();

                break;
        }
    }

    private void setArmlock(boolean opens,String time) {
        mTime.setText(time);
        //存储闹铃时间
        SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.APP_ALERT_ACCOUNT,time);
        //设置闹钟
        if (time != null && time.length() > 0) {
            String[] times = time.split(":");
            AlarmManagerUtil.setAlarm(AccountAlertActivity.this, 0, Integer.parseInt(times[0]), Integer.parseInt
                    (times[1]), 0, 0, "今天花了多少钱呢？记一笔账吧。", ring,opens);
        }
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // singleInstance 模式必须这么写
        overridePendingTransition( R.anim.slide_in_from_left,R.anim.slide_out_to_right);
    }
}
