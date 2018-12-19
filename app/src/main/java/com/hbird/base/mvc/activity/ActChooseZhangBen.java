package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.adapter.ChooseZhangBenAdapter;
import com.hbird.base.mvc.bean.AccountBookBean;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import sing.SmartRefreshLayout;

/**
 * @author: LiangYX
 * @ClassName: ChooseZhangBenActivity
 * @date: 2018/12/13 10:16
 * @Description: 添加账本 - 选择账本
 */
public class ActChooseZhangBen extends BaseActivity<BasePresenter> {

    @BindView(R.id.iv_backs)
    ImageView mBack;
    @BindView(R.id.tv_center_title)
    TextView mCenterTitle;
    @BindView(R.id.tv_right_title)
    TextView mRightTitle;

    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<AccountBookBean.AccountBean> list = new ArrayList<>();
    private ChooseZhangBenAdapter adapter;

    private String token;

    @Override
    protected int getContentLayout() {
        return R.layout.act_choose_account_type;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRightTitle.setVisibility(View.GONE);
        mCenterTitle.setText("选择账本");

        adapter = new ChooseZhangBenAdapter(this, list, R.layout.row_choose_account, (position, data, type) -> onItemClick((AccountBookBean.AccountBean) data));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        refresh.setOnRefreshListener(refreshLayout -> request());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");

        request();
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(v -> {
            playVoice(R.raw.changgui02);
            finish();
        });
    }

    private void request() {
        showProgress("");
        NetWorkManager.getInstance().setContext(this)
                .getAccountsType(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        list.clear();
                        refresh.finishRefresh();
                        list = ((AccountBookBean) b).result;
                        adapter.setData(list);
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        refresh.finishRefresh();
                        showMessage(s);
                    }
                });
    }

    // Item点击
    private void onItemClick(AccountBookBean.AccountBean data) {
        playVoice(R.raw.changgui02);
        Intent intent = new Intent(this, EditorZhangBenActivity.class);
        intent.putExtra("TAG", "create");
        intent.putExtra("ID", data.id);
        Log.e("111", "account_id = " + data.id);
        startActivityForResult(intent, 403);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 403 && resultCode == 403) {
            finish();
        }
    }
}