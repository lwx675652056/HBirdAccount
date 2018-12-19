package com.hbird.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.hbird.base.R;
import com.hbird.base.databinding.ActChooseAccountType1Binding;
import com.hbird.base.mvc.activity.ActSetAccountType;
import com.hbird.base.mvc.adapter.ChooseZhangBenAdapter;
import com.hbird.base.mvc.bean.AccountBookBean;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.util.SPUtil;
import com.hbird.viewmodel.ChooseAccountTypeViewModel;

import java.util.ArrayList;
import java.util.List;

import sing.common.util.LogUtil;

/**
 * @author: LiangYX
 * @ClassName: ActChooseAccountType
 * @date: 2018/12/18 17:02
 * @Description: 选择账本类别
 */
//public class ActChooseAccountType extends BaseActivity implements ChooseAccountTypeViewModel.CallBack {
public class ActChooseAccountType extends Activity implements ChooseAccountTypeViewModel.CallBack {

    private ChooseZhangBenAdapter adapter;
    private List<AccountBookBean.AccountBean> list = new ArrayList<>();
    private ActChooseAccountType1Binding binding;
    ChooseAccountTypeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_choose_account_type1);

//        viewModel = ViewModelProviders.of(ActChooseAccountType.this).get(ChooseAccountTypeViewModel.class);
//        viewModel.setCallBack(this);

        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        viewModel.getHadABType(token);
    }

    //    @Override
//    protected int getContentLayout() {
//        return ;
//    }

//    @Override
//    protected void initView(Bundle savedInstanceState) {
//        mRightTitle.setVisibility(View.GONE);
//        mCenterTitle.setText("选择账本类型");
//
//        adapter = new ChooseZhangBenAdapter(this, list, R.layout.row_choose_account, (position, data, type) -> onItemClick((AccountBookBean.AccountBean) data));
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//
//    }

    // Item点击
    private void onItemClick(AccountBookBean.AccountBean data) {
        Intent intent = new Intent(this, ActSetAccountType.class);
        intent.putExtra("account_id", data.id);
        SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.CURRENT_ACCOUNT_ID, data.id);
        startActivity(intent);
    }

    @Override
    public void getSuccess(String loginData) {
        LogUtil.e(loginData);
    }

    @Override
    public void getFailure() {

    }
}