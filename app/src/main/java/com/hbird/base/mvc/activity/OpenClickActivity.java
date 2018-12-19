package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbird.base.R;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.L;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Liul(245904552@qq.com) on 2018/10/29.
 * 暂时没有用到（极光推送过来的华为单独通道直接进到主界面（homeActivity）进行相关跳转的操作了） 先不删除
 */

public class OpenClickActivity extends BaseActivity<BasePresenter> {
    @BindView(R.id.tv_text)
    TextView mTextView;
    @Override
    protected int getContentLayout() {
        return R.layout.activity_open_click;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        handleOpenClick();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }
    /**
     * 处理点击事件，当前启动配置的 Activity 都是使⽤
     * Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
     * 方式启动，只需要在 onCreat 中调⽤此方法进行处理
     */
    private void handleOpenClick() {
        L.liul("⽤户点击打开了通知");
        if (getIntent().getData() == null) return;
        String data = getIntent().getData().toString();
        L.liul( "msg content is " + String.valueOf(data));
        if (TextUtils.isEmpty(data)) return;
        try {
            JSONObject jsonObject = JSON.parseObject(data);
            String msgId = jsonObject.getString("msg_id");
            byte whichPushSDK = (byte) jsonObject.getByte("rom_type");
            String title = jsonObject.getString("n_title");
            String content = jsonObject.getString("n_content");
            String extras = jsonObject.getString("n_extras");
            showMessage(content);
            StringBuilder sb = new StringBuilder();
            sb.append("msgId:");
            sb.append(String.valueOf(msgId));
            sb.append("\n");
            sb.append("title:");
            sb.append(String.valueOf(title));
            sb.append("\n");
            sb.append("content:");
            sb.append(String.valueOf(content));
            sb.append("\n");
            sb.append("extras:");
            sb.append(String.valueOf(extras));
            sb.append("\n");
            sb.append("platform:");
            sb.append(getPushSDKName(whichPushSDK));
            //mTextView.setText(sb.toString());
            //上报点击事件
            JPushInterface.reportNotificationOpened(this, msgId, whichPushSDK);
            //根据extras 分析否是内链 {"jumpType":"0","jumpPage":"tbfx"}
            if(!TextUtils.isEmpty(extras)){
                JSONObject js = JSON.parseObject(extras);
                String jumpType = js.getString("jumpType");
                String jumpPage = js.getString("jumpPage");
                L.liul("6666666666 "+jumpPage+" , "+jumpType);
                if(TextUtils.equals(jumpType,"0")){
                    switch (jumpPage){
                        case "mxsy":
                            L.liul("mxsy");
                            //setTiaozhuanFragement(0,0);
                            break;
                        case "tbtj":
                            L.liul("tbtj");
                            //setTiaozhuanFragement(1,0);
                            break;
                        case "tbfx":
                            L.liul("tbfx");
//                            setTiaozhuanFragement(1,1);
                            break;
                        case "tbzc":
                            L.liul("tbzc");
//                            setTiaozhuanFragement(1,2);
                            break;
                        case "jzsy":
                            L.liul("jzsy");
                            startActivity(new Intent(this, ChooseAccountTypeActivity.class));
                            break;
                        case "lppsy":
//                            setTiaozhuanFragement(2,0);
                            break;
                    }
                }

            }
        } catch (Exception e) {
            L.liul("parse notification error");
        }
    }
    private String getPushSDKName(byte whichPushSDK) {
        String name;
        switch (whichPushSDK){
            case 0:
                name = "jpush";
                break;
            case 1:
                name = "xiaomi";
                break;
            case 2:
                name = "huawei";
                break;
            case 3:
                name = "meizu";
                break;
            case 8:
                name = "fcm";
                break;
            default:
                name = "jpush";
        }
        return name;
    }



}
