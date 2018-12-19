package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.SuggestReq;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.L;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Liul on 2018/7/5.
 * 意见反馈
 */

public class SuggestFanKuiActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener{
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_record)
    EditText mSuggest;
    private String suggestText;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_suggest_fankui;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("意见反馈");
        mRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mSuggest.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                suggestText = Utils.encode(s.toString());
                //L.liul(suggestText);

            }
        });
    }
    @OnClick({R.id.iv_back,R.id.rl_top_btn})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.rl_top_btn:
                playVoice(R.raw.changgui02);
                if(null==suggestText){
                    showMessage("填写内容不能为空");
                    return;
                }

                String suggest = Utils.decode(suggestText).toString().trim();
                String mPhone = mEtPhone.getText().toString().trim();
                if(TextUtils.isEmpty(suggest)){
                    showMessage("填写内容不能为空");
                    return;
                }
                SuggestReq req = new SuggestReq();
                req.setContact(mPhone);
                req.setContent(suggest);
                String json = new Gson().toJson(req);
                String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
                showProgress("");
                NetWorkManager.getInstance().setContext(this)
                        .postSuggest(json, token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                hideProgress();
                                showMessage("提交成功");
                                mEtPhone.setText("");
                                mSuggest.setText("");
                                finish();

                            }

                            @Override
                            public void onError(String s) {
                                hideProgress();
                                showMessage(s);
                            }
                        });
                break;
        }
    }

}
