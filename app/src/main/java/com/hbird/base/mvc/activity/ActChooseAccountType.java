package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.adapter.ChooseZhangBenAdapter;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.AccountBookBean;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author: LiangYX
 * @ClassName: ActChooseAccountType
 * @date: 2018/12/11 15:24
 * @Description: 选择账本类别
 */
public class ActChooseAccountType extends BaseActivity<BaseActivityPresenter> {

    @BindView(R.id.iv_backs)
    ImageView mBack;
    @BindView(R.id.tv_center_title)
    TextView mCenterTitle;
    @BindView(R.id.tv_right_title)
    TextView mRightTitle;

//    @BindView(R.id.refresh)
//    SmartRefreshLayout refresh;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ChooseZhangBenAdapter adapter;
    private List<AccountBookBean.AccountBean> list = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.act_choose_account_type;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRightTitle.setVisibility(View.GONE);
        mCenterTitle.setText("选择账本类型");

        adapter = new ChooseZhangBenAdapter(this, list, R.layout.row_choose_account, (position, data, type) -> onItemClick((AccountBookBean.AccountBean) data));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

//        refresh.setOnRefreshListener(refreshLayout -> request());
    }

    // Item点击
    private void onItemClick(AccountBookBean.AccountBean data) {
        Intent intent = new Intent(this, ActSetAccountType.class);
        intent.putExtra("account_id", data.id);
        SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.CURRENT_ACCOUNT_ID, data.id);
        startActivity(intent);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        request();
    }

    private void request() {
        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        NetWorkManager.getInstance().setContext(this)
                .getHadABType(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        list.clear();
//                        refresh.finishRefresh();
                        list = ((AccountBookBean) b).result;
                        adapter.setData(list);
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
//                        refresh.finishRefresh();
                        showMessage(s);
                    }
                });
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(v -> {
            playVoice(R.raw.changgui02);
            finish();
        });
    }
}