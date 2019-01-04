package com.hbird.ui.fill_invitation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.databinding.ActFillInvitationBinding;
import com.hbird.base.mvc.activity.homeActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.util.SPUtil;
import com.hbird.common.Constants;
import com.ljy.devring.base.activity.IBaseActivity;

import sing.common.base.BaseActivity;
import sing.common.base.BaseViewModel;
import sing.util.KeyboardUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: ActFillInvitation
 * @date: 2019/1/3 15:48
 * @Description: 填写邀请码
 */
public class ActFillInvitation extends BaseActivity<ActFillInvitationBinding, BaseViewModel> implements IBaseActivity {

    private FillInvitationData data;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_fill_invitation;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initData() {
        data = new FillInvitationData();
        binding.setData(data);
        binding.setListener(new OnClick());

        ((TextView) binding.toolbar.findViewById(R.id.id_title)).setText("填写注册邀请码");
        binding.toolbar.findViewById(R.id.id_back).setOnClickListener(v -> onBackPressed());

        KeyboardUtil.openKeybord(binding.etCode, this);
    }

    @Override
    public void onBackPressed() {
        String userId = (String) SharedPreferencesUtil.get(Constants.USER_ID, "");// 用户id

        SharedPreferencesUtil.put(userId + Constants._FILLED_IN, true);// 是否填写过邀请码
        startActivity(new Intent(getApplicationContext(), homeActivity.class));

        KeyboardUtil.closeKeybord(binding.etCode, this);
        finish();
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    public boolean isUseFragment() {
        return false;
    }

    public class OnClick {
        public void skip(View view) {
            onBackPressed();
        }

        public void ok(View view) {
            submit();
        }
    }

    private void submit() {
        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");

        NetWorkManager.getInstance().setContext(this).bindInvite(token, data.getCode(), new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                onBackPressed();
            }

            @Override
            public void onError(String s) {
                ToastUtil.showShort(s);
            }
        });
    }
}