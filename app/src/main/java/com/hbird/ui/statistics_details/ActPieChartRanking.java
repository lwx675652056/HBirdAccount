package com.hbird.ui.statistics_details;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.hbird.base.R;
import com.hbird.base.databinding.ActDetailsBinding;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountDetailedBean;
import com.hbird.bean.TitleBean;
import com.hbird.common.Constants;

import java.util.ArrayList;
import java.util.List;

import sing.common.base.BaseActivity;
import sing.common.util.StatusBarUtil;

/**
 * @author: LiangYX
 * @ClassName: ActPieChart
 * @date: 2019/1/21 17:15
 * @Description: 心情消费排行详情
 */
public class ActPieChartRanking extends BaseActivity<ActDetailsBinding, DetailModle> {

    private StatisticsDetailData data;
    private List<AccountDetailedBean> list = new ArrayList<>();
    private StatisticsDetailAdapter adapter;
    private String firstDay;
    private String lastDay;
    private String typeName; // 标签名
    private int dateType; // 1 2 3 , 日 周 年
    private int orderType; // 1:支出  2:收入
    private boolean isAll; //
    private String persionId;// 个人的ID

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_details;
    }


    @Override
    public void initData() {
        persionId = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");

        typeName = getIntent().getExtras().getString(Constants.START_INTENT_A, "");
        String num = getIntent().getExtras().getString(Constants.START_INTENT_B, "0");
        firstDay = getIntent().getExtras().getString(Constants.START_INTENT_C, "2019-01-01");
        lastDay = getIntent().getExtras().getString(Constants.START_INTENT_D, "2019-01-02");
        dateType = getIntent().getExtras().getInt(Constants.START_INTENT_E, 1);
        orderType = getIntent().getExtras().getInt(Constants.START_INTENT_F, 1);
        isAll = getIntent().getExtras().getBoolean(Constants.START_INTENT_G, false);

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


        if (dateType == 1) {// 日
            data.setDate(DateUtils.str2Str(firstDay));
        } else if (dateType == 2) {// 周
            String t = DateUtils.str2Str(lastDay);
            data.setDate(DateUtils.str2Str(firstDay) + "-" + t.substring(5, t.length()));
        } else if (dateType == 3) {// 月
            String t = DateUtils.str2Str(lastDay);
            data.setDate(t.substring(0, t.length() - 3));
        }

        data.setType(typeName);
        binding.setTitle(new TitleBean("支出详情"));
        data.setMoney(num);

        adapter = new StatisticsDetailAdapter(this, list, R.layout.row_statictics_detail, orderType, (position, data, type) -> {
        });
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getData();
    }

    private void getData() {
        viewModel.getPieChatData(firstDay, lastDay, isAll, orderType, persionId, typeName, (dbList, moneyIncome, moneySpend) -> {
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
    public int initVariableId() {
        return 0;
    }
}
