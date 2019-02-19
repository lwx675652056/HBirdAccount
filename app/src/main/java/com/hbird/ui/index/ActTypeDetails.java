package com.hbird.ui.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.hbird.base.R;
import com.hbird.base.databinding.ActDetailsBinding;
import com.hbird.base.mvc.activity.MingXiInfoActivity;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountDetailedBean;
import com.hbird.bean.TitleBean;
import com.hbird.common.Constants;
import com.hbird.ui.statistics_details.StatisticsDetailAdapter;
import com.hbird.ui.statistics_details.StatisticsDetailData;
import com.hbird.util.Utils;

import java.util.ArrayList;
import java.util.List;

import sing.common.base.BaseActivity;
import sing.common.util.StatusBarUtil;

/**
 * @author: LiangYX
 * @ClassName: ActTypeDetails
 * @date: 2019/2/19 11:40
 * @Description: 首页饼图类别的排行
 */
public class ActTypeDetails extends BaseActivity<ActDetailsBinding, TypeDetailModle> {

    private StatisticsDetailData data;
    private List<AccountDetailedBean> list = new ArrayList<>();
    private StatisticsDetailAdapter adapter;
    private String firstDay;
    private String lastDay;
    private String typeName; // 标签名
    private int orderType = 1; // 1:支出  2:收入
    private String persionId;// 个人的ID
    private String SQL;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_details;
    }

    @Override
    public void initData() {
        persionId = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");

        typeName = getIntent().getExtras().getString(Constants.START_INTENT_A, "");
        String money = getIntent().getExtras().getString(Constants.START_INTENT_B, "0.00");
        firstDay = getIntent().getExtras().getString(Constants.START_INTENT_C, "2019-01-01");
        lastDay = getIntent().getExtras().getString(Constants.START_INTENT_D, "2019-01-02");
        SQL = getIntent().getExtras().getString(Constants.START_INTENT_E, "");

        binding.toolbar.ivBack.setOnClickListener(v -> onBackPressed());
        binding.toolbar.ivBack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_white_back));
        binding.toolbar.tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.toolbar.flParent.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_main_bg));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.toolbar.flParent.getLayoutParams();
        params.height += StatusBarUtil.getStateBarHeight(this);
        binding.toolbar.flParent.setLayoutParams(params);
        binding.toolbar.flParent.setPadding(0, StatusBarUtil.getStateBarHeight(this), 0, 0);

        data = new StatisticsDetailData();
        binding.setData(data);

        String t = DateUtils.str2Str(firstDay);
        data.setDate(t.substring(0, t.length() - 3));
        if (orderType == 1) {
            data.setType(typeName + "总计消费");
            binding.setTitle(new TitleBean("支出详情"));
        }
        data.setMoney("￥" + money);

        adapter = new StatisticsDetailAdapter(this, list, R.layout.row_statictics_detail, orderType, (position, data, type) -> onItemClick(((AccountDetailedBean) data).getId()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getData();
    }

    private void onItemClick(String id) {
        Intent intent = new Intent(this, MingXiInfoActivity.class);
        intent.putExtra("ID", id);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            getData();
        }
    }

    private void getData() {
        viewModel.getData(SQL, firstDay, lastDay, (dbList, moneyIncome, moneySpend) -> {
            list.clear();
            for (int i = 0; i < dbList.size(); i++) {
                AccountDetailedBean temp = new AccountDetailedBean();
                temp.setBean(dbList.get(i));
                list.add(temp);
            }
            adapter.notifyDataSetChanged();
            data.setNoData(list.size() < 1);
        });
    }

    @Override
    public void onBackPressed() {
        Utils.playVoice(this, R.raw.changgui02);
        finish();
    }

    @Override
    public int initVariableId() {
        return 0;
    }
}