package com.hbird.ui.statistics_details;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.hbird.base.R;
import com.hbird.base.databinding.RowStaticticsDetailBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountDetailedBean;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;

/**
 * @author: LiangYX
 * @ClassName: StatisticsDetailAdapter
 * @date: 2019/1/21 14:39
 * @Description: 统计详情
 */
public class StatisticsDetailAdapter extends BaseRecyclerAdapter<AccountDetailedBean, RowStaticticsDetailBinding> {

    private List<AccountDetailedBean> list;
    private Context context;
    private OnItemClickListener listener;
    private int orderType;// 1 是支出
    private int persionId = 0;
    public StatisticsDetailAdapter(Context context, List<AccountDetailedBean> list, int layoutId,int orderType, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.list = list;
        this.context = context;
        this.listener = listener;
        this.orderType = orderType;
        String s = SPUtil.getPrefString(context, com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        if (!TextUtils.isEmpty(s)) {
            persionId = Integer.parseInt(s);
        }
    }

    @Override
    protected void onBindItem(RowStaticticsDetailBinding binding, AccountDetailedBean accountDetailedBean, int position) {
        binding.setPosition(position);
        binding.setOrderType(orderType);
        binding.setIsLast(position == list.size()-1);
        binding.setBean(accountDetailedBean);

        binding.llView.setVisibility(View.GONE);
        if (position != 0) {
            if (position == list.size() - 1) {
                binding.llView.setVisibility(View.GONE);
            } else {
                if (list.get(position + 1).getIndexBeen() != null && list.get(position + 1).getIndexBeen().size() > 0) {
                    binding.llView.setVisibility(View.GONE);
                } else {
                    binding.llView.setVisibility(View.VISIBLE);
                }
            }
        }

        if (list.get(position).getIndexBeen() != null && list.get(position).getIndexBeen().size() > 0) {
            binding.llView.setVisibility(View.GONE);
        }

        if (position == list.size() - 1) {
            binding.llContent.setBackground(ContextCompat.getDrawable(context, R.drawable.select_bottom_white));
        } else {
            if (list.get(position + 1).getIndexBeen() != null) {// 一天的最后一条
                binding.llContent.setBackground(ContextCompat.getDrawable(context, R.drawable.select_bottom_white));
            } else {
                binding.llContent.setBackground(ContextCompat.getDrawable(context, R.drawable.select_white_0px));
            }
        }

        if (accountDetailedBean != null) {
            if (accountDetailedBean.getUpdateBy() == null || accountDetailedBean.getUpdateBy() == persionId) {
                binding.imagess.setBorderColor(Color.parseColor("#D80200"));
            } else {
                binding.imagess.setBorderColor(Color.parseColor("#41AB14"));
            }
        }

//        binding.llContent.setOnClickListener(v -> listener.onClick(position,accountDetailedBean,0));
//        binding.llContent.setOnLongClickListener(v -> {
//            listener.onClick(position,accountDetailedBean,1);
//            return true;
//        });
    }

}