package com.hbird.ui.statistics;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.hbird.base.R;
import com.hbird.base.databinding.RowStatisticsBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.bean.StatisticsSpendTopArraysBean;
import com.hbird.util.Utils;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;
import sing.util.ScreenUtil;

/**
 * @author: LiangYX
 * @ClassName: FragStatisticsAdapter
 * @date: 2019/1/17 16:34
 * @Description: 统计 - 排行榜
 */
public class FragStatisticsAdapter extends BaseRecyclerAdapter<StatisticsSpendTopArraysBean, RowStatisticsBinding> {

    private List<StatisticsSpendTopArraysBean> list;
    private OnItemClickListener listener;
    private double money;// 当前选择日期的总金钱
    private double ratio = 0;// 比率
    private int width;
    private Context context;
    private boolean open;

    public void setMoney(double money) {
        this.money = money;
        notifyItemRangeChanged(0, list.size());
//        notifyDataSetChanged();
    }

    public void setOpen(boolean open) {
        this.open = open;
        notifyItemRangeChanged(0, list.size());
//        notifyDataSetChanged();
    }

    public FragStatisticsAdapter(Context context, List<StatisticsSpendTopArraysBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.context = context;
        this.list = list;
        this.listener = listener;
        int temp = context.getResources().getDimensionPixelSize(sing.uiadapter.R.dimen.dp_85_x);
        width = (ScreenUtil.getScreenWidth(context) - temp) / 100;
    }

    @Override
    protected void onBindItem(RowStatisticsBinding binding, StatisticsSpendTopArraysBean bean, int position) {
        bean.money = Utils.to2Digit(bean.money);
        binding.setBean(bean);

        double ratios = bean.money / money;
        if (money == 0) {
            ratios = 0;
        }
        if (position == 0) {
            ratio = ratios;
        } else if (position == list.size() - 1) {
            ratio += ratios;
        } else {
            ratio = 1 - ratio;
        }
        binding.setRatio(Utils.to2DigitString(ratios * 100) + "%");

        int progress = 0;
        String t = String.valueOf(ratios * 100);
        if (t.contains(".")) {
            progress = (Integer.parseInt(t.substring(0, t.indexOf("."))));
        } else {
            progress = (Integer.parseInt(t));
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.ivProgress.getLayoutParams();
        params.width = width * (progress < 1 ? 1 : progress);
        binding.ivProgress.setLayoutParams(params);

        if (position == 0) {
            binding.llParent.setBackground(ContextCompat.getDrawable(context, R.drawable.select_top_white));
        } else {
            if (list.size() <= 5) {
                if (position == list.size() - 1) {// 不够5条的最后一条，要圆角
                    binding.llParent.setBackground(ContextCompat.getDrawable(context, R.drawable.select_bottom_white));
                } else {
                    binding.llParent.setBackground(ContextCompat.getDrawable(context, R.drawable.select_white_0px));
                }
            } else {// 大于5条，都是方角
                binding.llParent.setBackground(ContextCompat.getDrawable(context, R.drawable.select_white_0px));
            }
        }

        if (list.size() < 5) {
            if (position == list.size() - 1) {
                binding.view.setVisibility(View.GONE);
            } else {
                binding.view.setVisibility(View.VISIBLE);
            }
        } else {
            binding.view.setVisibility(View.VISIBLE);
        }

        binding.llParent.setOnClickListener(v -> listener.onClick(position, bean, 0));
    }
}