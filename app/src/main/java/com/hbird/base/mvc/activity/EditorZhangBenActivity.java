package com.hbird.base.mvc.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.CreatAccountReturn;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.SPUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/7.
 * 编辑账本
 */

public class EditorZhangBenActivity extends BaseActivity<BaseActivityPresenter> {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.left_title)
    TextView leftTitle;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.right_title2)
    TextView rightTitle2;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.et_zhangben_name)
    EditText etZhangbenName;
    @BindView(R.id.tv_hints)
    TextView tvHints;
    @BindView(R.id.rl_bottom_btn)
    RelativeLayout rlBottomBtn;
    @BindView(R.id.ll_shuru)
    LinearLayout mShuRu;
    private InputMethodManager imm;
    private String token;
    private boolean isTrue =false;
    private String id;
    private String title;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_editor_zhangben;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        centerTitle.setText("编辑账本");
        rightTitle2.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String tag = getIntent().getStringExtra("TAG");
        id = getIntent().getStringExtra("ID");
        title = getIntent().getStringExtra("TITLE");
        etZhangbenName.setHint(title);
        etZhangbenName.setFocusable(true);
        etZhangbenName.requestFocus();

        if(TextUtils.equals(tag,"create")){
            isTrue = true;
        }else {
            isTrue = false;
        }
        token = SPUtil.getPrefString(EditorZhangBenActivity.this, CommonTag.GLOABLE_TOKEN, "");
        //弹出软键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void initEvent() {
        etZhangbenName.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(temp)){
                    tvHints.setVisibility(View.VISIBLE);
                }else {
                    tvHints.setVisibility(View.GONE);
                }
                if (temp.length() > 20) {
                    showMessage("最多只能输入20字");
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.rl_bottom_btn,R.id.ll_shuru})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                if(imm!=null){
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                }
                finish();
                break;
            case R.id.rl_bottom_btn:
                //showMessage("账本完成");
                playVoice(R.raw.changgui02);
                //修改账本接口
                String title = etZhangbenName.getText().toString().trim();
                if(!TextUtils.isEmpty(title)){
                    if(isTrue){
                        //调用创建账本的接口
                        createAccounts(title);
                    }else {
                        //修改账本的接口
                        modifidAccount(title);
                    }

                }else {
                    showMessage("请输入账本名称");
                }

                break;
            case R.id.ll_shuru:
                playVoice(R.raw.changgui02);
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mShuRu, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                etZhangbenName.requestFocus();
                break;
        }
    }

    private void createAccounts(String title) {
        NetWorkManager.getInstance().setContext(EditorZhangBenActivity.this)
                .postCreatAccount(title,id,token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        CreatAccountReturn b1 = (CreatAccountReturn) b;
                        //创建成功后将新账本的标签插入到本地数据库
                        List<CreatAccountReturn.ResultBean.SpendBean> spend = b1.getResult().getSpend();
                        //当前账本类型
                        String abTypeId = id;
                        //个人userInfo
                        String userInfo = SPUtil.getPrefString(EditorZhangBenActivity.this, CommonTag.USER_INFO_PERSION, "");
                        if(spend!=null){
                            DBUtil.insertSpendToLocalDB(spend,abTypeId,userInfo);
                        }
                        List<CreatAccountReturn.ResultBean.IncomeBean> income = b1.getResult().getIncome();
                        if(income!=null){
                            DBUtil.insertIncomeToLocalDB(income,abTypeId,userInfo);
                        }
                        //创建成功 返回到我的账本界面
                        setResult(403);
                        finish();
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }

    private void modifidAccount(String title) {
        NetWorkManager.getInstance().setContext(EditorZhangBenActivity.this)
                .putModifiAccount(title,id,token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        GloableReturn b1 = (GloableReturn) b;
                        setResult(402);
                        finish();
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }
}
